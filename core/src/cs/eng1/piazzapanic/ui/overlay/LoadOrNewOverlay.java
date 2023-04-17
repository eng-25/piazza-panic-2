package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;

public class LoadOrNewOverlay extends BaseOverlay {
    public LoadOrNewOverlay(PiazzaPanicGame game) {
        super(game);

        TextButton newButton = buttonManager.createTextButton("New", ButtonManager.ButtonColour.BLUE);
        TextButton loadButton = buttonManager.createTextButton("Load", ButtonManager.ButtonColour.RED);

        newButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getModeOverlay().show();
                game.getModeOverlay().table.toFront();
                //hide();
            }
        });
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        table.add(newButton);
        table.row();
        table.add(loadButton).padTop(20f);
        table.row();
        addBackButton();
    }

}
