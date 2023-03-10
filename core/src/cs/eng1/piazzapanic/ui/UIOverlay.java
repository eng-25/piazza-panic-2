package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;

import java.util.List;

public class UIOverlay {

    //  private final Image pointer;
    private final Stack chefDisplay;
    private final Image chefImage;
    private final Image ingredientImagesBG;
    private final VerticalGroup ingredientImages;
    private final TextureRegionDrawable removeBtnDrawable;
    //  private final Image recipeImagesBG;
//  private final HorizontalGroup orders;
    private final HorizontalGroup recipeDisplay;
    private final Timer timer;
    private int recipeCount;
    private final Label resultLabel;
    private final Timer resultTimer;
    private final PiazzaPanicGame game;

    private final Table topTable;
    private final Table bottomTable;

    public UIOverlay(Stage uiStage, final PiazzaPanicGame game) {
        this.game = game;

        // Initialize tables
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.center().top().pad(15f);
        uiStage.addActor(topTable);
        topTable.pack();

        bottomTable = new Table();
        bottomTable.setFillParent(true);
        float topTablePadding = topTable.getRows() > 0 ? topTable.getRowHeight(0) : 45f;
        bottomTable.center().top().pad(topTablePadding + 15f, 15f, 0f, 15f);
        uiStage.addActor(bottomTable);
        bottomTable.pack();
        // Initialise pointer image
//    pointer = new Image(
//        new Texture("Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_sliderDown.png"));
//    pointer.setScaling(Scaling.none);

        // Initialize UI for showing current chef
        chefDisplay = new Stack();
        chefDisplay.add(new Image(new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png")));
        chefImage = new Image();
        chefImage.setScaling(Scaling.fit);
        chefDisplay.add(chefImage);

        // Initialize UI for showing current chef's ingredient stack
        Stack ingredientStackDisplay = new Stack();
        ingredientImagesBG = new Image(new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));
        ingredientImagesBG.setVisible(false);
        ingredientStackDisplay.add(ingredientImagesBG);
        ingredientImages = new VerticalGroup();
        ingredientImages.padBottom(10f);
        ingredientStackDisplay.add(ingredientImages);

        // Initialize the timer
        LabelStyle timerStyle = new Label.LabelStyle(game.getFontManager().getTitleFont(), null);
        timerStyle.background = new TextureRegionDrawable(new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/green_button_gradient_down.png"));
        timer = new Timer(timerStyle);
        timer.setAlignment(Align.center);

        // Initialize the home button
        ImageButton homeButton = game.getButtonManager().createImageButton(new TextureRegionDrawable(
                        new Texture(
                                Gdx.files.internal("Kenney-Game-Assets-1/2D assets/Game Icons/PNG/White/1x/home.png"))),
                ButtonManager.ButtonColour.BLUE, -1.5f);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadHomeScreen();
            }
        });
        removeBtnDrawable = new TextureRegionDrawable(
                new Texture("Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_crossWhite.png"));

        // Initialize the UI to display the currently requested recipe
//    Stack recipeDisplay = new Stack();
//    recipeImagesBG = new Image(new Texture(
//        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));
//    recipeImagesBG.setVisible(false);
//    recipeDisplay.add(recipeImagesBG);
//    orders = new HorizontalGroup();
//    recipeDisplay.add(orders);
//    recipeDisplay.debug();

        recipeDisplay = new HorizontalGroup();

        // Initialize counter for showing remaining recipes
        recipeCount = 0;

        // Initialize winning label
        LabelStyle labelStyle = new Label.LabelStyle(game.getFontManager().getTitleFont(), null);
        resultLabel = new Label("Congratulations! Your final time was:", labelStyle);
        resultLabel.setVisible(false);
        resultTimer = new Timer(labelStyle);
        resultTimer.setVisible(false);

        // Add everything
        Value scale = Value.percentWidth(0.04f, topTable);
        Value timerWidth = Value.percentWidth(0.2f, topTable);
        topTable.add(homeButton).left().width(scale).height(scale);
        topTable.add(timer).expandX().width(timerWidth).height(scale);
        topTable.add(chefDisplay).right().width(scale).height(scale);

