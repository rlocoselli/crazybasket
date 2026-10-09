package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import configuration.Configuration;
import configuration.Settings;
import gameobjects.GameObject;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.Value;

public class TimeUI extends GameObject {

    public Text timeText;
    TweenCallback cb;
    Tween timerTween;
    public Value timer = new Value();
    public TweenManager manager;
    float everyTime;
    private TweenCallback cbFinish;

    public TimeUI(final GameWorld world, float x, float y, float width, float height,
                  TextureRegion texture,
                  Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        timeText = new Text(world, x, y, width, height, AssetLoader.square, Color.WHITE,
                configuration.Localization.t("time") + "0", AssetLoader.font08, FlatColors.parseColor(Settings.TEXT_COLOR), 22,
                Align.center);
        everyTime = Settings.INITIAL_TIME;
        manager = new TweenManager();
        timer.setValue(0);
        cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                timerTween.pause();
                timerTween.kill();
                manager.killAll();
                startTimer(everyTime -= Settings.REDUCTION_EVERY_POINT, 0.5f);
            }
        };
        cbFinish = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
               world.finish();
            }
        };

    }

    @Override
    public void update(float delta) {
        manager.update(delta);
        super.update(delta);
        float value = timer.getValue();
        if (timer.getValue() >= 10)
            setTimeText("" + Math.round(value * 100) / 100);
        else setTimeText("0" + Math.round(value * 100) / 100);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
        timeText.render(batch, shapeRenderer, GameRenderer.fontShader);
    }

    public void setTimeText(String time) {
        timeText.setText(Configuration.TIME_TEXT + time);
    }

    public void startTimer(float time, float delay) {
        timerTween = null;
        timer = new Value();
        timer.setValue(time);
        timerTween = Tween.to(timer, -1, time).delay(delay).setCallback(cbFinish)
                .setCallbackTriggers(TweenCallback.COMPLETE).target(0).ease(
                        TweenEquations.easeNone).start(manager);
    }

    public void reset() {
        timerTween = null;
        timerTween = Tween.to(timer, -1, .3f).target(everyTime).ease(
                TweenEquations.easeInOutSine).setCallback(cb)
                .setCallbackTriggers(TweenCallback.COMPLETE).start(getManager());
    }

}
