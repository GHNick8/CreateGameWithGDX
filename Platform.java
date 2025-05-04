package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
	Rectangle bounds;
	Texture texture;
	
	// Movement
	public boolean moving = false;
	public float moveSpeed = 100f;
	float moveDistance = 100f;
	float startX, startY;
	public int direction = 1; // if not - replace with -1

	public Platform(float x, float y, float width, float height, Texture texture) {
		this.bounds = new Rectangle(x, y, width, height);
        this.texture = texture;
        this.startX = x;
        this.startY = y;
	}
	
	public void setMoving(float moveSpeed, float moveDistance) {
		this.moving = true;
		this.moveSpeed = moveSpeed;
		this.moveDistance = moveDistance;
	}
	
	public void update(float delta) {
		if (!moving) return;
		
		bounds.x += moveSpeed * direction * delta;
		
		if (bounds.x > startX + moveDistance) {
			bounds.x = startX + moveDistance;
			direction = -1;
		}
		
		if (bounds.x < startX) {
            bounds.x = startX;
            direction = 1;
        }
	}
	
	public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
