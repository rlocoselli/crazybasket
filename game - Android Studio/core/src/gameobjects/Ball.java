package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.Value;
import tweens.ValueAccessor;

/**
 * Created by ManuGil on 08/07/15.
 */
public class Ball extends GameObject {

    public Body body;
    public boolean isScored;
    public Sprite pointer;

    public enum BallState {FLYING, FLOOR, IDLE, LAUNCHER}

    public BallState ballState;
    private Sprite shadow;

    private Tween fadeOutTween;
    public Boolean c1 = false, c2 = false;

    public Ball(GameWorld world, float x, float y, float width, float height,
                TextureRegion texture,
                Color color, Shape s) {
        super(world, x, y, width, height, texture, color, s);

        ballState = BallState.IDLE;
        Tween.registerAccessor(Value.class, new ValueAccessor());
        isScored = false;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) /
                Settings.PTM, (sprite.getY() + sprite.getHeight() / 2) / Settings.PTM);
        body = world.worldB.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setPosition(new Vector2(sprite.getWidth() / 2 / Settings.PTM,
                sprite.getHeight() / 2 / Settings.PTM));
        shape.setRadius(circle.radius / Settings.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Settings.CATEGORY_BALL;
        fixtureDef.filter.maskBits = Settings.MASK_BALL;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.6f;
        body.createFixture(fixtureDef);
        shape.dispose();
        circle.setRadius(sprite.getWidth() / 2);

        pointer = new Sprite(AssetLoader.pointer);
        pointer.setPosition(-100, -100);
        pointer.setSize(50, 50);
        pointer.setColor(FlatColors.parseColor(Settings.TOP_POINTER_COLOR));

        shadow = new Sprite(AssetLoader.shadow);
        shadow.setPosition(circle.x, world.floor.getRectangle().height - 10);
        shadow.setSize(sprite.getWidth(), 20);
        shadow.setAlpha(0.2f);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isIdle()) {
            body.setLinearVelocity(Vector2.Zero);
            body.setAngularVelocity(0);
            body.setGravityScale(0);
            body.setTransform(new Vector2(-500, -500), 0);
        } else {
            sprite.setPosition((body.getPosition().x * Settings.PTM),
                    (body.getPosition().y * Settings.PTM));
            circle.setPosition(body.getWorldPoint(body.getLocalCenter()).x * Settings.PTM,
                    body.getWorldPoint(body.getLocalCenter()).y * Settings.PTM);
            sprite.setRotation((float) Math.toDegrees(body.getAngle()));
            sprite.setOrigin(0, 0);
            collisions();
            shadow.setPosition(
                    body.getWorldPoint(body.getLocalCenter()).x * Settings.PTM - circle.radius,
                    shadow.getY());
            if (sprite.getColor().a - 0.7f >= 0)
                shadow.setAlpha(sprite.getColor().a - 0.7f);
            else shadow.setAlpha(0);
            if (sprite.getY() > world.gameHeight + (circle.radius * 2)) {
                pointer.setPosition(body.getWorldPoint(body.getLocalCenter()).x * Settings.PTM,
                        world.gameHeight - pointer.getHeight());
            } else {
                pointer.setPosition(-100, -100);
            }
        }
    }


    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (!isIdle()) {
            shadow.draw(batch);
            super.render(batch, shapeRenderer);
            pointer.draw(batch);
        }

    }

    private void collisions() {
        if (Intersector.overlaps(circle, world.floor.rectangle) && isFlying()) {
            touchedFloor();
        }
        if (isFlying()) {
            if (sprite.getY() < -500 ||  sprite.getX() < -500 || sprite
                    .getX() > world.gameWidth + 500) {
                touchedFloor();
                if (world.ballsUI.balls <= 0) {
                    world.finish();
                }
            }
        }
    }

    @Override
    public void setPosition(Vector2 pos) {
        body.setLinearVelocity(new Vector2());
        body.setTransform(new Vector2(pos.x - circle.radius / Settings.PTM,
                pos.y - circle.radius / Settings.PTM), 0);
    }

    public void setTransform(Vector2 point1) {
        this.body.setTransform(new Vector2((point1.x - circle.radius) / Settings.PTM,
                (point1.y - circle.radius) / Settings.PTM), 0);
    }

    public void setToLauncher(Vector2 point1) {
        ballState = BallState.LAUNCHER;
        setTransform(point1);
        body.setLinearVelocity(0, 0);
        sprite.setPosition((body.getPosition().x * Settings.PTM),
                (body.getPosition().y * Settings.PTM));
        if (fadeOutTween != null) fadeOutTween.kill();
        sprite.setAlpha(1);
    }

    public void flight(Vector2 vel) {
        body.setLinearVelocity(vel);
        body.setAngularVelocity(MathUtils.random(-10, 10));
        body.setGravityScale(1);
        ballState = BallState.FLYING;
        if (fadeOutTween != null) {
            fadeOutTween.kill();
            fadeOutTween.pause();
        }
        sprite.setAlpha(1);
    }

    public void touchedFloor() {
        if (isFlying()) {
            if (Settings.FADE_OUT_ON_FLOOR)
                fadeOut(1.3f, 0.1f);
            ballState = BallState.FLOOR;
            Value timer = new Value();
            Tween.to(timer, -1, 1.4f).target(1).setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    ballState = BallState.IDLE;
                }
            }).setCallbackTriggers(TweenCallback.COMPLETE).start(getManager());
            world.basket.resetScoreLogic();
            if (world.ballsUI.balls <= 0) {
                world.finish();
            }
        }
    }

    public boolean isFlying() {
        return ballState == BallState.FLYING;
    }

    public boolean isFloor() {
        return ballState == BallState.FLOOR;
    }

    public boolean isIdle() {
        return ballState == BallState.IDLE;
    }

    public boolean isLauncher() {
        return ballState == BallState.LAUNCHER;
    }


    public void resetScoreLogics() {
        isScored = false;
        c1 = false;
        c2 = false;
    }

}
