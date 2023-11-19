package net.quackimpala7321.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.quackimpala7321.JsonConfig;

import java.util.HashMap;
import java.util.Map;

import static net.quackimpala7321.JsonConfig.DEFAULT_GSON;

public class ConfigObject extends ConfigElement<JsonObject> implements JsonConvertible{
    private final Map<String, ConfigElement<?>> jsonMap;
    private JsonConfig parent;

    public ConfigObject() {
        this(null);
    }

    public ConfigObject(JsonConfig parent) {
        this(parent, new JsonObject());
    }

    ConfigObject(JsonConfig parent, JsonObject value) {
        super(value);
        this.setParent(parent);
        this.jsonMap = new HashMap<>();
    }

    public void resolveMissingContents(ConfigObject defaultObj) {
        for (Map.Entry<String, ConfigElement<?>> entry : defaultObj.getJsonMap().entrySet()) {
            final ConfigElement<?> curValue = this.getJsonValue(entry.getKey());

            if (curValue == null || entry.getValue().getClass() != curValue.getClass()) {
                this.setJsonValue(entry.getKey(), entry.getValue());
                continue;
            }

            if (curValue instanceof ConfigObject e)
                e.resolveMissingContents((ConfigObject) entry.getValue());
            else if (curValue instanceof ConfigArray e)
                e.resolveMissingContents((ConfigArray) entry.getValue());
        }
    }

    public ConfigElement<?> getJsonValue(String key) {
        return this.jsonMap.getOrDefault(key, null);
    }

    public void setJsonValue(String key, boolean value) {
        final BooleanConfigElement element = new BooleanConfigElement(value);

        if (this.jsonMap.containsKey(key))
            this.jsonMap.replace(key, element);
        else
            this.jsonMap.put(key, element);
    }

    public void setJsonValue(String key, Number value) {
        final NumberConfigElement element = new NumberConfigElement(value);

        if (this.jsonMap.containsKey(key))
            this.jsonMap.replace(key, element);
        else
            this.jsonMap.put(key, element);
    }

    public void setJsonValue(String key, String value) {
        final StringConfigElement element = new StringConfigElement(value);

        if (this.jsonMap.containsKey(key))
            this.jsonMap.replace(key, element);
        else
            this.jsonMap.put(key, element);
    }

    public void setJsonValue(String key, ConfigElement<?> value) {
        if (this.jsonMap.containsKey(key))
            this.jsonMap.replace(key, value);
        else
            this.jsonMap.put(key, value);
    }

    public static ConfigObject fromJsonObject(JsonObject json) {
        final ConfigObject config = new ConfigObject();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getValue() instanceof JsonObject e)
                config.setJsonValue(entry.getKey(), fromJsonObject(e));
            else if (entry.getValue() instanceof JsonArray e)
                config.setJsonValue(entry.getKey(), ConfigArray.fromJsonArray(e));
            else if (entry.getValue() instanceof JsonPrimitive e) {
                if (e.isBoolean())
                    config.setJsonValue(entry.getKey(), e.getAsBoolean());
                else if (e.isNumber())
                    config.setJsonValue(entry.getKey(), e.getAsNumber());
                else if (e.isString())
                    config.setJsonValue(entry.getKey(), e.getAsString());
            }
        }

        return config;
    }

    public void setParent(JsonConfig parent) {
        this.parent = parent;
    }

    @Override
    public JsonObject getValue() {
        final JsonObject json = new JsonObject();

        for (Map.Entry<String, ConfigElement<?>> entry : this.jsonMap.entrySet()) {
            ConfigElement<?> value = entry.getValue();

            if (value instanceof BooleanConfigElement e)
                json.addProperty(entry.getKey(), e.getValue());
            else if (value instanceof NumberConfigElement e)
                json.addProperty(entry.getKey(), e.getValue());
            else if (value instanceof StringConfigElement e)
                json.addProperty(entry.getKey(), e.getValue());
            else if (value.getValue() instanceof JsonElement e)
                json.add(entry.getKey(), e);
        }

        return json;
    }

    @Override
    public void setValue(JsonObject value) {}

    public Map<String, ConfigElement<?>> getJsonMap() {
        return this.jsonMap;
    }

    @Override
    public JsonObject toJsonObject() {
        return this.getValue();
    }

    @Override
    public String toJson() {
        return (this.parent == null ? DEFAULT_GSON : this.parent.getGson())
                .toJson(this.toJsonObject());
    }

    public String toString() {
        return this.toJson();
    }
}
