package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.screens.GameScreen;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;
import static cs.eng1.piazzapanic.screens.GameScreen.MAX_LIVES;

public class GamePowerup {

    private final GameScreen game;

    public GamePowerup(GameScreen game) {
        this.game = game;
    }

    public void addMoney() {
        game.addMoney(RANDOM.nextInt(100, 200));
    }

    public void addLife() {
        int currentLives = game.getLives();
        if (currentLives < MAX_LIVES) {
            game.setLives(currentLives+1);
        }
    }

}
