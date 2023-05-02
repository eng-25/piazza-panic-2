package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.screen.GameScreen;
import cs.eng1.piazzapanic.ui.ButtonManager;

/**
 * A pause overlay in the game, implements a save and exit button as well as a tutorial overlay in case they want a
 * reminder.
 *
 * @author Faran Lane
 * @since 04-23
 */
public class PauseOverlay extends BaseOverlay {
    public PauseOverlay(PiazzaPanicGame game) {
        super(game);

        TextButton saveButton = buttonManager.createTextButton("Save and Exit", ButtonManager.ButtonColour.RED);
        TextButton tutorialButton = buttonManager.createTextButton("Tutorial", ButtonManager.ButtonColour.BLUE);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.getScreen() instanceof GameScreen) {
                    GameScreen gameScreen = (GameScreen) game.getScreen();
                    gameScreen.save();
                    game.loadHomeScreen();
                    hide();
                }
            }
        });
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getTutorialOverlay().show();
                game.getTutorialOverlay().table.toFront();
            }
        });

        table.add(tutorialButton).row();
        table.add(saveButton).padTop(20f).row();
        addBackButton();
    }

}
