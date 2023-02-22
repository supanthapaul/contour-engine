package org.supanthapaul.components;

import com.google.gson.*;
import org.supanthapaul.components.Component;

import java.lang.reflect.Type;

// Custom serializer and deserializer for Component
// So that it retains its correct inherited class while serializing and deserializing
public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // get the type string of the component class
        String type = jsonObject.get("type").getAsString();
        // get component properties
        JsonElement element = jsonObject.get("properties");
        try {
            // deserialize as the correct component class
            return context.deserialize(element, Class.forName(type));
        }
        catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        // add the type of component as a string so we can deserialize it correctly
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }
}
