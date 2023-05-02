package cs.eng1.piazzapanic.food;

import cs.eng1.piazzapanic.food.recipe.Recipe;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.station.RecipeStation;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.*;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;

/**
 * An intermediary class to manage all current customers in the game
 *
 * @author Faran Lane, Alistair Foggin
 * @since 12-22
 */
public class CustomerManager {

    private final List<Customer> currentOrders = new ArrayList<>(); // list of current customers waiting
    private final List<RecipeStation> recipeStations;
    private final UIOverlay overlay;
    private final boolean isScenario;
    private final int difficulty;
    private int completeOrders = 0; // number of complete orders
    private int maxCustomerCount; // used in scenario mode to check if all customers have been served
    private Recipe[] possibleRecipes;
    private float timeSinceCustomer;
    private float customerInterval;
    private final GameScreen gameScreen;
    private final int scenarioCustomerCount;

    public static final float SCENARIO_CUSTOMER_INTERVAL = 30f; // base time for customer interval

    /**
     * @param overlay               the UI overlay
     * @param isScenario            whether the game is scenario mode or not
     * @param difficulty            the game's difficulty
     * @param game                  an instance of the GameScreen, used to update money
     * @param scenarioCustomerCount the maximum customer count for scenario mode
     */
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
     * Initialise the CustomerManager, sets up the possible recipes, max customer count and base customer interval time.
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

    /**
     * Used to get lists of possible Recipes from a list of Recipe type strings.
     *
     * @param recipeTypes list of recipe type strings
     * @param manager     a food texture manager instance
     * @return the array of Recipes
     */
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
     * Generates a new order if the max count is not hit, then update the UI.
     */
    public void nextRecipe() {
        if ((completeOrders + currentOrders.size()) < maxCustomerCount || maxCustomerCount == -1) { // next recipe
            currentOrders.add(getNewCustomer());
        }
        notifyRecipeStations();
        overlay.updateRecipeUI(currentOrders);
    }

    /**
     * Used to remove a Recipe from the first available customer who has it in their order,
     * adding money on dish completion and attempting to add a power-up on full order completion.
     * If the max customer count has been hit, ends the game.
     *
     * @param order order which has been completed
     */
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

    /**
     * Randomly determines if a power-up should be activated or not.
     * If it lands, tells the PowerupManager to activate a random powerup.
     */
    private void tryRandomPowerup() {
        if (RANDOM.nextFloat() < 0.1 && !isScenario) {
            gameScreen.getPowerupManager().activateRandomPowerup();
        }
    }

    /**
     * Used to create a new customer
     *
     * @param maxGroupSize the maxmimum order size
     * @return the Customer created.
     */
    private Customer getNewCustomer(int maxGroupSize) {
        return new Customer(possibleRecipes.clone(), maxGroupSize, getCurrentCustomerTimeMultiplier(), difficulty);
    }

    /**
     * Convenience method to create a new customer based on the gamemode
     *
     * @return the Customer created.
     */
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

    /**
     * Used to tick all customer timers, checking if a new customer should be added to the game and how much
     * reputation should be lost based on expired timers.
     *
     * @param delta time which has passed since last call
     * @return how many reputation points should be lost
     */
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

    /**
     * Changes the customer interval timer in endless mode, based on the difficulty.
     * Allows the game to get progressively harder, scaling as the game goes on.
     */
    private void updateCustomerInterval() {
        if (!isScenario) {
            customerInterval = (float) Math.max(10, customerInterval * Math.pow(0.97 + ((2 - difficulty) * 0.01),
                    getTotalCustomerCount()));
        }
    }

    /**
     * Gets a timer multiplier to be passed into new Customers, based on how many customers have existed in the game
     * so far
     *
     * @return time multipiler
     */
    private float getCurrentCustomerTimeMultiplier() { //TODO: check this calculation
        return isScenario ? 1 :
                Math.max(1 - (getTotalCustomerCount() / 100f), 0.00001f);
    }

    /**
     * Calculates a total customer count based on numbers of current orders and complete orders.
     *
     * @return total customer count
     */
    private int getTotalCustomerCount() {
        return currentOrders.size() + completeOrders;
    }

    public int getCustomersServed() {
        return completeOrders;
    }

    public float getCustomerInterval() {
        return customerInterval;
    }

    /**
     * Used to load Customer and CustomerManager data from a save
     *
     * @param servedCount  number of served customers loaded
     * @param intervalTime current interval time loaded
     * @param customersMap map of customer parameters loaded, includes orders, timers and money data.
     */
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
