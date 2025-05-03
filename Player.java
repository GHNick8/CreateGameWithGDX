package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
	// Player logic
	Texture texture;
	Vector2 position;
	float speed = 300f;
	float velocityY = 0f;
	int tripleJump = 3;
	
	// Health logic 
	int playerHealth = 5;
	public boolean playerAlive = true;
	
	// Shooting logic
	ArrayList<Bullet> bullets = new ArrayList<>();
	Texture imageBullet = new Texture("shoot01.png");
	
	// Platform logic
	ArrayList<Platform> platforms;
	
	// Constructor player
	public Player(Texture texture, Vector2 startPosition, ArrayList<Platform> platforms) {
		this.texture = texture;
		this.position = startPosition;
		this.platforms = platforms;
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
		boolean onPlatform = false;

		for (Platform p : platforms) {
		    if (position.y > p.getBounds().y + p.getBounds().height - 1) {
		        if (position.x + texture.getWidth() > p.getBounds().x &&
		            position.x < p.getBounds().x + p.getBounds().width) {

		            if (position.y + velocityY * delta <= p.getBounds().y + p.getBounds().height) {
		                position.y = p.getBounds().y + p.getBounds().height;
		                velocityY = 0;
		                tripleJump = 3;
		                onPlatform = true;
		                if (p.moving) {
		                    position.x += p.moveSpeed * p.direction * delta;
		                }
		                break;
		            }
		        }
		    }
		}

		// Ground collision if not on platform 
		if (!onPlatform && position.y <= 0) {
		    position.y = 0;
		    velocityY = 0;
		    tripleJump = 3;
		}
		
		// Update bullets
		for (int i = bullets.size() - 1; i >= 0; i--) {
			Bullet b = bullets.get(i);
			b.update(delta);
			if (!b.isActive()) {
				bullets.remove(i);
			}
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
		
		// Shooting logic 
		int shootX = 0;
		int shootY = 0;
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) shootX = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) shootX = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) shootY = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) shootY = -1;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			Vector2 shootDir = new Vector2(shootX, shootY);
			if (shootDir.isZero()) {
				shootDir.x = 1;
			}
			bullets.add(new Bullet(imageBullet, new Vector2(position.x + texture.getWidth() / 2, position.y + texture.getHeight() / 2), shootDir));
		}
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
		
		// Draw bullets
		for (Bullet b: bullets) {
			b.draw(batch);
		}
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
