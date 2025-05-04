package com.mygdx.game.weapon;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Bullet;
import com.mygdx.game.Main;
import com.mygdx.game.entity.Player;

public class LaserWeapon extends Weapon {

	public LaserWeapon(Player player) {
		super(player);
	}

	@Override
	public void shoot(Vector2 direction, List<Bullet> bullets) {
		Bullet b = new Bullet(Main.bulletTexture, new Vector2(player.position.x + player.texture.getWidth() / 2,
                							player.position.y + player.texture.getHeight() / 2),
											direction);
		b.speed = 600;  
		bullets.add(b);
	}

	@Override
	public String getName() {
		return "Radiant Blaster";
	}
	
}
