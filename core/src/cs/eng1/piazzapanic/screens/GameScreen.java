package cs.eng1.piazzapanic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import cs.eng1.piazzapanic.PiazzaPanicGame;

public class GameScreen implements Screen {

  private final PiazzaPanicGame game;
  private final Stage stage;

  static class TestActor extends Actor {

    Texture image;
    float speed = 25.0f;
    public TestActor() {
      image = new Texture(Gdx.files.internal("badlogic.jpg"));
    }

    @Override
    public void act(float delta) {
      super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
      batch.draw(image, getX(), getY(), getWidth(), getHeight());
    }
  }

  public GameScreen(PiazzaPanicGame game) {
    this.game = game;
    OrthographicCamera camera = new OrthographicCamera();
    ScreenViewport viewport = new ScreenViewport(camera);
    this.stage = new Stage(viewport);
    TestActor testActor = new TestActor();
    testActor.setBounds(5, 5, 200, 200);
    testActor.setColor(1, 0, 0, 1);
    testActor.addAction(Actions.moveTo(1000, 50, 5.0f));
    this.stage.addActor(testActor);
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    this.stage.getViewport().update(width, height, true);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    stage.dispose();
  }
}
