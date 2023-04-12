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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.ui.ButtonManager;
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
    final String[] chefSprites = new String[]{
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Woman Green/womanGreen_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 2/hitman2_hold.png"
    };
    final float[] chefX = new float[]{
            4.5f, 10.5f, 4.5f
    };
    final float[] chefY = new float[]{
            3f, 3f, 6.5f
    };
    private int chefCount;
    private final int maxChefCount;
    private final float chefScale;
    private final Stage chefStage; // TODO: dont like this

    /**
     * @param chefScale      the amount to scale the texture by so that each chef is an accurate
     *                       size.
     * @param collisionLayer the tile map layer which the chefs can collide with.
     * @param overlay        the user interface overlay to display information about the current chef
     *                       and time, and to provide more controls.
     */
    public ChefManager(float chefScale, TiledMapTileLayer collisionLayer, UIOverlay overlay, boolean isScenarioMode,
                       Stage chefStage) {
        this.collisionLayer = collisionLayer;
        this.overlay = overlay;
        this.chefScale = chefScale;
        this.chefStage = chefStage;

        if (isScenarioMode) {
            chefCount = 2;
            maxChefCount = 2;
        } else {
            chefCount = 1;
            maxChefCount = 3; //TODO: should be different to 3?
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
            chefs.get(i).init(chefX[i], chefY[i]);
        }

        ClickListener callbackToBuy = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addNewChef();
            }
        };
        overlay.addBuyChefButton(callbackToBuy);
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

    public List<Chef> getChefs() {
        return chefs;
    }

    /**
     * Add the created Chefs to the game world
//     *
//     * @param chefStage The game world to which the chefs should be added.
     */
    public void addChefsToStage() {
        for (Chef chef : chefs) {
            chefStage.addActor(chef);
        }
        final ChefManager manager = this;
        chefStage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { // click to change
                Actor actorHit = chefStage.hit(x, y, false);
                if (actorHit instanceof Chef) {
                    manager.setCurrentChef((Chef) actorHit);
                } else {
                    manager.setCurrentChef(null);
                }
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) { // press E to cycle through cooks
                if (keycode != Input.Keys.E) {
                    return false;
                }

                if (currentChef == null && lastChef != null) { // convenience to go back to last chef if unselected
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

    public boolean addNewChef() {
        if (!atMaxChefs()) {
            chefCount++;
            Chef newChef = createChef(chefCount-1);
            chefStage.addActor(newChef);
            overlay.updateChefUI(currentChef, atMaxChefs());
            return true;
        }
        return false;
    }

    private Chef createChef(int chefI) {
        String sprite = Arrays.stream(chefSprites).findAny().get(); //TODO: change to random?
        Texture chefTexture = new Texture(Gdx.files.internal(sprite));
        Chef chef = new Chef(chefTexture, new Vector2(chefTexture.getWidth() * chefScale,
                chefTexture.getHeight() * chefScale), this);
        chef.setBounds(chefX[chefI], chefY[chefI], chefTexture.getHeight() * chefScale,
                chefTexture.getHeight() * chefScale);
        chef.setInputEnabled(false);
        chefs.add(chef);
        return chef;
    }

    /**
     * Given a chef, update the state of the chefs to make sure that only one has input enabled.
     *
     * @param chef the chef to be controlled by the user
     */
    public void setCurrentChef(Chef chef) {
        if (chef == null && currentChef != null) {
            currentChef.setInputEnabled(false);
            lastChef = currentChef;
            currentChef = null;
        }
        if (currentChef != chef) {
            if (currentChef != null) {
                currentChef.setInputEnabled(false);
            }
            currentChef = chef;
            lastChef = null;
            currentChef.setInputEnabled(true);
        }
        overlay.updateChefUI(currentChef, atMaxChefs());
    }

    public Chef getCurrentChef() {
        return currentChef;
    }

    private Chef getLastChef() {
        return lastChef;
    }

    public boolean atMaxChefs() {
        return chefCount >= maxChefCount;
    }

    /**
     * Update the UI when the current chef's stack has been updated
     */
    public void currentChefStackUpdated() {
        overlay.updateChefUI(currentChef, atMaxChefs());
    }

    @Override
    public void dispose() {
        for (Chef chef : chefs) {
            chef.dispose();
        }
    }
}
