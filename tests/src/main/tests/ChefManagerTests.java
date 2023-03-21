package main.tests;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

//@RunWith(GdxTestRunner.class)
//public class ChefManagerTests {
//
//    @Test
//    public void test1(){
//
//        TiledMap tm  = new TmxMapLoader().load("main-game-map.tmx");
//        TiledMapTileLayer tml = (TiledMapTileLayer) tm.getLayers().get("Foreground");
//        UIOverlay uio = new UIOverlay(new Stage(), new PiazzaPanicGame());
//        ChefManager cs = new ChefManager(1, tml, uio);
//
//        cs.getChefs();
//    }
//}
