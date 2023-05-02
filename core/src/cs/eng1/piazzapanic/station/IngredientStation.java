package cs.eng1.piazzapanic.station;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.ingredient.SimpleIngredient;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;

import java.util.LinkedList;
import java.util.List;

/**
 * A station for fetching ingredients and giving them to the nearby chef.
 * Holds infinite amounts of its specified ingredient.
 *
 * @author Faran Lane, Alistair Foggin, Matt Fitzpatrick
 * @since 12-22
 */
public class IngredientStation extends Station {

    protected final SimpleIngredient ingredientDispensed;

    /**
     * @param id           unique station identifier
     * @param image        station texture
     * @param uiController UI controller to get station actions from
     * @param alignment    directional alignment of station actions
     * @param ingredient   ingredient to be dispensed
     * @param isScenario   whether the game is scenario mode or not
     * @param locked       if the station is locked or not
     * @param game         current GameScreen instance
     */
    public IngredientStation(int id, TextureRegion image, StationUIController uiController,
                             StationActionUI.ActionAlignment alignment, SimpleIngredient ingredient,
                             boolean isScenario, boolean locked, GameScreen game) {
        super(id, image, uiController, alignment, isScenario, locked, game);
        ingredientDispensed = ingredient; //What ingredient the station will give to the player.
    }

    @Override
    public List<StationAction.ActionType> getActionTypes() {
        LinkedList<StationAction.ActionType> actionTypes = new LinkedList<>();
        if (nearbyChef == null) {
            return actionTypes;
        }
        if (nearbyChef.canGrabIngredient()) {
            actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
        }
        return actionTypes;
    }

    @Override
    public void doStationAction(StationAction.ActionType action) {
        if (action == StationAction.ActionType.GRAB_INGREDIENT) {
            if (this.nearbyChef != null && nearbyChef.canGrabIngredient()) {
                nearbyChef.grabIngredient(SimpleIngredient.fromString(ingredientDispensed.getType(),
                        ingredientDispensed.getTextureManager()));
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (ingredientDispensed != null) {
            drawFoodTexture(batch, ingredientDispensed.getTexture());
        }
    }
}
