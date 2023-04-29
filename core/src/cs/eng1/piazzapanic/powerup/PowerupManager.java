package cs.eng1.piazzapanic.powerup;

import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.screens.GameScreen;

import java.util.Map;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;

public class PowerupManager {

    private final InvulnerabilityPowerup invulnerabilityPowerup;
    private final SpeedPowerup speedPowerup;
    private final GamePowerup gamePowerup;
    private final CustomerPowerup customerPowerup;
    private final Stage stage;

    public static final float INVULNERABLE_TIME = 15f;

    public static final float SPEED_TIME = 15f;
    public static final float SPEED_MULTIPLIER = 1.75f;

    public PowerupManager(Stage gameStage, ChefManager chefManager, GameScreen game, CustomerManager customerManager) {
        this.stage = gameStage;
        invulnerabilityPowerup = new InvulnerabilityPowerup();
        speedPowerup = new SpeedPowerup(chefManager);
        gamePowerup = new GamePowerup(game);
        customerPowerup = new CustomerPowerup(customerManager);
    }

    public void addPowerup() {
        switch(RANDOM.nextInt(0, 5)) {
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

    public void init() {
        stage.addActor(invulnerabilityPowerup);
        stage.addActor(speedPowerup);
    }

    public InvulnerabilityPowerup getInvulnerabilityPowerup() {
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
        gamePowerup.addMoney();
    }

    private void activateLife() {
        gamePowerup.addLife();
    }

    private void activateCustomerReset() {
        customerPowerup.activate();
    }

    public void load(Object[] invData, Object[] speedData) throws ClassCastException {
        invulnerabilityPowerup.setActive((boolean) invData[0]);
        invulnerabilityPowerup.setTimer((float) invData[1]);

        speedPowerup.setActive((boolean) speedData[0]);
        speedPowerup.setTimer((float) speedData[1]);
    }
}
