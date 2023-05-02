package cs.eng1.piazzapanic.station;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.ingredient.CookedIngredient;
import cs.eng1.piazzapanic.food.ingredient.SimpleIngredient;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An extended cooking station for more complex recipes.
 * Used to combine various prepared and unprepared ingredients and cook them.
 * Preparation steps can fail in endless mode.
 */
public class OvenStation extends CookingStation {

    private Map<String, Integer> heldIngredientMap;


    /**
     * The constructor method for the class
     *
     * @param id           The unique identifier of the station
     * @param image        The rectangular area of the texture
     * @param uiController The controller from which we can get show and hide the action buttons
     *                     belonging to the station
     * @param alignment    Dictates where the action buttons are shown
     * @param dishes       An array of final dishes to define what should be formed when the right ingredients are added
     * @param failTime     the time in seconds before a finished step will cause the current ingredient to fail
     * @param isScenario   if the game is scenario mode or not
     * @param locked       if the station is locked or not
     * @param game         the current instance of GameScreen
     */
    public OvenStation(int id, TextureRegion image, StationUIController uiController,
                       StationActionUI.ActionAlignment alignment, SimpleIngredient[] dishes, float failTime,
                       boolean isScenario, boolean locked, GameScreen game) {
        super(id, image, uiController, alignment, dishes, failTime, isScenario, locked, game);
        heldIngredientMap = new HashMap<>(Map.of(
                "tomato", 0,
                "cheese", 0,
                "beans", 0,
                "potato", 0,
                "dough", 0
        ));
    }

    /**
     * Defines the valid ingredients the station can accept
     *
     * @param ingredientToCheck The ingredient presented by the chef to be checked if it can be used
     *                          by the station
     * @return
     */
    @Override
    protected boolean isCorrectIngredient(SimpleIngredient ingredientToCheck) {
        switch (ingredientToCheck.getType()) {
            case "potato":
            case "dough":
                return true;
            case "tomato": // tomato and cheese only accepted if chopped
            case "cheese":
                return ingredientToCheck.getIsChopped();
            case "beans": // beans only accepted if cooked
                return ingredientToCheck.getIsCooked();
            default:
                return false;
        }
    }

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
            if (currentIngredient instanceof CookedIngredient && ((CookedIngredient) currentIngredient).isHalfCooked()
                    && !currentIngredient.getIsCooked() && !progressVisible) {
                actionTypes.add(StationAction.ActionType.FLIP_ACTION);
            } else if (currentIngredient.getIsCooked()) {
                actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
            }
            if (!inUse) {
                actionTypes.add(StationAction.ActionType.COOK_ACTION);
            }
            addClearAction(actionTypes);
        }
        return actionTypes;
    }

    @Override
    public void doStationAction(StationAction.ActionType action) {
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
                        addIngredient(nearbyChef.placeIngredient());
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
                }
                uiController.showActions(this, getActionTypes());
                break;
            case CLEAR_STATION:
                clearStation();
                break;
            case BUY_STATION:
                buyStation();
                uiController.showActions(this, getActionTypes());

        }
    }

    /**
     * Adds an ingredient to the held ingredient map, and checks if a valid recipe exists.
     *
     * @param toAdd ingredient to be added to station.
     */
    private void addIngredient(SimpleIngredient toAdd) {
        String type = toAdd.getType();
        heldIngredientMap.replace(type, heldIngredientMap.get(type) + 1);
        checkForRecipe();
    }

    /**
     * Checks if the station's current held ingredients make up a recipe.
     * If they do, removes 1 of each for that recipe and sets the current ingredient
     * to the raw version of that recipe, allowing it to then be cooked in the station.
     */
    private void checkForRecipe() {
        if (heldIngredientMap.get("potato") > 0 && heldIngredientMap.get("beans") > 0
                && heldIngredientMap.get("cheese") > 0) { // potato
            heldIngredientMap.replaceAll((k, v) -> {
                List<String> valid = List.of("potato", "cheese", "beans");
                return valid.contains(k) ? 0 : v;
            });
            currentIngredient = validIngredients[0];
        } else if (heldIngredientMap.get("dough") > 0 && heldIngredientMap.get("tomato") > 0
                && heldIngredientMap.get("cheese") > 0) { // pizza
            heldIngredientMap.replaceAll((k, v) -> {
                List<String> valid = List.of("dough", "cheese", "tomato");
                return valid.contains(k) ? 0 : v;
            });
            currentIngredient = validIngredients[1];
        } else {
            currentIngredient = null;
        }
    }

    public Map<String, Integer> getHeldIngredientMap() {
        return heldIngredientMap;
    }

    /**
     * Formats map string for saving properly - using [] instead of {} to prevent parsing issues on load
     *
     * @return formatted map string
     */
    public String getIngredientSaveMap() {
        return heldIngredientMap.toString().replace('{', '[').replace('}', ']');
    }

    /**
     * Parses a string of ingredients loaded and updates the held ingredient map accordingly.
     *
     * @param ingredientStringList string of held ingredients loaded.
     */
    public void loadHeldIngredients(String[] ingredientStringList) {
        for (String ingString : ingredientStringList) {
            String[] split = ingString.split("=");
            heldIngredientMap.replace(split[0].replaceAll(" ", ""), Integer.valueOf(split[1]));
        }
    }
}
