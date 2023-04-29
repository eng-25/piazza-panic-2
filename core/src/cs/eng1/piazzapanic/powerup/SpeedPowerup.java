package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.chef.ChefManager;

public class SpeedPowerup extends TimedPowerup {

    private final ChefManager chefManager;

    public SpeedPowerup(ChefManager manager) {
        super();
        chefManager = manager;
    }

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
