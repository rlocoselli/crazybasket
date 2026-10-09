package configuration;

public class Configuration {
    public static final String GAME_NAME = "Crazy Basket";

    public static boolean FPS_COUNTER = false;
    public static boolean DEBUG = false;
    public static final float MUSIC_VOLUME = .8f;

    //ADMOB IDS
    public static String ADMOB_INTERSTITIAL_ID = "ca-app-pub-6147578034437241/3076406217";
    public static String ADMOB_BANNER_ID = "ca-app-pub-6147578034437241/4553139411";
    public static float AD_FREQUENCY = .6f; //number between 0 and 1

    //LEADERBOARDS IDS
    public static final String LEADERBOARD_HIGHSCORE = "CgkI9u67gIESEAIQBg";
    public static final String LEADERBOARD_GAMESPLAYED = "CgkI9u67gIESEAIQBw";

    public static final String SHARE_MESSAGE = "Can you beat me at " + GAME_NAME + "?";
    public static String SCORE_TEXT = "Score: ";
    public static String BEST_TEXT = "Best: ";
    public static String TIME_TEXT = "Time: ";
}
