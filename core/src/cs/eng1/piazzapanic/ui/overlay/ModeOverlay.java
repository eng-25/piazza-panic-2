package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;

/**
 * Overlay to prompt a user if they want a new game to be scenario or endless mode and then begin the game
 *
 * @author Faran Lane
 * @since 04-23
 */
public class ModeOverlay extends BaseOverlay {

    public ModeOverlay(final PiazzaPanicGame game) {
        super(game);

        TextButton scenarioButton = game.getButtonManager().createTextButton("Scenario",
                ButtonManager.ButtonColour.BLUE);
        scenarioButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadGameScreen(true, false);
            }
        });

        TextButton endlessButton = game.getButtonManager().createTextButton("Endless",
                ButtonManager.ButtonColour.BLUE);
        endlessButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadGameScreen(false, false);
            }
        });

        table.add(scenarioButton);
        table.row();
        table.add(endlessButton).padTop(20f);
        table.row();
        addBackButton();
    }
}
