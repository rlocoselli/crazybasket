package configuration;

/** Each court supplies its layout, palette, shot budget and gravity. */
public enum Scenario {
    STREET(0.30f, 12, "f08a5d", "8c5a45", 1f),
    ROOFTOP(0.40f, 10, "5b6ee1", "343b72", 1f),
    BEACH(0.50f, 8, "f6d365", "d6a84f", 1f),
    ARENA(0.60f, 7, "3ab795", "405c68", 1f),
    PARK(0.35f, 12, "8ed6a2", "487454", 1f),
    DESERT(0.45f, 10, "ffb766", "b97640", 1f),
    SNOW(0.50f, 9, "b1d9ef", "799eb7", 1f),
    SPACE(0.55f, 8, "6551a3", "393452", 0.65f);
    public final float basketXRatio;
    public final int balls;
    public final float gravityScale;
    private final String sky, floor;
    Scenario(float basketXRatio, int balls, String sky, String floor, float gravityScale) {
        this.basketXRatio = basketXRatio;
        this.balls = balls;
        this.sky = sky;
        this.floor = floor;
        this.gravityScale = gravityScale;
    }
    public String tint() { return sky; }
    public String floorColor() { return floor; }
    public String localizedName() { return Localization.t(name().toLowerCase(java.util.Locale.ROOT)); }
}
