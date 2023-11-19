package net.quackimpala7321.json;

import com.google.gson.JsonElement;

public abstract class ConfigElement<T> {
    JsonElement element;

    public ConfigElement(T value) {
        this.setValue(value);
    }

    public abstract T getValue();
    public abstract void setValue(T value);

    public boolean getAsBoolean() {
        return ((BooleanConfigElement) this).getValue();
    }

    public NumberConfigElement getAsNumberElement() {
        return ((NumberConfigElement) this);
    }

    public String getAsString() {
        return ((StringConfigElement) this).getValue();
    }

    public ConfigArray getAsArray() {
        return (ConfigArray) this;
    }
}
