package cs.eng1.piazzapanic.food;

import cs.eng1.piazzapanic.food.recipes.Recipe;

import java.util.ArrayList;
import java.util.List;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;

public class Customer {

    private final List<Recipe> order;
    private float timeElapsed = 0f;
    private final float maxTime;

    public static final int GLOBAL_MAX_ORDER_SIZE = 3;
    public static final int GLOBAL_MIN_ORDER_SIZE = 1;

    public Customer(Recipe[] possibleRecipes, int maxOrderSize, boolean isScenario, int difficulty) {
        order = new ArrayList<>();
        generateOrder(possibleRecipes, maxOrderSize);

        //TODO: base these values on difficulty and game mode (incorporate total game time in endless?)
        final int orderTimeVariationRange = (int) Math.pow(3, 2-difficulty);
        final float baseOrderTime = 5f;
        maxTime = maxOrderSize * (RANDOM.nextInt(orderTimeVariationRange) + baseOrderTime);
    }

    private void generateOrder(Recipe[] possibleRecipes, int maxOrderSize) {
        int orderSize = 1 + RANDOM.nextInt(
                Math.max(Math.min(maxOrderSize, GLOBAL_MAX_ORDER_SIZE), GLOBAL_MIN_ORDER_SIZE));
        for (int i=0; i<orderSize; i++) {
            Recipe recipe = possibleRecipes[RANDOM.nextInt(possibleRecipes.length)];
            order.add(recipe);
        }
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
        return timeElapsed >= maxTime;
    }
}
