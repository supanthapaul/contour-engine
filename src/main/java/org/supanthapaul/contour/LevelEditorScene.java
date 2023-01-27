package org.supanthapaul.contour;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.supanthapaul.components.FontRenderer;
import org.supanthapaul.components.SpriteRenderer;
import org.supanthapaul.renderer.Shader;
import org.supanthapaul.renderer.Texture;
import org.supanthapaul.util.Time;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

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
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x=0; x < 100; x++) {
            for (int y=0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObjectToScene(go);
            }
        }
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
