package cs.eng1.piazzapanic.ui.overlay;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager.ButtonColour;

/**
 * A settings overlay, used to change into fullscreen as well as configure the number of customers in scenario mode
 * to be served.
 *
 * @author Faran Lane
 * @since 04-23
 */
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
        fullscreenCheckbox.getLabelCell().space(5f);

        final Label dropdownLabel = new Label("Customers in\nScenario mode",
                new Label.LabelStyle(game.getFontManager().getLabelFont(), Color.BLACK));
        final SelectBox<Integer> scenarioCountDropdown = game.getButtonManager().createIntDropdown();
        scenarioCountDropdown.setItems(3, 4, 5, 6, 7, 8, 9, 10, 20);
        scenarioCountDropdown.setSize(fullscreenCheckbox.getWidth(), fullscreenCheckbox.getHeight());
        scenarioCountDropdown.setAlignment(Align.center);
        scenarioCountDropdown.setSelected(5);

        HorizontalGroup checkBoxGroup = new HorizontalGroup();
        checkBoxGroup.addActor(scenarioCountDropdown);
        checkBoxGroup.addActor(dropdownLabel);
        checkBoxGroup.space(5f);

        table.add(fullscreenCheckbox);
        table.row();
        table.add(checkBoxGroup).padTop(20f);
        table.row();
        addBackButton();
    }

    public int getScenarioCustomerCount() {
        Actor dropdownGroup = table.getChild(1);
        if (dropdownGroup instanceof HorizontalGroup) {
            Actor dropdownUnchecked = (((HorizontalGroup) dropdownGroup).getChild(0));
            if (dropdownUnchecked instanceof SelectBox) {
                return ((SelectBox<Integer>) dropdownUnchecked).getSelected();
            }
        }
        return 5; // default
    }
}
