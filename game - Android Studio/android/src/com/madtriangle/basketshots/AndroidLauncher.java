package com.madtriangle.basketshots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.example.games.basegameutils.GameHelper;

import MainGame.ActionResolver;
import MainGame.BasketballGame;
import configuration.Configuration;

public class AndroidLauncher extends AndroidApplication implements
        ActionResolver, GameHelper.GameHelperListener {

    private static final String AD_UNIT_ID_BANNER = Configuration.ADMOB_BANNER_ID;
    private static final String AD_UNIT_ID_INTERSTITIAL = Configuration.ADMOB_INTERSTITIAL_ID;
    private final static int REQUEST_CODE_UNUSED = 9002;

    private static String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
    protected AdView adView;
    protected View gameView;
    AdView admobView;
    private GameHelper _gameHelper;

    private boolean removeAdsVersion = false;
    SharedPreferences prefs;
    private BasketballGame game;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        prefs = getSharedPreferences(Configuration.GAME_NAME, Context.MODE_PRIVATE);
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        cfg.useImmersiveMode = true;

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id="
                + getPackageName();

        FrameLayout layout = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        admobView = createAdView();

        View gameView = createGameView(cfg);
        layout.addView(gameView);
        layout.addView(admobView);


        _gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        _gameHelper.enableDebugLog(false);
        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInSucceeded() {
            }

            @Override
            public void onSignInFailed() {
            }
        };
        _gameHelper.setup(gameHelperListener);


        setContentView(layout);
        startAdvertising(admobView);

    }

    private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID_BANNER);
        //adView.setId(1); // this is an arbitrary id, allows for relative
        // positioning in createGameView()
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;

        adView.setLayoutParams(params);
        if (!removeAdsVersion) {
            adView.setVisibility(View.VISIBLE);
        } else {
            adView.setVisibility(View.GONE);
        }
        adView.setBackgroundColor(Color.TRANSPARENT);
        return adView;
    }

    private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(new BasketballGame(this), cfg);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        gameView.setLayoutParams(params);
        return gameView;
    }

    private void startAdvertising(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null)
            adView.resume();
    }

    @Override
    public void onPause() {
        if (adView != null)
            adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null)
            adView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        _gameHelper.onStop();
    }


    @Override
    public void showOrLoadInterstital() {
        // Interstitials were removed from the legacy API during migration.
        // Banner ads remain active; this hook is intentionally safe/no-op.
    }

    @Override
    public void signIn() {
        try {
            runOnUiThread(new Runnable() {
                // @Override
                public void run() {
                    _gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage()
                    + ".");
        }
    }

    @Override
    public void signOut() {
        try {
            runOnUiThread(new Runnable() {
                // @Override
                public void run() {
                    _gameHelper.signOut();
                }
            });
        } catch (Exception e) {
            Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage()
                    + ".");
        }
    }

    @Override
    public void rateGame() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));
    }

    @Override
    public boolean isSignedIn() {
        return _gameHelper.isSignedIn();
    }

    @Override
    public void onSignInFailed() {
    }

    @Override
    public void onSignInSucceeded() {
    }

    @Override
    public void submitScore(long score) {
        // Legacy Google Play Games leaderboard API is no longer available.
    }

    @Override
    public void submitGamesPlayed(long score) {
        // Local statistics continue to be saved by the core game.
    }

    @Override
    public void showScores() {
        signIn();
    }

    @Override
    public void showAchievement() {
        signIn();
    }

    @Override
    public boolean shareGame(String msg) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg + GOOGLE_PLAY_URL);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share..."));
        return true;

    }

    @Override
    public void unlockAchievementGPGS(String string) {
        // Achievements are kept as a compatibility hook for the core module.
    }

    @Override
    public void viewAd(boolean view) {
        if (view) {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        admobView.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
            }

        } else {
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        admobView.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void toast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
