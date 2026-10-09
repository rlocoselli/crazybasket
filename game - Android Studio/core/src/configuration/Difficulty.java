package configuration;

/** Difficulty changes the shot budget, gravity and trajectory assistance. */
public enum Difficulty {
    EASY(5, 0.85f, true), NORMAL(0, 1f, false), HARD(-3, 1.15f, false);
    public final int extraBalls;
    public final float gravityScale;
    public final boolean trajectoryGuide;
    Difficulty(int extraBalls, float gravityScale, boolean trajectoryGuide) {
        this.extraBalls = extraBalls;
        this.gravityScale = gravityScale;
        this.trajectoryGuide = trajectoryGuide;
    }
    public int initialBalls(Scenario scenario) { return Math.max(4, scenario.balls + extraBalls); }
    public String localizedName() { return Localization.t(name().toLowerCase(java.util.Locale.ROOT)); }
}
