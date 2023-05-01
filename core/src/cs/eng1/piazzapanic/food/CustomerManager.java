package cs.eng1.piazzapanic.food;

import cs.eng1.piazzapanic.food.recipe.Recipe;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.station.RecipeStation;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.*;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;

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
    private final GameScreen gameScreen;
    private final int scenarioCustomerCount;

    public static final float SCENARIO_CUSTOMER_INTERVAL = 30f;

    public CustomerManager(UIOverlay overlay, boolean isScenario, int difficulty, GameScreen game,
                           int scenarioCustomerCount) {
        this.isScenario = isScenario;
        this.difficulty = difficulty;
        this.overlay = overlay;
        this.recipeStations = new LinkedList<>();
        gameScreen = game;
        this.scenarioCustomerCount = scenarioCustomerCount;
    }

    /**
     * Reset the scenario to the default scenario.
     *
     * @param textureManager The manager of food textures that can be passed to the recipes
     */
    public void init(FoodTextureManager textureManager) {
        if (isScenario) {
            possibleRecipes = getRecipeList(new String[]{"burger", "salad"}, textureManager);
            maxCustomerCount = scenarioCustomerCount;
        } else {
            possibleRecipes = getRecipeList(new String[]{"burger", "salad", "pizza_cooked", "jacket_potato_cooked"},
                    textureManager);
            maxCustomerCount = -1;
        }
        customerInterval = SCENARIO_CUSTOMER_INTERVAL;
        customerInterval += isScenario ? 15 : 0; // start a little higher in scenario
        currentOrders.clear();
    }

    private Recipe[] getRecipeList(String[] recipeTypes, FoodTextureManager manager) {
        Recipe[] recipeList = new Recipe[recipeTypes.length];
        for (int i = 0; i < recipeTypes.length; i++) {
            recipeList[i] = new Recipe(recipeTypes[i], manager);
        }
        return recipeList;
    }

    /**
     * Check to see if the recipe matches the currently requested order.
     *
     * @param recipe The recipe to check against the current order.
     * @return a boolean signifying if the recipe is correct.
     */
    public Recipe checkRecipe(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
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
                tryRandomPowerup();
                if (customerOrder.isEmpty()) {
                    gameScreen.addMoney(customer.getMoney());
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

    private void tryRandomPowerup() {
        if (RANDOM.nextFloat() < 0.1 && !isScenario) {
            gameScreen.getPowerupManager().activateRandomPowerup();
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
        timeSinceCustomer += delta;
        if (timeSinceCustomer > customerInterval) {
            timeSinceCustomer = 0;
            nextRecipe();
            updateCustomerInterval();
        }

        int reputationLost = 0;
        Iterator<Customer> iter = currentOrders.iterator();
        List<Customer> toRemove = new ArrayList<>(); // customers to remove with expired timers in endless mode
        while (iter.hasNext()) {
            Customer customer = iter.next();
            if (customer.tickTimer(delta)) {
                reputationLost++;
                if (!isScenario) {
                    toRemove.add(customer);
                }
            }
        }
        toRemove.forEach(currentOrders::remove);
        overlay.updateRecipeUI(currentOrders);
        return reputationLost;
    }

    private void updateCustomerInterval() {
        if (!isScenario) {
            customerInterval = (float) Math.max(10, customerInterval * Math.pow(0.97 + ((2 - difficulty) * 0.01), getTotalCustomerCount()));
        }
    }

    private float getCurrentCustomerTimeMultiplier() { //TODO: check this calculation
        return isScenario ? 1 :
                Math.max(1 - (getTotalCustomerCount() / 100f), 0.00001f);
    }

    private int getTotalCustomerCount() {
        return currentOrders.size() + completeOrders;
    }

    public int getCustomersServed() {
        return completeOrders;
    }

    public float getCustomerInterval() {
        return customerInterval;
    }

    public void load(int servedCount, float intervalTime, Map<Integer, String[]> customersMap) {
        this.completeOrders = servedCount;
        this.customerInterval = intervalTime;
        currentOrders.clear();
        for (String[] customerParams : customersMap.values()) {
            Customer customer = getNewCustomer();
            List<String> orderStrings = new ArrayList<>();
            for (String param : customerParams) {
                String[] splitParam = GameScreen.getParamSplit(param);
                switch (splitParam[0]) {
                    case "money":
                        customer.setMoney(Integer.parseInt(splitParam[1]));
                        break;
                    case "time_elapsed":
                        customer.setTimeElapsed(Float.parseFloat(splitParam[1]));
                        break;
                    case "max_time":
                        customer.setMaxTime(Float.parseFloat(splitParam[1]));
                        break;
                    case "order":
                        orderStrings.add(splitParam[1]
                                .replace("[", "").replace("]", ""));
                        break;
                    default:
                        orderStrings.add(splitParam[0].replace("]", ""));
                }
            }
            customer.loadOrder(orderStrings);
            currentOrders.add(customer);
        }
    }
}
