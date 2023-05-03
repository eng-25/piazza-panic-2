package cs.eng1.piazzapanic.food;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

/**
 * @author Faran Lane, Alistair Foggin
 * @since 12-22
 */
public class FoodTextureManager implements Disposable {

    private final HashMap<String, Texture> foodTextures;
    private final Texture notFoundImage;

    public FoodTextureManager() {
        this.foodTextures = new HashMap<>();
        this.foodTextures.put("patty_cooked",
                new Texture("food/original/cooked_patty.png"));
        this.foodTextures.put("patty_raw",
                new Texture("food/original/uncooked_patty.png"));
        this.foodTextures.put("patty_burnt",
                new Texture("food/original/burnt_patty.png"));
        this.foodTextures.put("lettuce_raw",
                new Texture("food/glitch/vegetable/lettuce.png"));
        this.foodTextures.put("lettuce_chopped",
                new Texture("food/original/lettuce_chopped.png"));
        this.foodTextures.put("tomato_raw",
                new Texture("food/glitch/fruit/tomato.png"));
        this.foodTextures.put("tomato_chopped",
                new Texture("food/original/tomato_chopped.png"));
        this.foodTextures.put("bun",
                new Texture("food/glitch/misc/bun.png"));
        this.foodTextures.put("burger",
                new Texture("food/glitch/misc/sandwich_burger_04.png"));
        this.foodTextures.put("salad",
                new Texture("food/glitch/misc/salad.png"));
        this.foodTextures.put("cheese_raw",
                new Texture("food/glitch/dairy/cheese_01.png"));
        this.foodTextures.put("cheese_chopped",
                new Texture("food/original/cheese_grated.png"));
        this.foodTextures.put("beans_raw",
                new Texture("food/glitch/vegetable/bean.png"));
        this.foodTextures.put("beans_cooked",
                new Texture("food/original/beans_baked.png"));
        this.foodTextures.put("beans_burnt",
                new Texture("food/original/beans_burnt.png"));
        this.foodTextures.put("dough",
                new Texture("food/glitch/dessert/pie.png"));
        this.foodTextures.put("potato",
                new Texture("food/glitch/vegetable/potato.png"));
        this.foodTextures.put("pizza_raw",
                new Texture("food/original/pizza_raw.png"));
        this.foodTextures.put("pizza_cooked",
                new Texture("food/glitch/misc/pizza_01.png"));
        this.foodTextures.put("pizza_burnt",
                new Texture("food/original/pizza_burnt.png"));
        this.foodTextures.put("jacket_potato_raw",
                new Texture("food/original/raw_jacket_potato.png"));
        this.foodTextures.put("jacket_potato_cooked",
                new Texture("food/original/cooked_jacket_potato.png"));
        this.foodTextures.put("jacket_potato_burnt",
                new Texture("food/original/burnt_jacket_potato.png"));
        notFoundImage = new Texture(Gdx.files.internal("badlogic.jpg"));
    }

    /**
     * @param foodType The food string to get the corresponding texture from the hashmap.
     * @return the texture for the specified food or an image to signify that the texture does not
     * exist.
     */
    public Texture getTexture(String foodType) {
        Texture texture = foodTextures.get(foodType);
        if (texture != null) {
            return texture;
        } else {
            return notFoundImage;
        }
    }

    @Override
    public void dispose() {
        for (Texture texture : foodTextures.values()) {
            texture.dispose();
        }
        notFoundImage.dispose();
    }
}
