package cs.eng1.piazzapanic.powerup;

import cs.eng1.piazzapanic.food.Customer;
import cs.eng1.piazzapanic.food.CustomerManager;

public class CustomerPowerup implements ISingleUsePowerup {

    private final CustomerManager customerManager;

    public CustomerPowerup(CustomerManager manager) {
        customerManager = manager;
    }

    public void activate() {
        customerManager.getCustomers().forEach(Customer::resetTimer);
    }
}
