package main.tests;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class ChoppingStationTests {

//Is Correct Ingredient
//Get action Types

//    @Test
//    public void testIsChefCloseToStation(){
//        PiazzaPanicGame ppgame = new PiazzaPanicGame();
//        final Stage uis = new Stage(); //TODO - figure out why it hates the stage
//        StationUIController suic = new StationUIController(uis, ppgame);
//        UIOverlay uio = new UIOverlay(uis, ppgame);
//        final TiledMap m = new TiledMap();
//        TiledMapTileLayer ml = (TiledMapTileLayer) m.getLayers().get(1);
//        Tomato t = new Tomato(new FoodTextureManager());
//        Lettuce l = new Lettuce(new FoodTextureManager());
//        Ingredient[] i = new Ingredient[] {t,l};
//
//        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suic, StationActionUI.ActionAlignment.TOP, i);
//        ChefManager cm = new ChefManager(1, ml, uio);
//        Chef c = cm.getCurrentChef();
//
//        cs.update(c);
//        assertNotEquals(cs.getActionTypes(), StationAction.ActionType.CHOP_ACTION);
//    }

//    @Test
//    public void test(){
//
//        PiazzaPanicGame ppgame = Mockito.mock(PiazzaPanicGame.class);
//        when(ppgame.getFontManager().getTitleFont()).thenReturn(new BitmapFont());
//        Stage stageMock = mock(Stage.class);
//        Mockito.when(stageMock.getWidth()).thenReturn(640f);
//        Mockito.when(stageMock.getHeight()).thenReturn(480f);
//        UIOverlay uio = new UIOverlay(stageMock, ppgame);
//
//
//        Chef c = new Chef(new Texture("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"),
//                new Vector2(), new ChefManager(1, new TiledMapTileLayer(1, 1, 1, 1), uio));
//
//        Mockito.verify(stageMock).getWidth();
//        Mockito.verify(stageMock).getHeight();
//
//        assertEquals(1, 1);
//    }



    //Tests to see that  only lettuce and tomato are allowed on the chopping board
//    @Test(expected = IllegalArgumentException.class)
//    public void testIsIncorrectIngredientsOnChoppingStation(){
//        StationUIController suic = new StationUIController(new Stage(), new PiazzaPanicGame());
//        Tomato t = new Tomato(new FoodTextureManager());
//        Lettuce l = new Lettuce(new FoodTextureManager());
//        Bun b = new Bun(new FoodTextureManager());
//
//        Ingredient[] list = new Ingredient[] {t, l, b};
//
//
//        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suic, StationActionUI.ActionAlignment.TOP, list);
//
//
//        cs.getActionTypes();
//    }
//    @Test
//    public void testIsCorrectIngredientsOnChoppingBoard(){
//        StationUIController suic = new StationUIController(new Stage(), new PiazzaPanicGame());
//        Tomato t = new Tomato(new FoodTextureManager());
//        Lettuce l = new Lettuce(new FoodTextureManager());
//
//        Ingredient[] list = new Ingredient[] {t, l};
//
//        ChoppingStation cs = new ChoppingStation(1, new TextureRegion(), suic, StationActionUI.ActionAlignment.TOP, list);
//
//        assertEquals(cs.getActionTypes(), StationAction.ActionType.CHOP_ACTION);
//    }

}
