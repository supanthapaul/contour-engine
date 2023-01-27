package org.supanthapaul.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.supanthapaul.contour.Component;

public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }
    private boolean firstTime = false;
    @Override
    public void start() {

    }
    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return color;
    }
}
