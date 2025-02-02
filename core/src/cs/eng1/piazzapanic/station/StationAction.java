package cs.eng1.piazzapanic.station;

/**
 * A class to store station actions, and get their String message equivalents to be rendered
 *
 * @author Faran Lane, Alistair Foggin
 * @since 12-22
 */
public class StationAction {

    public enum ActionType {
        CHOP_ACTION,
        COOK_ACTION,
        FLIP_ACTION,
        PLACE_INGREDIENT,
        GRAB_INGREDIENT,
        MAKE_BURGER,
        MAKE_SALAD,
        SUBMIT_ORDER,
        CLEAR_STATION,
        BUY_STATION
    }

    public static String getActionDescription(ActionType actionType) {
        switch (actionType) {
            case CHOP_ACTION:
                return "Chop";
            case COOK_ACTION:
                return "Cook";
            case FLIP_ACTION:
                return "Flip Item";
            case GRAB_INGREDIENT:
                return "Grab Item";
            case PLACE_INGREDIENT:
                return "Place Item";
            case MAKE_BURGER:
                return "Make Burger";
            case MAKE_SALAD:
                return "Make Salad";
            case SUBMIT_ORDER:
                return "Submit Order";
            case CLEAR_STATION:
                return "Clear Station";
            case BUY_STATION:
                return "Unlock (" + Station.LOCKED_PRICE + ")";
            default:
                return "Unknown Action";
        }
    }

    public static String getFlipDescription(Station station) {
        if (station instanceof CookingStation) {
            switch (((CookingStation) station).getCurrentIngredient().getType()) {
                case "patty":
                    return "Flip Patty";
                case "beans":
                    return "Stir Beans";
                case "pizza":
                    return "Turn Pizza";
                case "jacket_potato":
                    return "Turn Potato";
            }
        }
        return "Flip Item";
    }
}
