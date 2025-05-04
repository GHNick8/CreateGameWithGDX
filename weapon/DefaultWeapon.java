package com.mygdx.game.weapon;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Bullet;
import com.mygdx.game.Main;
import com.mygdx.game.entity.Player;

public class DefaultWeapon extends Weapon {

	public DefaultWeapon(Player player) {
		super(player);
	}

	@Override
	public void shoot(Vector2 direction, List<Bullet> bullets) {
		bullets.add(new Bullet(Main.bulletTexture, new Vector2(player.position.x + player.texture.getWidth() / 2,
											player.position.y + player.texture.getHeight() / 2),
											direction));
	}

	@Override
	public String getName() {
		return "Pup Blaster";
	}
	
}
