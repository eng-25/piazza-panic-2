package cs.eng1.piazzapanic.station;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredient.SimpleIngredient;
import cs.eng1.piazzapanic.food.recipe.Recipe;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.station.StationAction.ActionType;
import cs.eng1.piazzapanic.ui.StationActionUI.ActionAlignment;
import cs.eng1.piazzapanic.ui.StationUIController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The RecipeStation class is a station representing the place in the kitchen
 * where you combine ingredients to create food. The food can then be served to
 * the customer via the station.
 *
 * @author Faran Lane, Alistair Foggin, Matt Fitzpatrick
 * @since 12-22
 */
public class RecipeStation extends Station {
    private final FoodTextureManager textureManager;
    private final CustomerManager customerManager;
    private Recipe completedRecipe = null;
    private final Map<String, Integer> ingredientCountMap;

    /**
     * The constructor method for the class
     *
     * @param id                  The unique identifier of the station.
     * @param textureRegion       The rectangular area of the texture.
     * @param stationUIController The controller from which we can get show and hide the action
     *                            buttons belonging to the station.
     * @param alignment           Dictates where the action buttons are shown.
     * @param textureManager      The controller from which we can get information on what texture
     *                            each ingredient should have.
     * @param customerManager     The controller from which we can get information on what food
     *                            needs to be served.
     * @param isScenario          whether the game is scenario mode or not.
     * @param game                the current GameScreen instance.
     */
    public RecipeStation(int id, TextureRegion textureRegion, StationUIController stationUIController,
                         ActionAlignment alignment, FoodTextureManager textureManager,
                         CustomerManager customerManager, boolean isScenario, GameScreen game) {
        super(id, textureRegion, stationUIController, alignment, isScenario, false, game);
        this.textureManager = textureManager;
        this.customerManager = customerManager;
        ingredientCountMap = new HashMap<>(Map.of(
                "bun", 0,
                "patty", 0,
                "lettuce", 0,
                "tomato", 0
        ));

    }

    @Override
    public void reset() {
        resetIngredients();
        completedRecipe = null;
        super.reset();
    }

    /**
     * Resets all held ingredient counts to 0
     */
    private void resetIngredients() {
        ingredientCountMap.replaceAll((k, v) -> 0);
    }

    /**
     * Atomically iterates through the held ingredient map and totals the number of ingredients
     *
     * @return total number of ingredients held
     */
    private int getTotalIngredientCount() {
        AtomicInteger total = new AtomicInteger();
        ingredientCountMap.values().forEach(total::addAndGet);
        return total.get();
    }

    /**
     * Obtains the actions that can be currently performed depending on the states of the station
     * itself and the selected chef
     *
     * @return actionTypes - the list of actions the station can currently perform.
     */
    @Override
    public List<ActionType> getActionTypes() {
        LinkedList<ActionType> actionTypes = new LinkedList<>();
        if (nearbyChef != null) {
            if (!nearbyChef.getStack().isEmpty()) {
                SimpleIngredient checkItem = nearbyChef.getStack().peek();
                if (checkItem.getIsChopped() || checkItem.getIsCooked() || Objects.equals(
                        checkItem.getType(), "bun")) {
                    //If a chef is nearby and is carrying at least one ingredient
                    // and the top ingredient is cooked, chopped or a bun then display the action
                    actionTypes.add(ActionType.PLACE_INGREDIENT);
                }
            }
            if (completedRecipe == null) {
                if (ingredientCountMap.get("patty") >= 1 && ingredientCountMap.get("bun") >= 1 && nearbyChef.getStack().hasSpace()) {
                    actionTypes.add(ActionType.MAKE_BURGER);
                }
                if (ingredientCountMap.get("tomato") >= 1 && ingredientCountMap.get("lettuce") >= 1 && nearbyChef.getStack().hasSpace()) {
                    actionTypes.add(ActionType.MAKE_SALAD);
                }
            } else if (customerManager.checkRecipe(completedRecipe) != null) {
                actionTypes.add(ActionType.SUBMIT_ORDER);
            }

            if (getTotalIngredientCount() > 0 ||
                    customerManager.checkRecipe(completedRecipe) != null) {
                addClearAction(actionTypes);
            }
        }
        return actionTypes;
    }

