package cs.eng1.piazzapanic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.Customer;
import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredient.SimpleIngredient;
import cs.eng1.piazzapanic.food.recipe.Recipe;
import cs.eng1.piazzapanic.powerup.PowerupManager;
import cs.eng1.piazzapanic.station.*;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;

import java.util.*;

/**
 * The screen which can be used to load the tilemap and keep track of everything happening in the
 * game. It does all the initialization and then lets each actor do its actions based on the current
 * frame.
 *
 * @author Faran Lane, Alistair Foggin
 * @since 12-22
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
    private final List<Station> stationList;

    private boolean isFirstFrame;
    private int reputation; // current reputation
    private int money; // current money
    private float gameTime;

    public static final int MAX_LIVES = 3;

    /**
     * @param game       the Game class, used to callback to the homescreen and set up pause screens
     * @param isScenario whether the game is scenario mode or not
     * @param difficulty the game's difficulty
     * @param load       whether the game is being loaded from save or not
     */
    public GameScreen(final PiazzaPanicGame game, boolean isScenario, int difficulty, boolean load) {
        this.isScenario = isScenario;
        this.difficulty = difficulty;

        // Select tilemap based on gamemode
        TiledMap map;
        if (isScenario) {
            map = new TmxMapLoader().load("main-game-map.tmx");
        } else {
            map = new TmxMapLoader().load("endless-map.tmx");
        }
        int sizeX = map.getProperties().get("width", Integer.class);
        int sizeY = map.getProperties().get("height", Integer.class);
        float tileUnitSize = 1 / (float) map.getProperties().get("tilewidth", Integer.class);

        // Initialize stages and camera
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

        // Create managers
        foodTextureManager = new FoodTextureManager();
        chefManager = new ChefManager(tileUnitSize * 2.5f, collisionLayer, uiOverlay, isScenario, stage, 150);
        customerManager = new CustomerManager(uiOverlay, isScenario, difficulty, this,
                game.getSettingsOverlay().getScenarioCustomerCount());
        powerupManager = new PowerupManager(stage, chefManager, this, customerManager);

        // Add tile objects
        stationList = initialiseStations(tileUnitSize, objectLayer, 12f);
        chefManager.addChefsToStage(stage);

        game.getPauseOverlay().addToStage(uiStage);
        game.getTutorialOverlay().addToStage(uiStage);
        game.getEndOverlay().addToStage(uiStage);

        isFirstFrame = !load; // stops a new customer being added in the first frame of a loaded game
    }

    /**
     * Sets up all stations from a current tilemap's object layer
     *
     * @param tileUnitSize The ratio of world units over the pixel width of a single tile/station
     * @param objectLayer  The layer on the TMX tilemap which contains all the information about the
     *                     stations and station colliders including position, bounds and station
     *                     capabilities.
     */
    private List<Station> initialiseStations(float tileUnitSize, MapLayer objectLayer, float stationFailTimer) {
        List<Station> stationList = new ArrayList<>();

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
            stationList.add(station);
        }
        return stationList;
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        // Manager and UI init
        uiOverlay.init(chefManager.getChefCost());
        chefManager.init();
        customerManager.init(foodTextureManager);
        powerupManager.init();

        // Callback for chef buy button
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

        // Reset stations
        for (Actor actor : stage.getActors().items) {
            if (actor instanceof Station) {
                ((Station) actor).reset();
            }
        }

        // initial game values
        reputation = 3;
        money = 0;
        gameTime = 0;
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

        // tick customers and update reputation
        int reputationToLose = powerupManager.getInvulnerabilityPowerup().isActive() ? 0 : customerManager.tick(delta);
        reputation = Math.max(reputation - reputationToLose, 0);
        if (reputation == 0) { // game lost
            uiOverlay.finishGameUI(false, customerManager.getCustomersServed());
        }
        uiOverlay.updateLives(reputation);

        // adds money every second
        if (!isScenario && (Math.floor(gameTime + delta) - Math.floor(gameTime) > 0)) {
            money++;
            uiOverlay.updateMoney(money);
        }
        gameTime += delta; // update game timer

        // powerup notification
        if (powerupManager.isPowerupActive()) {
            uiOverlay.tickPowerupLabelColor(delta);
            powerupManager.oneTimeMessageShown(delta);
        } else {
            uiOverlay.hidePowerupLabel();
        }

        // Render stage
        stage.draw();
        uiStage.draw();

        if (isFirstFrame) { // initial customer
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

    /**
     * Used to change money amount
     *
     * @param amount amount to change money by
     */
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

    /**
     * Called to save all needed game parameters and write to a libgdx Preferences file
     */
    public void save() {
        Preferences save = Gdx.app.getPreferences("Save");
        save.clear(); // clear old save

        // Global vars
        save.putBoolean("is_scenario", isScenario);
        save.putInteger("difficulty", difficulty);
        save.putFloat("timer", uiOverlay.getTimerTime());
        save.putInteger("money", money);
        save.putInteger("reputation", reputation);

        // Stations
        Map<String, Map<String, String>> stationMap = new HashMap<>();
        for (Station station : stationList) {
            Map<String, String> stationParams = new HashMap<>();
            String stationType = ""; // so we know which data to parse when loading back
            boolean shouldAdd = true; // IngredientStations are not saved as they have no changing game parameters
            stationParams.put("locked", String.valueOf(station.isLocked()));
            stationParams.put("in_use", String.valueOf(station.isInUse()));
            if (station instanceof CookingStation) {
                CookingStation cookingStation = (CookingStation) station;
                if (station instanceof OvenStation) {
                    stationParams.put("held_ingredients", ((OvenStation) station).getIngredientSaveMap());
                    stationType = "oven";
                } else {
                    stationType = "cooking";
                }
                SimpleIngredient currentIngredient = cookingStation.getCurrentIngredient();
                String currentIngredientString;
                if (currentIngredient == null) {
                    currentIngredientString = "null";
                } else {
                    currentIngredientString = currentIngredient.getType();
                }
                stationParams.put("current_ingredient", currentIngredientString);
                stationParams.put("time_cooked", String.valueOf(cookingStation.getTimeCooked()));
                stationParams.put("progress_visible", String.valueOf(cookingStation.isProgressVisible()));
                stationParams.put("fail_should_tick", String.valueOf(cookingStation.shouldTickFailTimer()));
                stationParams.put("fail_timer", String.valueOf(cookingStation.getFailTimer()));

            } else if (station instanceof ChoppingStation) {
                stationType = "chopping";
                ChoppingStation choppingStation = (ChoppingStation) station;
                SimpleIngredient currentIngredient = choppingStation.getCurrentIngredient();
                String currentIngredientString;
                if (currentIngredient == null) {
                    currentIngredientString = "null";
                } else {
                    currentIngredientString = currentIngredient.getType();
                }
                stationParams.put("current_ingredient", currentIngredientString);
                stationParams.put("time_chopped", String.valueOf(choppingStation.getTimeChopped()));
                stationParams.put("progress_visible", String.valueOf(choppingStation.isProgressVisible()));

            } else if (station instanceof RecipeStation) {
                stationType = "recipe";
                RecipeStation recipeStation = (RecipeStation) station;
                Recipe completedRecipe = recipeStation.getCompletedRecipe();
                String completedRecipeString;
                if (completedRecipe == null) {
                    completedRecipeString = "null";
                } else {
                    completedRecipeString = completedRecipe.getType();
                }
                stationParams.put("completed_recipe", completedRecipeString);
                stationParams.put("held_ingredients", recipeStation.getIngredientSaveMap());
            } else { // IngredientStation and any other exceptions
                shouldAdd = false;
            }
            stationParams.put("type", stationType);
            if (shouldAdd)
                stationMap.put(String.valueOf(station.getId()), stationParams);
        }
        save.putString("stations", stationMap.toString());

        // Chefs
        save.putInteger("chef_count", chefManager.getChefCount());
        save.putInteger("current_chef_index", chefManager.getCurrentChefIndex());
        Map<String, Map<String, Object>> chefsMap = new HashMap<>();
        int chefIndex = 0;
        for (Chef chef : chefManager.getChefs()) {
            Map<String, Object> chefMap = new HashMap<>();
            chefMap.put("paused", chef.isPaused());
            chefMap.put("x", chef.getX());
            chefMap.put("y", chef.getY());
            chefMap.put("rotation", chef.getImageRotation());
            List<String> chefStack = new ArrayList<>();
            while (!chef.getStack().isEmpty()) {
                chefStack.add(chef.getStack().pop().getType());
            }
            chefMap.put("stack", chefStack);
            chefsMap.put(String.valueOf(chefIndex), chefMap);
            chefIndex++;
        }
        save.putString("chefs", chefsMap.toString());

        // Customers
        save.putInteger("complete_order_count", customerManager.getCustomersServed());
        save.putFloat("customer_interval_time", customerManager.getCustomerInterval());
        Map<String, Map<String, String>> customersMap = new HashMap<>();
        int customerIndex = 0;
        for (Customer customer : customerManager.getCustomers()) {
            Map<String, String> customerMap = new HashMap<>();
            customerMap.put("money", String.valueOf(customer.getMoney()));
            customerMap.put("time_elapsed", String.valueOf(customer.getTimeElapsed()));
            customerMap.put("max_time", String.valueOf(customer.getMaxTime()));
            List<String> order = new ArrayList<>();
            customer.getOrder().forEach(r -> {
                order.add(r.getType());
            });
            customerMap.put("order", Arrays.toString(order.toArray()));
            customersMap.put(String.valueOf(customerIndex), customerMap);
            customerIndex++;
        }
        save.putString("customers", customersMap.toString());

        // Powerups
        save.putBoolean("inv_active", powerupManager.getInvulnerabilityPowerup().isActive());
        save.putFloat("inv_timer", powerupManager.getInvulnerabilityPowerup().getTimer());
        save.putBoolean("speed_active", powerupManager.getSpeedPowerup().isActive());
        save.putFloat("speed_timer", powerupManager.getSpeedPowerup().getTimer());

        save.flush();
    }

    /**
     * Called when a game is to be loaded.
     *
     * @param gameData     a map of global game data
     * @param stationData  a map of all string data
     * @param chefData     a map of all chef data
     * @param customerData a map of all customer data
     * @param powerupData  a map of all powerup data
     */
    public void loadGame(Map<String, ?> gameData, Map<Integer, String[]> stationData, Map<String, Object> chefData,
                         Map<String, Object> customerData, Map<String, Object> powerupData) {
        // Game data
        uiOverlay.setTimerTime((float) gameData.get("timer"));
        this.money = (int) gameData.get("money");
        this.reputation = (int) gameData.get("reputation");

        // Stations
        for (Station station : stationList) {
            if (stationData.containsKey(station.getId())) {
                handleStationData(station, stationData.get(station.getId()));
            }
        }

        // Chefs
        @SuppressWarnings("unchecked") // we know this should be a safe case because of how we saved the data
        Map<Integer, String[]> chefDataMap = (Map<Integer, String[]>) chefData.get("chefs");
        chefManager.load((int) chefData.get("count"), (int) chefData.get("currentIndex"),
                chefDataMap);

        // Customers
        @SuppressWarnings("unchecked") // we know this should be a safe cast because of how we saved the data
        Map<Integer, String[]> customerDataMap = (Map<Integer, String[]>) customerData.get("customers");
        customerManager.load((int) customerData.get("servedCount"), (float) customerData.get("intervalTime"),
                customerDataMap);

        // Powerups
        Object[] invData = new Object[]{powerupData.get("invActive"), powerupData.get("invTimer")};
        Object[] speedData = new Object[]{powerupData.get("speedActive"), powerupData.get("speedTimer")};
        powerupManager.load(invData, speedData);
    }

    /**
     * Loads relevant data on a given station
     *
     * @param station     station to load data of
     * @param stationData station data array to load
     */
    private void handleStationData(Station station, String[] stationData) {
        if (station instanceof OvenStation) {
            ((OvenStation) station).loadHeldIngredients(getIngredientStrings(stationData, 5));
        } else if (station instanceof RecipeStation) {
            ((RecipeStation) station).loadHeldIngredients(getIngredientStrings(stationData, 4));
        }
        for (String param : stationData) {
            station.load(getParamSplit(param));
        }
    }

    /**
     * Used to parse a list of strings into just their ingredient strings
     * For example, due to how data must be saved and loaded back,
     * an unparsed stationData String[] will look something like this:
     * ["held_ingredients=[tomato,", "potato", "cheese]"]
     * and the resulting String[] will become:
     * ["tomato", "potato", "cheese"]
     *
     * @param stationData      station data to parse
     * @param numOfIngredients number of ingredients expected - is also the number of elements in station data list
     * @return a String array of SimpleIngredient type strings e.g. ["tomato", "potato", "cheese"]
     */
    private String[] getIngredientStrings(String[] stationData, final int numOfIngredients) {
        String[] ingredientStrings = new String[numOfIngredients];
        // remove "held_ingredients=[" from first
        ingredientStrings[0] = stationData[0].split("=", 2)[1].substring(1);
        // remove "]" from end of last
        String last = stationData[numOfIngredients - 1];
        ingredientStrings[numOfIngredients - 1] = last.substring(0, last.length() - 1);
        // remaining ingredients in middle
        System.arraycopy(stationData, 1, ingredientStrings, 1, numOfIngredients - 2);
        return ingredientStrings;
    }

    /**
     * Used in loading to split an "x=y" string into String[]{x, y}
     *
     * @param param the string to split
     * @return the array of split strings
     */
    public static String[] getParamSplit(String param) {
        String[] paramSplit = param.split("=", 2);
        paramSplit[0] = paramSplit[0].replaceAll(" ", "");
        return paramSplit;
    }
}
