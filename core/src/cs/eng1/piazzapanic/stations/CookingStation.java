package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.ingredients.CookedIngredient;
import cs.eng1.piazzapanic.food.ingredients.SimpleIngredient;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * The CookingStation class is a station representing the place in the kitchen where you cook
 * patties to be used in making burgers.
 */
public class CookingStation extends Station implements IFailable {

    protected final SimpleIngredient[] validIngredients;
    protected SimpleIngredient currentIngredient;
    protected float timeCooked;
    protected final float totalTimeToCook = 10f;
    protected boolean progressVisible = false;

    protected boolean shouldTickFailTimer;
    protected float failTimer;
    protected final float failTime;

    /**
     * The constructor method for the class
     *
     * @param id           The unique identifier of the station
     * @param image        The rectangular area of the texture
     * @param uiController The controller from which we can get show and hide the action buttons
     *                     belonging to the station
     * @param alignment    Dictates where the action buttons are shown
     * @param ingredients  An array of ingredients used to define what ingredients can be cooked
     */
    public CookingStation(int id, TextureRegion image, StationUIController uiController,
                          StationActionUI.ActionAlignment alignment, SimpleIngredient[] ingredients, float failTime,
                          boolean isScenario, boolean locked, GameScreen game) {
        super(id, image, uiController, alignment, isScenario, locked, game);
        validIngredients = ingredients; //A list of the ingredients that can be used by this station.
        this.failTime = failTime;
        failTimer = 0;
        shouldTickFailTimer = false;
        reset();
    }

    @Override
    public void reset() {
        currentIngredient = null;
        timeCooked = 0;
        failTimer = 0;
        shouldTickFailTimer = false;
        progressVisible = false;
    }

