package cs.eng1.piazzapanic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.SimpleIngredient;
import cs.eng1.piazzapanic.powerup.PowerupManager;
import cs.eng1.piazzapanic.stations.*;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.HashMap;

/**
 * The screen which can be used to load the tilemap and keep track of everything happening in the
 * game. It does all the initialization and then lets each actor do its actions based on the current
 * frame.
 */
public class GameScreen implements Screen {

    private final Stage stage;
    private final Stage uiStage;
    private final ChefManager chefManager;
    private final OrthogonalTiledMapRenderer tileMapRenderer;
    private final StationUIController stationUIController;
    private final UIOverlay uiOverlay;
    private final FoodTextureManager foodTextureManager;
    private final CustomerManager customerManager;
    private final PowerupManager powerupManager;
    private final boolean isScenario;
    private final int difficulty;

    private boolean isFirstFrame = true;
    private int reputation;
    private float gameTimer;
    private int money;

    public static final int MAX_LIVES = 3;

    public GameScreen(final PiazzaPanicGame game, boolean isScenario, int difficulty) {
        this.isScenario = isScenario;
        this.difficulty = difficulty;

        TiledMap map;
        if (isScenario) {
            map = new TmxMapLoader().load("main-game-map.tmx");
        } else {
            map = new TmxMapLoader().load("endless-map.tmx");
        }
        int sizeX = map.getProperties().get("width", Integer.class);
        int sizeY = map.getProperties().get("height", Integer.class);
        float tileUnitSize = 1 / (float) map.getProperties().get("tilewidth", Integer.class);

        // Initialize stage and camera
        OrthographicCamera camera = new OrthographicCamera();
        ExtendViewport viewport = new ExtendViewport(sizeX, sizeY, camera); // Number of tiles
        this.stage = new Stage(viewport);

        ScreenViewport uiViewport = new ScreenViewport();
        this.uiStage = new Stage(uiViewport);
        this.stationUIController = new StationUIController(uiStage, game);
        uiOverlay = new UIOverlay(uiStage, game, isScenario);

        // Initialize tilemap
        this.tileMapRenderer = new OrthogonalTiledMapRenderer(map, tileUnitSize);
        MapLayer objectLayer = map.getLayers().get("Stations");
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Foreground");

        foodTextureManager = new FoodTextureManager();
        chefManager = new ChefManager(tileUnitSize * 2.5f, collisionLayer, uiOverlay, isScenario, stage, 150);
        customerManager = new CustomerManager(uiOverlay, isScenario, difficulty, this);
        powerupManager = new PowerupManager(stage, chefManager, this, customerManager);

        // Add tile objects
        initialiseStations(tileUnitSize, objectLayer, 10f);
        chefManager.addChefsToStage(stage);

        game.getPauseOverlay().addToStage(uiStage);
        game.getTutorialOverlay().addToStage(uiStage);
        game.getEndOverlay().addToStage(uiStage);
    }

