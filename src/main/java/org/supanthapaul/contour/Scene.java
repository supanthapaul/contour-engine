package org.supanthapaul.contour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.supanthapaul.renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    protected boolean levelLoaded = false;

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

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if(!inFile.equals("")) {
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i =0; i < objs.length; i++) {
                this.addGameObjectToScene(objs[i]);
            }
            this.levelLoaded = true;
        }
    }

    public Camera getCamera() {
        return this.camera;
    }
}
