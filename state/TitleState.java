package com.mygdx.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Main;

public class TitleState implements GameState {

	Main game;
	String[] options = {"Start game", "Options", "Exit"};
	int selected = 0;
	float spacing = 60;
	
	public TitleState(Main game) {
		this.game = game;
	}
	
	@Override
	public void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selected--;
            if (selected < 0) selected = options.length - 1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selected++;
            if (selected >= options.length) selected = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (selected == 0) {
                game.setState(new PlayingState(game));
            } else if (selected == 1) {
            	// Add later 
            } else if (selected == 2) {
                Gdx.app.exit();
            }
        }
	}

	@Override
	public void render(SpriteBatch batch) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		game.font.setColor(Color.WHITE);
        game.font.draw(batch, "TITLE SCREEN", 300, 400);

        for (int i = 0; i < options.length; i++) {
            String text = options[i];

            if (i == selected) {
                game.font.setColor(Color.YELLOW);  
                text = "> " + text + " <";
            } else {
                game.font.setColor(Color.WHITE);
            }

            game.font.draw(batch, text, 300, 330 - i * spacing);
        }
        
		batch.end();
	}
	
}
