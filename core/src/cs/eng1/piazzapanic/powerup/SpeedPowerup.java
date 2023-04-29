package cs.eng1.piazzapanic.powerup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import cs.eng1.piazzapanic.chef.ChefManager;

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

    public boolean isActive() {
        return isActive;
    }

    public float getTimer() {
        return timer;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }
}
