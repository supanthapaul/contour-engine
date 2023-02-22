package org.supanthapaul.contour;

import com.google.gson.*;
import org.supanthapaul.components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // get game object properties
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.get("components").getAsJsonArray();
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        GameObject go = new GameObject(name, transform, zIndex);
        for(JsonElement el : components) {
            // this will deserialize using the custom ComponentDeserializer
            Component c = context.deserialize(el, Component.class);
            go.addComponent(c);
        }
        return go;
    }
}
