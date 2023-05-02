package cs.eng1.piazzapanic.powerup;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A base timed powerup, with a timer and an active boolean.
 * Also responsible for the invulnerability powerup.
 *
 * @author Faran Lane
 * @since 04-23
 */
public class TimedPowerup extends Actor {

    protected float timer;
    protected boolean isActive;

    public TimedPowerup() {
        isActive = false;
        timer = 0;
    }

    public void activate(float time) {
        isActive = true;
        timer = time;
    }

    public void deactivate() {
        isActive = false;
        timer = 0;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }
}
