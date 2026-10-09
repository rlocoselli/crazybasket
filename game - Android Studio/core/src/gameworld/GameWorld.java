package gameworld;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import MainGame.ActionResolver;
import MainGame.BasketballGame;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import configuration.Configuration;
import configuration.Settings;
import configuration.Scenario;
import configuration.Localization;
import gameobjects.Ball;
import gameobjects.Basket;
import gameobjects.Floor;
import gameobjects.GameObject;
import helpers.AssetLoader;
import helpers.FlatColors;
import screens.LoadingScreen;
import screens.MenuScreen;
import tweens.SpriteAccessor;
import tweens.Value;
import ui.BallsUI;
import ui.Banner;
import ui.Launcher;
import ui.MenuButton;
import ui.ScoresUI;

public class GameWorld {
    public float gameWidth, gameHeight;
    public int score = 0;
    public BasketballGame game;
    public ActionResolver actionResolver;

    //GAMEOBJECTS
    public Floor floor;
    public Ball ball;
    public Basket basket;
    public Launcher launcher;
    public Banner banner;
    public GameObject background, top;
    public ArrayList<Ball> balls = new ArrayList<Ball>();
    public MenuButton homeButton;
    public ScoresUI scoreUI;
    //public TimeUI timerUI;
    public BallsUI ballsUI;
    public ui.Text tutorial;


    public enum STATE {PRACTICE, GAME}

    public STATE state;
    public final Scenario scenario;
    public final configuration.Difficulty difficulty;
    public ui.ScoreCelebration celebration;
    public final int championshipRound;

    public enum GameState {GAMEOVER, RUNNING}

    public GameState gameState;

    //BOX2D
    public World worldB;
    Body body;
    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;
    Sprite sprite;


    public GameWorld(BasketballGame game, ActionResolver actionResolver, float gameWidth,
                     float gameHeight, STATE state) {
        this.game = game;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.actionResolver = actionResolver;
        this.state = state;
        int round = helpers.AssetLoader.getScenarioIndex() % Scenario.values().length;
        this.championshipRound = round;
        this.scenario = Scenario.values()[round];
        this.difficulty = AssetLoader.getDifficulty();
        start();
    }

    private void start() {
        worldB = new World(new Vector2(0, Settings.WORLD_GRAVITY * difficulty.gravityScale * scenario.gravityScale), true);
        debugRenderer = new Box2DDebugRenderer();
        gameState = GameState.RUNNING;
        //GAMEOBJECTS
        background = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.court(scenario),
                Color.WHITE,
                GameObject.Shape.RECTANGLE);
        top = new GameObject(this, 0, 0, gameWidth, gameHeight, AssetLoader.square,
                Color.BLACK, GameObject.Shape.RECTANGLE);
        top.fadeOut(.5f, 0f);
        floor = new Floor(this, 1, 0, gameWidth - 2, 150, AssetLoader.square, FlatColors.parseColor(scenario.floorColor()),
                GameObject.Shape.RECTANGLE);
        /*ball = new Ball(this, gameWidth / 2, gameHeight / 2, 100, 100, AssetLoader.ball,
                FlatColors.WHITE, GameObject.Shape.CIRCLE);*/
        for (int i = 0; i < difficulty.initialBalls(scenario); i++) {
            Ball b = new Ball(this, gameWidth / 2, gameHeight / 2, Settings.BALL_SIZE, Settings.BALL_SIZE, AssetLoader.ball,
                    FlatColors.WHITE, GameObject.Shape.CIRCLE);
            balls.add(b);
        }
        float basketX = Math.max(40, Math.min(gameWidth - 560, gameWidth * scenario.basketXRatio + (1 - AssetLoader.getBasketDistance()) * 140));
        basket = new Basket(this, basketX, 100, 50, gameHeight - 360 + (AssetLoader.getBasketHeight() - 1) * 120, AssetLoader.square,
                FlatColors.GREY,
                GameObject.Shape.RECTANGLE);
        launcher = new Launcher(this);
        celebration = new ui.ScoreCelebration(this);

