package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    Player player;
    private OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("mega.png");
        player = new Player(image, new Vector2(100, 100));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render() {
    	update(Gdx.graphics.getDeltaTime());
    	
    	Gdx.gl.glClearColor(0, 0, 0, 1);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	
    	batch.setProjectionMatrix(camera.combined);
    	batch.begin();
    	
    	// Draw player
    	player.draw(batch);

        batch.end();
    }
    
    public void update(float delta) {
    	handleInput();
    	player.update(delta);
    	camera.update();
    }
    
    private void handleInput() {
    	if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
    		Gdx.app.exit();
    	}
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
