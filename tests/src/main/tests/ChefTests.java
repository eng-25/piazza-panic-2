package main.tests;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(main.tests.GdxTestRunner.class)
public class ChefTests {

    @Test
    public void testChefStackEmpty(){
        PiazzaPanicGame ppgame = new PiazzaPanicGame();

        //map initialiasation
        TiledMap map;
        map = new TmxMapLoader().load("main-game-map.tmx");
        int sizeX = map.getProperties().get("width", Integer.class);
        int sizeY = map.getProperties().get("height", Integer.class);
        float tileUnitSize = 1 / (float) map.getProperties().get("tilewidth", Integer.class);
        OrthogonalTiledMapRenderer tileMapRenderer = new OrthogonalTiledMapRenderer(map, tileUnitSize);
        MapLayer objectLayer = map.getLayers().get("Stations");
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Foreground");

        //ui initialisation
        OrthographicCamera camera = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(sizeX, sizeY, camera); // Number of tiles
        Stage stage = new Stage(viewport);
        ScreenViewport uiViewport = new ScreenViewport();
        Stage uiStage = new Stage(uiViewport);
        //this.stationUIController = new StationUIController(uiStage, game);
        UIOverlay uio = new UIOverlay(uiStage, ppgame);

        ChefManager cManager = new ChefManager(1, collisionLayer, uio);
        cManager.addChefsToStage(stage);

        Chef chefInQ = cManager.getCurrentChef();
        FixedStack<Ingredient> emptyStack = new FixedStack<>(5);

        assertEquals(chefInQ.getStack(), emptyStack);

    }
}
