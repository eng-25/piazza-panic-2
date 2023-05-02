package cs.eng1.piazzapanic.food.ingredient;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * An ingredient to be chopped, holding multiple textures based on its chopped boolean.
 */
public class ChoppedIngredient extends SimpleIngredient {

    public ChoppedIngredient(String type, FoodTextureManager manager) {
        super(type, manager);
    }

    /**
     * Get texture based on whether the ingredient has been chopped or not.
     *
     * @return Texture to display
     */
    @Override
    public Texture getTexture() {
        String form;
        if (isChopped) {
            form = "chopped";
        } else {
            form = "raw";
        }
        return textureManager.getTexture((getType() + "_" + form));
    }
}
