package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.entity.Player;

public class HandleInput {
	Player player;

    public HandleInput(Player player) {
        this.player = player;
    }
    
    public void update() {

        float shootX = 0;
        float shootY = 0;

        // Movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
            shootX = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
            shootX = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            shootY = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            shootY = -1;
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player.jump();
        }

        // Shoot
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            player.shoot(shootX, shootY);
        }

        // Weapon switch - test keys for now 
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            player.switchWeapon(false);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            player.switchWeapon(true);
        }
    }
}
