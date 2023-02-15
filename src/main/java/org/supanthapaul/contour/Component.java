package org.supanthapaul.contour;

import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    // The game object that uses this component
    // transient properties wont be serialized by gson
    public transient GameObject gameObject = null;

    public void start() {

    }
    public void update(float dt) {

    }

    public void imgui() {
        ImGui.text(this.getClass().getSimpleName());
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields) {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                // do not show transient properties in editor
                if(isTransient) {
                    continue;
                }
                // temporarily change the field to public so that we can access it
                if(isPrivate) {
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class) {
                    int val = (int)value;
                    int[] imInt = {val};
                    if(ImGui.dragInt(name + ": ", imInt)) {
                        field.set(this, imInt[0]);
                    }
                } else if(type == float.class) {
                    float val = (float)value;
                    float[] imFloat = {val};
                    if(ImGui.dragFloat(name + ": ", imFloat)) {
                        field.set(this, imFloat[0]);
                    }
                } else if(type == boolean.class) {
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name + ": ", val)) {
                        val = !val;
                        field.set(this, val);
                    }
                } else if(type == Vector3f.class) {
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name + ": ", imVec)) {
                        val.set(imVec);
                    }
                } else if(type == Vector4f.class) {
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name + ": ", imVec)) {
                        val.set(imVec);
                    }
                }

                // if the field was private, change it back to private
                if(isPrivate) {
                    field.setAccessible(false);
                }
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void SetGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