    /**
     * Given an action, the station should attempt to do that action based on the chef that is nearby
     * or what ingredient(s) are currently on the station.
     *
     * @param action the action that needs to be done by this station if it can.
     */
    @Override
    public void doStationAction(ActionType action) {
        super.doStationAction(action);
        switch (action) {
            case PLACE_INGREDIENT:
                SimpleIngredient topItem = nearbyChef.getStack().peek();
                switch (topItem.getType()) {
                    case "patty":
                        nearbyChef.placeIngredient();
                        incrementMapValue("patty");
                        break;
                    case "tomato":
                        nearbyChef.placeIngredient();
                        incrementMapValue("tomato");
                        break;
                    case "lettuce":
                        nearbyChef.placeIngredient();
                        incrementMapValue("lettuce");
                        break;
                    case "bun":
                        nearbyChef.placeIngredient();
                        incrementMapValue("bun");
                        break;
                    case "pizza":
                        nearbyChef.placeIngredient();
                        completedRecipe = new Recipe("pizza_cooked", textureManager);
                        break;
                    case "jacket_potato":
                        nearbyChef.placeIngredient();
                        completedRecipe = new Recipe("jacket_potato_cooked", textureManager);
                        break;
                }

                break;
            case MAKE_BURGER:
                completedRecipe = new Recipe("burger", textureManager);
                decrementMapValue("patty");
                decrementMapValue("bun");
                break;

            case MAKE_SALAD:
                completedRecipe = new Recipe("salad", textureManager);
                decrementMapValue("tomato");
                decrementMapValue("lettuce");
                break;

            case SUBMIT_ORDER:
                if (completedRecipe != null) {
                    Recipe orderToComplete = customerManager.checkRecipe(completedRecipe);
                    if (orderToComplete != null) {
                        customerManager.completeOrder(orderToComplete);
                        completedRecipe = null;
                    }
                }
                break;
        }
        uiController.showActions(this, getActionTypes());
    }

    @Override
    protected void clearStation() {
        resetIngredients();
        super.clearStation();
    }

    /**
     * Displays ingredients that have been placed on the station
     *
     * @param batch       Used to display a 2D texture
     * @param parentAlpha The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all
     *                    children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (ingredientCountMap.get("bun") > 0) {
            drawFoodTexture(batch, textureManager.getTexture("bun"));
        }
        if (ingredientCountMap.get("patty") > 0) {
            drawFoodTexture(batch, textureManager.getTexture("patty_cooked"));
        }
        if (ingredientCountMap.get("lettuce") > 0) {
            drawFoodTexture(batch, textureManager.getTexture("lettuce_chopped"));
        }
        if (ingredientCountMap.get("tomato") > 0) {
            drawFoodTexture(batch, textureManager.getTexture("tomato_chopped"));
        }
        if (completedRecipe != null) {
            drawFoodTexture(batch, completedRecipe.getTexture());
        }
    }

    /**
     * Updates the current available actions based on the new customer's order
     */
    public void updateOrderActions() {
        uiController.showActions(this, getActionTypes());
    }

    /**
     * Convenience method to change a map value by a given amount
     *
     * @param key    key of map value to change
     * @param change amount to change value by
     */
    private void changeMapValue(String key, int change) {
        ingredientCountMap.replace(key, ingredientCountMap.get(key) + change);
    }

    /**
     * Convenience to increment a map value by 1
     *
     * @param key key of map value to change
     */
    private void incrementMapValue(String key) {
        changeMapValue(key, 1);
    }

    /**
     * Convenience to decrement a map value by 1
     *
     * @param key key of map value to change
     */
    private void decrementMapValue(String key) {
        changeMapValue(key, -1);
    }

    public Recipe getCompletedRecipe() {
        return completedRecipe;
    }

    public Map<String, Integer> getIngredientCountMap() {
        return ingredientCountMap;
    }

    public String getIngredientSaveMap() {
        return ingredientCountMap.toString().replace('{', '[').replace('}', ']');
    }

    /**
     * Loads held ingredients from a given String array
     *
     * @param ingredientStringList array of ingredient Strings of the form "ingredientTypeString=countInteger"
     */
    public void loadHeldIngredients(String[] ingredientStringList) {
        for (String ingString : ingredientStringList) {
            String[] split = ingString.split("=");
            ingredientCountMap.replace(split[0].replaceAll(" ", ""), Integer.valueOf(split[1]));
        }
    }
}
