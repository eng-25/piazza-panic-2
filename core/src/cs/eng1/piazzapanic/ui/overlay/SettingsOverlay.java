package cs.eng1.piazzapanic.ui.overlay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;
import cs.eng1.piazzapanic.ui.overlay.BaseOverlay;

public class SettingsOverlay extends BaseOverlay {

  public SettingsOverlay(final PiazzaPanicGame game) {

    super(game);

    final CheckBox fullscreenCheckbox = game.getButtonManager()
            .createCheckbox("Fullscreen", ButtonColour.BLUE);
    fullscreenCheckbox.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if (fullscreenCheckbox.isChecked()) {
          Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
          Gdx.graphics.setWindowedMode(1920 / 2, 1080 / 2);
        }
      }
    });

    table.add(fullscreenCheckbox);
    table.row();
    addBackButton();
  }
}
