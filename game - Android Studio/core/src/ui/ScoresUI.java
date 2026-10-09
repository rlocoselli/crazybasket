package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import configuration.Configuration;
import configuration.Settings;
import gameobjects.GameObject;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;

public class ScoresUI extends GameObject {
    public Text scoreText, bestText;

    public ScoresUI(GameWorld world, float x, float y, float width, float height,
                    TextureRegion texture,
                    Color color, Shape shape) {
        super(world, x, y, width, height, texture, color, shape);
        scoreText = new Text(world, x + 30, y, width, height, AssetLoader.square, Color.WHITE,
                "", AssetLoader.font08,
                FlatColors.parseColor("edf5ff"), 22, Align.left);
        bestText = new Text(world, x, y, world.gameWidth - x - 30, height, AssetLoader.square,
                Color.WHITE,
                Configuration.BEST_TEXT + AssetLoader.getHighScore(), AssetLoader.font08,
                FlatColors.parseColor("ffd082"), 22, Align.right);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        scoreText.setPosition(getSprite().getX() + 30, getSprite().getY());
        scoreText.update(delta);
        bestText.update(delta);
    }

    @Override
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        UiDraw.panel(batch,sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight(),
                new Color(0.07f,0.12f,0.22f,0.94f));
        scoreText.render(batch, shapeRenderer, GameRenderer.fontShader);
        bestText.render(batch, shapeRenderer, GameRenderer.fontShader);
    }

    public void renderMenu(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        super.render(batch, shapeRenderer);
        scoreText.render(batch, shapeRenderer, GameRenderer.fontShader);
    }

    public void setScoreText(String score) {
        scoreText.setText(score);
    }

    public void setBestText(String score) {
        bestText.setText(score);
    }


}
