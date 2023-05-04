package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import cs.eng1.piazzapanic.food.ingredient.SimpleIngredient;
import cs.eng1.piazzapanic.food.recipe.Recipe;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;

import java.util.ArrayList;

import static cs.eng1.piazzapanic.PiazzaPanicGame.RANDOM;
import static cs.eng1.piazzapanic.screen.GameScreen.MAX_LIVES;

/**
 * The main game UI class, responsible for rendering all UI elements aside from station actions.
 *
 * @author Faran Lane, Alistair Foggin
 * @since 12-22
 */
public class UIOverlay {

    private final Stack chefDisplay;
    private final Image chefImage;
    private final Image ingredientImagesBG;
    private final VerticalGroup ingredientImages;
    private final TextureRegionDrawable removeBtnDrawable;
    private final Table recipeGroupsDisplay;
    private final TextureRegionDrawable emptyLife;
    private final TextureRegionDrawable fullLife;
    private final Timer timer;
    private final PiazzaPanicGame game;
    private final Table topTable;
    private final Table midTable;
    private final Table bottomTable;
    private final HorizontalGroup livesGroup;
    private final HorizontalGroup coinGroup;
    private final TextButton chefBuyButton;
    private final Label powerupNotif;
    private float colorTime;

    // commonly used asset paths
    public static final String SQUARE_BG =
            "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png";
    public static final String TIMER_BG =
            "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_button_gradient_down.png";
    public static final String PAUSE_BUTTON =
            "Kenney-Game-Assets-1/2D assets/Game Icons/PNG/White/1x/pause.png";
    public static final String REMOVE_BUTTON =
            "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_crossWhite.png";
    public static final String LIFE_EMPTY =
            "Kenney-Game-Assets-3/2D assets/Emote Pack/PNG/Pixel/Style 8/emote_heartBroken.png";
    public static final String LIFE_FULL =
            "Kenney-Game-Assets-3/2D assets/Emote Pack/PNG/Pixel/Style 8/emote_heart.png";
    public static final String COIN =
            "Kenney-Game-Assets-1/2D assets/UI Space Pack/PNG/dotYellow.png";
    public static final String CUSTOMER_TIMER =
            "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/green_button_flat_down.png";
    public static final String CUSTOMER_TIMER_EXPIRED =
            "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/red_button_flat_down.png";

