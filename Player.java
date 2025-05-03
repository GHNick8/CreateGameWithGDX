package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Player {

    Vector2 position;
    Texture texture;

    float velocityY = 0;
    final float GRAVITY = -600;

    ArrayList<Bullet> bullets = new ArrayList<>();

    ArrayList<Platform> platforms;

    int tripleJump = 3;

    int playerHealth = 5;
    boolean playerAlive = true;

    public Player(Texture texture, Vector2 startPosition, ArrayList<Platform> platforms) {
        this.texture = texture;
        this.position = startPosition;
        this.platforms = platforms;

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
            shootDir = new Vector2(1, 0); // Default to right
        } else {
            shootDir.nor();
        }

        bullets.add(new Bullet(new Texture("shoot01.png"),
                new Vector2(position.x + texture.getWidth() / 2, position.y + texture.getHeight() / 2),
                shootDir));
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
