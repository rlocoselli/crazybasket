package MainGame;

import com.badlogic.gdx.Game;

import aurelienribon.tweenengine.Tween;
import helpers.Assets;
import screens.Boot;

public class BasketballGame extends Game {

    private ActionResolver actionresolver;

    public BasketballGame(ActionResolver actionresolver) {
        this.actionresolver = actionresolver;
        Assets.load();
    }

    @Override
    public void create() {
        Tween.setCombinedAttributesLimit(10);
        setScreen(new Boot(this, actionresolver));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        Assets.manager.dispose();
        Assets.manager = null;
    }

    @Override
    public void resume() {
       if (!Assets.manager.update()) {
            setScreen(new Boot(this, actionresolver));
        }
    }
}
