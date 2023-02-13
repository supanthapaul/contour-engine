package org.supanthapaul.contour;

public abstract class Component {

    // The game object that uses this component
    public GameObject gameObject = null;

    public void start() {

    }
    public void update(float dt) {

    }

    public void imgui() {

    }

    public void SetGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
