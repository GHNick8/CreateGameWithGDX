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
    OrthographicCamera camera;

    BitmapFont font;
    boolean gameOver = false;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture("mega.png");
        platformTexture = new Texture("platform001.png");
        enemyTexture = new Texture("dog.png");
        map = new TmxMapLoader().load("test.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        platforms = new ArrayList<>();
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
    }

    public void update(float delta) {

        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                restartGame();
            }
            return;
        }

        handleInput();
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

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            player.jump();
        }
        
        float shootX = 0;
        float shootY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            shootX = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            shootX = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            shootY = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            shootY = -1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            player.shoot(shootX, shootY);
        }
    }

    public void restartGame() {
        player = new Player(playerTexture, new Vector2(100, 100), platforms);
        gameOver = false;
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        mapRenderer.setView(camera);
        mapRenderer.render();

        for (Platform p : platforms) {
            p.draw(batch);
        }

        if (!gameOver) {
            player.draw(batch);
        }

        for (Enemy e : enemies) {
            if (e.isActive()) {
                e.draw(batch);
            }
        }

        if (gameOver) {
            font.draw(batch, "GAME OVER - Press R to Restart", camera.position.x - 200, camera.position.y);
        }

        batch.end();
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
