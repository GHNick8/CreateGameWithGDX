package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Enemy;
import com.mygdx.game.entity.Player;
import com.mygdx.game.manager.HUDManager;
import com.mygdx.game.state.GameState;
import com.mygdx.game.state.TitleState;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
	
    SpriteBatch batch;
    Texture playerTexture;
    Texture platformTexture;
    Texture enemyTexture;

    Player player;
    ArrayList<Platform> platforms;
    ArrayList<Enemy> enemies;

    TiledMap map;
    OrthogonalTiledMapRenderer mapRenderer;
    public OrthographicCamera camera;

    public BitmapFont font;
    boolean gameOver = false;
    
    GameState currentState;
    
    HandleInput handleInput;
    
    HUDManager hudManager;
    
    public static Texture bulletTexture;
    
    public void setState(GameState newState) {
        currentState = newState;
    }

    @Override
    public void create() {
    	bulletTexture = new Texture("shoot01.png");
    	
        batch = new SpriteBatch();
        playerTexture = new Texture("mega.png");
        platformTexture = new Texture("platform001.png");
        enemyTexture = new Texture("dog.png");
        map = new TmxMapLoader().load("test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        platforms = new ArrayList<>();
        hudManager = new HUDManager(this);
        
        float playerSpawnX = 100;
        float playerSpawnY = 100;

        MapObjects objects = map.getLayers().get("Collisions").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;

                float x = rectObject.getRectangle().x;
                float y = rectObject.getRectangle().y;
                float width = rectObject.getRectangle().width;
                float height = rectObject.getRectangle().height;

                String type = rectObject.getProperties().get("type", String.class);

                if ("platform".equals(type)) {
                    platforms.add(new Platform(x, y, width, height, platformTexture));
                }

                if ("player_spawn".equals(type)) {
                    playerSpawnX = x;
                    playerSpawnY = y;
                }
            }
        }

        platforms.add(new Platform(400, 150, 200, 20, platformTexture));
        platforms.add(new Platform(700, 250, 200, 20, platformTexture));
        platforms.add(new Platform(1000, 350, 200, 20, platformTexture));

        Platform movingPlatform = new Platform(500, 200, 200, 20, platformTexture);
        movingPlatform.setMoving(100, 150);
        platforms.add(movingPlatform);

        player = new Player(playerTexture, new Vector2(playerSpawnX, playerSpawnY), platforms);
        
        handleInput = new HandleInput(player);

        enemies = new ArrayList<>();

        Enemy e = new Enemy(enemyTexture, new Vector2(600, 300), 600, 700);
        e.setMoving(60, 100, false);
        enemies.add(e);

        Enemy f = new Enemy(enemyTexture, new Vector2(800, 200), 200, 400);
        f.setMoving(50, 200, true);
        enemies.add(f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);
        
        setState(new TitleState(this));
    }

    public void update(float delta) {

        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                restartGame();
            }
            return;
        }

        handleInput.update();
        player.update(delta);

        float targetX = player.getX() + player.getWidth() / 2;
        float targetY = player.getY() + player.getHeight() / 2;
        camera.position.x += (targetX - camera.position.x) * 0.1f;
        camera.position.y += (targetY - camera.position.y) * 0.1f;

        float levelWidth = 2000;
        float levelHeight = 800;

        float halfW = camera.viewportWidth / 2;
        float halfH = camera.viewportHeight / 2;

        camera.position.x = Math.max(halfW, Math.min(camera.position.x, levelWidth - halfW));
        camera.position.y = Math.max(halfH, Math.min(camera.position.y, levelHeight - halfH));

        camera.update();

        for (Enemy e : enemies) {
            if (e.isActive()) {
                e.update(delta);

                for (EnemyBullet b : e.bullets) {
                    if (b.isActive() && player.getBounds().contains(b.position)) {
                        b.active = false;
                        player.takeDamage();
                        if (!player.playerAlive) gameOver = true;
                        break;
                    }
                }
            }
        }
        
        for (Enemy e : enemies) {
            if (!e.isActive()) continue;

            for (int i = player.bullets.size() - 1; i >= 0; i--) {
                Bullet b = player.bullets.get(i);

                if (e.getBounds().overlaps(new Rectangle(b.position.x, b.position.y, 8, 8))) {
                    e.hit();
                    b.active = false;
                    player.bullets.remove(i);
                    break;
                }
            }
        }

        for (Platform p : platforms) {
            p.update(delta);
        }
    }

    public void restartGame() {
        player = new Player(playerTexture, new Vector2(100, 100), platforms);
        gameOver = false;
    }
    
    public void updateGameLogic() {
    	if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                restartGame();
            }
            return;
        }

    	handleInput.update();
        player.update(Gdx.graphics.getDeltaTime());

        float targetX = player.getX() + player.getWidth() / 2;
        float targetY = player.getY() + player.getHeight() / 2;
        camera.position.x += (targetX - camera.position.x) * 0.1f;
        camera.position.y += (targetY - camera.position.y) * 0.1f;

        float levelWidth = 2000;
        float levelHeight = 800;
        float halfW = camera.viewportWidth / 2;
        float halfH = camera.viewportHeight / 2;

        camera.position.x = Math.max(halfW, Math.min(camera.position.x, levelWidth - halfW));
        camera.position.y = Math.max(halfH, Math.min(camera.position.y, levelHeight - halfH));
        camera.update();

        for (Enemy e : enemies) {
            if (e.isActive()) {
                e.update(Gdx.graphics.getDeltaTime());

                for (EnemyBullet b : e.bullets) {
                    if (b.isActive() && player.getBounds().contains(b.position)) {
                        b.active = false;
                        player.takeDamage();
                        if (!player.playerAlive) gameOver = true;
                        break;
                    }
                }
            }
        }

        for (Enemy e : enemies) {
            if (!e.isActive()) continue;

            for (int i = player.bullets.size() - 1; i >= 0; i--) {
                Bullet b = player.bullets.get(i);

                if (e.getBounds().overlaps(new Rectangle(b.position.x, b.position.y, 8, 8))) {
                    e.hit();
                    b.active = false;
                    player.bullets.remove(i);
                    break;
                }
            }
        }

        for (Platform p : platforms) {
            p.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void renderGame(SpriteBatch batch) {
    	Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        hudManager.render(batch, camera.position, player);

        mapRenderer.setView(camera);
        mapRenderer.render();

        for (Platform p : platforms) {
            p.draw(batch);
        }

        // Draw enemies
        for (Enemy e : enemies) {
            if (e.isActive()) {
                e.draw(batch);

                // Draw enemy bullets
                for (EnemyBullet b : e.bullets) {
                    if (b.isActive()) {
                        b.draw(batch);
                    }
                }
            }
        }

        // Draw player
        if (!gameOver) {
            player.draw(batch);
        }

        // Draw player bullets
        for (Bullet b : player.bullets) {
            b.draw(batch);
        }

        // Draw HUD
        hudManager.render(batch, camera.position, player);

        // Draw game over message if game is over
        if (gameOver) {
            font.setColor(Color.WHITE);
            font.draw(batch, "GAME OVER - Press R to restart", camera.position.x - 200, camera.position.y);
        }

        batch.end();
    }


    @Override
    public void render() {
    	if (currentState != null) {
            currentState.update();
            currentState.render(batch);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        platformTexture.dispose();
        enemyTexture.dispose();
        map.dispose();
        mapRenderer.dispose();
        font.dispose();
    }
}
