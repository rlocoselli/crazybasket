package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import configuration.Configuration;
import configuration.Localization;

public class AssetLoader {

    public static Texture logoTexture, dotT, arrowCapT, arrowBotT, ballT, backgroundT, netT, shadowT, pointerT;
    public static TextureRegion logo, square, dot, arrowCap, arrowBot, ball, background, net, shadow, pointer;

    public static Texture backgroundMenuT, titleT, buttonsT;
    public static TextureRegion backgroundMenu, title, playButton, practiceButton, scoresButton,
            shareButton, homeButton, scoreBack, timerBack, tutorial, noMusicButton, musicButton,
            soundButton, nosoundButton, basket;

    public static Sound netS, swoosh, buzzer, click, goodVoice;
    public static TextureRegion goodSticker;
    public static Music music;

    private static Preferences prefs;
    public static BitmapFont font, font08;

    public static void load1() {
        Localization.init();
        Configuration.SCORE_TEXT = Localization.t("score");
        Configuration.BEST_TEXT = Localization.t("best");
        Configuration.TIME_TEXT = Localization.t("time");
        logoTexture = getAssetTexture("logo.png");
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        logo = new TextureRegion(logoTexture, 0, 0, logoTexture.getWidth(),
                logoTexture.getHeight());
        load();
    }

    public static void load() {
        square = new TextureRegion(getAssetTexture("square.png"));
        goodSticker = new TextureRegion(getAssetTexture("good-sticker.png"));
        goodVoice = getAssetSound("sounds/good.wav");
        dotT = getAssetTexture("dot.png");
        dotT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        dot = new TextureRegion(dotT);

        backgroundT = getAssetTexture("background.png");
        backgroundT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        background = new TextureRegion(backgroundT);

        //ARROW
        arrowCapT = getAssetTexture("arrowCap.png");
        arrowCapT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        arrowBotT = getAssetTexture("arrowBot.png");
        arrowBotT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        arrowCap = new TextureRegion(arrowCapT);
        arrowBot = new TextureRegion(arrowBotT);

        //BALL
        ballT = getAssetTexture("ball.png");
        ballT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        ball = new TextureRegion(ballT);

        //BASKET
        basket = new TextureRegion(getAssetTexture("basket.png"));

        //NET
        netT = getAssetTexture("net.png");
        netT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        net = new TextureRegion(netT);

        //shadow
        shadowT = getAssetTexture("shadow.png");
        shadowT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        shadow = new TextureRegion(shadowT);

        //pointer
        pointerT = getAssetTexture("pointer.png");
        pointerT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        pointer = new TextureRegion(pointerT);

        //MENU
        //BACKGROUND MENU
        backgroundMenuT = getAssetTexture("background_menu.png");
        backgroundMenuT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        backgroundMenu = new TextureRegion(backgroundMenuT);

        //TITLE LOGO
        titleT = getAssetTexture("crazy-basket-logo.png");
        titleT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        title = new TextureRegion(titleT);

        //BUTTONS
        int numOfLines = 5;
        buttonsT = getAssetTexture("buttons.png");
        buttonsT.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        playButton = new TextureRegion(buttonsT, 0, (buttonsT.getHeight() / numOfLines) * 0,
                buttonsT.getWidth(), buttonsT.getHeight() / numOfLines);
        practiceButton = new TextureRegion(buttonsT, 0, (buttonsT.getHeight() / numOfLines) * 1,
                buttonsT.getWidth(), buttonsT.getHeight() / numOfLines);
        scoresButton = new TextureRegion(buttonsT, 0, (buttonsT.getHeight() / numOfLines) * 2,
                buttonsT.getWidth(), buttonsT.getHeight() / numOfLines);
        shareButton = new TextureRegion(buttonsT, 0, (buttonsT.getHeight() / numOfLines) * 3,
                buttonsT.getWidth(), buttonsT.getHeight() / numOfLines);
        homeButton = new TextureRegion(buttonsT, 0, (buttonsT.getHeight() / numOfLines) * 4,
                168, buttonsT.getHeight() / numOfLines);

        //MUSIC BUTTONS
        Texture musicButtons = getAssetTexture("musicbuttons.png");
        noMusicButton = new TextureRegion(musicButtons, 0, 0, musicButtons.getWidth() / 2,
                musicButtons.getHeight());
        musicButton = new TextureRegion(musicButtons, musicButtons.getWidth() / 2, 0,
                musicButtons.getWidth() / 2, musicButtons.getHeight());

        //SOUND BUTTONS
        Texture soundButtons = getAssetTexture("soundbuttons.png");
        nosoundButton = new TextureRegion(soundButtons, 0, 0, soundButtons.getWidth() / 2,
                soundButtons.getHeight());
        soundButton = new TextureRegion(soundButtons, soundButtons.getWidth() / 2, 0,
                soundButtons.getWidth() / 2, soundButtons.getHeight());


        //SCOREBACK
        scoreBack = new TextureRegion(getAssetTexture("scoreback.png"));
        timerBack = new TextureRegion(getAssetTexture("timerback.png"));
        tutorial = new TextureRegion(getAssetTexture("tutorial.png"));

        //FONTS
        //LOADING FONT
        Texture tfont = new Texture(Gdx.files.internal("misc/localized-font.png"), true);
        tfont.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

        font = new BitmapFont(Gdx.files.internal("misc/localized-font.fnt"), new TextureRegion(tfont),
                true);
        font.getData().setScale(1.5f, -1.5f);
        font.setColor(FlatColors.WHITE);

        font08 = new BitmapFont(Gdx.files.internal("misc/localized-font.fnt"), new TextureRegion(tfont),
                true);
        font08.getData().setScale(0.8f, -0.8f);
        font08.setColor(FlatColors.WHITE);

        //PREFERENCES
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        if (!prefs.contains("games")) {
            prefs.putInteger("games", 0);
        }

        //Sounds
        netS = getAssetSound("sounds/net.wav");
        swoosh = getAssetSound("sounds/swoosh.wav");
        buzzer = getAssetSound("sounds/buzzer.wav");
        click = getAssetSound("sounds/click.wav");
        music = Assets.manager.get("sounds/music.mp3", Music.class);

    }

