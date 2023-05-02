package cs.eng1.piazzapanic.food;

import cs.eng1.piazzapanic.food.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;
import static cs.eng1.piazzapanic.food.recipe.Recipe.fromString;

/**
 * A customer class, used to hold an order, timer and money given on completion.
 *
 * @author Faran Lane
 * @since 04-23
 */
public class Customer {

    private final List<Recipe> order;
    private float timeElapsed = 0f; // timer time elapsed
    private float maxTime; // maximum timer time
    private int money; // money given on order completion

    /**
     * All orders are between sizes GLOBAL_MIN_ORDER_SIZE and GLOBAL_MAX_ORDER_SIZE.
     */
    public static final int GLOBAL_MAX_ORDER_SIZE = 3;
    public static final int GLOBAL_MIN_ORDER_SIZE = 1;

    /**
     * @param possibleRecipes an array of possible recipes to be ordered by this customer
     * @param maxOrderSize    the maximum possible order size for this customer - 1 in scenario mode
     * @param timeMultiplier  a multiplier for the order max timer
     * @param difficulty      the game difficulty
     */
    public Customer(Recipe[] possibleRecipes, int maxOrderSize, float timeMultiplier, int difficulty) {
        order = new ArrayList<>();
        generateOrder(possibleRecipes, maxOrderSize);

        // the max amount that an order can increase by: 1s in hard, 4s in med, 16s in easy
        final int orderTimeVariationRange = (int) Math.pow(4, 2 - difficulty);
        final float baseOrderTime = 25f;
        maxTime = maxOrderSize * (RANDOM.nextInt(0, orderTimeVariationRange) + 1 + baseOrderTime) * timeMultiplier;
    }

    /**
     * Used to generate the customer's order and money given on reward.
     *
     * @param possibleRecipes array of possible recipes which can be ordered by this customer.
     * @param maxOrderSize    the maximum possible order size for this customer.
     */
    private void generateOrder(Recipe[] possibleRecipes, int maxOrderSize) {
        int orderSize = 1 + RANDOM.nextInt(
                Math.max(Math.min(maxOrderSize, GLOBAL_MAX_ORDER_SIZE), GLOBAL_MIN_ORDER_SIZE));
        for (int i = 0; i < orderSize; i++) {
            Recipe recipe = possibleRecipes[RANDOM.nextInt(possibleRecipes.length)];
            order.add(recipe);
        }
        money = generateOrderMoney(75, 50);
    }

    /**
     * Generates a random amount of money.
     *
     * @param baseMoney  the minimum amount of money.
     * @param bonusBound the upper bound for a bonus amount to be added to the baseAmount.
     * @return random integer between baseMoney and baseMoney+bound
     */
    private int generateOrderMoney(final int baseMoney, final int bonusBound) {
        return baseMoney + RANDOM.nextInt(bonusBound);
    }

    /**
     * Check if the customer's order contains a given recipe.
     *
     * @param toFind a given recipe to look for
     * @return the recipe in this customer's order if found, null if not found
     */
    public Recipe hasRecipe(Recipe toFind) {
        for (Recipe r : order) {
            if (toFind.getType().equals(r.getType())) {
                return r;
            }
        }
        return null;
    }

    public List<Recipe> getOrder() {
        return order;
    }

    /**
     * returns the customer's time elapsed as a float percentage of the max time
     *
     * @return time elapsed percentage
     */
    public float getTimeElapsedPercentage() {
        return Math.min(timeElapsed / maxTime, 1) * 100;
    }

    /**
     * A method to update the customer's timer, its return value is used to check if reputation should be lost.
     *
     * @param delta delta time passed since last call
     * @return true if the Customer's timer expired in this period of delta time, false otherwise.
     */
    public boolean tickTimer(float delta) {
        if (timeElapsed >= maxTime) {
            return false;
        }
        timeElapsed += delta;
        if (timeElapsed >= maxTime) {
            money /= 2;
            return true;
        }
        return false;
    }

    public void resetTimer() {
        timeElapsed = 0f;
    }

    public int getMoney() {
        return money;
    }

    public float getTimeElapsed() {
        return timeElapsed;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setTimeElapsed(float timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public void setMaxTime(float maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * Used to load an order from a given list of Recipe type strings
     *
     * @param newOrderString list of Recipe type strings
     */
    public void loadOrder(List<String> newOrderString) {
        FoodTextureManager manager = new FoodTextureManager();
        List<Recipe> newOrder = new ArrayList<>();
        for (String orderString : newOrderString) {
            newOrder.add(fromString(orderString, manager));
        }
        order.clear();
        order.addAll(newOrder);
    }


}
