package cs.eng1.piazzapanic.food;

import com.badlogic.gdx.utils.Queue;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.stations.RecipeStation;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CustomerManager {

    private final List<Customer> currentOrders = new ArrayList<>();
    private final List<RecipeStation> recipeStations;
    private final UIOverlay overlay;
    private final boolean isScenario;
    private final int difficulty;
    private int completeOrders = 0;
    private int maxCustomerCount;
    private Recipe[] possibleRecipes;
    private float timeSinceCustomer;
    private float customerInterval;

    public static final float SCENARIO_CUSTOMER_INTERVAL = 30f;

    public CustomerManager(UIOverlay overlay, boolean isScenario, int difficulty) {
        this.isScenario = isScenario;
        this.difficulty = difficulty;
        this.overlay = overlay;
        this.recipeStations = new LinkedList<>();
    }

    /**
     * Reset the scenario to the default scenario.
     *
     * @param textureManager The manager of food textures that can be passed to the recipes
     */
    public void init(FoodTextureManager textureManager) {
        if (isScenario) {
            possibleRecipes = new Recipe[]{new Burger(textureManager), new Salad(textureManager)};
            maxCustomerCount = 5;
        } else {
            possibleRecipes = new Recipe[]{new Burger(textureManager), new Salad(textureManager)};
            maxCustomerCount = -1;
        }
        customerInterval = SCENARIO_CUSTOMER_INTERVAL;
        currentOrders.clear();
    }

    /**
     * Check to see if the recipe matches the currently requested order.
     *
     * @param recipe The recipe to check against the current order.
     * @return a boolean signifying if the recipe is correct.
     */
    public Recipe checkRecipe(Recipe recipe) {
        if (recipe == null) { return null; }
        for (Customer order : currentOrders) {
            Recipe r = order.hasRecipe(recipe);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    /**
     * Complete the current order nad move on to the next one. Then update the UI. If all the recipes
     * are completed, then show the winning UI.
     */
    public void nextRecipe() {
        if ((completeOrders + currentOrders.size()) < maxCustomerCount || maxCustomerCount == -1) { // next recipe
            //overlay.updateRecipeCounter(maxCustomerCount - completeOrders);
            currentOrders.add(getNewCustomer());
        }
        notifyRecipeStations();
        overlay.updateRecipeUI(currentOrders);
    }

    public void completeOrder(Recipe order) {
        for (Customer customer : currentOrders) {
            List<Recipe> customerOrder = customer.getOrder();
            if (customerOrder.contains(order)) {
                customerOrder.remove(order);
                if (customerOrder.isEmpty()) {
                    currentOrders.remove(customer);
                }
                break;
            }
        }
        overlay.updateRecipeUI(currentOrders);
        completeOrders++;
        if (completeOrders >= maxCustomerCount && maxCustomerCount != -1) {
            currentOrders.clear();
            overlay.finishGameUI(true, getCustomersServed());
        }
    }

    private Customer getNewCustomer(int maxGroupSize) {
        return new Customer(possibleRecipes.clone(), maxGroupSize, getCurrentCustomerTimeMultiplier(), difficulty);
    }

    private Customer getNewCustomer() {
        return getNewCustomer(isScenario ? 1 : 3);
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

    public List<List<Recipe>> getCurrentOrders() {
        List<List<Recipe>> orders = new ArrayList<>();
        currentOrders.forEach(c -> orders.add(c.getOrder()));
        return orders;
    }

    public List<Customer> getCustomers() {
        return currentOrders;
    }

    public int tick(float delta) {
        timeSinceCustomer+=delta;
        if (timeSinceCustomer > customerInterval) {
            timeSinceCustomer = 0;
            nextRecipe();
            updateCustomerInterval();
        }

        int reputationLost = 0;
        Iterator<Customer> iter = currentOrders.iterator();
        while (iter.hasNext()) {
            Customer customer = iter.next();
            if (customer.tickTimer(delta)) {
                reputationLost++;
            }
        }
        overlay.updateRecipeUI(currentOrders);
        return reputationLost;
    }

    private void updateCustomerInterval() {
        //TODO: in endless, decrease this slowly? (base on customer count = currentOrders.size + completeOrders)
    }

    private float getCurrentCustomerTimeMultiplier() { //TODO: check this calculation
        return isScenario ? 1 :
                Math.max(1-(getTotalCustomerCount()/100f), 0.00001f);
    }

    private int getTotalCustomerCount() {
        return currentOrders.size() + completeOrders;
    }

    public int getCustomersServed() {
        return completeOrders;
    }
}
