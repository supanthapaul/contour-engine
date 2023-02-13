package org.supanthapaul.contour;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int width, height;
    String title;
    long glfwWindow;
    public float r, g, b;

    private static Window window = null;
    private static Scene currentScene = null;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private ImGuiLayer imGuiLayer;

    private String glslVersion = null;

    private Window(ImGuiLayer layer) {
        this.width = 1280;
        this.height = 720;
        this.title = "Contour Engine";
        r = 1;
        g = 1;
        b = 1;
        this.imGuiLayer = layer;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene: " + newScene;
                break;
        }
    }

    public static Window get() {
        if(Window.window == null) {
            Window.window = new Window(new ImGuiLayer());
        }

        return Window.window;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion());

        init();
        initImGui();
        imGuiGlfw.init(glfwWindow, true);
        imGuiGl3.init(glslVersion);
        loop();

        // window destroy cleanup
        destroy();
    }

    public void init() {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // init glfw
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // set glsl and gl version
        // https://en.wikipedia.org/wiki/OpenGL_Shading_Language
        glslVersion = "#version 330 core";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // configure glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // setup input callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // make the opengl context current
        glfwMakeContextCurrent(glfwWindow);
        // enable vsync
        glfwSwapInterval(1);

        // make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // alpha blending
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // start first scene
        Window.changeScene(0);
    }

    private void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();

        // retain imgui states bw sessions (eg: window positions)
        io.setIniFilename("imgui.ini");

        // Fonts configuration
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        // Fonts merge example
        fontConfig.setPixelSnapH(true);


        // Fonts from file/memory example
        // We can add new fonts from the file system
        fontAtlas.addFontFromFileTTF("assets/fonts/segoeui.ttf", 32, fontConfig);
        fontConfig.destroy(); // After all fonts were added we don't need this config more

        // Use freetype instead of stb_truetype to build a fonts texture
        //ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

        // enable ImGui viewports
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
    }

    public void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime = (float) glfwGetTime();
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)) {
            // poll events
            glfwPollEvents();

            glClearColor(r, g, b, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            // update current scene
            if(dt >= 0) {
                currentScene.update(dt);
            }

            // imgui new frame
            imGuiGlfw.newFrame();
            ImGui.newFrame();

            // Call the current scene's imgui functions
            currentScene.sceneImgui();
            // call all the imgui functions
            imGuiLayer.imgui();

            // render imgui
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            // end of frame stuff
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }
            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            // calculate delta time
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void destroy() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        // free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
