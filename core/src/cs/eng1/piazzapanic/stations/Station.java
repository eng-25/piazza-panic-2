package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.observable.Observer;
import cs.eng1.piazzapanic.observable.Subject;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Station extends Actor implements Observer<Chef> {

    protected final int id;
    protected final StationUIController uiController;
    protected final StationActionUI.ActionAlignment actionAlignment;
    protected final TextureRegion stationImage;
    protected final GameScreen gameScreen;

    protected boolean inUse = false;
    protected boolean locked;

    protected final List<Subject<Chef>> chefSubjects = new LinkedList<>();
    protected Chef nearbyChef = null;
    private float imageRotation = 0.0f;

    protected final boolean isScenario;

    public static final int LOCKED_PRICE = 250;
    public static final TextureRegion LOCKED_IMAGE = new TextureRegion(new Texture("new/locked_station.png"));

    public Station(int id, TextureRegion image, StationUIController uiController,
                   StationActionUI.ActionAlignment alignment, boolean isScenario, boolean locked, GameScreen game) {
        this.id = id;
        stationImage = image; // Texture of the object
        actionAlignment = alignment;
        this.uiController = uiController;
        this.isScenario = isScenario;
        this.locked = locked;
        this.gameScreen = game;
    }

    /**
     * Reset the station values to be the default.
     */
    public void reset() {
        uiController.hideActions(this);
        uiController.hideProgressBar(this);
    }

    public void setImageRotation(float rotation) {
        this.imageRotation = rotation;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getStationImage(), getX(), getY(), 0.5f, 0.5f, getWidth(), getHeight(), 1f, 1f,
                imageRotation);
    }

    protected TextureRegion getStationImage() {
        return locked ? LOCKED_IMAGE : stationImage;
    }

    /**
     * Take a food texture and render it on top of the station at a smaller size than the station.
     *
     * @param batch       the sprite batch to draw rectangles to the screen.
     * @param foodTexture the texture to be drawn onto the screen.
     */
    protected void drawFoodTexture(Batch batch, Texture foodTexture) {
        batch.draw(foodTexture, getX() + .2f, getY() + .2f, .6f, .6f);
    }

    /**
     * Draw the outline of the shape of the station as a rectangle and draw a blue line from the
     * centre of this station (which is an Observer) to the centre of the stationCollider that it is
     * linked to (the Subject that this is registered to).
     *
     * @param shapes The renderer to use to draw debugging information
     */
    @Override
    public void drawDebug(ShapeRenderer shapes) {
        Color oldColor = shapes.getColor();

        // Draw bounds of this station
        shapes.setColor(Color.RED);
        shapes.rect(getX(), getY(), getWidth(), getHeight());

        // Check for any station colliders
        if (chefSubjects.isEmpty()) {
            shapes.setColor(oldColor);
            return;
        }

        // Draw lines to linked station colliders
        shapes.setColor(Color.BLUE);
        for (Subject<Chef> chefSubject : chefSubjects) {
            if (chefSubject instanceof Actor) {
                Actor collider = (Actor) chefSubject;
                Vector2 start = new Vector2(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
                Vector2 end = new Vector2(collider.getX() + collider.getWidth() / 2f,
                        collider.getY() + collider.getHeight() / 2f);
                shapes.line(start, end);
            }
        }

        // Reset colour
        shapes.setColor(oldColor);
    }

    /**
     * Take the chef sent from the Subject and decide what interactions are possible.
     *
     * @param chef The chef that the station should interact with which is given from the Subject to
     *             this Observer.
     */
    @Override
    public void update(Chef chef) {
        if (chef != null) {
            this.nearbyChef = chef;
            uiController.showActions(this, getActionTypes());
        } else if (this.nearbyChef != null) {
            Chef remainingChef = null;
            for (Subject<Chef> chefSubject : chefSubjects) {
                remainingChef = chefSubject.getLastNotification();
                if (remainingChef != null) {
                    break;
                }
            }
            if (remainingChef == null) {
                this.nearbyChef = null;
                uiController.hideActions(this);
            } else if (remainingChef != nearbyChef) {
                this.nearbyChef = remainingChef;
                uiController.showActions(this, getActionTypes());
            } else {
                uiController.showActions(this, getActionTypes());
            }
        }
    }

    @Override
    public void addSubject(Subject<Chef> chefSubject) {
        this.chefSubjects.add(chefSubject);
    }

    @Override
    public void removeSubject(Subject<Chef> chefSubject) {
        this.chefSubjects.remove(chefSubject);
    }

    @Override
    public void deregisterFromAllSubjects() {
        for (Subject<Chef> chefSubject : this.chefSubjects) {
            chefSubject.deregister(this);
        }
        this.chefSubjects.clear();
    }

    @Override
    public List<Subject<Chef>> getSubjects() {
        return this.chefSubjects;
    }

    /**
     * @return the list of possible actions that this station based on the current state
     */
    public List<StationAction.ActionType> getActionTypes() {
        return new LinkedList<>();
    }

    protected void addClearAction(List<StationAction.ActionType> actionList) {
        actionList.add(StationAction.ActionType.CLEAR_STATION);
    }

    /**
     * Given an action, the station should attempt to do that action based on the chef that is nearby
     * or what ingredient(s) are currently on the station.
     *
     * @param action the action that needs to be done by this station if it can.
     */
    public void doStationAction(StationAction.ActionType action) {
        if (action == StationAction.ActionType.CLEAR_STATION) {
            clearStation();
        }
        if (action == StationAction.ActionType.BUY_STATION) {
            buyStation();
            uiController.showActions(this, getActionTypes());
        }
    }

    protected void clearStation() {
    }

    /**
     * @return the direction in which the action buttons should be displayed.
     */
    public StationActionUI.ActionAlignment getActionAlignment() {
        return actionAlignment;
    }

    public int getId() {
        return id;
    }

    public void buyStation() {
        if (locked && canBuy()) {
            locked = false;
            gameScreen.addMoney(-LOCKED_PRICE);
        }
    }

    public boolean canBuy() {
        return gameScreen.getMoney() >= LOCKED_PRICE;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void loadData(String[] param) {
        switch (param[0]) {
            case "locked":
                locked = Boolean.parseBoolean(param[1]);
                break;
            case "in_use":
                inUse = Boolean.parseBoolean(param[1]);
                break;
//            default:
//                System.out.println(Arrays.toString(param));
        }
    }
}
