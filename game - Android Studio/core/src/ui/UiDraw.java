package ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** Small code-drawn UI primitives; corners retain their radius at any panel size. */
public final class UiDraw {
    private static NinePatch panel;
    private static TextureRegion star, ring;
    public static void init() {
        if (panel != null) return;
        Pixmap p = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fillRectangle(16, 0, 32, 64); p.fillRectangle(0, 16, 64, 32);
        for (int x : new int[]{16,47}) for (int y : new int[]{16,47}) p.fillCircle(x,y,16);
        Texture t = new Texture(p); t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        panel = new NinePatch(t, 18,18,18,18); p.dispose();
        p = new Pixmap(64,64,Pixmap.Format.RGBA8888); p.setColor(Color.WHITE);
        for(int i=0;i<10;i++) {
            double a=i*Math.PI/5-Math.PI/2,b=(i+1)*Math.PI/5-Math.PI/2;
            float ra=i%2==0?30:13, rb=(i+1)%2==0?30:13;
            p.fillTriangle(32,32,32+(int)(Math.cos(a)*ra),32+(int)(Math.sin(a)*ra),
                    32+(int)(Math.cos(b)*rb),32+(int)(Math.sin(b)*rb));
        }
        t = new Texture(p); t.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        star=new TextureRegion(t);p.dispose();
        p=new Pixmap(128,128,Pixmap.Format.RGBA8888);p.setColor(Color.WHITE);
        for(int r=55;r<60;r++) p.drawCircle(64,64,r);
        t=new Texture(p);t.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        ring=new TextureRegion(t);p.dispose();
    }
    public static void panel(SpriteBatch batch,float x,float y,float width,float height,Color color) {
        init(); panel.setColor(color); panel.draw(batch,x,y,width,height);
    }
    public static TextureRegion star() {init();return star;}
    public static TextureRegion ring() {init();return ring;}
}
