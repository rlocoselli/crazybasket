import com.badlogic.gdx.*;
import configuration.*;
import java.lang.reflect.*;
import java.util.*;
public class GameRulesCheck {
 public static void main(String[] args) {
  Map<String,Object> saved = new HashMap<>();
  Preferences prefs = (Preferences) Proxy.newProxyInstance(Preferences.class.getClassLoader(), new Class[]{Preferences.class}, (p,m,a)-> {
   if(m.getName().equals("getString")) return saved.getOrDefault(a[0], a.length>1?a[1]:"");
   if(m.getName().equals("getInteger")) return saved.getOrDefault(a[0],a.length>1?a[1]:0);
   if(m.getName().equals("putInteger")) {saved.put((String)a[0],a[1]);return p;}
   if(m.getName().equals("putString")) {saved.put((String)a[0],a[1]);return p;}
   return null;
  });
  Gdx.app=(Application)Proxy.newProxyInstance(Application.class.getClassLoader(),new Class[]{Application.class},(p,m,a)->m.getName().equals("getPreferences")?prefs:null);
  String[] keys={"play","practice","scores","share","language","court","distance","height","score","best","time","championship","near","normal","far","low","high","street","rooftop","beach","arena","share_message","drag","difficulty","easy","hard","park","desert","snow","space"};
  for(Localization.Language language:Localization.Language.values()) {
   Localization.setLanguage(language); Localization.init();
   if(Localization.getLanguage()!=language) throw new AssertionError("Persistence");
   for(String key:keys) if(Localization.t(key).equals(key)||Localization.t(key).isEmpty()) throw new AssertionError(language+":"+key);
  }
  Localization.setLanguage(Localization.Language.EN);
  for(int i=0;i<5;i++) Localization.nextLanguage();
  if(Localization.getLanguage()!=Localization.Language.EN) throw new AssertionError("Cycle");
  saved.put("language","unknown");Localization.init();
  if(Localization.getLanguage()!=Localization.Language.EN) throw new AssertionError("Fallback");
  helpers.AssetLoader.prefs();
  saved.put("scenario",-1);
  if(helpers.AssetLoader.getScenarioIndex()!=Scenario.values().length-1) throw new AssertionError("Invalid court recovery");
  saved.put("difficulty",-1);
  if(helpers.AssetLoader.getDifficulty()!=Difficulty.HARD) throw new AssertionError("Invalid difficulty recovery");
  for(Scenario scenario:Scenario.values()) {
   if(!(Difficulty.EASY.initialBalls(scenario)>Difficulty.NORMAL.initialBalls(scenario)
       && Difficulty.NORMAL.initialBalls(scenario)>Difficulty.HARD.initialBalls(scenario))) throw new AssertionError("Difficulty budgets");
  }
  System.out.println("PASS: five languages, all translation keys, persistence, cycling and fallback");
 }
}
