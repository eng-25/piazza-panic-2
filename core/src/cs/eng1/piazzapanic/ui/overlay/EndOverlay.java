package cs.eng1.piazzapanic.ui.overlay;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.Timer;

/**
 * The end screen overlay, shown when the game has finished
 *
 * @author Faran Lane
 * @since 04-23
 */
public class EndOverlay extends BaseOverlay {

    private final Label endMessage;

    public EndOverlay(PiazzaPanicGame game) {
        super(game);

        // add end message Label and main menu Button to table
        endMessage = new Label("", new Label.LabelStyle(game.getFontManager().getHeaderFont(), Color.BLACK));
        endMessage.setAlignment(Align.center);
        TextButton homeButton = buttonManager.createTextButton("Main Menu", ButtonManager.ButtonColour.RED);

        homeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadHomeScreen();
            }
        });

        table.add(endMessage).row();
        table.add(homeButton).padTop(20f);
    }

    /**
     * Shows the end overlay
     *
     * @param won           whether the game was won or not
     * @param timer         game timer
     * @param customerCount count of dishes served in game
     */
    public void show(boolean won, Timer timer, int customerCount) {
        String winMessage = "Game won\nTime taken: ";
        String lossMessage = "Game lost\nTime played: ";
        endMessage.setText((won ? winMessage : lossMessage) + timer.getText() +
                "\nDishes served: " + customerCount);
        show();
    }
}
