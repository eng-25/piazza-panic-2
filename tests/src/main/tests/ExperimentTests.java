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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class ExperimentTests {
    //private BitmapFont bmfMock;
    private TiledMapTileLayer tmtlMock;
    private FontManager fmMock;
    private ButtonManager bmMock;
    private TutorialOverlay toMock;
    private SettingsOverlay soMock;
    private PiazzaPanicGame pMock;
    private UIOverlay  uioMock;

    @Before
    public void setup(){
        //bmfMock = Mockito.mock(BitmapFont.class);
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

        //when(pMock.getFontManager().getTitleFont()).thenReturn(bmfMock);


    }

    @Test
    public void test1(){
        ChefManager cm = new ChefManager(1, tmtlMock, uioMock);
        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
                new Vector2(), cm);
        Lettuce l = new Lettuce(new FoodTextureManager());
        c.grabIngredient(l);
        assertTrue("This test will only work if the Chef has an ingredient (in this case a lettuce) in their stack", c.hasIngredient());

    }
}
