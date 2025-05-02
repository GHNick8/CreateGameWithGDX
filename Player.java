package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
	Texture texture;
	Vector2 position;
	float speed = 300f;
	float velocityY = 0f;
	int tripleJump = 3;
	
	// Health logic 
	int playerHealth = 5;
	public boolean playerAlive = true;
	
	// Constructor player
	public Player(Texture texture, Vector2 position) {
		this.texture = texture;
		this.position = position;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
	}
	
	public void takeDamage() {
		playerHealth--;
		if (playerHealth <= 0) {
			playerAlive = false;
		}
	}
	
	public void update(float delta) {
		handleInput(delta);
		
		// Gravity
		velocityY -= 500 * delta;
		position.y += velocityY * delta;
		
		// Ground collision
		if (position.y < 0) {
			position.y = 0;
			velocityY = 0;
			tripleJump = 3;
		}
	}
	
	public void handleInput(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			position.x -= speed * delta;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			position.x += speed * delta;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.W) && tripleJump > 0) {
			velocityY = 300;
			tripleJump--;
		}
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}
	
	public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public float getWidth() {
        return texture.getWidth();
    }
    
    public float getHeight() {
    	return texture.getHeight();
    }
}
