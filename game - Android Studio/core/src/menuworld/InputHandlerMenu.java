package menuworld;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import configuration.Configuration;
import configuration.Localization;

public class InputHandlerMenu implements InputProcessor {

    private MenuWorld world;
    private float scaleFactorX;
    private float scaleFactorY;
    private int pressedSetting = -1;

    public InputHandlerMenu(MenuWorld world, float scaleFactorX, float scaleFactorY) {
        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        this.world = world;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
        } else if (keycode == Input.Keys.D) {
            Configuration.DEBUG = !Configuration.DEBUG;
        } else if (keycode == Input.Keys.A) {
        } else if (keycode == Input.Keys.S) {
        } else if (keycode == Input.Keys.L) {
            Localization.nextLanguage();
            world.refreshLanguage();
        } else if (keycode == Input.Keys.F) {
            helpers.AssetLoader.nextDifficulty();
            world.refreshLanguage();
        } else if (keycode == Input.Keys.C) {
            world.nextScenario();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        pressedSetting = -1;
        for (int i = 0; i < world.settingsControls.size(); i++) {
            if (world.settingsControls.get(i).getRectangle().contains(screenX, screenY)) pressedSetting = i;
        }
        for (int i = 0; i < world.menuButtons.size(); i++) {
            world.menuButtons.get(i).isTouchDown(screenX, screenY);
        }

        world.musicButton.isTouchDown(screenX,screenY);
        world.soundButton.isTouchDown(screenX,screenY);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        int setting = pressedSetting;
        pressedSetting = -1;
        if (setting >= 0 && world.settingsControls.get(setting).getRectangle().contains(screenX, screenY)) {
            if (setting == 0) Localization.nextLanguage();
            else if (setting == 1) world.nextScenario();
            else if (setting == 2) helpers.AssetLoader.nextBasketDistance();
            else if (setting == 3) helpers.AssetLoader.nextBasketHeight();
            else helpers.AssetLoader.nextDifficulty();
            world.refreshLanguage();
            return true;
        }
        if (world.menuButtons.get(0).isTouchUp(screenX, screenY)) {
            world.goToGameScreen();
        } else if (world.menuButtons.get(1).isTouchUp(screenX, screenY)) {
            world.goToPracticeMode();
        } else if (world.menuButtons.get(2).isTouchUp(screenX, screenY)) {
            world.actionResolver.showScores();
        } else if (world.menuButtons.get(3).isTouchUp(screenX, screenY)) {
            world.actionResolver.shareGame(Localization.t("share_message"));
            }
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (world.gameHeight - screenY / scaleFactorY);
    }
}