    /**
     * Called every frame. Used to update the progress bar and check if enough time has passed for the
     * ingredient to be changed to its half cooked or cooked variant
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void act(float delta) {
        tickFailTimer(delta);
        if (inUse) {
            timeCooked += delta;
            uiController.updateProgressValue(this, (timeCooked / totalTimeToCook) * 100f);
            uiController.updateFailValue(this, getFailPct()*100f);
            if (timeCooked >= totalTimeToCook && progressVisible) {
                if (!isScenario) { // only allow failing steps if in endless mode
                    if (!shouldTickFailTimer) {
                        startFailTimer();
                    }
                    uiController.showFailBar(this);
                }
                if (currentIngredient instanceof CookedIngredient && !((CookedIngredient) currentIngredient).isHalfCooked()) {
                    ((CookedIngredient) currentIngredient).setHalfCooked();
                } else if (currentIngredient instanceof CookedIngredient
                        && ((CookedIngredient) currentIngredient).isHalfCooked() && !currentIngredient.getIsCooked()) {
                    currentIngredient.setIsCooked(true);
                }
                uiController.hideProgressBar(this);
                progressVisible = false;
                uiController.showActions(this, getActionTypes());
            }
        }
        super.act(delta);
    }

    /**
     * Checks the presented ingredient with the list of valid ingredients to see if it can be cooked
     *
     * @param ingredientToCheck The ingredient presented by the chef to be checked if it can be used
     *                          by the station
     * @return true if the ingredient is in the validIngredients array; false otherwise
     */
    protected boolean isCorrectIngredient(SimpleIngredient ingredientToCheck) {
        if (!ingredientToCheck.getIsCooked()) {
            for (SimpleIngredient item : this.validIngredients) {
                if (Objects.equals(ingredientToCheck.getType(), item.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtains the actions that can be currently performed depending on the states of the station
     * itself and the selected chef
     *
     * @return actionTypes - the list of actions the station can currently perform.
     */
    @Override
    public List<StationAction.ActionType> getActionTypes() {
        LinkedList<StationAction.ActionType> actionTypes = new LinkedList<>();
        if (nearbyChef == null) {
            return actionTypes;
        }
        if (locked) {
            actionTypes.add(StationAction.ActionType.BUY_STATION);
            return actionTypes;
        }
        if (currentIngredient == null) {
            if (nearbyChef.hasIngredient() && isCorrectIngredient(nearbyChef.getStack().peek())) {
                actionTypes.add(StationAction.ActionType.PLACE_INGREDIENT);
            }
        } else {
            if (hasFailed()) {
                addClearAction(actionTypes);
                return actionTypes;
            }
            //check to see if total number of seconds has passed to progress the state of the patty.
            if (currentIngredient instanceof CookedIngredient && ((CookedIngredient) currentIngredient).isHalfCooked()
                    && !currentIngredient.getIsCooked() && !progressVisible) {
                actionTypes.add(StationAction.ActionType.FLIP_ACTION);
            } else if (currentIngredient.getIsCooked()) {
                actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
            }
            if (!inUse) {
                actionTypes.add(StationAction.ActionType.COOK_ACTION);
            }
            if (!progressVisible) {
                addClearAction(actionTypes);
            }
        }
        return actionTypes;
    }

    /**
     * Given an action, the station should attempt to do that action based on the chef that is nearby
     * or the state of the ingredient currently on the station.
     *
     * @param action the action that needs to be done by this station if it can.
     */
    @Override
    public void doStationAction(StationAction.ActionType action) {
        super.doStationAction(action);
        switch (action) {
            case COOK_ACTION:
                timeCooked = 0;
                inUse = true;
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                progressVisible = true;
                break;

            case FLIP_ACTION:
                timeCooked = 0;
                shouldTickFailTimer = false;
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                uiController.hideFailBar(this);
                progressVisible = true;
                break;

            case PLACE_INGREDIENT:
                if (this.nearbyChef != null && nearbyChef.hasIngredient() && currentIngredient == null) {
                    if (this.isCorrectIngredient(nearbyChef.getStack().peek())) {
                        currentIngredient = nearbyChef.placeIngredient();
                    }
                }
                uiController.showActions(this, getActionTypes());
                break;

            case GRAB_INGREDIENT:
                if (nearbyChef.canGrabIngredient()) {
                    nearbyChef.grabIngredient(currentIngredient);
                    uiController.hideFailBar(this);
                    currentIngredient = null;
                    inUse = false;
                    shouldTickFailTimer = false;
                }
                uiController.showActions(this, getActionTypes());
                break;
        }
    }

    @Override
    protected void clearStation() {
        currentIngredient = null;
        uiController.showActions(this, getActionTypes());
        failTimer = 0;
        inUse = false;
    }

    /**
     * Displays ingredients that have been placed on the station
     *
     * @param batch       Used to display a 2D texture
     * @param parentAlpha The parent alpha, to be multiplied with this actor's alpha, allowing the
     *                    parent's alpha to affect all children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (currentIngredient != null) {
            drawFoodTexture(batch, currentIngredient.getTexture());
        }
    }

    @Override
    public void startFailTimer() {
        shouldTickFailTimer = true;
        failTimer = 0;
    }

    @Override
    public void stopFailTimer() {
        shouldTickFailTimer = false;
        uiController.showActions(this, getActionTypes());
        uiController.hideFailBar(this);
        if (currentIngredient instanceof CookedIngredient) {
            ((CookedIngredient) currentIngredient).setBurnt();
        }
    }

    @Override
    public void tickFailTimer(float delta) {
        if (shouldTickFailTimer) {
            failTimer += delta;
            if (hasFailed()) {
                stopFailTimer();
            }
        }
    }

    public SimpleIngredient getCurrentIngredient() {
        return currentIngredient;
    }

    @Override
    public float getFailPct() {
        return Math.min(failTimer/failTime, 1); // at most 1
    }

    public float getTimeCooked() {
        return timeCooked;
    }

    public boolean isProgressVisible() {
        return progressVisible;
    }

    public boolean shouldTickFailTimer() {
        return shouldTickFailTimer;
    }

    public float getFailTimer() {
        return failTimer;
    }
}
