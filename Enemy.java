package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {
	// Enemy logic
	Vector2 position;
	Texture texture;
	boolean active = true;
	
	// Health logic
	int enemyHealth = 7;
	
	// Patrol logic 
	boolean moving = false;
    float moveSpeed = 50f;
    float moveDistance = 100f;
    float patrolStartX, patrolStartY;
    int direction = 1; // 1 = right/down, -1 = left/up
    boolean moveVertical = false;
    
	// Shooting logic 
	ArrayList<EnemyBullet> bullets = new ArrayList<>();
	Texture bulletTexture = new Texture("shoot01.png");
	float shootTimer = 0;
	float shootInterval = 2.0f; 
	
	// Constructor enemy 
	public Enemy(Texture texture, Vector2 position, float patrolStartX, float patrolEndX) {
		this.texture = texture;
		this.position = position;
		this.patrolStartX = position.x;
        this.patrolStartY = position.y;
	}
	
	public void setMoving(float speed, float distance, boolean vertical) {
		moving = true;
        moveSpeed = speed;
        moveDistance = distance;
        moveVertical = vertical;
        patrolStartX = position.x;
        patrolStartY = position.y;
	}
	
	public void update(float delta) {
		// Patrol Movement
		if (moving) {
            if (moveVertical) {
                position.y += moveSpeed * direction * delta;

                if (position.y > patrolStartY + moveDistance) {
                    position.y = patrolStartY + moveDistance;
                    direction = -1;
                }
                if (position.y < patrolStartY) {
                    position.y = patrolStartY;
                    direction = 1;
                }

            } else {
                position.x += moveSpeed * direction * delta;

                if (position.x > patrolStartX + moveDistance) {
                    position.x = patrolStartX + moveDistance;
                    direction = -1;
                }
                if (position.x < patrolStartX) {
                    position.x = patrolStartX;
                    direction = 1;
                }
            }
        }

        // Update shooting 
        shootTimer += delta;

        if (shootTimer >= shootInterval) {
            shootTimer = 0;

            Vector2 playerPosition = EnemyManager.playerPosition;
            Vector2 shootDir = new Vector2(playerPosition.x - position.x, playerPosition.y - position.y);

            bullets.add(new EnemyBullet(bulletTexture,
                    new Vector2(position.x + texture.getWidth() / 2, position.y + texture.getHeight() / 2),
                    shootDir));
        }

        // Update bullets 
        for (int i = bullets.size() - 1; i >= 0; i--) {
            EnemyBullet b = bullets.get(i);
            b.update(delta);
            if (!b.isActive()) {
                bullets.remove(i);
            }
        }
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
		
		// Draw health bar for testing purposes 
		for (int i = 0; i < enemyHealth; i++) {
			batch.draw(texture, position.x + i * 8, position.y + texture.getHeight() + 5, 6, 6);
		}
		
		// Draw bullets
		for (EnemyBullet b : bullets) {
		    b.draw(batch);
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
