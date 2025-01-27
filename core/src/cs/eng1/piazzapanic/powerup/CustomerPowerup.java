package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.food.Customer;
import cs.eng1.piazzapanic.food.CustomerManager;

/**
 * A simple single-use powerup to reset all customer timers
 * @author Faran Lane
 * @since 04-23
 */
public class CustomerPowerup implements ISingleUsePowerup {

    private final CustomerManager customerManager;

    public CustomerPowerup(CustomerManager manager) {
        customerManager = manager;
    }

    public void activate() {
        customerManager.getCustomers().forEach(Customer::resetTimer);
    }
}
