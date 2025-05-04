package com.mygdx.game.manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Main;
import com.mygdx.game.entity.Player;

public class HUDManager {
	Main game;
	ShapeRenderer shapeRenderer;
	
	public HUDManager(Main game) {
		this.game = game;
		shapeRenderer = new ShapeRenderer();
	}
	
	public void render(SpriteBatch batch, Vector3 cameraPosition, Player player) {
		BitmapFont font = game.font;
		
		// HUD positions 
		float startX = cameraPosition.x - 380;
		float startY = cameraPosition.y + 220;
		
		// Draw health
		font.setColor(Color.WHITE);
	    font.draw(batch, "LIVES: x 3", startX, startY);
	    font.draw(batch, "WEAPON: Normal Shot", startX, startY - 30);
	    
	    // End batch before using ShapeRenderer 
	    batch.end();
	    
	    shapeRenderer.setProjectionMatrix(game.camera.combined);
	    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	    shapeRenderer.setColor(Color.DARK_GRAY); 
	    shapeRenderer.rect(startX, startY - 90, 200, 20);

	    // Health amount 
	    float healthWidth = (player.playerHealth / 5f) * 200;  
	    shapeRenderer.setColor(Color.GREEN);
	    shapeRenderer.rect(startX, startY - 90, healthWidth, 20);

	    shapeRenderer.end();

	    // Restart batch after shapeRenderer
	    batch.begin();
	}
}
