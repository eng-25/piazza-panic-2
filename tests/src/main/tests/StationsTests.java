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
import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.*;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.observable.Observer;
import cs.eng1.piazzapanic.observable.Subject;
import cs.eng1.piazzapanic.stations.*;
import cs.eng1.piazzapanic.ui.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//@RunWith(PowerMockRunner.class)
@RunWith(main.tests.GdxTestRunner.class)
public class StationsTests {
    private TiledMapTileLayer tmtlMock;
    private FontManager fmMock;
    private ButtonManager bmMock;
    private TutorialOverlay toMock;
    private SettingsOverlay soMock;
    private PiazzaPanicGame pMock;
    private UIOverlay  uioMock;
    private StationUIController suicMock;
    private StationActionUI sauiMock;
    Lettuce l, lc;
    Tomato t;
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
    /*
    -----------------------------------------------------STATION + CHOPPING STATION TESTS
     */

    @Test
    public void testx(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Chef c2 = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        cs.update(c);
        cs.update(c2);
    }
    @Test
    public void testChoppingStationWithNoChef(){
        Chef c = null;
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, cs.getActionTypes());
    }

    @Test
    public void testChoppingStationChefWithNoIngredient(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testChoppingStationChefWithCorrectIngredient(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.PLACE_INGREDIENT);
        assertEquals(actions, cs.getActionTypes());

    }

    @Test
    public void testChoppingStationChefWithIncorrectIngredient(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(b);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.PLACE_INGREDIENT);
        assertNotEquals(actions, cs.getActionTypes());
    }

    @Test
    public void testChoppingStationPlaceIngredient(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.CHOP_ACTION);
        //actions.add(StationAction.ActionType.GRAB_INGREDIENT);
        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testChoppingStationChopActionInvalidTime(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(4f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.GRAB_INGREDIENT);

        assertNotEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testChoppingStationChopActionValidTime(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(5f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.GRAB_INGREDIENT);

        assertEquals(actions, cs.getActionTypes());
    }

    @Test
    public void testChoppingStationChopActionValidTime2(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(6f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.GRAB_INGREDIENT);

        assertEquals(actions, cs.getActionTypes());
    }
    @Test(expected = NullPointerException.class)
    public void testChoppingStationChoppedIngredient(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        l.setIsChopped(true);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(6f);
    }

    @Test
    public void testChoppingStationGrabIngredientAction(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(6f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.GRAB_INGREDIENT);

        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testChoppingStationReturnsCorrectIngredient(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(6f);
        cs.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        FixedStack<Ingredient> ingredientStack = new FixedStack<>(5);
        l.setIsChopped(true);
        ingredientStack.add(l);
        assertEquals(ingredientStack, c.getStack());
    }
    @Test
    public void testChoppingStationReturnsCorrectIngredient2(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{l,t};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(l);
        c.grabIngredient(t);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.CHOP_ACTION);
        cs.act(6f);
        cs.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        FixedStack<Ingredient> ingredientStack = new FixedStack<>(5);
        l.setIsChopped(true);
        ingredientStack.add(l);
        ingredientStack.add(t);
        assertEquals(ingredientStack, c.getStack());
    }
    //---------------------------------------------------------------COOKING STATION---------------------------------
    @Test
    public void testCookingStationNoChef(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = null;
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testCookingStationChefWithNoIngredient(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testCookingStationChefWithCorrectIngredient(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.PLACE_INGREDIENT);
        assertEquals(actions, cs.getActionTypes());
    }

    @Test
    public void testCookingStationChefWithIncorrectIngredient(){
        Patty p = new Patty(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(b);
        cs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.PLACE_INGREDIENT);
        assertNotEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testCookingStationPlaceIngredient(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        //actions.add(StationAction.ActionType.GRAB_INGREDIENT);
        actions.add(StationAction.ActionType.COOK_ACTION);
        assertEquals(actions, cs.getActionTypes());
    }

    @Test
    public void testCookingStationCookActionValidTime(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.COOK_ACTION);
        cs.act(10f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.FLIP_ACTION);
        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testCookingStationCookActionInvalidTime(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.COOK_ACTION);
        cs.act(9f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.FLIP_ACTION);
        assertNotEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testCookingStationFlipAction(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.COOK_ACTION);
        cs.act(10f);
        cs.doStationAction(StationAction.ActionType.FLIP_ACTION);
        cs.act(10f);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.GRAB_INGREDIENT);
        assertEquals(actions, cs.getActionTypes());
    }
    @Test
    public void testCookingStationHalfCookedPatty(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.COOK_ACTION);
        cs.act(10f);
        cs.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        FixedStack<Ingredient> ingredientStack = new FixedStack<>(5);
        p.setHalfCooked();
        ingredientStack.add(p);
        assertEquals(ingredientStack, c.getStack());
    }
    @Test
    public void testCookingStationCookedPatty(){
        Patty p = new Patty(new FoodTextureManager());
        Ingredient[] i = new Ingredient[]{p};
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CookingStation cs = new CookingStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, i);
        c.grabIngredient(p);
        cs.update(c);
        cs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        cs.doStationAction(StationAction.ActionType.COOK_ACTION);
        cs.act(10f);
        cs.doStationAction(StationAction.ActionType.FLIP_ACTION);
        cs.act(10f);
        cs.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        FixedStack<Ingredient> ingredientStack = new FixedStack<>(5);
        p.getIsCooked();
        ingredientStack.add(p);
        assertEquals(ingredientStack, c.getStack());
    }
    //----------------------------------------------------------------------INGREDIENT STATION--------------------------

    @Test
    public void testIngredientStationNoChef(){
        Tomato t = new Tomato(new FoodTextureManager());
        IngredientStation is = new IngredientStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, t);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = null;
        is.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, is.getActionTypes());
    }
    @Test
    public void testIngredientStationWithChef(){
        Tomato t = new Tomato(new FoodTextureManager());
        IngredientStation is = new IngredientStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, t);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        is.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.GRAB_INGREDIENT);
        assertEquals(actions, is.getActionTypes());
    }
    @Test
    public void testIngredientStationGrabIngredientValid(){
        Tomato t = new Tomato(new FoodTextureManager());
        IngredientStation is = new IngredientStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, t);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        assertFalse(c.hasIngredient());
        is.update(c);
        is.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        assertTrue(c.hasIngredient());
    }
    @Test
    public void testIngredientStationGrabLastIngredient(){
        Tomato t = new Tomato(new FoodTextureManager());
        IngredientStation is = new IngredientStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, t);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        c.grabIngredient(t);
        c.grabIngredient(t);
        c.grabIngredient(t);
        c.grabIngredient(t);
        assertTrue(c.canGrabIngredient());
        is.update(c);
        is.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        assertFalse(c.canGrabIngredient());
    }
    @Test//(expected = StackOverflowError.class)
    public void testIngredientStationGrabIngredientInvalid(){
        Tomato t = new Tomato(new FoodTextureManager());
        IngredientStation is = new IngredientStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, t);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        c.grabIngredient(t);
        c.grabIngredient(t);
        c.grabIngredient(t);
        c.grabIngredient(t);
        c.grabIngredient(t);
        assertFalse(c.canGrabIngredient());
        is.update(c);
        is.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
    }
    //---------------------------------------------------------RECIPE STATION------------------------------------------------------
    @Test
    public void testRecipeStationNoChef(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = null;
        //Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
        //        new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        rs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationChefNoIngredient(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        rs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationPlaceableIngredients(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Patty p = new Patty(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        t.setIsChopped(true);
        l.setIsChopped(true);
        p.setIsCooked(true);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(b);
        c.grabIngredient(p);
        c.grabIngredient(t);
        c.grabIngredient(l);

        rs.update(c);
        //Lettuce check
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.PLACE_INGREDIENT);
        assertEquals(actions, rs.getActionTypes());
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        //Tomato check
        assertEquals(actions, rs.getActionTypes());
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        actions.add(StationAction.ActionType.MAKE_SALAD);
        //Patty check
        assertEquals(actions, rs.getActionTypes());
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        //Bun check
        assertEquals(actions, rs.getActionTypes());
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        actions.add(StationAction.ActionType.MAKE_BURGER);
        //Chef stack empty
        actions.remove();
        assertFalse(c.hasIngredient());
    }
    @Test
    public void testRecipeStationInvalidIngredient(){
        Tomato t = new Tomato(new FoodTextureManager());
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(t);
        rs.update(c);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.PLACE_INGREDIENT);
        assertNotEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationBurgerIngredients(){
        Patty p = new Patty(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        p.setIsCooked(true);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(p);
        c.grabIngredient(b);
        rs.update(c);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.MAKE_BURGER);
        assertEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationSaladIngredients(){
        Tomato t = new Tomato(new FoodTextureManager());
        Lettuce l = new Lettuce(new FoodTextureManager());
        t.setIsChopped(true);
        l.setIsChopped(true);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(t);
        c.grabIngredient(l);
        rs.update(c);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.MAKE_SALAD);
        assertEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationMakeBurger(){
        Patty p = new Patty(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        Burger br = new Burger(new FoodTextureManager());
        p.setIsCooked(true);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        //Initialising the Customer Manager to check the submission of the orders
        cmm.init(new FoodTextureManager());
        //First burger order appears as second order always, as the orders are manually ordered and not randomised
        cmm.nextRecipe();
        cmm.nextRecipe();
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(p);
        c.grabIngredient(b);
        rs.update(c);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.MAKE_BURGER);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.SUBMIT_ORDER);
        assertEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationMakeSalad(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Salad sl = new Salad(new FoodTextureManager());
        l.setIsChopped(true);
        t.setIsChopped(true);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        //Initialising the Customer Manager to check the submission of the orders
        cmm.init(new FoodTextureManager());
        //First salad order appears as first order always, as the orders are manually ordered and not randomised
        cmm.nextRecipe();
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(l);
        c.grabIngredient(t);
        rs.update(c);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.MAKE_SALAD);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        actions.add(StationAction.ActionType.SUBMIT_ORDER);
        assertEquals(actions, rs.getActionTypes());
    }
    @Test
    public void testRecipeStationSubmitOrder(){
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Salad sl = new Salad(new FoodTextureManager());
        l.setIsChopped(true);
        t.setIsChopped(true);
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        CustomerManager cmm = new CustomerManager(uioMock);
        //Initialising the Customer Manager to check the submission of the orders
        cmm.init(new FoodTextureManager());
        //First salad order appears as first order always, as the orders are manually ordered and not randomised
        cmm.nextRecipe();
        RecipeStation rs = new RecipeStation(1, new TextureRegion(), suicMock, StationActionUI.ActionAlignment.TOP, new FoodTextureManager(), cmm);
        c.grabIngredient(l);
        c.grabIngredient(t);
        rs.update(c);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.PLACE_INGREDIENT);
        rs.doStationAction(StationAction.ActionType.MAKE_SALAD);
        rs.doStationAction(StationAction.ActionType.SUBMIT_ORDER);
        LinkedList<StationAction.ActionType> actions = new LinkedList<>();
        assertEquals(actions, rs.getActionTypes());
    }
}
