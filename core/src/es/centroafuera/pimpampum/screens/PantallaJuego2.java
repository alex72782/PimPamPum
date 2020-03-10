package es.centroafuera.pimpampum.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import es.centroafuera.pimpampum.ConfigurationManager;
import es.centroafuera.pimpampum.model.Bala;
import es.centroafuera.pimpampum.model.Marciano;
import es.centroafuera.pimpampum.model.Nave;
import es.centroafuera.pimpampum.model.Roca;

import java.awt.*;

public class PantallaJuego2 implements Screen {
    private SpriteBatch batch;
    private Nave nave;
    private Array<Marciano> marcianos;
    private Array<Roca> rocas;
    private Array<Bala> balas;
    private long tiempoJuego;


    @Override
    public void show() {
        batch = new SpriteBatch();
        nave = new Nave(Vector2.Zero, new Texture("ship/f1.png"), 3, Constantes.VELOCIDAD_NAVE,
                Nave.TipoDisparo.RAFAGA, false);
        marcianos = new Array<>();
        rocas = new Array<>();
        balas = new Array<>();

        tiempoJuego = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        actualizar();
        pintar();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void actualizar() {
        // Input de usuario (teclado)
        comprobarTeclado();
        // Generar enemigos
        generarEnemigos();
        // Mover balas
        moverBalas();
        // Mover enemigos
        moverEnemigos();
        // Comprobar colisiones
        comprobarColisiones();
        // Comprobaciones varias . . .
    }

    private void comprobarTeclado() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            nave.moverArriba();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            nave.moverAbajo();
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            disparar();
    }

    private void disparar() {
        Sound m = Gdx.audio.newSound(Gdx.files.internal("sounds/pistola.mp3"));
        if(ConfigurationManager.isSoundEnabled())
            m.play();
        Vector2 pos = nave.getPosicion().cpy();
        pos.x += nave.rect.width;
        pos.y += nave.rect.height/2;
        balas.add(new Bala(pos, new Texture("enemy/bullet.png"), 1, 4));
    }

    private void generarEnemigos() {
        System.out.println(TimeUtils.millis() + " " + tiempoJuego + " " + Constantes.TIEMPO_ENTRE_MARCIANOS + (TimeUtils.millis() - tiempoJuego > Constantes.TIEMPO_ENTRE_MARCIANOS));
        if (TimeUtils.millis() - tiempoJuego > Constantes.TIEMPO_ENTRE_MARCIANOS) {
            Texture textura = new Texture("enemy/e_f1.png");
            Vector2 posicion = new Vector2(Gdx.graphics.getWidth() - textura.getWidth(),
                    MathUtils.random(0, Gdx.graphics.getHeight()));
            Marciano marciano = new Marciano(posicion, textura, 1, 2);
            marcianos.add(marciano);

            tiempoJuego = TimeUtils.millis();
        }
    }

    private void moverEnemigos() {
        for (Marciano marciano : marcianos) {
            marciano.moverIzquierda();
            if (marciano.getPosicion().x < 0) {
                marcianos.removeValue(marciano, true);
            }
        }
    }

    private void moverBalas() {
        for (Bala bala : balas) {
            bala.moverDerecha();
            if (bala.getPosicion().x > Gdx.graphics.getWidth()) {
                balas.removeValue(bala, true);
            }
        }
    }

    private void comprobarColisiones() {
        for (Marciano marciano : marcianos) {
            if (marciano.rect.overlaps(nave.rect)) {
                marciano.quitarVida();
                nave.quitarVida();

                if ( nave.muere() )
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaMenuPrincipal());
                if ( marciano.muere() )
                    marcianos.removeValue(marciano, true);
            }
            for (Bala bala : balas)
                if (marciano.rect.overlaps(bala.rect)) {
                    marciano.quitarVida();
                    bala.quitarVida();

                    if ( bala.muere() )
                        balas.removeValue(bala, true);
                    if ( marciano.muere() )
                        marcianos.removeValue(marciano, true);
                }
        }
    }

    private void pintar() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        // Pintar la nave
        nave.pintar(batch);
        // Pintar los enemigos
        for (Marciano marciano : marcianos)
            marciano.pintar(batch);
        for (Roca roca : rocas)
            roca.pintar(batch);
        for (Bala bala : balas)
            bala.pintar(batch);

        batch.end();
    }

}
