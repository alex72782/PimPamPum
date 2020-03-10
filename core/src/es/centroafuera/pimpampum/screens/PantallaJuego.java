package es.centroafuera.pimpampum.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import es.centroafuera.pimpampum.ConfigurationManager;
import es.centroafuera.pimpampum.Fire;
import es.centroafuera.pimpampum.Player;
import es.centroafuera.pimpampum.Shooter;

import java.util.ArrayList;

public class PantallaJuego implements Screen {
    private Stage stage;
    private final int TIME_BETWEEN_SHOOT = 1000;
    private Player player;
    private Array<Shooter> enemyShooter;
    private Fire enemyFire;
    private TiledMap map;
    Batch batch;
    SpriteBatch enemy;
    SpriteBatch fireEnemy;
    SpriteBatch playerBatch;
    SpriteBatch batchContador;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private TiledMapTileLayer collisionLayer;
    private String wallKey = "wall";
    private BitmapFont contador;
    long gameTime;
    Sound sound;


    @Override
    public void show() {
        stage = new Stage();
        camera = new OrthographicCamera();
        // Fija la anchura y altura de la camara en base al número de tiles que se mostrarán
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        map = new TmxMapLoader().load("levels/level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        batch = renderer.getBatch();

        renderer.setView(camera);
        TextureRegion[] textures = {
                new Sprite(new Texture(Gdx.files.internal("mainCharacter/f0.png"))),
                new Sprite(new Texture(Gdx.files.internal("mainCharacter/f1.png"))),
                new Sprite(new Texture(Gdx.files.internal("mainCharacter/f2.png"))),
                new Sprite(new Texture(Gdx.files.internal("mainCharacter/f3.png")))};
        Animation animation = new Animation(0.25f, textures);
        player = new Player(4,new Texture(Gdx.files.internal("mainCharacter/f0.png")), new Vector2(100 ,100), 3, animation);
        playerBatch = new SpriteBatch();
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);

        enemyShooter = new Array<Shooter>();
        enemy = new SpriteBatch();
        fireEnemy = new SpriteBatch();
        enemyFire = new Fire(1, new Texture(Gdx.files.internal("enemies/f0.png")), new Vector2(400, 260), 0);

        contador = new BitmapFont();
        batchContador = new SpriteBatch();
        sound = Gdx.audio.newSound(Gdx.files.local("backgroundMusic.mp3"));
        if (ConfigurationManager.isSoundEnabled()) {
            sound.loop();
        }

        gameTime = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // actualizar
        comprobarTeclado();
        generateEnemies();
        moveEnemies();
        collision();

        // pintar
        renderer.render();

        if (ConfigurationManager.isLifesEnabled()) {
            batchContador.begin();
            contador.setColor(Color.WHITE);
            contador.draw(batchContador, "Vida: " + player.getLifes(), 200, 200);
            batchContador.end();
        }

        player.update(Gdx.graphics.getDeltaTime());
        player.pintar(playerBatch);
        if (enemyFire != null)
            enemyFire.pintar(fireEnemy);
        for (Shooter enem: enemyShooter)
            enem.pintar(enemy);
    }

    private void collision() {
        for (Shooter enem : enemyShooter)
            if (enem.getRectangle().overlaps(player.getRectangle())){

                enem.setLifes(enem.getLifes()-1);
                player.setLifes(player.getLifes()-1);

                if (enem.getLifes() <= 0)
                    enemyShooter.removeValue(enem, true);
                if (player.getLifes() <= 0)
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaMenuPrincipal());
            }
        if (enemyFire != null)
            if (player.getRectangle().overlaps(enemyFire.getRectangle())){
                player.setLifes(player.getLifes() - 1);
                enemyFire.setLifes(enemyFire.getLifes() - 1);

                if (enemyFire.getLifes() <= 0)
                    enemyFire = null;

                if (player.getLifes() <= 0)
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaMenuPrincipal());
            }
    }

    private void drawEnemies() {
        for (Shooter enem: enemyShooter)
            enem.pintar(enemy);
    }

    private void generateEnemies() {
        if ((TimeUtils.millis() - gameTime)> TIME_BETWEEN_SHOOT) {

            enemyShooter.add(new Shooter(1, new Texture(Gdx.files.internal("enemies/weapon.png")), new Vector2(220, 430), 2));

            gameTime = TimeUtils.millis();
        }
    }
    private void moveEnemies() {
        for (Shooter enem : enemyShooter) {
            enem.moveDown();

            if (enem.getY() <= 320) {
                enemyShooter.removeValue(enem, true);
            }
        }
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
        dispose();
    }

    @Override
    public void dispose() {
/*
        map.dispose();
        renderer.dispose();

        if (ConfigurationManager.isSoundEnabled()) {
            sound.stop();
            sound.dispose();
        }

        if (ConfigurationManager.isLifesEnabled())
            batchContador.dispose();

        playerBatch.dispose();
        enemy.dispose();
        contador.dispose();
*/
    }

    private void comprobarTeclado() {
        float tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getHeight();
        boolean collisionX = false, collisionY = false;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

            collisionX = collidesRight(wallKey);
            if (!collisionX)
                player.moveRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            collisionX = collidesLeft(wallKey);
            if (!collisionX)
                player.moveLeft();
        }

        // eje y
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            collisionY = collidesTop(wallKey);
            if (!collisionY)
                player.moveUp();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            collisionY = collidesBottom(wallKey);
            if (!collisionY)
                player.moveDown();
        }
        if (collides("ship")) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new PantallaJuego2());
        }
    }

    private boolean collides(String key){
        if (collidesTop(key))
            return true;
        if (collidesBottom(key))
            return true;
        if (collidesLeft(key))
            return true;
        if (collidesRight(key))
            return true;

        return false;
    }

    private boolean isCellBlocked(float x, float y, String key) {

        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(key);
    }

    public boolean collidesRight(String key) {
        for(float step = 0; step < player.getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(player.getX() + player.getWidth(), player.getY() + step, key))
                return true;
        return false;
    }

    public boolean collidesLeft(String key) {
        for(float step = 0; step < player.getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(player.getX(), player.getY() + step, key))
                return true;
        return false;
    }

    public boolean collidesTop(String key) {
        for(float step = 0; step < player.getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(player.getX() + step, player.getY() + player.getHeight(), key))
                return true;
        return false;

    }

    public boolean collidesBottom(String key) {
        for(float step = 0; step < player.getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(player.getX() + step, player.getY(), key))
                return true;
        return false;
    }

    public boolean collidesRightEnemy(Shooter enem, String key) {
        for(float step = 0; step < enem.getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(enem.getX() + enem.getWidth(), enem.getY() + step, key))
                return true;
        return false;
    }

    public boolean collidesLeftEnemy(Shooter enem, String key) {
        for(float step = 0; step < enem.getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(enem.getX(), enem.getY() + step, key))
                return true;
        return false;
    }

    public boolean collidesTopEnemy(Shooter enem, String key) {
        for(float step = 0; step < enem.getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(enem.getX() + step, enem.getY() + enem.getHeight(), key))
                return true;
        return false;

    }

    public boolean collidesBottomEnemy(Shooter enem, String key) {
        for(float step = 0; step < enem.getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(enem.getX() + step, enem.getY(), key))
                return true;
        return false;
    }
}
