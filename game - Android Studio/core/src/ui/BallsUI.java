package ui;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import aurelienribon.tweenengine.Tween;
import gameobjects.GameObject;
import gameworld.GameRenderer;
import gameworld.GameWorld;
import helpers.AssetLoader;
import helpers.FlatColors;
import tweens.SpriteAccessor;
public class BallsUI extends GameObject {
    public Text timeText;
    public GameObject ball;
    public int balls;
    public BallsUI(GameWorld world,float x,float y,float width,float height,TextureRegion texture,Color color,Shape shape) {
        super(world,x,y,width,height,texture,color,shape);
        balls=world.difficulty.initialBalls(world.scenario);
        ball=new GameObject(world,x+width/2-80,y+20,height-40,height-40,AssetLoader.ball,FlatColors.WHITE,Shape.CIRCLE);
        timeText=new Text(world,ball.position.x+ball.sprite.getWidth()+5,y,width,height,AssetLoader.square,
                Color.WHITE,"x "+balls,AssetLoader.font08,FlatColors.parseColor("edf5ff"),22,Align.left);
    }
    public void update(float delta) {super.update(delta);ball.update(delta);}
    public void render(SpriteBatch batch,ShapeRenderer renderer) {
        UiDraw.panel(batch,sprite.getX(),sprite.getY(),sprite.getWidth(),sprite.getHeight(),new Color(0.07f,0.12f,0.22f,0.94f));
        timeText.render(batch,renderer,GameRenderer.fontShader);ball.render(batch,renderer);
    }
    public void addBall(int amount) {balls+=amount;timeText.setText("x "+balls);}
    public void removeBall(int amount) {balls=Math.max(0,balls-amount);timeText.setText("x "+balls);}
    public void effectBall() {
        ball.sprite.setScale(1);
        Tween.to(ball.sprite,SpriteAccessor.SCALE,.2f).target(1.2f).repeatYoyo(1,0f).start(ball.getManager());
    }
}
