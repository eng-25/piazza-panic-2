package cs.eng1.piazzapanic.food;

import com.badlogic.gdx.utils.Queue;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.stations.RecipeStation;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CustomerManager {

  private final Queue<Recipe> customerOrders;
  private final List<Recipe> currentOrders = new ArrayList<>();
  private final List<RecipeStation> recipeStations;
  private final UIOverlay overlay;

  public CustomerManager(UIOverlay overlay) {
    this.overlay = overlay;
    this.recipeStations = new LinkedList<>();
    customerOrders = new Queue<>();
  }

  /**
   * Reset the scenario to the default scenario.
   *
   * @param textureManager The manager of food textures that can be passed to the recipes
   */
  public void init(FoodTextureManager textureManager) {
    Recipe[] possibleRecipes = new Recipe[]{new Burger(textureManager), new Salad(textureManager)};

    // Salad, Burger, Burger, Salad, Burger. This can be replaced by randomly selecting from
    // possibleRecipes or by using another scenario
    customerOrders.clear();
    int[] recipeIndices = new int[]{1, 0, 0, 1, 0};
    for (int recipeIndex : recipeIndices) {
      customerOrders.addLast(possibleRecipes[recipeIndex]);
    }
  }

  /**
   * Check to see if the recipe matches the currently requested order.
   *
   * @param recipe The recipe to check against the current order.
   * @return a boolean signifying if the recipe is correct.
   */
  public boolean checkRecipe(Recipe recipe) {
    // iterate through list, check each
    for (Recipe r : currentOrders) {
      if (recipe.getType().equals(r.getType())) {
        return true;
      }
    }
    return false;
//    if (currentOrders == null) {
//      return false;
//    }
//    return recipe.getType().equals(currentOrders.getType());
  }

  /**
   * Complete the current order nad move on to the next one. Then update the UI. If all the recipes
   * are completed, then show the winning UI.
   */
  public void nextRecipe() { //TODO: Working for multiple orders, but should not complete an order upon this method's call
    if (customerOrders.isEmpty()) { //TODO: change this condition as the check for completion?
      currentOrders.clear();
      overlay.updateRecipeCounter(0);
    } else { // next recipe
      overlay.updateRecipeCounter(customerOrders.size);
      currentOrders.add(customerOrders.removeFirst());
    }
    notifyRecipeStations();
    currentOrders.forEach(overlay::updateRecipeUI);
    if (currentOrders.isEmpty()) {
      System.out.println("WIN");
      overlay.finishGameUI();
    }
  }

  /**
   * If one recipe station has been updated, let all the other ones know that there is a new recipe
   * to be built.
   */
  private void notifyRecipeStations() {
    for (RecipeStation recipeStation : recipeStations) {
      recipeStation.updateOrderActions();
    }
  }

  public void addRecipeStation(RecipeStation station) {
    recipeStations.add(station);
  }
}
