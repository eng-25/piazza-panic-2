package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.ui.ButtonManager;

import java.awt.*;

public class ModeOverlay extends BaseOverlay {

    public ModeOverlay(final PiazzaPanicGame game) {
        super(game);

        TextButton scenarioButton = game.getButtonManager().createTextButton("Scenario",
                ButtonManager.ButtonColour.BLUE);
        scenarioButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadGameScreen(true);
            }
        });

        TextButton endlessButton = game.getButtonManager().createTextButton("Endless",
                ButtonManager.ButtonColour.BLUE);
        endlessButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.loadGameScreen(false);
            }
        });

        table.add(scenarioButton);
        table.row();
        table.add(endlessButton).padTop(20f);
        table.row();
        addBackButton();
    }
}
