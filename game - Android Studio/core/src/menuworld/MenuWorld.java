package menuworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import MainGame.ActionResolver;
import MainGame.BasketballGame;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import configuration.Configuration;
import configuration.Localization;
import gameobjects.GameObject;
import gameworld.GameWorld;
import gameworld.GameRenderer;
import helpers.AssetLoader;
import helpers.FlatColors;
import screens.GameScreen;
import screens.MenuScreen;
import tweens.SpriteAccessor;
import tweens.Value;
import ui.MenuButton;
import ui.MusicButton;
import ui.ScoresUI;
import ui.SoundButton;

public class MenuWorld extends GameWorld {

    private GameObject background, title, top;
    public ArrayList<MenuButton> menuButtons = new ArrayList<MenuButton>();
    public ActionResolver actionResolver;
    public MusicButton musicButton;
    public SoundButton soundButton;
    private ui.Text modeText, audioText;
    public ArrayList<ui.Text> settingsControls = new ArrayList<ui.Text>();
    private ArrayList<ui.Text> buttonLabels = new ArrayList<ui.Text>();

    public MenuWorld(BasketballGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight) {
        super(game, actionResolver, gameWidth, gameHeight, null);
        this.actionResolver = actionResolver;
        top = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.square, Color.BLACK,
                GameObject.Shape.RECTANGLE);
        background = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.square,
                FlatColors.parseColor("101b32"), GameObject.Shape.RECTANGLE);
        float columnWidth = (gameWidth - 240) / 2;
        float rightX = gameWidth / 2 + 40;
        float titleWidth = Math.min(690, columnWidth - 60);
        float titleHeight = titleWidth * AssetLoader.title.getRegionHeight() / AssetLoader.title.getRegionWidth();
        title = new GameObject(this, 80 + (columnWidth - titleWidth)/2,
                1030 - titleHeight, titleWidth,titleHeight,AssetLoader.title,Color.WHITE,GameObject.Shape.RECTANGLE);
        scoreUI = new ScoresUI(this,rightX,930,columnWidth,85,AssetLoader.square,Color.WHITE,GameObject.Shape.RECTANGLE);
        scoreUI.sprite.setAlpha(0);
        scoreUI.scoreText.setFontColor(FlatColors.parseColor("ffd082"));
        modeText = new ui.Text(this,rightX+30,55,columnWidth-60,130,AssetLoader.square,Color.WHITE,
                "",AssetLoader.font08,FlatColors.parseColor("adbed8"),22,com.badlogic.gdx.utils.Align.center);
        for(int i=0;i<4;i++) {
            float y = new float[]{650,510,370,230}[i];
            MenuButton button = new MenuButton(this,rightX,y,columnWidth,115,AssetLoader.square,
                    FlatColors.parseColor(i==0?"ffa94a":"263a59"),GameObject.Shape.RECTANGLE);
            button.cardStyle=true;
            menuButtons.add(button);
            buttonLabels.add(new ui.Text(this,rightX,y,columnWidth,115,AssetLoader.square,Color.WHITE,
                    "",AssetLoader.font08,FlatColors.parseColor(i==0?"152038":"edf5ff"),38,com.badlogic.gdx.utils.Align.center));
        }
        // The existing index order is retained for language/court/distance/height/difficulty input.
        float[] settingY={115,420,217,319,521};
        for(int i=0;i<5;i++) settingsControls.add(new ui.Text(this,110,settingY[i],columnWidth-60,84,
                AssetLoader.square,Color.WHITE,"",AssetLoader.font08,FlatColors.parseColor("e2edff"),24,
                com.badlogic.gdx.utils.Align.left));
        for(ui.Text control:settingsControls) control.setAutoFit(true);
        for(ui.Text label:buttonLabels) label.setAutoFit(true);
        audioText = new ui.Text(this,rightX+25,810,columnWidth-210,85,AssetLoader.square,Color.WHITE,
                "",AssetLoader.font08,FlatColors.parseColor("a7bddb"),24,com.badlogic.gdx.utils.Align.left);
        audioText.setAutoFit(true);
        musicButton = new MusicButton(rightX+columnWidth-165,810,65,65,AssetLoader.noMusicButton,AssetLoader.musicButton,Color.WHITE);
        soundButton = new SoundButton(rightX+columnWidth-80,810,65,65,AssetLoader.nosoundButton,AssetLoader.soundButton,Color.WHITE);
        refreshLanguage();
        startEffects();
        checkIfMusicWasPlaying();
    }

    private void startEffects() {
        top.fadeOut(0.5f, 0f);
        title.effectY(title.getPosition().y + gameHeight, title.getPosition().y, .5f, .5f);
        Tween.to(title.sprite, SpriteAccessor.SCALE, 0.5f).target(0.98f).repeatYoyo(10000, 0)
                .start(title.getManager());
        for (int i = 0; i < menuButtons.size(); i++) {
            menuButtons.get(i).effectX(menuButtons.get(i).getPosition().x + gameWidth / 2,
                    menuButtons.get(i).getPosition().x, .5f, 0.04f * i + (0.5f));
        }


    }

    public void update(float delta) {
        top.update(delta);
        background.update(delta);
        title.update(delta);
        for (int i = 0; i < menuButtons.size(); i++) {
            menuButtons.get(i).update(delta);
        }
        for (int i = 0; i < buttonLabels.size(); i++) {
            buttonLabels.get(i).setPosition(menuButtons.get(i).sprite.getX(), menuButtons.get(i).sprite.getY());
            buttonLabels.get(i).update(delta);
        }
        scoreUI.update(delta);
        musicButton.update(delta);

        soundButton.update(delta);


    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        background.render(batch, shapeRenderer);
        float columnWidth = (gameWidth - 240)/2;
        ui.UiDraw.panel(batch,80,80,columnWidth,555,FlatColors.parseColor("1a2a45"));
        ui.UiDraw.panel(batch,gameWidth/2+40,810,columnWidth,85,FlatColors.parseColor("1a2a45"));
        for(ui.Text control:settingsControls) {
            com.badlogic.gdx.math.Rectangle r=control.getRectangle();
            ui.UiDraw.panel(batch,r.x-12,r.y,r.width+24,r.height,FlatColors.parseColor("233754"));
        }
        title.render(batch, shapeRenderer);
        for (int i = 0; i < menuButtons.size(); i++) {
            menuButtons.get(i).render(batch, shapeRenderer);
        }
        for (ui.Text label : buttonLabels) label.render(batch, shapeRenderer, GameRenderer.fontShader);
        for (ui.Text control : settingsControls) control.render(batch, shapeRenderer, GameRenderer.fontShader);
        scoreUI.scoreText.render(batch,shapeRenderer,GameRenderer.fontShader);
        audioText.render(batch,shapeRenderer,GameRenderer.fontShader);
        musicButton.draw(batch);
        soundButton.draw(batch);
        modeText.render(batch, shapeRenderer, GameRenderer.fontShader);
        top.render(batch, shapeRenderer);
    }

    private void checkIfMusicWasPlaying() {
        if (AssetLoader.getVolume()) {
            AssetLoader.music.setLooping(true);
            AssetLoader.music.play();
            AssetLoader.music.setVolume(Configuration.MUSIC_VOLUME);
            AssetLoader.setVolume(true);
        } else AssetLoader.music.pause();

        if (AssetLoader.music.isPlaying()) musicButton.setIsPressed(false);
        else musicButton.setIsPressed(true);

        if (AssetLoader.getSounds()) soundButton.setIsPressed(false);
        else soundButton.setIsPressed(true);
    }


    public void goToGameScreen() {
        finishEffects();
        Value timer = new Value();
        Tween.to(timer, -1, 1f).target(0).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                MenuScreen.game
                        .setScreen(new GameScreen(MenuScreen.game, actionResolver, STATE.GAME));
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(title.getManager());

    }

    private void finishEffects() {
        top.fadeIn(.5f, .5f);
        title.effectY(title.getPosition().y, title.getPosition().y + gameHeight, .5f, .0f);

        for (int i = menuButtons.size() - 1; i >= 0; i--) {
            menuButtons.get(i).effectX(menuButtons.get(i).getPosition().x,
                    menuButtons.get(i).getPosition().x + gameWidth / 2, .5f,
                    (0.04f * menuButtons.size()) - (0.04f * i));
        }
    }

    public void goToPracticeMode() {
        MenuScreen.game.setScreen(new GameScreen(MenuScreen.game, actionResolver, STATE.PRACTICE));
    }

    public void refreshLanguage() {
        audioText.setText(Localization.t("audio"));
        Configuration.SCORE_TEXT = Localization.t("score");
        Configuration.BEST_TEXT = Localization.t("best");
        Configuration.TIME_TEXT = Localization.t("time");
        String[] keys = {"play", "practice", "scores", "share"};
        for (int i = 0; i < buttonLabels.size(); i++) buttonLabels.get(i).setText(Localization.t(keys[i]));
        settingsControls.get(0).setText(Localization.t("language") + ": " + Localization.getLanguage().label + " >");
        settingsControls.get(1).setText(Localization.t("court") + ": " + configuration.Scenario.values()[AssetLoader.getScenarioIndex()].localizedName() + " >");
        settingsControls.get(2).setText(Localization.t("distance") + ": " + Localization.t(new String[]{"near", "normal", "far"}[AssetLoader.getBasketDistance()]) + " >");
        settingsControls.get(3).setText(Localization.t("height") + ": " + Localization.t(new String[]{"low", "normal", "high"}[AssetLoader.getBasketHeight()]) + " >");
        settingsControls.get(4).setText(Localization.t("difficulty") + ": " + AssetLoader.getDifficulty().localizedName() + " >");
        modeText.setText(Localization.t("championship") + " • " + configuration.Scenario.values()[AssetLoader.getScenarioIndex()].localizedName() + " • " + AssetLoader.getDifficulty().localizedName());
        scoreUI.setScoreText(Configuration.BEST_TEXT + AssetLoader.getHighScore());
    }

    public void nextScenario() {
        super.nextScenario();
        refreshLanguage();
    }
}