    /**
     * @param uiStage    the game's UI stage
     * @param game       the main game class itself, to set up pause overlays and exiting properly
     * @param isScenario whether the game is scenario mode or not
     */
    public UIOverlay(Stage uiStage, final PiazzaPanicGame game, boolean isScenario) {
        this.game = game;

        // Initialize tables
        // Top table for current chef, timer and pause button
        topTable = new Table();
        topTable.setFillParent(true);
        topTable.center().top().pad(15f);
        uiStage.addActor(topTable);
        topTable.pack();

        // Mid table for orders and chef stack
        midTable = new Table();
        midTable.setFillParent(true);
        float midTablePadding = topTable.getRows() > 0 ? topTable.getRowHeight(0) : 45f;
        midTable.center().top().pad(midTablePadding + 15f, 15f, 0f, 15f);
        uiStage.addActor(midTable);
        midTable.pack();

        // Bottom table for reputation, money and buy button
        bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.right().bottom().pad(15f);
        uiStage.addActor(bottomTable);
        bottomTable.pack();

        recipeGroupsDisplay = new Table();


        // Initialize UI for showing current chef
        chefDisplay = new Stack();
        chefDisplay.add(new Image(new Texture(SQUARE_BG)));
        chefImage = new Image();
        chefImage.setScaling(Scaling.fit);
        chefDisplay.add(chefImage);

        // Initialize UI for showing current chef's ingredient stack
        Stack ingredientStackDisplay = new Stack();
        ingredientImagesBG = new Image(new Texture(SQUARE_BG));
        ingredientImages = new VerticalGroup();
        ingredientImages.padBottom(10f);
        ingredientStackDisplay.add(ingredientImages);

        // Initialize the timer
        LabelStyle timerStyle = new Label.LabelStyle(game.getFontManager().getTitleFont(), null);
        timerStyle.background = new TextureRegionDrawable(new Texture(TIMER_BG));
        timer = new Timer(timerStyle);
        timer.setAlignment(Align.center);

        // Initialize the pause button
        ImageButton pauseButton = game.getButtonManager().createImageButton(new TextureRegionDrawable(
                        new Texture(PAUSE_BUTTON)),
                ButtonManager.ButtonColour.BLUE, -1.5f);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getPauseOverlay().show();
            }
        });
        removeBtnDrawable = new TextureRegionDrawable(new Texture(REMOVE_BUTTON));

        // Reputation and coin setup
        emptyLife = new TextureRegionDrawable(new Texture(LIFE_EMPTY));
        fullLife = new TextureRegionDrawable(new Texture(LIFE_FULL));
        TextureRegionDrawable coin = new TextureRegionDrawable(new Texture(COIN));
        livesGroup = new HorizontalGroup();
        livesGroup.left();
        coinGroup = new HorizontalGroup();
        coinGroup.left();
        coinGroup.addActor(new Image(coin));
        coinGroup.addActor(new Label("0", new LabelStyle(game.getFontManager().getTitleFont(), Color.YELLOW)));

        // Chef purchase button
        chefBuyButton = game.getButtonManager().createTextButton("0",
                ButtonManager.ButtonColour.BLUE);

        // Powerup notification - uses title font for outline
        FreeTypeFontGenerator.FreeTypeFontParameter powerupNotifFontParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        powerupNotifFontParam.size = 30;
        powerupNotifFontParam.borderColor = Color.BLACK;
        powerupNotifFontParam.borderWidth = 1;
        powerupNotif = new Label("",
                new LabelStyle(game.getFontManager().generateFont(powerupNotifFontParam), Color.WHITE));

        // Add everything
        Value scale = Value.percentWidth(0.04f, topTable);
        Value timerWidth = Value.percentWidth(0.2f, topTable);
        topTable.add(pauseButton).left().width(scale).height(scale); // pause button
        topTable.add(timer).expandX().width(timerWidth).height(scale); // timer
        topTable.add(chefDisplay).right().width(scale).height(scale); // chef selection

        midTable.add(recipeGroupsDisplay).left().top().expandX(); // current orders
        midTable.add(ingredientStackDisplay).right().top().width(scale); // chef stack

        if (!isScenario) { // only endless contains chef buying and coins
            bottomTable.add(powerupNotif).top().center().expandX().row();

            bottomTable.add(coinGroup).top().left().pad(25f).height(scale)
                    .width(Value.percentWidth(0.12f, topTable));
            Value chefButtonScale = Value.percentWidth(0.06f, topTable);
            bottomTable.add(chefBuyButton).top().left().pad(15f).width(chefButtonScale).height(chefButtonScale);
            bottomTable.row();
        }
        bottomTable.add(livesGroup).bottom().left().pad(15f).height(scale) // lives
                .width(Value.percentWidth(0.12f, topTable));

    }

    /**
     * Reset values and UI to be in their default state.
     */
    public void init(int chefCostInitial) {
        timer.reset();
        timer.start();
        updateChefUI(null, false, chefCostInitial);
        updateLives(MAX_LIVES);
    }

    /**
     * Show the image of the currently selected chef as well as have the stack of ingredients
     * currently held by the chef.
     *
     * @param chef       The chef that is currently selected for which to show the UI.
     * @param atMaxChefs whether the current chef count is maxed out, determining if the chef buy button should still
     *                   be rendered or not
     * @param newCost    the updated chef cost, to render the amount on screen
     */
    public void updateChefUI(final Chef chef, boolean atMaxChefs, int newCost) {
        // chef buy button
        chefBuyButton.setVisible(!atMaxChefs);
        chefBuyButton.setText("Buy\nChef:\n" + newCost);
        if (chef == null) {
            chefImage.setDrawable(null);
            ingredientImages.clearChildren();
            ingredientImagesBG.setVisible(false);
            return;
        }

        // chef selection
        Texture texture = chef.getTexture();
        chefImage.setDrawable(new TextureRegionDrawable(texture));

        // chef stack
        ingredientImages.clearChildren();
        for (SimpleIngredient ingredient : chef.getStack()) {
            Stack textureStack = new Stack();
            textureStack.add(new Image(new Texture(SQUARE_BG)));
            Image image = new Image(ingredient.getTexture());
            textureStack.add(image);
            ingredientImages.addActor(textureStack);
        }
        resizeStack();

        // remove button on stack
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
     * Called when the game has finished, transitions to the EndOverlay
     *
     * @param won           whether the game was won or not
     * @param customerCount number of dishes served
     */
    public void finishGameUI(boolean won, int customerCount) {
        timer.stop();
        game.getEndOverlay().show(won, timer, customerCount);
        game.getTutorialOverlay().toggleStage();
    }

    /**
     * Show all current orders, grouped horizontally by customer
     *
     * @param orders The orders to display the grouped dishes for.
     */
    public void updateRecipeUI(java.util.List<Customer> orders) {
        recipeGroupsDisplay.clear();
        for (Customer customer : orders) {
            if (customer != null && !(customer.getOrder().isEmpty())) {
                addRecipeGroup(customer);
            }
        }
    }

    /**
     * Adds a HorizontalGroup to the RecipeGroups table, based on a given customer's order
     *
     * @param customer the customer to render the order of
     */
    private void addRecipeGroup(Customer customer) {
        HorizontalGroup orderGroup = new HorizontalGroup();
        orderGroup.clearChildren(); // reset order render

        // add each dish
        for (Recipe dish : customer.getOrder()) {
            if (dish != null) {
                addDishToGroup(dish, orderGroup);
            }
        }

        // add timer - default texture of expired
        Image timerImage = new Image(new TextureRegionDrawable(new Texture(CUSTOMER_TIMER_EXPIRED)));
        float timerWidth = chefDisplay.getWidth() * 3;
        float timerHeight = chefDisplay.getHeight() / 4f;
        timerImage.setSize(timerWidth, timerHeight);
        float orderTimePercentage = customer.getTimeElapsedPercentage();

        if (orderTimePercentage != 100) { // if not out of time, change texture to not expired
            timerImage.setDrawable(new TextureRegionDrawable(new Texture(CUSTOMER_TIMER)));
            timerWidth *= (1 - (customer.getTimeElapsedPercentage() / 100));
        }

        recipeGroupsDisplay.add(timerImage)
                .width(timerWidth)
                .height(timerHeight)
                .left().row();
        recipeGroupsDisplay.add(orderGroup).left(); // add order group and pad for next
        recipeGroupsDisplay.row().padTop(chefDisplay.getWidth() / 20f);
    }

    /**
     * Used to add a single Recipe dish's textures to a given HorizontalGroup
     *
     * @param dish  dish to be added
     * @param group HorizontalGroup to add to
     */
    private void addDishToGroup(Recipe dish, HorizontalGroup group) {
        Stack dishStack = new Stack();

        // background image
        Texture recipeImagesBGTex = new Texture(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/grey_button_square_gradient_down.png");
        Image recipeImagesBG = new Image(recipeImagesBGTex);
        recipeImagesBG.setVisible(true);
        dishStack.add(recipeImagesBG);

        // dish texture
        Image recipeImage = new Image(dish.getTexture());
        recipeImage.getDrawable().setMinHeight(chefDisplay.getHeight());
        recipeImage.getDrawable().setMinWidth(chefDisplay.getWidth());
        dishStack.addActor(recipeImage);

        group.addActor(dishStack); // add to group and space for next
        group.space(chefDisplay.getWidth() / 20f);
    }

    /**
     * Called to resize most UI elements
     *
     * @param width  new width to resize accordingly to
     * @param orders current orders to update
     */
    public void resizeUI(int width, java.util.List<Customer> orders) {
        // top table
        topTable.pack();
        topTable.padTop(width / 64f);

        // mid table
        float topTablePadding = topTable.getRows() > 0 ? topTable.getRowHeight(0) + 15f : 45f;
        midTable.padTop(topTablePadding + width / 64f);
        updateRecipeUI(orders);
        resizeStack();

        // bottom table
        resizeLives();
        resizeCoins();
        chefBuyButton.getLabel().setFontScale(Math.max(width * 0.001f, 1)); // scale buy button text
    }

    /**
     * Resizes the drawable images of the chef's stack
     */
    private void resizeStack() {
        ingredientImages.getChildren().forEach(child -> {
            if (child instanceof Stack) {
                Stack imageStack = (Stack) child;
                ((Stack) child).getChildren().forEach(c -> {
                    if (c instanceof Image) {
                        Image image = (Image) c;
                        image.getDrawable().setMinHeight(chefDisplay.getHeight());
                        image.getDrawable().setMinWidth(chefDisplay.getWidth());
                    }
                });
                imageStack.setWidth(chefDisplay.getWidth());
                imageStack.setHeight(chefDisplay.getHeight());
            }
        });
    }

    /**
     * Update the rendered lives by drawing background textures for all, and rendering the full life texture
     * if lives still remain
     *
     * @param livesCount updated reputation amount
     */
    public void updateLives(int livesCount) {

        livesGroup.clearChildren();
        for (int i = 0; i < MAX_LIVES; i++) {
            Image life = new Image(emptyLife);
            if (livesCount > 0) {
                life.setDrawable(fullLife);
            }
            livesGroup.addActor(life);
            livesGroup.space(Math.max(chefDisplay.getWidth() / 4f, 15f));
            livesCount--;
        }
        resizeLives();
    }

    /**
     * Resizes the lives UI element
     */
    private void resizeLives() {
        livesGroup.getChildren().forEach(c -> {
            if (c instanceof Image) {
                ((Image) c).getDrawable().setMinHeight(chefDisplay.getWidth());
                ((Image) c).getDrawable().setMinWidth(chefDisplay.getWidth());
            }
        });
    }

    /**
     * Resizes the coins UI element
     */
    private void resizeCoins() {
        coinGroup.getChildren().forEach(c -> {
            if (c instanceof Image) {
                ((Image) c).getDrawable().setMinHeight(chefDisplay.getWidth() * 0.7f);
                ((Image) c).getDrawable().setMinWidth(chefDisplay.getWidth() * 0.7f);
            } else if (c instanceof Label) {
                ((Label) c).setFontScale(Math.round(chefDisplay.getWidth() / 40f));
            }
        });
        coinGroup.space(8f * (chefDisplay.getWidth() / 35f));
    }

    /**
     * Update the money text shown
     *
     * @param amount updated amount of money
     */
    public void updateMoney(int amount) {
        ((Label) coinGroup.getChild(1)).setText(amount);
    }

    /**
     * Adds a ClickListener callback to the chef buy button, allowing it to function properly
     *
     * @param callback action to take place on click
     */
    public void addBuyChefButton(ClickListener callback) {
        chefBuyButton.addListener(callback);
    }

    public float getTimerTime() {
        return timer.getTime();
    }

    public void setTimerTime(float newTime) {
        timer.setTime(newTime);
    }

    public void showPowerupLabel(String message) {
        powerupNotif.setText(message);
        powerupNotif.setVisible(true);
    }

    public void tickPowerupLabelColor(float delta) {
        colorTime += delta;
        if (colorTime > 0.125f) {
            powerupNotif.setColor(FontManager.COLOUR_LIST.get(
                    RANDOM.nextInt(FontManager.COLOUR_LIST.size() - 1)));
            colorTime = 0f;
        }
    }

    public void hidePowerupLabel() {
        powerupNotif.setVisible(false);
    }
}
