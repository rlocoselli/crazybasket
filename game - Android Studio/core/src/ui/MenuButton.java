package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import gameobjects.GameObject;
import gameworld.GameWorld;
import helpers.AssetLoader;

/**
 * Created by ManuGil on 14/03/15.
 */
public class MenuButton extends GameObject {

    public GameObject back;
    public boolean cardStyle;
    public MenuButton(final GameWorld world, float x, float y, float width, float height,
                      TextureRegion texture, Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        back = new GameObject(world,x+5,y+5,width-10,height-10, AssetLoader.square,Color.WHITE,Shape.RECTANGLE);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (cardStyle) {
            UiDraw.panel(batch, sprite.getX(), sprite.getY()-8, sprite.getWidth(), sprite.getHeight(),
                    new Color(0,0,0,0.2f));
            Color fill = new Color(sprite.getColor());
            if(isPressed) fill.mul(0.85f,0.85f,0.85f,1f);
            UiDraw.panel(batch,sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight(),fill);
            return;
        }
        back.render(batch,shapeRenderer);
        super.render(batch, shapeRenderer);
        if (isPressed) {
            //icon.setAlpha(.5f);
            getSprite().setAlpha(0.8f);
        } else {
            // icon.setAlpha(1f);
            getSprite().setAlpha(1f);
        }
    }

    @Override
    public void update(float delta) {


        super.update(delta);back.getSprite().setPosition(sprite.getX()+5,sprite.getY()+5);
    }

}