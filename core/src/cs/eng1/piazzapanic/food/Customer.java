package cs.eng1.piazzapanic.food;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.ui.FontManager;
import cs.eng1.piazzapanic.ui.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {

    private final List<Recipe> order;
    private final Timer timer;

    public Customer(Recipe[] possibleRecipes, int maxOrderSize) {
        order = new ArrayList<>();
        timer = new Timer(new Label.LabelStyle(new FontManager().getLabelFont(), Color.BLACK));

        generateOrder(possibleRecipes, maxOrderSize);
    }

    private void generateOrder(Recipe[] possibleRecipes, int maxOrderSize) {
        Random random = new Random();
        int orderSize = random.nextInt(Math.max(Math.min(maxOrderSize, 3), 1))+1; // Bounds of 1-3 group size
        for (int i=0; i<orderSize; i++){
            Recipe r = possibleRecipes[random.nextInt(possibleRecipes.length)];
            order.add(r);
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

}
