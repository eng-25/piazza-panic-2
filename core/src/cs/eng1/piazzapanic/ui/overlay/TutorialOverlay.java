package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import cs.eng1.piazzapanic.ui.ButtonManager;

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
        "Left click on a chef to select them. Then use WASD or the arrow keys to move around.",
        labelStyle);
    chefMovement.setWrap(true);

    // Initialize station usage label
    Label stationUsage = new Label(
        "Move towards a station to get possible actions to appear as buttons including placing ingredients, picking up ingredients, and dealing with the ingredients.",
        labelStyle);
    stationUsage.setWrap(true);

    // Initialize recipe creation label
    Label recipeLabel = new Label(
        "Take the ingredients and process them as required for the recipe shown on the right. Then take them to the counter (table with the orange tablecloth) to build the recipe and complete the order.",
        labelStyle);
    recipeLabel.setWrap(true);

    // Add items to table
    table.pad(100f);
    table.add(chefMovement).fillX().expandX().pad(20f).padTop(0f);
    table.row();
    table.add(stationUsage).fillX().expandX().pad(20f).padTop(0f);
    table.row();
    table.add(recipeLabel).fillX().expandX().pad(20f).padTop(0f);
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
