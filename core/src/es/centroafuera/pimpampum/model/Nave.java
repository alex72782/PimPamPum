package es.centroafuera.pimpampum.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Nave extends Personaje {

    public enum TipoDisparo {
        UNO, RAFAGA
    }

    private TipoDisparo disparo;
    private boolean inmune;
    private Texture texturaNaveAuxiliar;

    public Nave(Vector2 posicion, Texture textura, int vidas, int velocidad, TipoDisparo disparo,
                boolean inmune) {
        super(posicion, textura, vidas, velocidad);
        this.disparo = disparo;
        this.inmune = inmune;
    }

    public TipoDisparo getDisparo() {
        return disparo;
    }

    public void setDisparo(TipoDisparo disparo) {
        this.disparo = disparo;
    }

    public boolean isInmune() {
        return inmune;
    }

    public void setInmune(boolean inmune) {
        this.inmune = inmune;
    }

    @Override
    public void pintar(SpriteBatch batch) {
        super.pintar(batch);
        //batch.draw(texturaNaveAuxiliar, getPosicion().x, getPosicion().y + 10);
    }
}
