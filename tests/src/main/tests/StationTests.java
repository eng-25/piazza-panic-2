package main.tests;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Bun;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Lettuce;
import cs.eng1.piazzapanic.stations.ChoppingStation;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationTests {

    @Test
    public void testIsCorrectIngredient() {
        FoodTextureManager FoodTextureManager = new FoodTextureManager();
        Lettuce lettuce = new Lettuce(FoodTextureManager);
        // ChoppingStation choppingStation = new ChoppingStation();

    }

}
