package cs.eng1.piazzapanic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.screen.HomeScreen;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.FontManager;
import cs.eng1.piazzapanic.ui.overlay.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PiazzaPanicGame extends Game {

    private FontManager fontManager;
    private ButtonManager buttonManager;
    private GameScreen gameScreen;
    private HomeScreen homeScreen;
    private TutorialOverlay tutorialOverlay;
    private SettingsOverlay settingsOverlay;
    private ModeOverlay modeOverlay;
    private DifficultyOverlay difficultyOverlay;
    private LoadOrNewOverlay loadOrNewOverlay;
    private EndOverlay endOverlay;
    private PauseOverlay pauseOverlay;
    private int difficulty;

    public static final Random RANDOM = new Random(); // a global static random instance

    /**
     * Creates all overlays and loads the home screen.
     */
    @Override
    public void create() {
        fontManager = new FontManager();
        buttonManager = new ButtonManager(fontManager);
        tutorialOverlay = new TutorialOverlay(this);
        settingsOverlay = new SettingsOverlay(this);
        modeOverlay = new ModeOverlay(this);
        difficultyOverlay = new DifficultyOverlay(this);
        loadOrNewOverlay = new LoadOrNewOverlay(this);
        endOverlay = new EndOverlay(this);
        pauseOverlay = new PauseOverlay(this);
        difficulty = 1; // medium default
        loadHomeScreen();
    }

    @Override
    public void dispose() {
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        if (homeScreen != null) {
            homeScreen.dispose();
        }
        fontManager.dispose();
        buttonManager.dispose();
    }

    public void loadHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen(this);
        }
        setScreen(homeScreen);
    }

    /**
     * Sets the game screen
     *
     * @param isScenarioMode whether the game is scenario mode or not
     * @param load           if the game has been loaded from save or not
     */
    public void loadGameScreen(boolean isScenarioMode, boolean load) {
        gameScreen = new GameScreen(this, isScenarioMode, difficulty, load);
        setScreen(gameScreen);
        setupGameOverlays();
    }

    /**
     * Called when loading from save
     */
    public void loadGameFromSave() {
        Preferences save = Gdx.app.getPreferences("Save");
        if (save.get().size() == 0) { // prevents crash on no save file being present
            return;
        }

        // Station data
        Map<Integer, String[]> stationData = parseSavedMap(save.getString("stations"));

        // Chef data
        Map<String, Object> chefData = new HashMap<>();
        chefData.put("count", save.getInteger("chef_count"));
        chefData.put("currentIndex", save.getInteger("current_chef_index"));
        chefData.put("chefs", parseSavedMap(save.getString("chefs")));

        // Customer data
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("servedCount", save.getInteger("complete_order_count"));
        customerData.put("intervalTime", save.getFloat("customer_interval_time"));
        customerData.put("customers", parseSavedMap(save.getString("customers")));

        // Powerups
        Map<String, Object> powerupData = Map.of(
                "invActive", save.getBoolean("inv_active"),
                "invTimer", save.getFloat("inv_timer"),
                "speedActive", save.getBoolean("speed_active"),
                "speedTimer", save.getFloat("speed_timer")
        );

        // Difficulty
        setDifficulty(save.getInteger("difficulty"));

        // Other game data
        Map<String, ?> gameData = Map.of(
                "timer", save.getFloat("timer"),
                "money", save.getInteger("money"),
                "reputation", save.getInteger("reputation")
        );

        // Create game
        loadGameScreen(save.getBoolean("is_scenario"), true);

        // Load data
        gameScreen.loadGame(gameData, stationData, chefData, customerData, powerupData);
    }

    /**
     * Parses a map string back the way it was loaded.
     *
     * @param toParse map string to parse
     * @return Map of data from parsed strings
     */
    private Map<Integer, String[]> parseSavedMap(String toParse) {
        toParse = toParse.substring(1, toParse.length() - 2); // remove outer { and }
        String[] paramList = toParse.split("},");
        Map<Integer, String[]> paramMap = new HashMap<>();
        for (String param : paramList) {
            String[] indexSplit = param.split("=", 2);
            String[] params = indexSplit[1].substring(1).split(",");
            int id = Integer.parseInt(indexSplit[0].replaceAll(" ", ""));
            paramMap.put(id, params);
        }
        return paramMap;
    }

    private void setupGameOverlays() {
        // should not need to hide ALL overlays here, but just in case
        tutorialOverlay.hide();
        settingsOverlay.hide();
        modeOverlay.hide();
        difficultyOverlay.hide();
        loadOrNewOverlay.hide();
        endOverlay.hide();
    }

    /**
     * Updates the game's difficulty
     *
     * @param newDifficulty updated difficulty - must be 0-2 inclusive
     */
    public void setDifficulty(int newDifficulty) {
        if (newDifficulty <= 0) {
            difficulty = 0;
        } else if (newDifficulty >= 2) {
            difficulty = 2;
        } else {
            difficulty = 1;
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public TutorialOverlay getTutorialOverlay() {
        return tutorialOverlay;
    }

    public SettingsOverlay getSettingsOverlay() {
        return settingsOverlay;
    }

    public LoadOrNewOverlay getLoadOrNewOverlay() {
        return loadOrNewOverlay;
    }

    public PauseOverlay getPauseOverlay() {
        return pauseOverlay;
    }

    public EndOverlay getEndOverlay() {
        return endOverlay;
    }

    public ModeOverlay getModeOverlay() {
        return modeOverlay;
    }

    public DifficultyOverlay getDifficultyOverlay() {
        return difficultyOverlay;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public ButtonManager getButtonManager() {
        return buttonManager;
    }
}