        banner = new Banner(this, 0, gameHeight / 2 - (Settings.BANNER_HEIGHT / 2), gameWidth,
                Settings.BANNER_HEIGHT, AssetLoader.square, Color.BLACK,
                GameObject.Shape.RECTANGLE);
        banner.setText(Localization.t(state == STATE.PRACTICE ? "practice" : "championship") + " • " + scenario.localizedName() + " • " + difficulty.localizedName());
        banner.startAndFinish();
        homeButton = new MenuButton(this, 50,
                gameHeight - 50 - (AssetLoader.homeButton.getRegionHeight()) / 2,
                AssetLoader.homeButton.getRegionWidth() / 2,
                AssetLoader.homeButton.getRegionHeight() / 2, AssetLoader.homeButton,
                FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);
        scoreUI = new ScoresUI(this, gameWidth - 630,
                gameHeight - 45 - AssetLoader.scoreBack.getRegionHeight(),
                AssetLoader.scoreBack.getRegionWidth(), AssetLoader.scoreBack.getRegionHeight(),
                AssetLoader.scoreBack, FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);

        /*timerUI = new TimeUI(this, gameWidth / 2 - AssetLoader.timerBack.getRegionWidth() / 2,
                gameHeight - 45 - AssetLoader.timerBack.getRegionHeight(),
                AssetLoader.timerBack.getRegionWidth(), AssetLoader.timerBack.getRegionHeight(),
                AssetLoader.timerBack, FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);*/
        ballsUI = new BallsUI(this, gameWidth / 2 - AssetLoader.timerBack.getRegionWidth() / 2,
                gameHeight - 45 - AssetLoader.timerBack.getRegionHeight(),
                AssetLoader.timerBack.getRegionWidth(), AssetLoader.timerBack.getRegionHeight(),
                AssetLoader.timerBack, FlatColors.WHITE,
                GameObject.Shape.RECTANGLE);

        tutorial = new ui.Text(this, launcher.rect.x + 40, floor.rectangle.height + 80,
                Math.max(100, gameWidth - launcher.rect.x - 80), 200,
                AssetLoader.square, FlatColors.WHITE, Localization.t("drag"), AssetLoader.font08,
                FlatColors.WHITE, 22, com.badlogic.gdx.utils.Align.center);
        tutorial.getSprite().setAlpha(0);

        /*if (state == STATE.GAME)
            timerUI.startTimer(Settings.INITIAL_TIME, 2f);*/

