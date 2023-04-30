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
public class CustomerManagerTests {
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

    @Test
    public void testXY(){
        CustomerManager cm = new CustomerManager(uioMock);
        //Burger br = new Burger(new FoodTextureManager());
        Recipe rc = new Recipe("burger", new FoodTextureManager());
        cm.init(new FoodTextureManager());
        cm.nextRecipe();
        assertTrue(cm.checkRecipe(rc));
    }
}
