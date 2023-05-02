package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.screen.GameScreen;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;
import static cs.eng1.piazzapanic.screen.GameScreen.MAX_LIVES;

/**
 * A single user powerup class to handle adding money and reputation
 */
public class GamePowerup implements ISingleUsePowerup {

    private final GameScreen game;
    private int type; // type to be activated - 1 = money, 2 = reputation

    /**
     * @param game current GameScreen instance
     */
    public GamePowerup(GameScreen game) {
        this.game = game;
        type = -1;
    }

    /**
     * Adds a random amount of money between 100-400
     */
    public void addMoney() {
        game.addMoney(RANDOM.nextInt(100, 400));
    }

    /**
     * Adds a reputation point back, if not at max reputation
     */
    public void addLife() {
        int currentLives = game.getLives();
        if (currentLives < MAX_LIVES) {
            game.setLives(currentLives + 1);
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
