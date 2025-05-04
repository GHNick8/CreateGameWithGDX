package com.mygdx.game.weapon;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Bullet;
import com.mygdx.game.entity.Player;

public abstract class Weapon {
	Player player;
	
	public Weapon(Player player) {
		this.player = player;
	}
	
	public abstract void shoot(Vector2 direction, List<Bullet> bullets);
	public abstract String getName();
}
