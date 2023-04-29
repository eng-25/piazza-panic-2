package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.screens.GameScreen;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;
import static cs.eng1.piazzapanic.screens.GameScreen.MAX_LIVES;

public class GamePowerup implements ISingleUsePowerup {

    private final GameScreen game;
    private int type;

    public GamePowerup(GameScreen game) {
        this.game = game;
        type = -1;
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

    public void activate(int type) {
        this.type = type;
        activate();
    }

    @Override
    public void activate() {
        if (type == 1) {
            addMoney();
        } else if (type == 2) {
            addLife();
        }
    }
}
