package cs.eng1.piazzapanic.powerup;

import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.screen.GameScreen;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;

/**
 * An intermediary class which manages all game power-ups.
 *
 * @author Faran Lane
 * @since 04-23
 */
public class PowerupManager {

    private final TimedPowerup invulnerabilityPowerup;
    private final SpeedPowerup speedPowerup;
    private final GamePowerup gamePowerup;
    private final CustomerPowerup customerPowerup;
    private final Stage stage;

    // Timed power-up timers
    public static final float INVULNERABLE_TIME = 15f;
    public static final float SPEED_TIME = 15f;
    public static final float SPEED_MULTIPLIER = 1.75f;

    /**
     * @param gameStage       the stage to add timed powerups to
     * @param chefManager     the game's chef manager to handle chef powerups
     * @param game            the current GameScreen instance to handle money and reputation powerups
     * @param customerManager the game's customer manager to handle customer powerups
     */
    public PowerupManager(Stage gameStage, ChefManager chefManager, GameScreen game, CustomerManager customerManager) {
        this.stage = gameStage;
        invulnerabilityPowerup = new TimedPowerup();
        speedPowerup = new SpeedPowerup(chefManager);
        gamePowerup = new GamePowerup(game);
        customerPowerup = new CustomerPowerup(customerManager);
    }

    /**
     * Picks a random powerup and activates it
     */
    public void activateRandomPowerup() {
        switch (RANDOM.nextInt(0, 5)) {
            case 0:
                activateInvulnerability();
                break;
            case 1:
                activateSpeed();
                break;
            case 2:
                activateLife();
                break;
            case 3:
                activateCustomerReset();
                break;
            default:
                activateMoney();
                break;
        }
    }

    /**
     * Adds timed powerups to the game stage
     */
    public void init() {
        stage.addActor(invulnerabilityPowerup);
        stage.addActor(speedPowerup);
    }

    public TimedPowerup getInvulnerabilityPowerup() {
        return invulnerabilityPowerup;
    }

    public SpeedPowerup getSpeedPowerup() {
        return speedPowerup;
    }

    private void activateInvulnerability() {
        invulnerabilityPowerup.activate(INVULNERABLE_TIME);
    }

    private void activateSpeed() {
        speedPowerup.activate(SPEED_TIME, SPEED_MULTIPLIER);
    }

    private void activateMoney() {
        gamePowerup.activate(1);
    }

    private void activateLife() {
        gamePowerup.activate(2);
    }

    private void activateCustomerReset() {
        customerPowerup.activate();
    }

    /**
     * Used to load timed powerup data from a save
     *
     * @param invData   invulnerability powerup data array
     * @param speedData speed powerup data array
     * @throws ClassCastException in the case that both data arrays are not of the form Object[2]{Boolean, Float}
     */
    public void load(Object[] invData, Object[] speedData) throws ClassCastException {
        invulnerabilityPowerup.setActive((boolean) invData[0]);
        invulnerabilityPowerup.setTimer((float) invData[1]);

        speedPowerup.setActive((boolean) speedData[0]);
        speedPowerup.setTimer((float) speedData[1]);
    }
}
