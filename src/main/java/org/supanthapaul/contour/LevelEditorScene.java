package org.supanthapaul.contour;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene;
    private float timeToChangeScene = 2f;

    public LevelEditorScene() {
        System.out.println("Inside Level Editor");
    }

    @Override
    public void update(float dt) {
        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if(changingScene && timeToChangeScene > 0f) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        }
        else if(changingScene) {
            Window.changeScene(1);
        }
    }
}
