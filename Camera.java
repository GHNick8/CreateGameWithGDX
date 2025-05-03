package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera {
	OrthographicCamera camera;
	float levelWidth, levelHeight;
	
	public Camera(float viewportWidth, float viewportHeight, float levelWidth, float levelHeight) {
		camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
	}
	
	public void update(float targetX, float targetY) {
		float lerp = 0.1f;
		
		camera.position.x += (targetX - camera.position.x) * lerp;
		camera.position.y += (targetY - camera.position.y) * lerp;
		
		float halfViewportWidth = camera.viewportWidth / 2;
		float halfViewportHeight = camera.viewportHeight / 2;
		
		if (camera.position.x < halfViewportWidth) {
			camera.position.x = halfViewportWidth;
		}
		
		if (camera.position.x > levelWidth - halfViewportWidth) {
            camera.position.x = levelWidth - halfViewportWidth;
        }
		
		if (camera.position.y < halfViewportHeight) {
            camera.position.y = halfViewportHeight;
        }
		
        if (camera.position.y > levelHeight - halfViewportHeight) {
            camera.position.y = levelHeight - halfViewportHeight;
        }
        
        camera.update();
	}
	
	public OrthographicCamera getCamera() {
        return camera;
    }
}
