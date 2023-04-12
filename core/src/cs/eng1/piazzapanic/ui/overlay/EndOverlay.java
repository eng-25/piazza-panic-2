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


public class EndOverlay extends BaseOverlay {

    private final String winMessage = "Game won\nTime taken: ";
    private final String lossMessage = "Game lost\nTime played: ";
    private Label endMessage;
    public EndOverlay(PiazzaPanicGame game) {
        super(game);

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

    public void show(boolean won, Timer timer) {
        endMessage.setText((won ? winMessage : lossMessage) + timer.getText());
        show();
    }
}
