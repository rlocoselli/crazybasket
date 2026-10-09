package configuration;
import com.badlogic.gdx.Gdx;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
public final class Localization {
    public enum Language {
        EN("English"), ES("Español"), PT("Português"), FR("Français"), IT("Italiano");
        public final String label;
        Language(String label) { this.label = label; }
    }
    private static Language language;
    private static final Map<String, String[]> TEXT = new HashMap<String, String[]>();
    static {
        TEXT.put("audio",new String[]{"Music & sound", "Música y sonido", "Música e som", "Musique et son", "Musica e suoni"});
        TEXT.put("difficulty", new String[]{"Difficulty","Dificultad","Dificuldade","Difficulté","Difficoltà"});
        TEXT.put("easy", new String[]{"Easy","Fácil","Fácil","Facile","Facile"});
        TEXT.put("hard", new String[]{"Hard","Difícil","Difícil","Difficile","Difficile"});
        TEXT.put("park", new String[]{"Green park","Parque verde","Parque verde","Parc verdoyant","Parco verde"});
        TEXT.put("desert", new String[]{"Desert sunset","Desierto","Deserto","Désert","Deserto"});
        TEXT.put("snow", new String[]{"Snow court","Cancha nevada","Quadra na neve","Terrain enneigé","Campo innevato"});
        TEXT.put("space", new String[]{"Space court","Cancha espacial","Quadra espacial","Terrain spatial","Campo spaziale"});
        TEXT.put("drag", new String[]{"Drag here to aim, release to shoot", "Arrastra para apuntar, suelta para lanzar", "Arraste para mirar, solte para lançar", "Glissez pour viser, relâchez pour tirer", "Trascina per mirare, rilascia per tirare"});
        TEXT.put("score", new String[] {"Score: ", "Puntuación: ", "Pontuação: ", "Score : ", "Punteggio: "});
        TEXT.put("best", new String[] {"Best: ", "Récord: ", "Recorde: ", "Record : ", "Record: "});
        TEXT.put("time", new String[] {"Time: ", "Tiempo: ", "Tempo: ", "Temps : ", "Tempo: "});
        TEXT.put("championship", new String[] {"CHAMPIONSHIP", "CAMPEONATO", "CAMPEONATO", "CHAMPIONNAT", "CAMPIONATO"});
        TEXT.put("practice", new String[] {"PRACTICE", "PRÁCTICA", "TREINO", "ENTRAÎNEMENT", "ALLENAMENTO"});
        TEXT.put("play", new String[] {"PLAY", "JUGAR", "JOGAR", "JOUER", "GIOCA"});
        TEXT.put("scores", new String[] {"RECORDS", "RÉCORDS", "RECORDES", "RECORDS", "RECORD"});
        TEXT.put("share", new String[] {"SHARE", "COMPARTIR", "COMPARTILHAR", "PARTAGER", "CONDIVIDI"});
        TEXT.put("language", new String[] {"Language", "Idioma", "Idioma", "Langue", "Lingua"});
        TEXT.put("court", new String[] {"Court", "Cancha", "Cenário", "Terrain", "Campo"});
        TEXT.put("distance", new String[] {"Distance", "Distancia", "Distância", "Distance", "Distanza"});
        TEXT.put("height", new String[] {"Height", "Altura", "Altura", "Hauteur", "Altezza"});
        TEXT.put("near", new String[] {"Near", "Cerca", "Perto", "Proche", "Vicino"});
        TEXT.put("normal", new String[] {"Normal", "Normal", "Normal", "Normale", "Normale"});
        TEXT.put("far", new String[] {"Far", "Lejos", "Longe", "Loin", "Lontano"});
        TEXT.put("low", new String[] {"Low", "Baja", "Baixa", "Basse", "Bassa"});
        TEXT.put("high", new String[] {"High", "Alta", "Alta", "Haute", "Alta"});
        TEXT.put("street", new String[] {"Street court", "Cancha urbana", "Quadra urbana", "Terrain urbain", "Campo urbano"});
        TEXT.put("rooftop", new String[] {"Rooftop", "Azotea", "Cobertura", "Toit de la ville", "Terrazza"});
        TEXT.put("beach", new String[] {"Beach", "Playa", "Praia", "Plage", "Spiaggia"});
        TEXT.put("arena", new String[] {"Final arena", "Arena final", "Arena final", "Grande salle", "Arena finale"});
        TEXT.put("share_message", new String[] {"Can you beat me at Crazy Basket?", "¿Puedes ganarme en Crazy Basket?", "Você consegue me vencer no Crazy Basket?", "Peux-tu me battre à Crazy Basket ?", "Riesci a battermi a Crazy Basket?"});
    }
    public static void init() {
        String code = Gdx.app.getPreferences(Configuration.GAME_NAME).getString("language", Locale.getDefault().getLanguage());
        language = Language.EN;
        for (Language candidate : Language.values()) {
            if (candidate.name().equalsIgnoreCase(code)) language = candidate;
        }
    }
    public static Language getLanguage() { if (language == null) init(); return language; }
    public static void setLanguage(Language value) {
        language = value;
        Gdx.app.getPreferences(Configuration.GAME_NAME).putString("language", value.name().toLowerCase(Locale.ROOT)).flush();
    }
    public static void nextLanguage() { setLanguage(Language.values()[(getLanguage().ordinal() + 1) % Language.values().length]); }
    public static String t(String key) {
        String[] values = TEXT.get(key);
        return values == null ? key : values[getLanguage().ordinal()];
    }
}
