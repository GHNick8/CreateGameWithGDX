package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
	Rectangle bounds;
	Texture texture;
	
	public Platform(float x, float y, float width, float height, Texture texture) {
		this.bounds = new Rectangle(x, y, width, height);
        this.texture = texture;
	}
	
	public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
