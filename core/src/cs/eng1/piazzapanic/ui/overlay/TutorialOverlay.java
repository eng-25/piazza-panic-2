package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import cs.eng1.piazzapanic.PiazzaPanicGame;

/**
 * A tutorial overlay, containing instructions and guidance on how to play
 *
 * @author Faran Lane
 * @since 04-23
 */
public class TutorialOverlay extends BaseOverlay {

    private Stage currentStage;
    private Stage otherStage;

    public TutorialOverlay(final PiazzaPanicGame game) {
        super(game);

        currentStage = null;
        otherStage = null;

        // Initialize movement instructions label
        LabelStyle labelStyle = new LabelStyle(game.getFontManager().getHeaderFont(), Color.BLACK);
        Label chefMovement = new Label(
                "Click on a chef to select them. You can then click on which chef you want or press E to swap between them. Then use WASD or the arrow keys to move around.\n" +
                        "\n" +
                        "Move towards a station to get possible actions to appear as buttons such as place ingredient, pick up ingredient, deal with ingredient and clear station.\n" +
                        "\n" +
                        "Take the prepared ingredients to the counter (the table with the orange tablecloth) to build the recipe and complete the order\n" +
                        "\n" +
                        "In Scenario mode, you will have 5 customers to serve, each having a time limit, that will ask for a dish. If you do not serve them in this time limit, you will lose one reputation point. You will have to serve the following dishes: Salad and Burger.\n" +
                        "\n" +
                        "In Endless mode, there will be an infinite number of customers to serve. Their time limits will reduce over time, making it more difficult to handle. You will also be able to burn some ingredients so watch out for that. You will get money and the chance of a powerup for each customer you serve. You can use this money to unlock more stations and buy more chefs. Powerups include gaining a life and a temporary speed boost. Customers will also be able to ask for up to 3 dishes at a time. The dishes could be: Salad, Burger, Pizza and Jacket Potato\n" +
                        "\n" +
                        "The recipes are:\n" +
                        "\tSalad: Chopped Lettuce + Chopped Tomato\n" +
                        "\tBurger: Bun + Cooked Patty\n" +
                        "\tPizza: Cooked (Dough + Chopped Tomato + Chopped Cheese)\n" +
                        "\tJacket Potato: Cooked (Potato + Chopped Cheese + Cooked Beans)",
                labelStyle);
        chefMovement.setWrap(true);

        // Initialize station usage label
        Label stationUsage = new Label(
                "Move towards a station to get possible actions to appear as buttons including placing ingredients, picking up ingredients, and preparing them.",
                labelStyle);
        stationUsage.setWrap(true);

        // Add items to table
        table.pad(100f);
        table.add(chefMovement).fillX().expandX().pad(20f).padTop(0f);
        table.row();
        table.add(stationUsage).fillX().expandX().pad(20f).padTop(0f);
        table.row();
        addBackButton();

    }

    @Override
    public void addToStage(Stage uiStage) {
        if (currentStage != null) {
            otherStage = currentStage;
        }
        currentStage = uiStage;
        super.addToStage(uiStage);
    }

    public void toggleStage() {
        if (otherStage != null) {
            otherStage.addActor(table);
            Stage temp = currentStage;
            currentStage = otherStage;
            otherStage = temp;
        }
    }
}
