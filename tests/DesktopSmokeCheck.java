import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;
import java.lang.reflect.Proxy;
import MainGame.*;
import configuration.*;
import helpers.*;
import gameobjects.*;
import gameworld.*;
import menuworld.*;

/** Exercises real OpenGL rendering, menu input and Box2D scoring with isolated preferences. */
public class DesktopSmokeCheck extends ApplicationAdapter {
    static boolean passed;
    int voicePlays, voiceStops;
    final String output;
    DesktopSmokeCheck(String output) { this.output = output; }
    public void create() {
        try {
            Gdx.audio = (Audio) Proxy.newProxyInstance(Audio.class.getClassLoader(), new Class[]{Audio.class}, (p,m,a)-> {
                if (m.getName().equals("newSound") || m.getName().equals("newMusic")) {
                    boolean goodVoice = m.getName().equals("newSound") && a[0].toString().endsWith("good.wav");
                    Class<?> type = m.getReturnType();
                    return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (q,n,b)-> {
                        if (goodVoice && n.getName().equals("play")) voicePlays++;
                        if (goodVoice && n.getName().equals("stop")) voiceStops++;
                        if(n.getReturnType()==boolean.class) return false;
                        if(n.getReturnType()==long.class) return 0L;
                        if(n.getReturnType()==float.class) return 0f;
                        return null;
                    });
                }
                return null;
            });
            aurelienribon.tweenengine.Tween.setCombinedAttributesLimit(10);
            ActionResolver resolver = (ActionResolver) Proxy.newProxyInstance(ActionResolver.class.getClassLoader(),
                    new Class[]{ActionResolver.class}, (p,m,a)->m.getReturnType()==boolean.class?false:null);
            BasketballGame game = new BasketballGame(resolver);
            Assets.manager.finishLoading(); AssetLoader.load1();
            AssetLoader.setScenarioIndex(0);
            Gdx.app.getPreferences(Configuration.GAME_NAME).putInteger("difficulty", 1).flush();
            screens.LoadingScreen splash = new screens.LoadingScreen(game, resolver);
            splash.render(0.8f); capture("splash.png"); splash.dispose();
            Localization.setLanguage(Localization.Language.PT);
            AssetLoader.setSounds(false);
            AssetLoader.setVolume(false);
            MenuWorld menu = new MenuWorld(game, resolver, 1920, 1080);
            GameRenderer menuRenderer = new GameRenderer(menu);
            InputHandlerMenu input = new InputHandlerMenu(menu, 0.5f, 0.5f);
            for (int i = 0; i < 8; i++) {
                tapSetting(input,menu,1);
                if (AssetLoader.getScenarioIndex() != (i + 1) % 8) throw new AssertionError("Court touch cycle");
            }
            for (int i = 0; i < 3; i++) tapSetting(input,menu,4);
            if (AssetLoader.getDifficulty() != Difficulty.NORMAL) throw new AssertionError("Difficulty touch cycle");
            for (int i = 0; i < 5; i++) tapSetting(input,menu,0);
            if (Localization.getLanguage() != Localization.Language.PT) throw new AssertionError("Language touch cycle");
            for (int i = 0; i < 100; i++) menu.update(1f / 60);
            menuRenderer.render(); capture("menu.png");
            menu.worldB.dispose();
            for(int width:new int[]{1440,2400}) {
                for(Localization.Language language:Localization.Language.values()) {
                    Localization.setLanguage(language);
                    MenuWorld responsive=new MenuWorld(game,resolver,width,1080);
                    for(int i=0;i<100;i++)responsive.update(1f/60);
                    new GameRenderer(responsive).render();
                    if(language==Localization.Language.PT)capture("menu-"+width+".png");
                    for(ui.Text control:responsive.settingsControls)
                        if(control.rectangle.x+control.rectangle.width>width) throw new AssertionError("Setting bounds");
                    responsive.worldB.dispose();
                }
            }
            Localization.setLanguage(Localization.Language.PT);
            for (Scenario scenario : Scenario.values()) {
                for (Difficulty difficulty : Difficulty.values()) {
                    AssetLoader.setScenarioIndex(scenario.ordinal());
                    Gdx.app.getPreferences(Configuration.GAME_NAME).putInteger("difficulty", difficulty.ordinal()).flush();
                    GameWorld world = new GameWorld(game, resolver, 1920, 1080, GameWorld.STATE.GAME);
                    if (world.ballsUI.balls != difficulty.initialBalls(scenario)) throw new AssertionError("Shot budget");
                    float gravity = Settings.WORLD_GRAVITY * difficulty.gravityScale * scenario.gravityScale;
                    if (Math.abs(world.worldB.getGravity().y - gravity) > .001f) throw new AssertionError("Gravity");
                    if (world.launcher.rect.x + world.launcher.rect.width > world.gameWidth) throw new AssertionError("Launch bounds");
                    GameRenderer renderer = new GameRenderer(world);
                    for (int i = 0; i < 160; i++) world.update(1f / 60);
                    // Drop an actual Box2D ball through the hoop; scoring must fire once.
                    Ball ball = world.getIdleBall();
                    float cx = world.basket.position.x + 310;
                    float cy = world.basket.position.y + 230 + world.basket.sprite.getHeight() / 2;
                    ball.setToLauncher(new Vector2(cx, cy + 130));
                    ball.flight(new Vector2(0, -3));
                    for (int i = 0; i < 70; i++) world.update(1f / 60);
                    if (world.score != 1) throw new AssertionError("Hoop scoring: " + scenario + " " + difficulty + " = " + world.score);
                    world.addScore(1); // retrigger while the previous effect is active
                    world.update(0.45f); renderer.render();
                    if(scenario==Scenario.STREET && difficulty==Difficulty.NORMAL) {
                        capture("good-pop.png");
                        world.update(0.6f);renderer.render();capture("good-stars.png");
                        world.addScore(1);
                        java.nio.file.Files.createDirectories(java.nio.file.Paths.get("/dev/shm/crazybasket-work/animation"));
                        for(int frame=0;frame<60;frame++) {
                            world.update(1f/30);renderer.render();
                            capture("/dev/shm/crazybasket-work/animation/"+String.format("%03d",frame)+".png");
                        }
                    }
                    if (difficulty == Difficulty.NORMAL) capture(scenario.name().toLowerCase() + "-good.png");
                    world.worldB.dispose();
                }
            }
            // Verify the easy-mode preview renders during a real drag.
            Gdx.app.getPreferences(Configuration.GAME_NAME).putInteger("difficulty", 0).flush();
            AssetLoader.setScenarioIndex(0);
            GameWorld easy = new GameWorld(game, resolver, 1920, 1080, GameWorld.STATE.PRACTICE);
            if (!easy.difficulty.trajectoryGuide) throw new AssertionError("Easy guide disabled");
            for(int i=0;i<160;i++) easy.update(1f/60);
            easy.launcher.touchDown((int)easy.launcher.rect.x + 260, 300);
            easy.launcher.touchDragged((int)easy.launcher.rect.x + 460, 180);
            easy.update(1f/60); new GameRenderer(easy).render(); capture("easy-guide.png");
            AssetLoader.setSounds(true);
            easy.addScore(1); easy.addScore(1);
            if (voicePlays != 2 || voiceStops != 1) throw new AssertionError("Voice and retrigger");
            AssetLoader.setSounds(false); easy.addScore(1);
            if (voicePlays != 2) throw new AssertionError("Mute must silence voice");
            easy.worldB.dispose();
            System.out.println("PASS: eight courts × three difficulties, touch selectors, Box2D score once, celebration, voice/mute and easy guide");
            passed = true;
        } catch (Throwable failure) { failure.printStackTrace(); System.exit(1); }
        Gdx.app.exit();
    }
    void tapSetting(InputHandlerMenu input, MenuWorld menu, int index) {
        com.badlogic.gdx.math.Rectangle r=menu.settingsControls.get(index).getRectangle();
        tap(input,(int)(r.x+r.width/2),(int)(r.y+r.height/2));
    }
    void tap(InputHandlerMenu input, int x, int y) {
        input.touchDown(x/2, (1080-y)/2, 0, 0);
        input.touchUp(x/2, (1080-y)/2, 0, 0);
    }
    void capture(String name) {
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Pixmap flipped = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
        for(int y=0;y<pixmap.getHeight();y++) for(int x=0;x<pixmap.getWidth();x++)
            flipped.drawPixel(x, pixmap.getHeight()-y-1, pixmap.getPixel(x,y));
        PixmapIO.writePNG(Gdx.files.absolute(name.startsWith("/")?name:output+"/"+name), flipped);
        pixmap.dispose(); flipped.dispose();
    }
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=960; config.height=540; config.title="Crazy Basket verification";
        config.preferencesDirectory=System.getProperty("java.io.tmpdir")+"/crazybasket-smoke-preferences/";
        config.preferencesFileType=Files.FileType.Absolute;
        config.forceExit=false; config.allowSoftwareMode=true;
        LwjglApplicationConfiguration.disableAudio=true;
        new LwjglApplication(new DesktopSmokeCheck(args[0]), config);
    }
}
