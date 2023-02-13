package org.supanthapaul.contour;

import imgui.ImGui;
import org.supanthapaul.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    // The game objects that makes up this scene
    protected List<GameObject> gameObjects = new ArrayList<>();
    // the currently active game object
    protected GameObject activeGameObject = null;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        // start all game objects of the scene
        for(GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
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
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public void sceneImgui() {
        if(activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }

    public Camera getCamera() {
        return this.camera;
    }
}
