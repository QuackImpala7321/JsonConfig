package net.quackimpala7321.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class ConfigArray extends ConfigElement<JsonArray>{
    private final List<ConfigElement<?>> array;

    public ConfigArray() {
        super(new JsonArray());
        this.array = new ArrayList<>();
    }

    // fill in missing or bad elements
    public void resolveMissingContents(ConfigArray defaultArr) {
        for ( int i = 0; i < defaultArr.array.size(); i++ ) {
            final ConfigElement<?> defaultValue = defaultArr.getElementAt(i);

            if (this.array.size() <= i)
                this.addElement(defaultValue);

            final ConfigElement<?> curValue = this.getElementAt(i);

            if (curValue == null || defaultValue.getClass() != curValue.getClass()) {
                this.setElementAt(i, defaultValue);
                continue;
            }

            if (curValue instanceof ConfigObject e)
                e.resolveMissingContents((ConfigObject) defaultValue);
            else if (curValue instanceof ConfigArray e)
                e.resolveMissingContents((ConfigArray) defaultValue);
        }
    }

    public ConfigElement<?> getElementAt(int i) {
        return this.array.get(i);
    }

    public void addElement(Number value) {
        this.array.add(new NumberConfigElement(value));
    }

    public void addElement(String value) {
        this.array.add(new StringConfigElement(value));
    }

    public void addElement(Boolean value) {
        this.array.add(new BooleanConfigElement(value));
    }

    public void addElement(ConfigElement<?> value) {
        this.array.add(value);
    }

    public void setElementAt(int i, Number value) {
        final NumberConfigElement element = new NumberConfigElement(value);

        this.setElementAt(i, element);
    }

    public void setElementAt(int i, String value) {
        final StringConfigElement element = new StringConfigElement(value);

        this.setElementAt(i, element);
    }

    public void setElementAt(int i, boolean value) {
        final BooleanConfigElement element = new BooleanConfigElement(value);

        this.setElementAt(i, element);
    }

    public void setElementAt(int i, ConfigElement<?> value) {
        if (i >= this.array.size())
            this.addElement(value);
        else
            this.array.set(i, value);
    }

    public void removeElementAt(int i) {
        if (this.array.size() <= i) {
            throw new ArrayIndexOutOfBoundsException(i);
        } else {
            this.array.remove(i);
        }
    }

    public static ConfigArray fromJsonArray(JsonArray array) {
        ConfigArray config = new ConfigArray();

        for (JsonElement entry : array) {
            if (entry instanceof JsonObject e)
                config.addElement(ConfigObject.fromJsonObject(e));
            else if (entry instanceof JsonArray e)
                config.addElement(ConfigArray.fromJsonArray(e));
            else if (entry instanceof JsonPrimitive e) {
                if (e.isBoolean())
                    config.addElement(e.getAsBoolean());
                else if (e.isNumber())
                    config.addElement(e.getAsNumber());
                else if (e.isString())
                    config.addElement(e.getAsString());
            }
        }

        return config;
    }

    @Override
    public JsonArray getValue() {
        final JsonArray array = new JsonArray();

        for (ConfigElement<?> element : this.array) {
            if (element instanceof BooleanConfigElement e)
                array.add(e.getValue());
            else if (element instanceof NumberConfigElement e)
                array.add(e.getValue());
            else if (element instanceof StringConfigElement e)
                array.add(e.getValue());
            else if (element.getValue() instanceof JsonElement e)
                array.add(e);
        }

        return array;
    }

    @Override
    public void setValue(JsonArray value) { }

    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
