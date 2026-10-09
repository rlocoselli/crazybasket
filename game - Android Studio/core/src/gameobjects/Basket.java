package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import configuration.Configuration;
import configuration.Settings;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;

public class Basket extends GameObject {

    Body body;
    GameObject contact1, contact2;
    public boolean c1 = false, c2 = false;
    public Sprite net,basket;
    private Ball currentBall;

    public Basket(GameWorld world, float x, float y, float width, float height,
                  TextureRegion texture,
                  Color color, Shape s) {
        super(world, x, y, width, height, texture, color, s);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((sprite.getX() - 10 + sprite.getWidth() / 2) / Settings.PTM,
                (sprite.getY() + sprite.getHeight() / 2) / Settings.PTM);
        body = world.worldB.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(((sprite.getWidth() - 40) / 2) / Settings.PTM,
                sprite.getHeight() / 2 / Settings.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Settings.CATEGORY_BASKET;
        fixtureDef.filter.maskBits = Settings.MASK_BASKET;
        body.createFixture(fixtureDef);
        shape.dispose();

        BodyDef bodyDef1 = new BodyDef();
        bodyDef1.type = BodyDef.BodyType.StaticBody;
        bodyDef1.position.set((sprite.getX() + 160 + sprite.getWidth() / 2) / Settings.PTM,
                (sprite.getY() + 230 + sprite.getHeight() / 2) / Settings.PTM);
        body = world.worldB.createBody(bodyDef1);

        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(sprite.getWidth() / 2 / Settings.PTM, 5 / Settings.PTM);
        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape1;
        fixtureDef1.filter.categoryBits = Settings.CATEGORY_BASKET;
        fixtureDef1.filter.maskBits = Settings.MASK_BASKET;
        body.createFixture(fixtureDef1);
        shape1.dispose();

        BodyDef bodyDef2 = new BodyDef();
        bodyDef2.type = BodyDef.BodyType.StaticBody;
        bodyDef2.position.set((sprite.getX() + 390 + sprite.getWidth() / 2) / Settings.PTM,
                (sprite.getY() + 230 + sprite.getHeight() / 2) / Settings.PTM);
        body = world.worldB.createBody(bodyDef2);

        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(10 / Settings.PTM, 5 / Settings.PTM);
        FixtureDef fixtureDef2 = new FixtureDef();
        fixtureDef2.shape = shape2;
        fixtureDef2.filter.categoryBits = Settings.CATEGORY_BASKET;
        fixtureDef2.filter.maskBits = Settings.MASK_BASKET;
        body.createFixture(fixtureDef2);
        shape2.dispose();


        //TABLERO
        BodyDef bodyDef3 = new BodyDef();
        bodyDef3.type = BodyDef.BodyType.StaticBody;
        bodyDef3.position.set((sprite.getX() + 160 - sprite.getWidth() + sprite
                        .getWidth() / 2) / Settings.PTM,
                (sprite.getY() + 330 + sprite.getHeight() / 2) / Settings.PTM);
        body = world.worldB.createBody(bodyDef3);

        PolygonShape shape3 = new PolygonShape();
        shape3.setAsBox(sprite.getWidth() / 2 / Settings.PTM, 170 / Settings.PTM);
        FixtureDef fixtureDef3 = new FixtureDef();
        fixtureDef3.shape = shape3;
        fixtureDef3.filter.categoryBits = Settings.CATEGORY_BASKET;
        fixtureDef3.filter.maskBits = Settings.MASK_BASKET;
        body.createFixture(fixtureDef3);
        shape3.dispose();

        //NET
        BodyDef bodyDef4 = new BodyDef();
        bodyDef4.type = BodyDef.BodyType.StaticBody;
        bodyDef4.position.set((sprite.getX() + 205 + sprite
                        .getWidth() / 2) / Settings.PTM,
                (sprite.getY() + 140 + sprite.getHeight() / 2) / Settings.PTM);
        bodyDef4.angle = (float) Math.toRadians(80);
        body = world.worldB.createBody(bodyDef4);

        PolygonShape shape4 = new PolygonShape();
        shape4.setAsBox(5 / Settings.PTM, 70 / Settings.PTM);
        FixtureDef fixtureDef4 = new FixtureDef();
        fixtureDef4.shape = shape4;
        fixtureDef4.filter.categoryBits = Settings.CATEGORY_BASKET;
        fixtureDef4.filter.maskBits = Settings.MASK_BASKET;
        body.createFixture(fixtureDef4);
        shape4.dispose();

        BodyDef bodyDef5 = new BodyDef();
        bodyDef5.type = BodyDef.BodyType.StaticBody;
        bodyDef5.position.set((sprite.getX() + 375 + sprite
                        .getWidth() / 2) / Settings.PTM,
                (sprite.getY() + 140 + sprite.getHeight() / 2) / Settings.PTM);
        bodyDef5.angle = (float) Math.toRadians(105);
        body = world.worldB.createBody(bodyDef5);

        PolygonShape shape5 = new PolygonShape();
        shape5.setAsBox(5 / Settings.PTM, 70 / Settings.PTM);
        FixtureDef fixtureDef5 = new FixtureDef();
        fixtureDef5.shape = shape5;
        fixtureDef5.filter.categoryBits = Settings.CATEGORY_BASKET;
        fixtureDef5.filter.maskBits = Settings.MASK_BASKET;
        body.createFixture(fixtureDef5);
        shape5.dispose();

        contact1 = new GameObject(world,
                (sprite.getX() + 190 + sprite.getWidth() / 2) + (sprite.getWidth() / 2),
                (sprite.getY() + 230 + sprite.getHeight() / 2), 140, 5,
                AssetLoader.square, FlatColors.RED, Shape.RECTANGLE);

        contact2 = new GameObject(world,
                (sprite.getX() + 190 + sprite.getWidth() / 2) + (sprite.getWidth() / 2),
                (sprite.getY() + 130 + sprite.getHeight() / 2), 140, 5,
                AssetLoader.square, FlatColors.BLUE, Shape.RECTANGLE);

        net = new Sprite(AssetLoader.net);
        net.setSize(AssetLoader.netT.getWidth(), AssetLoader.netT.getHeight());
        net.setPosition((sprite.getX() + 180 + sprite.getWidth() / 2),
                (sprite.getY() + 235 + sprite.getHeight() / 2) - AssetLoader.netT.getHeight());

        basket = new Sprite(AssetLoader.basket);
        basket.setSize(AssetLoader.basket.getRegionWidth(), AssetLoader.basket.getRegionHeight());
        basket.setPosition(net.getX() - 203, net.getY() + net.getHeight() - 544);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        contact1.update(delta);
        contact2.update(delta);
        scoreCollision();
    }

    private void scoreCollision() {
        for (int i = 0; i < world.balls.size(); i++) {
            if (!world.balls.get(i).isScored && world.balls.get(i)
                    .isFlying() && world.gameState == GameWorld.GameState.RUNNING) {
                currentBall = world.balls.get(i);
                if (Intersector.overlaps(currentBall.circle, contact1.getRectangle())) {
                    currentBall.c1 = true;
                }
                if (currentBall.c1 && Intersector.overlaps(currentBall.circle,
                        contact2.getRectangle())) {
                    currentBall.c2 = true;
                    world.balls.get(i).isScored = true;
                    scoreAPoint();
                }
            }
        }
    }

    private void scoreAPoint() {
        world.addScore(1);
        if (world.state == GameWorld.STATE.GAME)
            //world.timerUI.reset();
            world.ballsUI.addBall(1);
        world.ballsUI.effectBall();
        if (AssetLoader.getSounds()) AssetLoader.netS.play();
    }

    public void resetScoreLogic() {
        if (currentBall != null) {
            currentBall.isScored = false;
            currentBall.c1 = false;
            currentBall.c2 = false;
        }
    }


    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (Configuration.DEBUG) {
            //super.render(batch, shapeRenderer);
            contact1.render(batch, shapeRenderer);
            contact2.render(batch, shapeRenderer);
        }
        basket.draw(batch);
    }

    public void renderNet(SpriteBatch batch) {
        net.draw(batch);
    }


}
