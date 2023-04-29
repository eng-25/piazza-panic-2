package cs.eng1.piazzapanic.chef;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The controller that handles switching control between chefs and tells them about the surrounding
 * environment.
 */
public class ChefManager implements Disposable {

    private final ArrayList<Chef> chefs;
    private Chef currentChef = null;
    private Chef lastChef = null;
    private final TiledMapTileLayer collisionLayer;
    private final UIOverlay overlay;
    private final int maxChefCount;
    private int chefCount;
    private int chefCost;
    private final float chefScale;
    private final Stage chefStage;

    public static final String[] CHEF_SPRITES = new String[]{
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Woman Green/womanGreen_hold.png"
    };
    public static final float[] CHEF_X = new float[]{
            3.5f, 11.5f, 3.5f
    };
    public static final float[] CHEF_Y = new float[]{
            2.5f, 2.5f, 6f
    };


    /**
     * @param chefScale      the amount to scale the texture by so that each chef is an accurate
     *                       size.
     * @param collisionLayer the tile map layer which the chefs can collide with.
     * @param overlay        the user interface overlay to display information about the current chef
     *                       and time, and to provide more controls.
     */
    public ChefManager(float chefScale, TiledMapTileLayer collisionLayer, UIOverlay overlay, boolean isScenarioMode,
                       Stage chefStage, int initialChefCost) {
        this.collisionLayer = collisionLayer;
        this.overlay = overlay;
        this.chefScale = chefScale;
        this.chefStage = chefStage;
        this.chefCost = initialChefCost;

        if (isScenarioMode) {
            chefCount = maxChefCount = 2;
        } else {
            chefCount = 1;
            maxChefCount = 3;
        }

        // Load chef sprites
        chefs = new ArrayList<>(chefCount);

        // Initialize chefs
        for (int i = 0; i < chefCount; i++) {
            createChef(i);
        }
    }

    /**
     * Reset each chef to their original position when you load
     */
    public void init() {
        for (int i = 0; i < chefs.size(); i++) {
            chefs.get(i).init(CHEF_X[i], CHEF_Y[i]);
        }
    }

    /**
     * Get the tile in the foreground collision layer at the specified point
     *
     * @param x the x coordinate of the tile
     * @param y the y coordinate of the tile
     * @return the cell/tile at the coordinates
     */
    public Cell getCellAtPosition(int x, int y) {
        return collisionLayer.getCell(x, y);
    }

    private Chef createChef(int chefIndex) {
        String sprite = Arrays.stream(CHEF_SPRITES).findAny().get(); //TODO: change to random?
        Texture chefTex = new Texture(Gdx.files.internal(sprite));
        Chef chef = new Chef(chefTex, new Vector2(chefTex.getWidth() * chefScale,
                chefTex.getHeight() * chefScale), this);
        chef.setBounds(CHEF_X[chefIndex], CHEF_Y[chefIndex], chefTex.getHeight() * chefScale,
                chefTex.getHeight() * chefScale);
        chef.setInputEnabled(false);
        chefs.add(chef);
        return chef;
    }

    public void addNewChef() {
        if (!atMaxChefs()) {
            chefCount++;
            Chef newChef = createChef(chefCount-1);
            chefStage.addActor(newChef);
            chefCost *= 2;
            overlay.updateChefUI(currentChef, atMaxChefs(), chefCost);
        }
    }

    public List<Chef> getChefs() {
        return chefs;
    }

    /**
     * Add the created Chefs to the game world
     *
     * @param stage The game world to which the chefs should be added.
     */
    public void addChefsToStage(final Stage stage) {
        for (Chef chef : chefs) {
            stage.addActor(chef);
        }
        final ChefManager manager = this;
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Actor actorHit = stage.hit(x, y, false);
                if (actorHit instanceof Chef) {
                    manager.setCurrentChef((Chef) actorHit);
                } else {
                    manager.setCurrentChef(null);
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.E) {
                    return false;
                }

                if (currentChef == null && lastChef != null) { // go back to last chef if they were unselected
                    manager.setCurrentChef(lastChef);
                    return true;
                }

                int newIndex = chefs.indexOf(currentChef) + 1;
                if (newIndex == chefs.size()) {
                    newIndex = 0;
                }
                manager.setCurrentChef(chefs.get(newIndex));
                return true;
            }
        });

    }

    /**
     * Given a chef, update the state of the chefs to make sure that only one has input enabled.
     *
     * @param chef the chef to be controlled by the user
     */
    public void setCurrentChef(Chef chef) {
        if (chef == null && currentChef != null) {
            currentChef.setInputEnabled(false);
            currentChef = null;
        }
        if (currentChef != chef) {
            if (currentChef != null) {
                currentChef.setInputEnabled(false);
            }
            currentChef = chef;
            currentChef.setInputEnabled(true);
        }
        overlay.updateChefUI(currentChef, atMaxChefs(), chefCost);
    }

    public Chef getCurrentChef() {
        return currentChef;
    }

    public int getCurrentChefIndex() { return currentChef == null ? -1 : chefs.indexOf(currentChef); }

    public Chef getLastChef() { return lastChef; }

    public boolean atMaxChefs() { return chefCount >= maxChefCount; }

    /**
     * Update the UI when the current chef's stack has been updated
     */
    public void currentChefStackUpdated() {
        overlay.updateChefUI(currentChef, atMaxChefs(), chefCost);
    }

    @Override
    public void dispose() {
        for (Chef chef : chefs) {
            chef.dispose();
        }
    }

    public int getChefCost() {
        return chefCost;
    }

    public int getChefCount() {
        return chefCount;
    }

}
