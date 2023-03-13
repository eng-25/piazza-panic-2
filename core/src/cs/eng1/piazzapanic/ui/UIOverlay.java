package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import cs.eng1.piazzapanic.food.Customer;
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
    private final Table recipeGroupsDisplay;
    private final TextureRegionDrawable emptyLife;
    private final TextureRegionDrawable fullLife;
    private final TextureRegionDrawable coin;
    // private final HorizontalGroup recipeGroup
    private final Timer timer;
    private int recipeCount;
    private final Label resultLabel;
    private final Timer resultTimer;
    private final PiazzaPanicGame game;

    private final Table topTable;
    private final Table midTable;
    private final Table bottomTable;
    private final HorizontalGroup livesGroup;
    private final HorizontalGroup coinGroup;

    public UIOverlay(Stage uiStage, final PiazzaPanicGame game) {
        this.game = game;

        // Initialize tables
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.center().top().pad(15f);
        uiStage.addActor(topTable);
        topTable.pack();

        midTable = new Table();
        midTable.setFillParent(true);
        float topTablePadding = topTable.getRows() > 0 ? topTable.getRowHeight(0) : 45f;
        midTable.center().top().pad(topTablePadding + 15f, 15f, 0f, 15f);
        uiStage.addActor(midTable);
        midTable.pack();

        bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.right().bottom().pad(15f);
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
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_button_gradient_down.png"));
        timer = new Timer(timerStyle);
        timer.setAlignment(Align.center);

        // Initialize the pause button
        ImageButton homeButton = game.getButtonManager().createImageButton(new TextureRegionDrawable(
                        new Texture(
                                Gdx.files.internal("Kenney-Game-Assets-1/2D assets/Game Icons/PNG/White/1x/pause.png"))),
                ButtonManager.ButtonColour.BLUE, -1.5f);
        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { //TODO: pause window
                game.loadHomeScreen();
            }
        });
        removeBtnDrawable = new TextureRegionDrawable(
                new Texture("Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_crossWhite.png"));

        emptyLife = new TextureRegionDrawable(
                new Texture("Kenney-Game-Assets-1/2D assets/UI Space Pack/PNG/dot_shadow.png")
        );
        fullLife = new TextureRegionDrawable(
                new Texture("Kenney-Game-Assets-1/2D assets/UI Space Pack/PNG/dotBlue.png")
        );
        coin = new TextureRegionDrawable(
                new Texture("Kenney-Game-Assets-1/2D assets/UI Space Pack/PNG/dotYellow.png")
        );

        // Initialize the UI to display the currently requested recipe
//    Stack recipeDisplay = new Stack();
//    recipeImagesBG = new Image(new Texture(
//        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png"));
//    recipeImagesBG.setVisible(false);
//    recipeDisplay.add(recipeImagesBG);
//    orders = new HorizontalGroup();
//    recipeDisplay.add(orders);
//    recipeDisplay.debug();

        recipeGroupsDisplay = new Table();

        // Initialize counter for showing remaining recipes
        recipeCount = 0;

        // Initialize winning label //TODO: keep? or change to window/screen
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

        midTable.add(recipeGroupsDisplay).top().left().expandX();
        midTable.add(ingredientStackDisplay).right().top();

        livesGroup = new HorizontalGroup();
        int lives = 3; //TODO from constructor parameter
        for (int i = 0; i < lives; i++) {
            Stack lifeStack = new Stack();
            lifeStack.add(new Image(emptyLife));
            lifeStack.add(new Image(fullLife));
            livesGroup.addActor(lifeStack);
            livesGroup.space(Math.max(scale.get() / 4f, 15f));
        }

        coinGroup = new HorizontalGroup();
        coinGroup.addActor(new Image(coin));
        coinGroup.space(Math.max(scale.get() / 8f, 5f));
        coinGroup.addActor(new Label("0", new LabelStyle(game.getFontManager().getHeaderFont(), Color.WHITE)));

        bottomTable.add(livesGroup).top().pad(15f);
        bottomTable.row();
        bottomTable.add(coinGroup).top().left().pad(15f);

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
     * @param orders The recipes to display the ingredients for.
     */
    public void updateRecipeUI(List<Customer> orders) {
        recipeGroupsDisplay.clear();
        for (Customer customer : orders) {
            if (customer != null && !(customer.getOrder().isEmpty())) {
                addRecipeGroup(customer);
            }
        }
    }

    private void addRecipeGroup(Customer customer) {
        HorizontalGroup orderGroup = new HorizontalGroup();
        orderGroup.clearChildren();

        for (Recipe dish : customer.getOrder()) {
            if (dish != null) {
                addDishToGroup(dish, orderGroup);
            }
        }

        Image timerImage = new Image(emptyLife);
        float timerWidth = chefDisplay.getWidth()*3;
        float timerHeight = chefDisplay.getHeight()/4f;
        timerImage.setSize(timerWidth, timerHeight);
        float orderTimePercentage = customer.getTimeElapsedPercentage();

        if (orderTimePercentage != 1) { // not out of time
            timerImage.setDrawable(coin);
            timerWidth *= (1-customer.getTimeElapsedPercentage());
        }

        recipeGroupsDisplay.add(timerImage)
                .width(timerWidth)
                .height(timerHeight)
                .left().row();
        recipeGroupsDisplay.add(orderGroup).left();
        recipeGroupsDisplay.row().padTop(chefDisplay.getWidth() / 20f);
        //midTable.debug();
    }

    private void addDishToGroup(Recipe dish, HorizontalGroup group) {
        Stack dishStack = new Stack();

        Texture recipeImagesBGTex = new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png");
        Image recipeImagesBG = new Image(recipeImagesBGTex);
        recipeImagesBG.setVisible(true);
        dishStack.add(recipeImagesBG);

        Image recipeImage = new Image(dish.getTexture());
        recipeImage.getDrawable().setMinHeight(chefDisplay.getHeight());
        recipeImage.getDrawable().setMinWidth(chefDisplay.getWidth());
        dishStack.addActor(recipeImage);

        group.addActor(dishStack);
        group.space(chefDisplay.getWidth() / 20f);
    }

    /**
     * Update the number of remaining recipes to be displayed.
     *
     * @param remainingRecipes The number of remaining recipes.
     */
    public void updateRecipeCounter(int remainingRecipes) {
        recipeCount = remainingRecipes;
    }

    public void resizeUI(int width, List<Customer> orders) {
        topTable.pack();
        float topTablePadding = topTable.getRows() > 0 ? topTable.getRowHeight(0) + 15f : 45f;
        topTable.padTop(width / 64f);
        midTable.padTop(topTablePadding + width / 64f);
        updateRecipeUI(orders);
    }
}