    /**
     * @param tileUnitSize The ratio of world units over the pixel width of a single tile/station
     * @param objectLayer  The layer on the TMX tilemap which contains all the information about the
     *                     stations and station colliders including position, bounds and station
     *                     capabilities.
     */
    private void initialiseStations(float tileUnitSize, MapLayer objectLayer, float stationFailTimer) {
        Array<TiledMapTileMapObject> tileObjects = objectLayer.getObjects()
                .getByType(TiledMapTileMapObject.class);
        Array<RectangleMapObject> colliderObjects = objectLayer.getObjects()
                .getByType(RectangleMapObject.class);
        HashMap<Integer, StationCollider> colliders = new HashMap<>();

        for (RectangleMapObject colliderObject : new Array.ArrayIterator<>(colliderObjects)) {
            Integer id = colliderObject.getProperties().get("id", Integer.class);
            StationCollider collider = new StationCollider(chefManager);
            Rectangle bounds = colliderObject.getRectangle();
            collider.setBounds(bounds.getX() * tileUnitSize, bounds.getY() * tileUnitSize,
                    bounds.getWidth() * tileUnitSize, bounds.getHeight() * tileUnitSize);
            stage.addActor(collider);
            colliders.put(id, collider);
        }

        for (TiledMapTileMapObject tileObject : new Array.ArrayIterator<>(tileObjects)) {
            // Check if it is actually a station
            if (!tileObject.getProperties().containsKey("stationType")) {
                continue;
            }

            // Get basic station properties
            Station station;
            int id = tileObject.getProperties().get("id", Integer.class);
            String ingredients = tileObject.getProperties().get("ingredients", String.class);
            StationActionUI.ActionAlignment alignment = StationActionUI.ActionAlignment.valueOf(
                    tileObject.getProperties().get("actionAlignment", "TOP", String.class));
            boolean locked = tileObject.getProperties().get("locked", false, Boolean.class);

            // Initialize specific station types
            switch (tileObject.getProperties().get("stationType", String.class)) {
                case "cookingStation":
                    station = new CookingStation(id, tileObject.getTextureRegion(), stationUIController,
                            alignment, SimpleIngredient.arrayFromString(ingredients, foodTextureManager), stationFailTimer,
                            isScenario, locked, this);
                    break;
                case "ingredientStation":
                    station = new IngredientStation(id, tileObject.getTextureRegion(), stationUIController,
                            alignment, SimpleIngredient.fromString(ingredients, foodTextureManager),
                            isScenario, locked, this);
                    break;
                case "choppingStation":
                    station = new ChoppingStation(id, tileObject.getTextureRegion(), stationUIController,
                            alignment, SimpleIngredient.arrayFromString(ingredients, foodTextureManager),
                            isScenario, locked, this);
                    break;
                case "recipeStation":
                    station = new RecipeStation(id, tileObject.getTextureRegion(), stationUIController,
                            alignment, foodTextureManager, customerManager, isScenario, this);
                    customerManager.addRecipeStation((RecipeStation) station);
                    break;
                case "ovenStation":
                    station = new OvenStation(id, tileObject.getTextureRegion(), stationUIController,
                            alignment, SimpleIngredient.arrayFromString(ingredients, foodTextureManager), stationFailTimer,
                            isScenario, locked, this);
                    break;
                default:
                    station = new Station(id, tileObject.getTextureRegion(), stationUIController, alignment,
                            isScenario, locked, this);
            }
            float tileX = tileObject.getX() * tileUnitSize;
            float tileY = tileObject.getY() * tileUnitSize;
            float rotation = tileObject.getRotation();

            // Adjust x and y positions based on Tiled quirks with rotation changing the position of the tile
            if (rotation == 90) {
                tileY -= 1;
            } else if (rotation == 180) {
                tileX -= 1;
                tileY -= 1;
            } else if (rotation == -90 || rotation == 270) {
                tileX -= 1;
            }

            station.setBounds(tileX, tileY, 1, 1);
            station.setImageRotation(-tileObject.getRotation());
            stage.addActor(station);

            String colliderIDs = tileObject.getProperties().get("collisionObjectIDs", String.class);
            for (String idString : colliderIDs.split(",")) {
                try {
                    Integer colliderID = Integer.parseInt(idString);
                    StationCollider collider = colliders.get(colliderID);
                    if (collider != null) {
                        collider.register(station);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing collider ID: " + e.getMessage());
                }

            }
        }
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        uiOverlay.init(chefManager.getChefCost());
        chefManager.init();
        ClickListener callbackToBuy = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int cost = chefManager.getChefCost();
                if (money > cost) {
                    chefManager.addNewChef();
                    addMoney(-cost);
                }
            }
        };
        uiOverlay.addBuyChefButton(callbackToBuy);

        customerManager.init(foodTextureManager);
        powerupManager.init();

        for (Actor actor : stage.getActors().items) {
            if (actor instanceof Station) {
                ((Station) actor).reset();
            }
        }
        isFirstFrame = true;

        gameTimer = 0;
        reputation = 3;
        money = 0;
        uiOverlay.updateLives(reputation);
        uiOverlay.updateMoney(money);
    }

    @Override
    public void render(float delta) {
        // Initialize screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getCamera().update();
        uiStage.getCamera().update();

        // Render background
        tileMapRenderer.setView((OrthographicCamera) stage.getCamera());
        tileMapRenderer.render();

        // Stage actions
        stage.act(delta);
        uiStage.act(delta);

        gameTimer += delta;
        int reputationToLose = powerupManager.getInvulnerabilityPowerup().isActive() ? 0 : customerManager.tick(delta);
        reputation = Math.max(reputation - reputationToLose, 0);
        if (reputation == 0) {
            uiOverlay.finishGameUI(false, customerManager.getCustomersServed());
        }
        uiOverlay.updateLives(reputation);

        // Render stage
        stage.draw();
        uiStage.draw();

        if (isFirstFrame) {
            customerManager.nextRecipe();
            isFirstFrame = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
        this.uiStage.getViewport().update(width, height, true);
        uiOverlay.resizeUI(width, customerManager.getCustomers());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        uiStage.dispose();
        tileMapRenderer.dispose();
        foodTextureManager.dispose();
        chefManager.dispose();
    }

    public void addMoney(int amount) {
        money += amount;
        uiOverlay.updateMoney(money);
    }

    public int getMoney() {
        return money;
    }

    public int getLives() {
        return reputation;
    }

    public void setLives(int lives) {
        reputation = lives;
    }

    public PowerupManager getPowerupManager() {
        return powerupManager;
    }
}
