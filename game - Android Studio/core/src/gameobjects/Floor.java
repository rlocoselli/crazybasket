package gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import configuration.Settings;
import gameworld.GameWorld;

public class Floor extends GameObject {

    private Body body;

    public Floor(GameWorld world, float x, float y, float width, float height,
                 TextureRegion texture,
                 Color color, Shape shap) {
        super(world, x, y, width, height, texture, color, shap);

        rectangle.setHeight(rectangle.height + 5);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((sprite.getX() + sprite.getWidth() / 2) / Settings.PTM,
                (sprite.getY() + sprite.getHeight() / 2) / Settings.PTM);
        body = world.worldB.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / Settings.PTM, sprite.getHeight() / 2 / Settings.PTM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Settings.CATEGORY_FLOOR;
        fixtureDef.filter.maskBits = Settings.MASK_FLOOR;
        body.createFixture(fixtureDef);
        shape.dispose();

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        sprite.setPosition((body.getPosition().x * Settings.PTM) - sprite.getWidth() / 2,
                (body.getPosition().y * Settings.PTM) - sprite.getHeight() / 2);
        rectangle.setPosition((body.getPosition().x * Settings.PTM) - sprite.getWidth() / 2,
                (body.getPosition().y * Settings.PTM) - sprite.getHeight() / 2);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        //if (Configuration.DEBUG)
        //super.render(batch, shapeRenderer);
    }
}
