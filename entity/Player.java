package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Bullet;
import com.mygdx.game.Platform;
import com.mygdx.game.weapon.DefaultWeapon;
import com.mygdx.game.weapon.LaserWeapon;
import com.mygdx.game.weapon.SpreadWeapon;
import com.mygdx.game.weapon.Weapon;

import java.util.ArrayList;

public class Player {

    public Vector2 position;
    public Texture texture;

    float velocityY = 0;
    final float GRAVITY = -600;

    public ArrayList<Bullet> bullets = new ArrayList<>();

    ArrayList<Platform> platforms;

    int tripleJump = 3;

    public int playerHealth = 5;
    public boolean playerAlive = true;
    
    Weapon[] weapons;
    int currentWeaponIndex = 0;

    public Player(Texture texture, Vector2 startPosition, ArrayList<Platform> platforms) {
        this.texture = texture;
        this.position = startPosition;
        this.platforms = platforms;
        
        weapons = new Weapon[] {
        		new DefaultWeapon(this),
        		new SpreadWeapon(this),
        		new LaserWeapon(this)
        };
        
        currentWeaponIndex = 0;

        // Make sure player starts alive + full health
        this.playerHealth = 5;
        this.playerAlive = true;
    }

    public void update(float delta) {
        if (!playerAlive) {
            return;
        }

        velocityY += GRAVITY * delta;
        position.y += velocityY * delta;

        // Check for platform collisions
        boolean onPlatform = false;

        for (Platform p : platforms) {
            if (velocityY <= 0) {
                if (position.y >= p.getBounds().y + p.getBounds().height - 5) {
                    if (position.x + texture.getWidth() > p.getBounds().x &&
                            position.x < p.getBounds().x + p.getBounds().width) {

                        if (position.y + velocityY * delta <= p.getBounds().y + p.getBounds().height) {
                            // Land on platform
                            position.y = p.getBounds().y + p.getBounds().height;
                            velocityY = 0;
                            tripleJump = 3;
                            onPlatform = true;

                            // Move with platform if moving
                            if (p.moving) {
                                position.x += p.moveSpeed * p.direction * delta;
                            }
                            break;
                        }
                    }
                }
            }
        }

        // Ground check
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

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);

        for (Bullet b : bullets) {
            b.draw(batch);
        }
    }

    public void moveLeft() {
        if (!playerAlive) return;

        position.x -= 200 * Gdx.graphics.getDeltaTime();
    }

    public void moveRight() {
        if (!playerAlive) return;

        position.x += 200 * Gdx.graphics.getDeltaTime();
    }

    public void jump() {
        if (!playerAlive) return;

        if (tripleJump > 0) {
            velocityY = 400;
            tripleJump--;
        }
    }

    public void shoot(float shootX, float shootY) {
        if (!playerAlive) return;

        Vector2 shootDir = new Vector2(shootX, shootY);
        if (shootDir.isZero()) {
            shootDir.set(1, 0);
        } else {
            shootDir.nor();  
        }

        weapons[currentWeaponIndex].shoot(shootDir, bullets);
    }
    
    public void switchWeapon(boolean next) {
    	if (next) {
    		currentWeaponIndex = (currentWeaponIndex + 1) % weapons.length;
    	} else {
    		currentWeaponIndex--;
            if (currentWeaponIndex < 0) currentWeaponIndex = weapons.length - 1;
    	}
    }
    
    public String getWeaponName() {
        return weapons[currentWeaponIndex].getName();
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
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

    public void takeDamage() {
        playerHealth--;

        if (playerHealth <= 0) {
            playerAlive = false;
        }
    }
}
