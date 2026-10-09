package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import configuration.Configuration;
import configuration.Settings;
import gameobjects.Ball;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;

/**
 * Created by ManuGil on 08/07/15.
 */
public class Launcher {
    private ArrayList<Vector2> points = new ArrayList<Vector2>();
    private Vector2 point1 = new Vector2(), point2 = new Vector2();
    public boolean isPressed = false;
    private GameWorld world;
    private Sprite sprite, arrowCap;
    private Ball currentBall;
    public Rectangle rect;

    public Launcher(GameWorld world) {
        this.world = world;
        sprite = new Sprite(AssetLoader.arrowBot);
        sprite.setSize(Settings.ARROW_WIDTH, 100);
        arrowCap = new Sprite(AssetLoader.arrowCap);
        arrowCap.setSize(Settings.ARROW_WIDTH, (Settings.ARROW_WIDTH * AssetLoader.arrowCap
                .getRegionHeight()) / AssetLoader.arrowCap
                .getRegionWidth());
        float launchX = world.basket.getPosition().x + 460;
        rect = new Rectangle(launchX, world.floor.getRectangle().height + Settings.BALL_SIZE / 2,
                Math.max(100, world.gameWidth - launchX - Settings.BALL_SIZE / 2),
                Math.min(400, world.gameHeight - world.floor.getRectangle().height - 250));
    }

    public void update(float delta) {
        if (isPressed) {
            currentBall.setToLauncher(point1);
            sprite.setRotation(angleBetweenTwoPoints(point1, point2) + 180);
            sprite.setSize(Settings.ARROW_WIDTH, distanceBetweenTwoPoints(point1, point2));
            sprite.setOrigin(sprite.getWidth() / 2, 0f);
            sprite.setPosition(point1.x - (sprite.getWidth() / 2), point1.y);
            arrowCap.setRotation(angleBetweenTwoPoints(point1, point2) + 180);
            arrowCap.setPosition(point2.x - (arrowCap.getWidth() / 2), point2.y);
            arrowCap.setOrigin(arrowCap.getWidth() / 2, 0f);
        }
        /*
        points.clear();
        if (isPressed) {
            float distance = 0;
            for (int i = 0; i < 100; i++) {
                float h = point2.x;
                float k = point2.y;
                float x = point1.x;
                float y = point1.y;
                double a = -y / (Math.pow(x - h, 2) + k);
                Vector2 p = new Vector2(point1.x - (i * 5),
                        (float) (a * Math.pow((point1.x - (i * 5)) - h, 2) + k));
                points.add(p);
                distance = (float) (a * Math.pow((point1.x) - h, 2) + k);
            }
            for (int i = 0; i < points.size(); i++) {
                points.get(i).set(points.get(i).x, points.get(i).y - distance + point1.y);
            }
        }*/
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (isPressed) {
            if (Configuration.DEBUG) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                shapeRenderer.setColor(FlatColors.RED);
                shapeRenderer.circle(point1.x, point1.y, 5);
                shapeRenderer.circle(point2.x, point2.y, 5);
                shapeRenderer.end();
                batch.begin();
            }
            if (world.difficulty.trajectoryGuide) {
                Vector2 launchVelocity = velFromTwoPoints(point1, point2).scl(Settings.BALL_FORCE_SCALE);
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                for (int i = 1; i <= 24; i++) {
                    float t = i * 0.06f;
                    float px = point1.x + launchVelocity.x * t * Settings.PTM;
                    float py = point1.y + launchVelocity.y * t * Settings.PTM
                            + 0.5f * world.worldB.getGravity().y * t * t * Settings.PTM;
                    if (py < world.floor.rectangle.height || px < 0 || px > world.gameWidth) break;
                    shapeRenderer.setColor(1f, 1f, 0.7f, 1f - i / 30f);
                    shapeRenderer.circle(px, py, 7, 12);
                }
                shapeRenderer.end();
                batch.begin();
            }
            sprite.draw(batch);
            arrowCap.draw(batch);
        }
    }

    public void touchDown(int x, int y) {
        if (rect.contains(x, y)) {
            if (world.ballsUI.balls > 0) {
                isPressed = true;
                point1.set(x, y);
                point2.set(x, y);
                currentBall = world.getIdleBall();
                currentBall.resetScoreLogics();
            }
        } else {
            world.tutorialEffect();
        }
    }

    public void touchUp() {
        if (isPressed) {
            isPressed = false;
            currentBall.flight(velFromTwoPoints(point1, point2).scl(Settings.BALL_FORCE_SCALE));
            if (world.state == GameWorld.STATE.GAME)
                world.ballsUI.removeBall(1);
            if (AssetLoader.getSounds()) AssetLoader.swoosh.play();
        }

    }

    public void touchDragged(int x, int y) {
        float px = x - point1.x;
        float py = y - point1.y;

        point2.set(point1.x - px, point1.y - py);
        fadeColor(FlatColors.parseColor(Settings.ARROW_RED_COLOR),
                FlatColors.parseColor(Settings.ARROW_GREEN_COLOR),
                distanceBetweenTwoPoints(point1, point2) - 250);
    }

    public static float angleBetweenTwoPoints(Vector2 one, Vector2 two) {
        float deltaY = one.y - two.y;
        float deltaX = two.x - one.x;
        double angle = Math.toDegrees(Math.atan2(deltaX, deltaY));
        if (angle < 0) {
            angle = 360 + angle;
        }

        return (float) angle;
    }

    public float distanceBetweenTwoPoints(Vector2 p1, Vector2 p2) {

        return (float) Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2));
    }

    public Vector2 velFromTwoPoints(Vector2 p1, Vector2 p2) {
        return new Vector2(p2.x - p1.x, p2.y - p1.y);
    }

    private void fadeColor(Color color1, Color color2, float step) {
        final float dRed = color1.r - color2.r;
        final float dGreen = color1.g - color2.g;
        final float dBlue = color1.b - color2.b;
        // No point if no difference.
        if (dRed != 0 || dGreen != 0 || dBlue != 0) {
            // Do it in n steps.
            if (step < 0) step = 0;
            final Color c = new Color(
                    color2.r + ((dRed * step) / 80),
                    color2.g + ((dGreen * step) / 30),
                    color2.b + ((dBlue * step) / 80), 1f);
            arrowCap.setColor(c);
            sprite.setColor(c);

        }
    }
}
