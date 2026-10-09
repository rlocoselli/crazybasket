package helpers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {

    public static AssetManager manager;

    public static void load() {
        manager = new AssetManager();
        Texture.setAssetManager(manager);
        //Textures
        manager.load("arrow.png", Texture.class);
        manager.load("arrowBot.png", Texture.class);
        manager.load("arrowCap.png", Texture.class);
        manager.load("background.png", Texture.class);
        manager.load("background_menu.png", Texture.class);
        manager.load("ball.png", Texture.class);
        manager.load("basket.png", Texture.class);
        manager.load("buttons.png", Texture.class);
        manager.load("dot.png", Texture.class);
        manager.load("logo.png", Texture.class);
        manager.load("crazy-basket-logo.png", Texture.class);
        manager.load("good-sticker.png", Texture.class);
        manager.load("musicbuttons.png", Texture.class);
        manager.load("soundbuttons.png", Texture.class);
        manager.load("net.png", Texture.class);
        manager.load("pointer.png", Texture.class);
        manager.load("scoreback.png", Texture.class);
        manager.load("shadow.png", Texture.class);
        manager.load("square.png", Texture.class);
        manager.load("timerback.png", Texture.class);
        manager.load("title.png", Texture.class);
        manager.load("tutorial.png", Texture.class);

        for (configuration.Scenario scene : configuration.Scenario.values())
            manager.load("courts/" + scene.name().toLowerCase(java.util.Locale.ROOT) + ".png", Texture.class);

        //Font
        manager.load("misc/font.fnt", BitmapFont.class);

        //Sounds
        manager.load("sounds/net.wav", Sound.class);
        manager.load("sounds/good.wav", Sound.class);
        manager.load("sounds/swoosh.wav", Sound.class);
        manager.load("sounds/buzzer.wav", Sound.class);
        manager.load("sounds/click.wav", Sound.class);

        //Music
        manager.load("sounds/music.mp3", Music.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}
