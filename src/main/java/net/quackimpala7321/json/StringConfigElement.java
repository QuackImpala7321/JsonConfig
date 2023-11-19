package net.quackimpala7321.json;

import com.google.gson.JsonPrimitive;

public class StringConfigElement extends ConfigElement<String> {
    public StringConfigElement(String value) {
        super(value);
    }

    @Override
    public String getValue() {
        return this.element.getAsString();
    }

    @Override
    public void setValue(String value) {
        this.element = new JsonPrimitive(value);
    }
}
