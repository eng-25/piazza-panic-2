package cs.eng1.piazzapanic.food;

import com.badlogic.gdx.utils.Queue;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.stations.RecipeStation;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.*;

public class CustomerManager {

  //private final Queue<Recipe> customerOrders;
  private final List<Recipe> currentOrders = new ArrayList<>();
  private final List<RecipeStation> recipeStations;
  private final UIOverlay overlay;
  private int completeOrders = 0;
  private int customerCount;
  private Recipe[] possibleRecipes;

  public CustomerManager(UIOverlay overlay) {
    this.overlay = overlay;
    this.recipeStations = new LinkedList<>();
    //customerOrders = new Queue<>();
  }

  /**
   * Reset the scenario to the default scenario.
   *
   * @param textureManager The manager of food textures that can be passed to the recipes
   */
  public void init(FoodTextureManager textureManager, int customerCount) {
    this.customerCount = customerCount;
    if (customerCount == -1) {
      possibleRecipes = new Recipe[]{new Burger(textureManager), new Salad(textureManager)}; // TODO: add more dishes
    } else {
      possibleRecipes = new Recipe[]{new Burger(textureManager), new Salad(textureManager)};
    }
    currentOrders.clear();
  }

  /**
   * Check to see if the recipe matches the currently requested order.
   *
   * @param recipe The recipe to check against the current order.
   * @return a boolean signifying if the recipe is correct.
   */
  public Recipe checkRecipe(Recipe recipe) {
    // iterate through list, check each
    for (Recipe r : currentOrders) {
      if (recipe.getType().equals(r.getType())) {
        return r;
      }
    }
    return null;
//    if (currentOrders == null) {
//      return false;
//    }
//    return recipe.getType().equals(currentOrders.getType());
  }

  /**
   * Complete the current order nad move on to the next one. Then update the UI. If all the recipes
   * are completed, then show the winning UI.
   */
  public void nextRecipe() {
    if (completeOrders >= customerCount && customerCount != -1) {
      currentOrders.clear();
      overlay.updateRecipeCounter(0);
      overlay.finishGameUI();
    } else { // next recipe
      overlay.updateRecipeCounter(customerCount - completeOrders);
      currentOrders.add(getRandomRecipe());
    }
    notifyRecipeStations();
    overlay.updateRecipeUI(currentOrders);
  }

  public void completeRecipe(Recipe order) {
    currentOrders.remove(order);
    overlay.updateRecipeUI(currentOrders);
  }

  private Recipe getRandomRecipe() {
    return possibleRecipes[new Random().nextInt(possibleRecipes.length)];
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

  public List<Recipe> getCurrentOrders() {
    return currentOrders;
  }
}
