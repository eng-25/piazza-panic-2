package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.station.Station;
import cs.eng1.piazzapanic.station.StationAction;

import java.util.List;


public class StationActionUI extends Table {

    private ActionAlignment actionAlignment = ActionAlignment.TOP;

    public enum ActionAlignment {
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }

    private final Station station;
    private final PiazzaPanicGame game;
    private final ProgressBar progress;
    private final ProgressBar failBar;


    public StationActionUI(final Station station, final PiazzaPanicGame game) {
        this.station = station;
        this.game = game;
        setVisible(false);
        center();
        bottom();

        ProgressBarStyle progressBarStyle = new ProgressBarStyle(new TextureRegionDrawable(new Texture(
                Gdx.files.internal(
                        "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_button_outline_up.png"))), null);
        progressBarStyle.knobBefore = new TextureRegionDrawable(new Texture(Gdx.files.internal(
                "Kenney-Game-Assets-1/2D assets/UI Base Pack/PNG/blue_button_gradient_up.png")));
        progress = new ProgressBar(0, 100, 0.1f, false, progressBarStyle);

        ProgressBarStyle failBarStyle = new ProgressBarStyle(new TextureRegionDrawable(new Texture(
                Gdx.files.internal(
                        "new/failBarBg.png"))), null);
        failBarStyle.knobBefore = new TextureRegionDrawable(new Texture(Gdx.files.internal(
                "new/failBarTop.png")));
        failBar = new ProgressBar(0, 100, 0.1f, false, failBarStyle);
    }

    /**
     * Initialise and show the progress bar with 0 progress.
     */
    public void showProgressBar() {
        progress.setValue(0);
        add(progress).pad(10f);
        setVisible(true);
    }

    public void showFailBar() {
        add(failBar).pad(10f).row();
        setVisible(true);
    }

    public void hideFailBar() {
        removeActor(failBar);
    }

    /**
     * @param percentage A value between 0 and 100 representing the percentage completed
     */
    public void updateProgress(float percentage) {
        progress.setValue(percentage);
    }

    public void updateFailValue(float percentage) {
        failBar.setValue(100 - percentage);
    }

    public void hideProgressBar() {
        removeActor(progress);
    }

    /**
     * Take a list of actions, clear the current visible buttons and replace them with one for every
     * possible action and generate callbacks to the station.
     *
     * @param actions The list of possible station actions to display.
     */
    public void showActions(List<StationAction.ActionType> actions, Station station) {
        hideActions();
        for (final StationAction.ActionType action : actions) {
            String actionDescription;
            if (action == StationAction.ActionType.FLIP_ACTION) {
                actionDescription = StationAction.getFlipDescription(station);
            } else {
                actionDescription = StationAction.getActionDescription(action);
            }
            TextButton actionButton;
            if (action == StationAction.ActionType.CLEAR_STATION || (action == StationAction.ActionType.BUY_STATION
                    && !station.canBuy())) {
                actionButton = game.getButtonManager()
                        .createTextButton(actionDescription, ButtonManager.ButtonColour.RED);
            } else if (action == StationAction.ActionType.BUY_STATION) {
                actionButton = game.getButtonManager()
                        .createTextButton(actionDescription, ButtonManager.ButtonColour.GREEN);
            } else {
                actionButton = game.getButtonManager()
                        .createTextButton(actionDescription, ButtonManager.ButtonColour.BLUE);
            }
            actionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    station.doStationAction(action);
                    super.clicked(event, x, y);
                }
            });
            add(actionButton).width(100).height(30).pad(2f);
            row();
        }
        setVisible(true);
    }

    /**
     * Hide all the possible actions, while keeping the progress visible if it is there.
     */
    public void hideActions() {
        setVisible(false);

        boolean hasProgress = getChildren().contains(progress, true);
        boolean hasFail = getChildren().contains(failBar, true);

        clearChildren();
        if (hasProgress) {
            add(progress).pad(10f);
            setVisible(true);
        }

        if (hasFail) {
            showFailBar();
        }
    }

    public void setActionAlignment(ActionAlignment actionAlignment) {
        this.actionAlignment = actionAlignment;
        center();
        switch (actionAlignment) {
            case TOP:
                bottom();
                break;
            case BOTTOM:
                top();
                break;
            case LEFT:
                right();
                break;
            case RIGHT:
                left();
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        calculatePositionFromAlignment();
        super.draw(batch, parentAlpha);
    }

    /**
     * Take the set alignment of buttons and position them and this class on the given side of the
     * station by transforming the station's world position to screen position.
     */
    private void calculatePositionFromAlignment() {
        Vector3 worldPosition = new Vector3();
        switch (actionAlignment) {
            case LEFT:
                // Get left center of station in screen coordinates
                worldPosition.x = station.getX();
                worldPosition.y = station.getY() + station.getHeight() / 2f;
                break;
            case TOP:
                // Get upper middle of station in screen coordinates
                worldPosition.x = station.getX() + station.getWidth() / 2f;
                worldPosition.y = station.getY() + station.getHeight();
                break;
            case RIGHT:
                // Get right center of station in screen coordinates
                worldPosition.x = station.getX() + station.getWidth();
                worldPosition.y = station.getY() + station.getHeight() / 2f;
                break;
            case BOTTOM:
                // Get lower middle of station in screen coordinates
                worldPosition.x = station.getX() + station.getWidth() / 2f;
                worldPosition.y = station.getY();
                break;
        }
        Vector3 screenPosition = station.getStage().getCamera().project(worldPosition);
        setPosition(screenPosition.x, screenPosition.y);
    }
}
