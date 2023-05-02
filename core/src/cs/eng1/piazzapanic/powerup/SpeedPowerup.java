package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.chef.ChefManager;

/**
 * A timed powerup, increasing all chef speeds
 *
 * @author Faran Lane
 * @since 04-23
 */
public class SpeedPowerup extends TimedPowerup {

    private final ChefManager chefManager;

    /**
     * @param manager the game's ChefManager, to handle all chefs at once
     */
    public SpeedPowerup(ChefManager manager) {
        super();
        chefManager = manager;
    }

    /**
     * Activates the powerup
     *
     * @param time       how long to activate for
     * @param multiplier the speed multiplier for all chefs
     */
    public void activate(float time, float multiplier) {
        super.activate(time);
        chefManager.getChefs().forEach(chef -> chef.setSpeedMultiplier(multiplier));
    }

    @Override
    public void deactivate() {
        super.deactivate();
        chefManager.getChefs().forEach(chef -> chef.setSpeedMultiplier(1f));
    }
}
