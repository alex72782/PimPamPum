package es.centroafuera.pimpampum;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Person {
    private int lifes;
    private Texture texture;
    private Rectangle rectangle;
    private Vector2 position;
    private int speed;
    private int tileSize;

    public Person(int lifes, Texture texture, Vector2 position, int speed) {
        this.lifes = lifes;
        this.texture = texture;
        this.position = position;
        this.speed = speed;
        this.rectangle = new Rectangle(position.x, position.y,texture.getWidth(), texture.getHeight());
        tileSize = 16;
    }
    public void pintar(SpriteBatch batch) {
        batch.begin();
        batch.draw(getTexture(), getPosition().x, getPosition().y);
        batch.end();
    }
    public void move(Vector2 movimiento) {
        position.add(movimiento.scl(speed));
        rectangle.setPosition(position);
    }
    public void moveUp() {
        move(new Vector2(0, 1));
    }

    public void moveDown() {
        move(new Vector2(0, -1));
    }

    public void moveRight() {
        move(new Vector2(1, 0));
    }

    public void moveLeft() {
        move(new Vector2(-1, 0));
    }

    public void quitarVida() {
        lifes--;
    }
    public boolean muere(){
        if ( lifes <= 0 ){
            return true;
        }
        return false;
    }

    /* ---- Getters and Setters ----*/

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPlace(Vector2 place) {
        this.position = position;
    }
    public float getHeight() {
        return rectangle.getHeight();
    }
    public float getWidth() {
        return rectangle.getWidth();
    }
    public float getX(){
        return position.x;
    }
    public float getY(){
        return position.y;
    }
}