    public static void dispose() {
        font.dispose();
        font08.dispose();
    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static void setButtonsClicked(int val) {
        prefs.putInteger("buttonsClicked", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void addGamesPlayed() {
        prefs.putInteger("games", prefs.getInteger("games") + 1);
        prefs.flush();
    }

    public static int getGamesPlayed() {
        return prefs.getInteger("games");
    }

    public static int getScenarioIndex() { return Math.floorMod(prefs.getInteger("scenario", 0), configuration.Scenario.values().length); }
    public static void setScenarioIndex(int index) { prefs.putInteger("scenario", index).flush(); }

    public static configuration.Difficulty getDifficulty() {
        return configuration.Difficulty.values()[Math.floorMod(prefs.getInteger("difficulty", 1), configuration.Difficulty.values().length)];
    }
    public static void nextDifficulty() {
        prefs.putInteger("difficulty", (getDifficulty().ordinal() + 1) % configuration.Difficulty.values().length).flush();
    }

    public static int getBasketDistance() { return Math.floorMod(prefs.getInteger("basketDistance", 1), 3); }
    public static int getBasketHeight() { return Math.floorMod(prefs.getInteger("basketHeight", 1), 3); }
    public static void nextBasketDistance() { prefs.putInteger("basketDistance", (getBasketDistance() + 1) % 3).flush(); }
    public static void nextBasketHeight() { prefs.putInteger("basketHeight", (getBasketHeight() + 1) % 3).flush(); }

    public static void setAds(boolean removeAdsVersion) {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
        prefs.putBoolean("ads", removeAdsVersion);
        prefs.flush();
    }

    public static boolean getAds() {
        Gdx.app.log("ADS", prefs.getBoolean("ads") + "");
        return prefs.getBoolean("ads", false);
    }


    public static int getCoinNumber() {
        return prefs.getInteger("bonus");
    }

    public static void addCoinNumber(int number) {
        prefs.putInteger("bonus", prefs.getInteger("bonus") + number);
        prefs.flush();
    }

    public static void prefs() {
        prefs = Gdx.app.getPreferences(Configuration.GAME_NAME);
    }

    public static void setVolume(boolean val) {
        prefs.putBoolean("volume", val);
        prefs.flush();
    }

    public static boolean getSounds() {
        return prefs.getBoolean("sound", true);
    }

    public static void setSounds(boolean val) {
        prefs.putBoolean("sound", val);
        prefs.flush();
    }

    public static boolean getVolume() {
        return prefs.getBoolean("volume");
    }


    public static TextureRegion court(configuration.Scenario scene) {
        Texture texture = getAssetTexture("courts/" + scene.name().toLowerCase(java.util.Locale.ROOT) + ".png");
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        return new TextureRegion(texture);
    }

    public static Texture getAssetTexture(String fileName) {
        return Assets.manager.get(fileName, Texture.class);
    }

    public static Sound getAssetSound(String fileName) {
        return Assets.manager.get(fileName, Sound.class);
    }
}
