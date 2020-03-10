package es.centroafuera.pimpampum;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ConfigurationManager {

    private static Preferences prefs = Gdx.app.getPreferences("PimPamPum");

    /**
     * Comprueba si el sonido está o no activado durante el juego
     * @return
     */
    public static boolean isSoundEnabled() {

        return prefs.getBoolean("sound");
    }

    public static boolean isLifesEnabled(){
        return prefs.getBoolean("showLifes");
    }
    public static void setLifesEnabled(boolean bool){
        prefs.putBoolean("showLifes", bool);
    }
    public static void setSoundEnabled(boolean bool){
        prefs.putBoolean("sound", bool);
    }
}
