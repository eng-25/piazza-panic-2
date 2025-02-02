package cs.eng1.piazzapanic.food.ingredient;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * An ingredient to be cooked which can be burnt, holding multiple textures based on its cook status
 *
 * @author Faran Lane
 * @since 04-23
 */
public class CookedIngredient extends SimpleIngredient {

    protected boolean halfCooked;
    private boolean isBurnt;

    public CookedIngredient(String type, FoodTextureManager manager) {
        super(type, manager);
        halfCooked = isBurnt = false;
    }

    public void setHalfCooked() {
        halfCooked = true;
    }

    public boolean isHalfCooked() {
        return halfCooked;
    }

    public void setBurnt() {
        isBurnt = true;
    }

    /**
     * Get the texture based on whether the patty is raw, cooked or burnt.
     *
     * @return the texture to display.
     */
    @Override
    public Texture getTexture() {
        String form;
        if (isBurnt) {
            form = "burnt";
        } else if (isCooked) {
            form = "cooked";
        } else {
            form = "raw";
        }
        return textureManager.getTexture(getType() + "_" + form);
    }
}
