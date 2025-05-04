package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class EnemyBullet {
	public Vector2 position;
	Vector2 velocity;
	Texture texture;
	public boolean active = true;
	
	public EnemyBullet(Texture texture, Vector2 startPosition, Vector2 direction) {
		this.texture = texture;
		this.position = new Vector2(startPosition);
		this.velocity = direction.nor().scl(200);
	}
	
	public void update(float delta) {
		position.x += velocity.x * delta;
		position.y += velocity.y * delta;
		
		if (position.x > 2000 || position.x < -100 || position.y > 1000 || position.y < -100) {
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