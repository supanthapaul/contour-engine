package org.supanthapaul.components;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.supanthapaul.contour.Component;
import org.supanthapaul.contour.Transform;
import org.supanthapaul.renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();

    // transient properties wont be serialized by gson
    private transient Transform lastTransform;
    // The sprite is "dirty" if it needs to be redrawn by the GPU
    private transient boolean isDirty = true;

//    public SpriteRenderer(Vector4f color) {
//        this.color = color;
//        this.sprite = new Sprite(null);
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite) {
//        this.sprite = sprite;
//        this.color = new Vector4f(1, 1, 1, 1);
//        this.isDirty = true;
//    }
    @Override
    public void start() {
        this.lastTransform = this.gameObject.transform.copy();
    }
    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(lastTransform);
            // the sprite is dirty now
            this.isDirty = true;
        }
    }

    @Override
    public void imgui() {
        float[] col = {this.color.x, this.color.y, this.color.z, this.color.w};
        if(ImGui.colorPicker4("Color picker", col)) {
            this.setColor(new Vector4f(col));
        }
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        // the sprite is dirty now
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        if(!this.color.equals(color)) {
            this.color.set(color);
            // the sprite is dirty now
            this.isDirty = true;
        }
    }
}
