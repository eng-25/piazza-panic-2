package main.tests;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Bun;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Lettuce;
import cs.eng1.piazzapanic.food.ingredients.Tomato;
import cs.eng1.piazzapanic.stations.ChoppingStation;
import cs.eng1.piazzapanic.stations.IngredientStation;
import cs.eng1.piazzapanic.stations.Station;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class StationTests {

//    @Test
//    public void testIsCorrectIngredient() {
//        FoodTextureManager FoodTextureManager = new FoodTextureManager();
//        Lettuce lettuce = new Lettuce(FoodTextureManager);
//        // ChoppingStation choppingStation = new ChoppingStation();
//
//    }



    @Test(expected = IllegalArgumentException.class)
    public void testIsIncorrectIngredientOnChoppingStation(){

        Tomato t = new Tomato(new FoodTextureManager());
        Lettuce l = new Lettuce(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());

        Ingredient[] list = new Ingredient[] {t, l, b};

        StationUIController suic = new StationUIController(new Stage(), new PiazzaPanicGame());
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suic, StationActionUI.ActionAlignment.TOP, list);


        cs.getActionTypes();
    }

}
