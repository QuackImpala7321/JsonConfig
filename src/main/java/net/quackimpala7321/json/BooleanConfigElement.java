package net.quackimpala7321.json;

import com.google.gson.JsonPrimitive;

public class BooleanConfigElement extends ConfigElement<Boolean>{
    public BooleanConfigElement(Boolean value) {
        super(value);
    }

    @Override
    public Boolean getValue() {
        return this.element.getAsBoolean();
    }

    @Override
    public void setValue(Boolean value) {
        this.element = new JsonPrimitive(value);
    }
}