        //topTable.row().padTop(10f);
        bottomTable.add(recipeDisplay).top().left().expandX();
        bottomTable.add(ingredientStackDisplay).right().top();
        //topTable.add().expandX().width(timerWidth);
        //TODO: remove all below here?
//    topTable.row();
//    topTable.add(resultLabel).colspan(3);
//    topTable.row();
//    topTable.add(resultTimer).colspan(3);
//    topTable.debug();
//    bottomTable.debug();
    }

    /**
     * Reset values and UI to be in their default state.
     */
    public void init() {
        timer.reset();
        timer.start();
        resultLabel.setVisible(false);
        resultTimer.setVisible(false);
        updateChefUI(null);
    }

    /**
     * Show the image of the currently selected chef as well as have the stack of ingredients
     * currently held by the chef.
     *
     * @param chef The chef that is currently selected for which to show the UI.
     */
    public void updateChefUI(final Chef chef) {
        if (chef == null) {
            chefImage.setDrawable(null);
            ingredientImages.clearChildren();
            ingredientImagesBG.setVisible(false);
            return;
        }
        Texture texture = chef.getTexture();
        chefImage.setDrawable(new TextureRegionDrawable(texture));

        ingredientImages.clearChildren();
        for (Ingredient ingredient : chef.getStack()) {
            Image image = new Image(ingredient.getTexture());
            image.getDrawable().setMinHeight(chefDisplay.getHeight());
            image.getDrawable().setMinWidth(chefDisplay.getWidth());
            ingredientImages.addActor(image);
        }
        if (!chef.getStack().isEmpty()) {
            ImageButton btn = game.getButtonManager().createImageButton(removeBtnDrawable,
                    ButtonColour.RED, -1.5f);
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chef.placeIngredient();
                }
            });
            ingredientImages.addActor(btn);
        }
        ingredientImagesBG.setVisible(!chef.getStack().isEmpty());

    }

    /**
     * Show the label displaying that the game has finished along with the time it took to complete.
     */
    public void finishGameUI() {
        resultLabel.setVisible(true);
        resultTimer.setTime(timer.getTime());
        resultTimer.setVisible(true);
        timer.stop();
    }

    /**
     * Show the current requested recipe that the player needs to make, the ingredients for that, and
     * the number of remaining recipes.
     *
     * @param recipes The recipes to display the ingredients for.
     */
    public void updateRecipeUI(List<Recipe> recipes) {
        // recipe will be null when we reach the end of the scenario
        //float widthOffset = 20;
        recipeDisplay.clear();
        for (Recipe recipe : recipes) {

            if (recipe != null) {

                Stack stack = new Stack();
                Image recipeImagesBG = new Image(new Texture(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));
                stack.add(recipeImagesBG);

                VerticalGroup recipeImages = new VerticalGroup();
                recipeImages.clearChildren();
                recipeImages.addActor(new Label(String.valueOf(recipeCount),
                        new LabelStyle(game.getFontManager().getLabelFont(), Color.BLACK)));
                for (String recipeIngredient : recipe.getRecipeIngredients()) {
                    Image image = new Image(recipe.getTextureManager().getTexture(recipeIngredient));
                    image.getDrawable().setMinHeight(chefDisplay.getHeight());
                    image.getDrawable().setMinWidth(chefDisplay.getWidth());
                    recipeImages.addActor(image);
                }

                Image pointer = new Image(
                        new Texture("Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_sliderDown.png"));
                pointer.setScaling(Scaling.fillY);
                recipeImages.addActor(pointer);

                Image recipeImage = new Image(recipe.getTexture());
                recipeImage.getDrawable().setMinHeight(chefDisplay.getHeight());
                recipeImage.getDrawable().setMinWidth(chefDisplay.getWidth());
                recipeImages.addActor(recipeImage);
                recipeImagesBG.setVisible(true);
                stack.addActor(recipeImages);

                recipeDisplay.addActor(stack);
                recipeDisplay.space(chefDisplay.getWidth() / 20f);
            }
        }
    }

    /**
     * Update the number of remaining recipes to be displayed.
     *
     * @param remainingRecipes The number of remaining recipes.
     */
    public void updateRecipeCounter(int remainingRecipes) {
        recipeCount = remainingRecipes;
    }

    public void resizeUI(int width, List<Recipe> orders) {
        topTable.pack();
        float topTablePadding = topTable.getRows() > 0 ? topTable.getRowHeight(0) + 15f : 45f;
        topTable.padTop(width / 64f);
        bottomTable.padTop(topTablePadding + width / 64f);
        updateRecipeUI(orders);
    }
}
