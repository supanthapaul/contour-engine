package org.supanthapaul.contour;

import org.joml.Vector2f;
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
        testObject = new GameObject("Test object");
        testObject.addComponent(new SpriteRenderer());
        testObject.addComponent(new FontRenderer());
        this.addGameObjectToScene(testObject);

        this.camera = new Camera(new Vector2f(-20f, -20f));

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/Characters/character_0000.png");


        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // create VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices(elements) and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        // position attribute
        glVertexAttribPointer(0, positionsSize, GL_FLOAT,false, vertexSizeBytes, 0); // pointer is like an offset
        glEnableVertexAttribArray(0);
        // color attribute
        glVertexAttribPointer(1, colorSize, GL_FLOAT,false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);
        // UV attribute
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        camera.position.x -= dt * 50f;

        // upload texture to shader
        defaultShader.uploadTexture("TEXT_SAMPLER", 0);
        // activate slot 0
        glActiveTexture(GL_TEXTURE0);
        // bind texture
        testTexture.bind();

        // upload projection and view matrices
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // bind the VAO that we're using
        glBindVertexArray(vaoID);

        // enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // draw
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

        if(!firstTime) {
            System.out.println("Creating test object 2");
            testObject = new GameObject("Test object 2");
            testObject.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(testObject);
            firstTime= true;
        }
        // update game objects
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}
