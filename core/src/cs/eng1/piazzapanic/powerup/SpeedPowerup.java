package cs.eng1.piazzapanic.powerup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;

import java.util.List;

public class SpeedPowerup extends Actor {

    private boolean isActive;
    private float timer;

    public ChefManager chefManager;

    public SpeedPowerup(ChefManager manager) {
        isActive = false;
        chefManager = manager;
    }

    public void activate(float time, float multiplier) {
        isActive = true;
        timer = time;
        chefManager.getChefs().forEach(chef -> chef.setSpeedMultiplier(multiplier));
    }

    @Override
    public void act(float delta) {
        if (isActive) {
            timer -= delta;
            if (timer <= 0) {
                deactivate();
            }
        }
    }

    public void deactivate() {
        isActive = false;
        chefManager.getChefs().forEach(chef -> chef.setSpeedMultiplier(1f));
    }
}
