package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
	Vector2 position;
	Texture texture;
	boolean active = true;
	
	// Health logic
	int enemyHealth = 7;
	
	public Enemy(Texture texture, Vector2 position) {
		this.texture = texture;
		this.position = position;
	}
	
	public void update(float delta) {
		
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
		
		// Draw health bar for testing purposes 
		for (int i = 0; i < enemyHealth; i++) {
			batch.draw(texture, position.x + i * 8, position.y + texture.getHeight() + 5, 6, 6);
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void hit() {
		enemyHealth--;
		if (enemyHealth <= 0) {
			active = false;
		}
	}
}
