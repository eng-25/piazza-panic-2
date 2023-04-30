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
import cs.eng1.piazzapanic.food.ingredients.Bun;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Lettuce;
import cs.eng1.piazzapanic.food.ingredients.Tomato;
import cs.eng1.piazzapanic.stations.ChoppingStation;
import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.piazzapanic.ui.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.*;
import java.util.EmptyStackException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//@RunWith(PowerMockRunner.class)
@RunWith(main.tests.GdxTestRunner.class)
public class ChefTests {

    private TiledMapTileLayer tmtlMock;
    private FontManager fmMock;
    private ButtonManager bmMock;
    private TutorialOverlay toMock;
    private SettingsOverlay soMock;
    private PiazzaPanicGame pMock;
    private UIOverlay  uioMock;
    @Before
    public void setup(){
        tmtlMock = Mockito.mock(TiledMapTileLayer.class);

        fmMock = Mockito.mock(FontManager.class);
        bmMock = Mockito.mock(ButtonManager.class);
        toMock = Mockito.mock(TutorialOverlay.class);
        soMock = Mockito.mock(SettingsOverlay.class);

        pMock = Mockito.mock(PiazzaPanicGame.class);
        uioMock = Mockito.mock(UIOverlay.class);

        when(pMock.getFontManager()).thenReturn(fmMock);
        when(pMock.getTutorialOverlay()).thenReturn(toMock);
        when(pMock.getButtonManager()).thenReturn(bmMock);
        when(pMock.getSettingsOverlay()).thenReturn(soMock);


    }
    //TODO - draw?,act?,getinput?, calculatemovement, getcollisionboundaries, adjustHorizontal, adjustVertical, hasingredient, placeingredient,
    //TODO 2 - get stack, setinputvector/is enabled, setpaused/ispaused, gettexture?, notifyaboutupdated stack

    //TODO - movement AND collision tests would have to be done manually i think as the methods are private and the GdxTestRunner runner is needed so powerMock can't be used
    @Test
    public void test2(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Chef cMock = Mockito.mock(Chef.class);

        c.act(3f);
    }

    //CHEF INGREDIENT STACK TESTS
    @Test //This tests checks if the chef can add an ingredient into their inventory, in this case a lettuce
    public void testCanChefPickUpIngredient(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        c.grabIngredient(l);
        assertTrue("This test will only work if the Chef has an ingredient (in this case a lettuce) in their stack", c.hasIngredient());
    }

    @Test
    public void testDoesChefHaveRightIngredient(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        c.grabIngredient(l);
        assertEquals(l, c.placeIngredient());
    }

    @Test
    public void testIsChefStackCorrectValid(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        FixedStack<Ingredient> i = new FixedStack<>(5);
        i.push(l);
        i.push(t);
        i.push(b);
        c.grabIngredient(l);
        c.grabIngredient(t);
        c.grabIngredient(b);
        assertEquals(i, c.getStack());
    }

    @Test
    public void testIsChefStackCorrectInvalid(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        Tomato t = new Tomato(new FoodTextureManager());
        Bun b = new Bun(new FoodTextureManager());
        FixedStack<Ingredient> i = new FixedStack<>(5);
        i.push(t);
        i.push(l);
        i.push(b);
        c.grabIngredient(l);
        c.grabIngredient(t);
        c.grabIngredient(b);
        assertNotEquals(i, c.getStack());
    }

    @Test //This test checks if more ingredients than the size of the fixed stack can be added using the canGrabIngredient() method
    public void testCanChefPickUpTooManyIngredientsValid(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        for(int i = 0; i < 5; i++){
            c.grabIngredient(l);
        }
        assertFalse("This test will only run if the chef's ingredient stack is full and they can't grab any more ingredients", c.canGrabIngredient());

    }

    //TODO review
    @Test(expected = EmptyStackException.class)
    public void testCanChefPickUpTooManyIngredientsInvalid(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        for(int i = 0; i < 6; i++){
            c.grabIngredient(l);
        }
        for(int i = 0; i < 6; i++){
            c.placeIngredient();
        }

    }

    @Test //This test checks that after picking up and ingredient and placing it down, the chef's stack is empty
    public void testCanChefPlaceIngredientValid(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Tomato t = new Tomato(new FoodTextureManager());
        c.grabIngredient(t);
        c.placeIngredient();
        assertFalse("This test will only work if the Chef's stack is empty", c.hasIngredient());
    }

    @Test(expected = EmptyStackException.class) //This test makes sure the chef can't place down an ingredient when their stack is empty
    public void testCanChefPlaceIngredientInvalid(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        //Bun b = new Bun(new FoodTextureManager());
        c.placeIngredient();
    }
}
