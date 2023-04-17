package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;

public class DifficultyOverlay extends BaseOverlay {

    private final TextButton[] buttons;
    public DifficultyOverlay(PiazzaPanicGame game) {
        super(game);

        TextButton easyButton = game.getButtonManager().createTextButton("Easy", ButtonManager.ButtonColour.BLUE);
        TextButton medButton = game.getButtonManager().createTextButton("Medium", ButtonManager.ButtonColour.BLUE);
        TextButton hardButton = game.getButtonManager().createTextButton("Hard", ButtonManager.ButtonColour.BLUE);
        buttons = new TextButton[]{easyButton, medButton, hardButton};

        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateDifficulty(0);
            }
        });

        medButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateDifficulty(1);
            }
        });
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateDifficulty(2);
            }
        });

        table.add(easyButton).pad(10f);
        table.row();
        table.add(medButton).pad(10f);
        table.row();
        table.add(hardButton).pad(10f);
        table.row();
        addBackButton();
    }

    private void updateDifficulty(int newDifficulty) {
        if (newDifficulty >= 0 && newDifficulty < 3) {
            ButtonManager manager = game.getButtonManager();
            TextButton.TextButtonStyle blueStyle = manager.getTextButtonStyle(ButtonManager.ButtonColour.BLUE);
            TextButton.TextButtonStyle greenStyle = manager.getTextButtonStyle(ButtonManager.ButtonColour.GREEN);
            for (int i=0; i<3; i++) {
                if (i==newDifficulty) {
                    buttons[i].setStyle(greenStyle);
                } else {
                    buttons[i].setStyle(blueStyle);
                }
            }
            game.setDifficulty(newDifficulty);
        }
    }

    public void init() {
        updateDifficulty(game.getDifficulty());
    }
}
