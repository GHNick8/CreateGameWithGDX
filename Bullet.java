package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    Texture texture;
    Vector2 position;
    Vector2 direction;
    public float speed = 400;

    boolean active = true;

    public Bullet(Texture texture, Vector2 startPosition, Vector2 direction) {
        this.texture = texture;
        this.position = startPosition;
        this.direction = direction.nor();
    }

    public void update(float delta) {
        position.x += direction.x * speed * delta;
        position.y += direction.y * speed * delta;

        if (position.x < -50 || position.x > 2000 || position.y < -50 || position.y > 1500) {
            active = false;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public boolean isActive() {
        return active;
    }
}
