package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Main extends ApplicationAdapter {
	// Sprite batch textured planes 
    private SpriteBatch batch;
    
    // Player & Camera 
    private Texture image;
    Player player;
    Camera camera;
    
    // Enemies
    Texture imageEnemy;
    ArrayList<Enemy> enemies;
    
    // Game over
    boolean gameOver = false;
    BitmapFont font;
    
    // Platforms 
    Texture imagePlatform001;
    ArrayList<Platform> platforms;

    @Override
    public void create() {
    	// Initialize new Sprite Batch 
        batch = new SpriteBatch();
        
        // Create platforms
        imagePlatform001 = new Texture("platform001.png");
        platforms = new ArrayList<>();
        
        // Add platforms 
        platforms.add(new Platform(400, 150, 200, 20, imagePlatform001));
        platforms.add(new Platform(700, 250, 200, 20, imagePlatform001));
        platforms.add(new Platform(1000, 350, 200, 20, imagePlatform001));
        
        // Create player & Camera
        image = new Texture("mega.png");
        player = new Player(image, new Vector2(100, 100), platforms);
        camera = new Camera(800, 480, 2000, 800);
        
        // Create enemies
        imageEnemy = new Texture("dog.png");
        enemies = new ArrayList<>();
        
        // Add some enemies 
        enemies.add(new Enemy(imageEnemy, new Vector2(500, 200)));
        enemies.add(new Enemy(imageEnemy, new Vector2(600, 100)));
        
        // Add game over
        font = new BitmapFont();
    }

    @Override
    public void render() {
    	// Update screen 
    	update(Gdx.graphics.getDeltaTime());
    	
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	
    	batch.setProjectionMatrix(camera.getCamera().combined);
    	batch.begin();
    	
    	// Draw player
    	player.draw(batch);
    	
    	// Draw enemies
    	for (Enemy e: enemies) {
    		if (e.isActive()) {
    			e.draw(batch);
    		}
    	}
    	
    	// Draw platforms
    	for (Platform p : platforms) {
    	    p.draw(batch);
    	}
    	
    	// Draw game over
    	if (gameOver) {
    		font.draw(batch,  "GAME OVER - Press R to restart", camera.getCamera().position.x - 100, camera.getCamera().position.y);
    	}

        batch.end();
    }
    
    public void update(float delta) {
    	// Update game over
    	if (gameOver) {
    		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
    			restartGame();
    		}
    		return;
    	}
    	
    	// Update input & player 
    	handleInput();
    	player.update(delta);
    	
    	EnemyManager.playerPosition = new Vector2(player.getX(), player.getY());
    	
    	// Update camera 
    	camera.update(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);

    	// Update enemies 
    	for (Enemy e: enemies) {
    		if (e.isActive()) {
    			e.update(delta);
    		}
    	}
    	
    	// Update player shooting enemies & collision 
    	for (Enemy e: enemies) {
    		if (!e.isActive()) continue;
    		
    		for (Bullet b: player.bullets) {
    			if (b.isActive() && e.getBounds().contains(b.position)) {
    				e.hit();
    				b.active = false;
    				break;
    			}
    		}
    	}
    }
    
    private void handleInput() {
    	if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
    		Gdx.app.exit();
    	}
    }
    
    public void restartGame() {
    	// Reset player
    	player = new Player(image, new Vector2(100, 100), platforms);
    	
    	// Reset enemies
    	enemies.clear();
    	enemies.add(new Enemy(imageEnemy, new Vector2(500, 200)));
        enemies.add(new Enemy(imageEnemy, new Vector2(600, 100)));
        
        // Reset game over
        gameOver  = false;
    	
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        font.dispose();
    }
}
