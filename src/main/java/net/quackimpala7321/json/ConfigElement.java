package net.quackimpala7321.json;

import com.google.gson.JsonElement;

public abstract class ConfigElement<T> {
    JsonElement element;

    public ConfigElement(T value) {
        this.setValue(value);
    }

    abstract T getValue();
    abstract void setValue(T value);
}
