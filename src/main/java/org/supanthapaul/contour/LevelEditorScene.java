package org.supanthapaul.contour;

import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.supanthapaul.components.Rigidbody;
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
    GameObject obj1;
    Spritesheet characterSpritesheet, envSpritesheet;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        envSpritesheet = AssetPool.getSpritesheet("assets/images/Tilemap/tiles_packed.png");
        characterSpritesheet = AssetPool.getSpritesheet("assets/images/Tilemap/characters_packed.png");

        if(levelLoaded) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        obj1 = new GameObject("1",
                new Transform(new Vector2f(150, 100), new Vector2f(256, 256)), 0);
        SpriteRenderer spriteRenderer1 = new SpriteRenderer();
        spriteRenderer1.setSprite(characterSpritesheet.getSprite(0));
        obj1.addComponent(spriteRenderer1);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), -1);
        SpriteRenderer spriteRenderer2 = new SpriteRenderer();
        spriteRenderer2.setSprite(characterSpritesheet.getSprite(2));
        obj2.addComponent(spriteRenderer2);
        this.addGameObjectToScene(obj2);

//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(Component.class, new ComponentDeserializer())
//                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
//                .create();
//        String objJson = gson.toJson(obj1);
//        System.out.println(objJson);
//        GameObject go = gson.fromJson(objJson, GameObject.class);
//        go.transform.position.x += 400;
//        this.addGameObjectToScene(go);

    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/Tilemap/characters_packed.png",
                new Spritesheet(AssetPool.getTexture("assets/images/Tilemap/characters_packed.png"),
                        24, 24, 27, 0));
        AssetPool.addSpritesheet("assets/images/Tilemap/tiles_packed.png",
                new Spritesheet(AssetPool.getTexture("assets/images/Tilemap/tiles_packed.png"),
                        18, 18, 180, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.3f;
    private float spriteFlipTimeLeft = 0f;

    @Override
    public void update(float dt) {
        //System.out.println(1/dt);
//        spriteFlipTimeLeft -= dt;
//        if(spriteFlipTimeLeft <= 0f) {
//            spriteFlipTimeLeft = spriteFlipTime;
//            spriteIndex++;
//            if(spriteIndex > 1) {
//                spriteIndex = 0;
//            }
//            obj1.getComponent(SpriteRenderer.class).setSprite(spritesheet.getSprite(spriteIndex));
//        }
        //obj1.transform.position.x += 10f * dt;

        MouseListener.getOrthoY();
        // update game objects
        for(GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Level Editor");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < envSpritesheet.size(); i++) {
            Sprite sprite = envSpritesheet.getSprite(i);
            float spriteWidth = sprite.getWidth() * 3;
            float spriteHeight = sprite.getHeight() * 3;
            int texId = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            // custom id since imgui uses tex id for image button and using a spritesheet
            //  will result in all buttons to have the same id (i.e button clicks wont register properly)
            ImGui.pushID(i);
            // image button
            if(ImGui.imageButton(texId, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                System.out.println("Button " + i);
            }
            ImGui.popID();

            // Calculate line break for next button
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < envSpritesheet.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
