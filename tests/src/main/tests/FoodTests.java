package main.tests;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.*;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.observable.Subject;
import cs.eng1.piazzapanic.stations.ChoppingStation;
import cs.eng1.piazzapanic.stations.Station;
import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.piazzapanic.ui.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(main.tests.GdxTestRunner.class)
public class FoodTests {
    private TiledMapTileLayer tmtlMock;
    private FontManager fmMock;
    private ButtonManager bmMock;
    private TutorialOverlay toMock;
    private SettingsOverlay soMock;
    private PiazzaPanicGame pMock;
    private UIOverlay  uioMock;
    private StationUIController suicMock;
    private StationActionUI sauiMock;

    @Before
    public void setup(){
        tmtlMock = Mockito.mock(TiledMapTileLayer.class);

        fmMock = Mockito.mock(FontManager.class);
        bmMock = Mockito.mock(ButtonManager.class);
        toMock = Mockito.mock(TutorialOverlay.class);
        soMock = Mockito.mock(SettingsOverlay.class);
        suicMock = Mockito.mock(StationUIController.class);
        sauiMock = Mockito.mock(StationActionUI.class);


        pMock = Mockito.mock(PiazzaPanicGame.class);
        uioMock = Mockito.mock(UIOverlay.class);

        when(pMock.getFontManager()).thenReturn(fmMock);
        when(pMock.getTutorialOverlay()).thenReturn(toMock);
        when(pMock.getButtonManager()).thenReturn(bmMock);
        when(pMock.getSettingsOverlay()).thenReturn(soMock);
    }
    //TOMATO TESTS-----------------------------------------------------------------------------------------------------
    @Test
    public void testIngredientNameTomatoRaw(){
        Tomato t = new Tomato(new FoodTextureManager());
        assertEquals("tomato_raw", t.toString());
    }
    @Test
    public void testIngredientNameTomatoChopped(){
        Tomato t = new Tomato(new FoodTextureManager());
        t.setIsChopped(true);
        assertEquals("tomato_chopped", t.toString());
    }
    @Test
    public void testIngredientTypeTomato(){
        Tomato t = new Tomato(new FoodTextureManager());
        assertEquals("tomato", t.getType());
    }
    @Test
    public void testIngredientSetIsChoppedTomatoFalse(){
        Tomato t = new Tomato(new FoodTextureManager());
        assertFalse(t.getIsChopped());
    }
    @Test
    public void testIngredientSetIsChoppedTomatoTrue(){
        Tomato t = new Tomato(new FoodTextureManager());
        t.setIsChopped(true);
        assertTrue(t.getIsChopped());
    }

    //LETTUCE TESTS---------------------------------------------------------------------------------------------------
    @Test
    public void testIngredientNameLettuceRaw(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        assertEquals("lettuce_raw", l.toString());
    }
    @Test
    public void testIngredientNameLettuceChopped(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        l.setIsChopped(true);
        assertEquals("lettuce_chopped", l.toString());
    }
    @Test
    public void testIngredientTypeLettuce(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        assertEquals("lettuce", l.getType());
    }
    @Test
    public void testIngredientSetIsChoppedLettuceFalse(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        assertFalse(l.getIsChopped());
    }
    @Test
    public void testIngredientSetIsChoppedLettuceTrue(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        l.setIsChopped(true);
        assertTrue(l.getIsChopped());
    }

    //BUN TESTS-------------------------------------------------------------------------------------------------------
    @Test
    public void testIngredientNameBunRaw(){
        Bun b = new Bun(new FoodTextureManager());
        assertEquals("bun_raw", b.toString());
    }

    //PATTY TESTS----------------------------------------------------------------------------------------------------
    @Test
    public void testIngredientNamePattyRaw(){
        Patty p = new Patty(new FoodTextureManager());
        assertEquals("patty_raw", p.toString());
    }
    @Test
    public void testIngredientNamePattyCooked(){
        Patty p = new Patty(new FoodTextureManager());
        p.setIsCooked(true);
        assertEquals("patty_cooked", p.toString());
    }
    @Test
    public void testIngredientTypePatty(){
        Patty p = new Patty(new FoodTextureManager());
        assertEquals("patty", p.getType());
    }
    @Test
    public void testIngredientIsCookedPattyFalse(){
        Patty p = new Patty(new FoodTextureManager());
        assertFalse(p.getIsCooked());
    }
    @Test
    public void testIngredientIsCookedPattyTrue(){
        Patty p = new Patty(new FoodTextureManager());
        p.setIsCooked(true);
        assertTrue(p.getIsCooked());
    }
    @Test
    public void testIngredientIsHalfCookedPattyTrue(){
        Patty p = new Patty(new FoodTextureManager());
        p.setHalfCooked();
        assertTrue(p.getIsHalfCooked());
    }
    @Test
    public void testIngredientIsHalfCookedPattyFalse(){
        Patty p = new Patty(new FoodTextureManager());
        assertFalse(p.getIsHalfCooked());
    }
    @Test
    public void testIngredientNamePattyHalfCooked(){
        Patty p = new Patty(new FoodTextureManager());
        p.setHalfCooked();
        assertEquals("patty_raw", p.toString());
    }

    //RECIPES TESTS-----------------------------------------------------------------------------
    @Test
    public void testRecipeBurgerType(){
        Burger br = new Burger(new FoodTextureManager());
        assertEquals("burger", br.getType());
    }
    @Test
    public void testRecipeBurgerIngredientsCorrect(){
        Burger br = new Burger(new FoodTextureManager());
        LinkedList<String> ingredients = new LinkedList<>();
        ingredients.add("bun");
        ingredients.add("patty_cooked");
        assertEquals(ingredients, br.getRecipeIngredients());
    }
    @Test
    public void testRecipeBurgerIngredientsIncorrect(){
        Burger br = new Burger(new FoodTextureManager());
        LinkedList<String> ingredients = new LinkedList<>();
        ingredients.add("bun");
        ingredients.add("patty_raw");
        assertNotEquals(ingredients, br.getRecipeIngredients());
    }
    @Test
    public void testRecipeSaladType(){
        Salad sl = new Salad(new FoodTextureManager());
        assertEquals("salad", sl.getType());
    }
    @Test
    public void testRecipeSaladIngredientsCorrect(){
        Salad sl = new Salad(new FoodTextureManager());
        LinkedList<String> ingredients = new LinkedList<>();
        ingredients.add("tomato_chopped");
        ingredients.add("lettuce_chopped");
        assertEquals(ingredients, sl.getRecipeIngredients());
    }
    @Test
    public void testRecipeSaladIngredientsIncorrect(){
        Salad sl = new Salad(new FoodTextureManager());
        LinkedList<String> ingredients = new LinkedList<>();
        ingredients.add("lettuce_chopped");
        ingredients.add("tomato_raw");
        assertNotEquals(ingredients, sl.getRecipeIngredients());
    }
}