        scoreUI.setScoreText(Configuration.SCORE_TEXT + score);
        if (state != null) tutorialEffect();
    }

    public void update(float delta) {
        worldB.step(1f / 60f, 6, 2);
        background.update(delta);
        floor.update(delta);
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).update(delta);
        }
        basket.update(delta);
        launcher.update(delta);
        celebration.update(delta);
        banner.update(delta);
        top.update(delta);
        homeButton.update(delta);
        //timerUI.update(delta);
        ballsUI.update(delta);
        scoreUI.update(delta);
        tutorial.update(delta);
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (Configuration.DEBUG) {
            batch.end();
            debugMatrix = batch.getProjectionMatrix().cpy().scale(Settings.PTM, Settings.PTM, 0);
            batch.begin();


        }
        background.render(batch, shapeRenderer);
        basket.render(batch,shapeRenderer);
        floor.render(batch, shapeRenderer);
        launcher.render(batch, shapeRenderer);
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).render(batch, shapeRenderer);
        }
        basket.renderNet(batch);
        tutorial.render(batch, shapeRenderer, GameRenderer.fontShader);
        celebration.render(batch);
        banner.render(batch, shapeRenderer);
        homeButton.render(batch, shapeRenderer);
        scoreUI.render(batch, shapeRenderer);
        /*if (state == STATE.GAME) timerUI.render(batch, shapeRenderer);*/
        if (state == STATE.GAME) ballsUI.render(batch, shapeRenderer);
        top.render(batch, shapeRenderer);

        if (Configuration.DEBUG) debugRenderer.render(worldB, debugMatrix);

    }

    public void addScore(int i) {
        score += i;
        if (i > 0) celebration.trigger();
        scoreUI.setScoreText(Configuration.SCORE_TEXT + score);
        if (score > AssetLoader.getHighScore()) {
            scoreUI.setBestText(Configuration.BEST_TEXT + (score));
        }
    }

    public Ball getIdleBall() {
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i).isIdle()) {
                return balls.get(i);
            }
        }

        return addBall();
    }

    private Ball addBall() {
        for (int i = 0; i < balls.size(); i++) {
            if (balls.size() > Settings.NUM_OF_INITIAL_BALLS && balls.get(i).isIdle()) {
                worldB.destroyBody(balls.get(i).body);
            }
        }
        balls.add(new Ball(this, gameWidth / 2, gameHeight / 2, 100, 100, AssetLoader.ball,
                FlatColors.WHITE, GameObject.Shape.CIRCLE));
        return balls.get(balls.size() - 1);
    }

    public void goToMenu() {
        finishEffects();
        Value timer = new Value();
        timer.setValue(0);
        Tween.to(timer, -1, .5f).target(1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new MenuScreen(game, actionResolver));
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(background.getManager());
    }

    public void goToMenuFromGameover() {
        banner.finish(.5f);
        finishEffects();
        Value timer = new Value();
        timer.setValue(0);
        Tween.to(timer, -1, .5f).target(1).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new MenuScreen(game, actionResolver));
            }
        }).setCallbackTriggers(TweenCallback.COMPLETE).start(background.getManager());
    }


    public void finish() {
        if (!anyFlying()) {
            if (state == STATE.GAME)
                saveScoreLogic();
            Value timer = new Value();
            Tween.to(timer, -1, 1f).target(1).setCallback(new TweenCallback() {
                @Override
                public void onEvent(int type, BaseTween<?> source) {
                    gameState = GameState.GAMEOVER;
                    if (Math.random() < Configuration.AD_FREQUENCY) {
                        actionResolver.showOrLoadInterstital();
                    }
                }
            }).setCallbackTriggers(TweenCallback.COMPLETE).start(background.getManager());
            banner.setText(Configuration.SCORE_TEXT + score);
            banner.start();

        /*timerUI.manager.killAll();
        timerUI.timer.setValue(0);*/
            if (AssetLoader.getSounds()) AssetLoader.buzzer.play();
        }
    }

    private void saveScoreLogic() {
        AssetLoader.addGamesPlayed();
        int gamesPlayed = AssetLoader.getGamesPlayed();
        actionResolver.submitScore(score);
        actionResolver.submitGamesPlayed(gamesPlayed);
        if (score > AssetLoader.getHighScore()) {
            AssetLoader.setHighScore(score);
        }
        AssetLoader.setScenarioIndex((scenario.ordinal() + 1) % Scenario.values().length);
    }

    public void nextScenario() {
        AssetLoader.setScenarioIndex((AssetLoader.getScenarioIndex() + 1) % Scenario.values().length);
    }

    private void finishEffects() {
        top.fadeIn(0.5f, 0f);
    }

    public void goToLoadingScreen() {
        game.setScreen(new LoadingScreen(game, actionResolver));
    }

    public void tutorialEffect() {
        tutorial.getManager().killAll();
        tutorial.sprite.setAlpha(0);
        Tween.to(tutorial.sprite, SpriteAccessor.ALPHA, .4f).target(1).repeatYoyo(1, .1f)
                .start(tutorial.getManager());
    }

    public boolean anyFlying() {
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i).isFlying()) {
                return true;
            }
        }
        return false;
    }
}
