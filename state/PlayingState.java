package com.mygdx.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Main;

public class PlayingState implements GameState {

	Main game;
	
	public PlayingState(Main game) {
        this.game = game;
    }
	
	@Override
	public void update() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setState(new PausedState(game));
            return;
        }

        game.updateGameLogic();
	}

	@Override
	public void render(SpriteBatch batch) {
		game.renderGame(batch);
	}

}
