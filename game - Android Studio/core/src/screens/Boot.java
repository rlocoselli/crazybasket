package screens;

import com.badlogic.gdx.Screen;

import MainGame.ActionResolver;
import MainGame.BasketballGame;

public class Boot implements Screen {
    private final BasketballGame game;
    private final ActionResolver actionResolver;

    public Boot(BasketballGame game, ActionResolver actionResolver) {
        this.game = game;
        this.actionResolver = actionResolver;
        game.setScreen(new LoadingScreen(game, actionResolver));
    }

    @Override
    public void show() {
        game.setScreen(new LoadingScreen(game, actionResolver));
    }

    @Override
    public void render(float delta) {

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
