package cs.eng1.piazzapanic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.screens.HomeScreen;
import cs.eng1.piazzapanic.ui.overlay.*;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.FontManager;

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

  public static final Random RANDOM = new Random();

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
    difficulty = 0;
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

  public void loadGameScreen(boolean isScenarioMode) {
    gameScreen = new GameScreen(this, isScenarioMode, difficulty);
    setScreen(gameScreen);
    setupGameOverlays();
  }

  public void loadGameFromSave() {
    Preferences save = Gdx.app.getPreferences("Save");
    System.out.println(save.getString("asd"));
  }

  private void setupGameOverlays() {
    // should not need to hide ALL overlays here, but just in case
    tutorialOverlay.hide();
    settingsOverlay.hide();
    modeOverlay.hide();
    difficultyOverlay.hide();
    loadOrNewOverlay.hide();
    endOverlay.hide();
    //pauseOverlay.show();
  }

  public void setDifficulty(int newDifficulty) {
    // should never be <0 or >2
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
