package org.supanthapaul.contour;

public abstract class Scene {

    protected Camera camera;

    public Scene() {

    }

    public void init() {

    }

    public abstract void update(float dt);
}
