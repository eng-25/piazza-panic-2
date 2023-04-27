package cs.eng1.piazzapanic.food;

import cs.eng1.piazzapanic.food.recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;

public class Customer {

    private final List<Recipe> order;
    private float timeElapsed = 0f;
    private final float maxTime;
    private int money;

    public static final int GLOBAL_MAX_ORDER_SIZE = 3;
    public static final int GLOBAL_MIN_ORDER_SIZE = 1;

    public Customer(Recipe[] possibleRecipes, int maxOrderSize, float timeMultiplier, int difficulty) {
        order = new ArrayList<>();
        generateOrder(possibleRecipes, maxOrderSize);

        // the max amount that an order can increase by: 1s in hard, 4s in med, 16s in easy
        final int orderTimeVariationRange = (int) Math.pow(4, 2-difficulty);
        final float baseOrderTime = 25f;
        maxTime = maxOrderSize * (RANDOM.nextInt(0, orderTimeVariationRange)+1 + baseOrderTime) * timeMultiplier;
    }

    private void generateOrder(Recipe[] possibleRecipes, int maxOrderSize) {
        int orderSize = 1 + RANDOM.nextInt(
                Math.max(Math.min(maxOrderSize, GLOBAL_MAX_ORDER_SIZE), GLOBAL_MIN_ORDER_SIZE));
        for (int i=0; i<orderSize; i++) {
            Recipe recipe = possibleRecipes[RANDOM.nextInt(possibleRecipes.length)];
            order.add(recipe);
        }
        money = generateOrderMoney();
    }

    private int generateOrderMoney() {
        int baseMoney = 75;
        return baseMoney + RANDOM.nextInt(50);
    }

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

    public float getTimeElapsedPercentage() {
        return Math.min(timeElapsed/maxTime, 1); // return 100% in the case that timeElapsed > maxTime
    }

    /**
     * A method to update the customer's timer, its return value is used to check if reputation should be lost.
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
}
