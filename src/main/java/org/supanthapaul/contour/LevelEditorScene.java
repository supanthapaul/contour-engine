package org.supanthapaul.contour;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.supanthapaul.components.Sprite;
import org.supanthapaul.components.SpriteRenderer;
import org.supanthapaul.components.Spritesheet;
import org.supanthapaul.renderer.Shader;
import org.supanthapaul.renderer.Texture;
import org.supanthapaul.util.AssetPool;

public class LevelEditorScene extends Scene {

    private Shader defaultShader;
    private Texture testTexture;
    private GameObject testObject;

    private float[] vertexArray = {
            // position              // color                   // UV coordinates
            100.0f, 0.0f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,    1, 1,  // bottom right 0
            0.0f, 100.0f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,    0, 0,  // top left     1
            100.0f, 100.0f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f,    1, 0,  // top right    2
            0.0f, 0.0f, 0.0f,        1.0f, 1.0f, 0.0f, 1.0f,    0, 1,  // bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
        /*
                x       x

                x       x
         */
            2, 1, 0, // top right triangle
            0, 1, 3  // bottom left triangle
    };
    private int vaoID, vboID, eboID;
    private boolean firstTime = false;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        Spritesheet spritesheet = AssetPool.getSpritesheet("assets/images/Tilemap/characters_packed.png");

        GameObject obj1 = new GameObject("1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(spritesheet.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(spritesheet.getSprite(4)));
        this.addGameObjectToScene(obj2);
        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/Tilemap/characters_packed.png",
                new Spritesheet(AssetPool.getTexture("assets/images/Tilemap/characters_packed.png"),
                        24, 24, 27, 0));
    }

    @Override
    public void update(float dt) {
        System.out.println(1/dt);
        // update game objects
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
