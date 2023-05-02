package cs.eng1.piazzapanic.ui.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;

/**
 * A base overlay class, holding a single table with a solid background
 */
public abstract class BaseOverlay {

    protected final PiazzaPanicGame game;
    protected final Table table;
    protected final ButtonManager buttonManager;

    public BaseOverlay(final PiazzaPanicGame game) {
        this.game = game;
        buttonManager = game.getButtonManager();
        table = new Table();
        setupTable();
    }

    /**
     * Sets up the overlay table, centering and filling the background color
     */
    private void setupTable() {
        table.setFillParent(true);
        table.setVisible(false);
        table.center();
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.LIGHT_GRAY);
        bgPixmap.fill();
        table.setBackground(new TextureRegionDrawable(new Texture(bgPixmap)));
    }

    public void addToStage(Stage uiStage) {
        uiStage.addActor(table);
    }

    public void show() {
        table.toFront();
        table.setVisible(true);
    }

    public void hide() {
        table.setVisible(false);
    }

    /**
     * Convenience method to add a back button to the overlay table
     * which will hide this overlay, showing the one underneath
     */
    public void addBackButton() {
        TextButton backButton = game.getButtonManager()
                .createTextButton("Back", ButtonManager.ButtonColour.GREY);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        table.add(backButton).padTop(20f);
    }

}
