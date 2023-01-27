package org.supanthapaul.contour;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    // The game objects that makes up this scene
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        // start all game objects of the scene
        for(GameObject go : gameObjects) {
            go.start();
        }
        // scene is now running
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if(!isRunning) {
            // if the scene is not running then just add the gameobject
            // start function will handle starting the gameobjects
            gameObjects.add(go);
        } else {
            // if the scene is running then add and start the gameobject
            gameObjects.add(go);
            go.start();
        }
    }

    public abstract void update(float dt);
}
