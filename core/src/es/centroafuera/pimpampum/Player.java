package es.centroafuera.pimpampum;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player extends Person {
    private Animation animation;
    TextureRegion currentFrame;
    float stateTime;
    public Player(int lifes, Texture texture, Vector2 position, int speed, Animation animation) {
        super(lifes, texture, position, speed);
        this.animation = animation;
    }

    @Override
    public void pintar(SpriteBatch batch) {
        super.pintar(batch);
    }

    public void update(float dt){
        stateTime += dt;
        currentFrame = (TextureRegion) animation.getKeyFrame(stateTime, true);
        setTexture(currentFrame.getTexture());
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }
}
