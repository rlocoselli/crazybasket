package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import MainGame.ActionResolver;
import MainGame.BasketballGame;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.InputHandler;

public class GameScreen implements Screen {
    private static GameWorld world;
    public static BasketballGame game;
    private GameRenderer renderer;
    public static float sW = Gdx.graphics.getWidth();
    public static float sH = Gdx.graphics.getHeight();
    public static float gameHeight = 1080;
    public static float gameWidth = sW / (sH / gameHeight);

    public GameScreen(BasketballGame game, ActionResolver actionResolver, GameWorld.STATE state) {
        this.game = game;
        world = new GameWorld(game, actionResolver, gameWidth, gameHeight, state);
        Gdx.input.setInputProcessor(new InputHandler(world, sW / gameWidth, sH
                / gameHeight));
        renderer = new GameRenderer(world);
    }

    @Override
    public void render(float delta) {
        world.update(delta);
        renderer.render();
    }


    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
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
    }
}
