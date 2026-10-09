package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import MainGame.ActionResolver;
import MainGame.BasketballGame;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import configuration.Settings;
import helpers.Assets;
import helpers.FlatColors;
import tweens.SpriteAccessor;
import tweens.Value;
import tweens.ValueAccessor;

public class LoadingScreen implements Screen {

    private Sprite background, logo, progressBar, top, audela, brandPanel;
    private com.badlogic.gdx.graphics.g2d.BitmapFont brandFont;
    private ActionResolver actionResolver;
    private BasketballGame game;
    private SpriteBatch batch = new SpriteBatch();
    private TweenManager manager;
    private boolean goingToMenu = false, finishedTween = false;

    public LoadingScreen(BasketballGame game, ActionResolver actionresolver) {

        this.game = game;
        this.actionResolver = actionresolver;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float desiredWidth = Math.min(width * 0.60f, height * 0.95f);

        background = new Sprite(new Texture("square.png"));
        background.setPosition(0, 0);
        background.setColor(FlatColors.parseColor(Settings.COLOR_LOADING_SCREEN_BACKGROUND));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        top = new Sprite(background.getTexture());
        top.setPosition(0, 0);
        top.setColor(Color.BLACK);
        top.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        top.setAlpha(0);

        logo = new Sprite(new Texture("crazy-basket-logo.png"));
        float scale = desiredWidth / logo.getWidth();
        logo.setSize(logo.getWidth() * scale, logo.getHeight() * scale);
        logo.setPosition(Gdx.graphics.getWidth() / 2 - (logo.getWidth() / 2),
                Gdx.graphics.getHeight() / 2 - (logo.getHeight() / 2)+20);

        audela = new Sprite(new Texture("audela-logo.png"));
        float brandSize = Math.min(height * 0.14f, 88);
        audela.setSize(brandSize, brandSize);
        audela.setPosition(width / 2 - brandSize - 44, height * 0.10f);
        brandPanel = new Sprite(background.getTexture());
        brandPanel.setColor(Color.WHITE);
        brandPanel.setSize(brandSize + 164, brandSize + 16);
        brandPanel.setPosition(audela.getX() - 8, audela.getY() - 8);
        brandFont = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        brandFont.setColor(FlatColors.parseColor("112448"));
        brandFont.getData().setScale(Math.max(1f, brandSize / 36f));

        progressBar = new Sprite(background.getTexture());
        progressBar.setPosition(0, logo.getY()-60);
        progressBar.setSize(0, 20);
        progressBar.setColor(FlatColors.LOGO_GREEN);
        setupTween();
    }

    private void setupTween() {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        logo.setAlpha(0);
        Tween.to(logo, SpriteAccessor.ALPHA, .9f).target(1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                finishedTween = true;
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        manager.update(delta);
        batch.begin();
        background.draw(batch);
        progressBar.setSize(background.getWidth() * Assets.manager.getProgress(),
                progressBar.getHeight());
        progressBar.draw(batch);
        logo.draw(batch);
        brandPanel.draw(batch);
        audela.draw(batch);
        brandFont.draw(batch, "Audela", audela.getX() + audela.getWidth() + 8,
                audela.getY() + audela.getHeight() * 0.64f);
        top.draw(batch);
        batch.end();
        if (finishedTween) {
            if (Assets.manager.update() && !goingToMenu) {
                goToMenu();
            }
        }
    }

    private void goToMenu() {
        goingToMenu = true;
        Value timer = new Value();
        Tween.to(logo, SpriteAccessor.ALPHA, .9f).target(0).start(manager);
        Tween.to(progressBar, SpriteAccessor.ALPHA, .9f).target(0).start(manager);
        Tween.to(top, SpriteAccessor.ALPHA, .5f).target(1).delay(.4f).start(manager);
        Tween.to(timer, -1, 1f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new MenuScreen(game, actionResolver));
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(manager);
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
        batch.dispose();
        brandFont.dispose();
        background.getTexture().dispose();
        logo.getTexture().dispose();
        audela.getTexture().dispose();
    }
}
