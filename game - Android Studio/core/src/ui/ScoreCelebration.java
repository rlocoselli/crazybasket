package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import gameworld.GameWorld;
import helpers.AssetLoader;

/** Elastic entrance, rotating star burst, expanding rings and falling confetti. */
public final class ScoreCelebration {
    private static final float DURATION=1.9f;
    private final GameWorld world;
    private final Sprite sticker;
    private final Sprite[] confetti=new Sprite[48], stars=new Sprite[12];
    private final Sprite[] rings=new Sprite[2];
    private final float[] vx=new float[48],vy=new float[48];
    private float remaining,centerX,centerY,baseY;
    private long voiceId=-1;
    public ScoreCelebration(GameWorld world) {
        this.world=world;
        sticker=new Sprite(AssetLoader.goodSticker);
        float width=Math.min(540,world.gameWidth*0.32f);
        sticker.setSize(width,width*sticker.getRegionHeight()/sticker.getRegionWidth());
        sticker.setOriginCenter();
        for(int i=0;i<confetti.length;i++) {
            confetti[i]=new Sprite(i%3==0?UiDraw.star():AssetLoader.square);
            confetti[i].setSize(i%3==0?22:12,i%3==0?22:22);confetti[i].setOriginCenter();
        }
        for(int i=0;i<stars.length;i++) {stars[i]=new Sprite(UiDraw.star());stars[i].setSize(40+i%3*12,40+i%3*12);stars[i].setOriginCenter();}
        for(int i=0;i<rings.length;i++) {rings[i]=new Sprite(UiDraw.ring());rings[i].setSize(300,300);rings[i].setOriginCenter();}
    }
    public void trigger() {
        remaining=DURATION;centerX=world.gameWidth/2;centerY=world.gameHeight*0.7f;
        baseY=centerY-sticker.getHeight()/2;
        sticker.setPosition(centerX-sticker.getWidth()/2,baseY);sticker.setScale(0.05f);sticker.setAlpha(1);
        for(int i=0;i<confetti.length;i++) {
            Sprite piece=confetti[i];piece.setPosition(centerX,centerY);piece.setRotation(i*37);
            piece.setColor(new Color(new int[]{0xffc54dff,0x45d6e8ff,0xff718fff,0x9bea78ff}[i%4]));
            float a=MathUtils.PI2*i/confetti.length;
            vx[i]=MathUtils.cos(a)*MathUtils.random(300,620);
            vy[i]=MathUtils.sin(a)*MathUtils.random(240,460)+190;
        }
        if(AssetLoader.getSounds()) {
            if(voiceId!=-1) AssetLoader.goodVoice.stop(voiceId);
            voiceId=AssetLoader.goodVoice.play(0.9f);
        }
    }
    public void update(float delta) {
        if(remaining<=0)return;
        remaining=Math.max(0,remaining-delta);
        float t=DURATION-remaining, scale;
        if(t<0.24f) {float u=t/0.24f;scale=1.35f*(1-(1-u)*(1-u)*(1-u));}
        else if(t<0.42f) scale=1.35f-0.43f*(t-0.24f)/0.18f;
        else scale=1f+0.06f*MathUtils.sin((t-0.42f)*13f)*(remaining/DURATION);
        sticker.setScale(scale,scale*(1f+0.07f*MathUtils.sin(t*20f)*(remaining/DURATION)));
        sticker.setAlpha(Math.min(1,remaining/0.35f));
        sticker.setRotation(9f*MathUtils.sin(t*10f)*(remaining/DURATION));
        sticker.setY(baseY+32*t+12*MathUtils.sin(t*6f));
        for(int i=0;i<stars.length;i++) {
            float a=MathUtils.PI2*i/stars.length+t*0.7f;
            float radius=100+180*Math.min(1,t/0.55f);
            Sprite star=stars[i];star.setPosition(centerX+MathUtils.cos(a)*radius-star.getWidth()/2,
                    centerY+MathUtils.sin(a)*radius*0.65f-star.getHeight()/2);
            star.setRotation(t*130+i*30);star.setScale(0.6f+0.4f*MathUtils.sin(t*7f+i));
            star.setColor(i%2==0?new Color(1f,0.8f,0.3f,1):new Color(0.45f,0.9f,1,1));
            star.setAlpha(Math.min(1,remaining/0.6f));
        }
        for(int i=0;i<rings.length;i++) {
            float u=Math.max(0,t-i*0.15f);
            rings[i].setPosition(centerX-150,centerY-150);rings[i].setScale(0.4f+u*2.2f);
            rings[i].setColor(1,0.8f,0.3f,Math.max(0,0.45f-u*0.5f));
        }
        for(int i=0;i<confetti.length;i++) {
            vy[i]-=560*delta;confetti[i].translate(vx[i]*delta,vy[i]*delta);
            confetti[i].rotate((i%2==0?240:-240)*delta);confetti[i].setAlpha(Math.min(1,remaining/0.5f));
        }
    }
    public void render(SpriteBatch batch) {
        if(remaining<=0)return;
        for(Sprite ring:rings)ring.draw(batch);
        for(Sprite piece:confetti)piece.draw(batch);
        for(Sprite star:stars)star.draw(batch);
        sticker.draw(batch);
    }
}
