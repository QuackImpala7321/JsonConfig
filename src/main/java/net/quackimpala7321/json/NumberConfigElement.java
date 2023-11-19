package net.quackimpala7321.json;

import com.google.gson.JsonPrimitive;

public class NumberConfigElement extends ConfigElement<Number> {
    public NumberConfigElement(Number value) {
        super(value);
    }

    @Override
    public Number getValue() {
        return this.element.getAsNumber();
    }

    @Override
    public void setValue(Number value) {
        this.element = new JsonPrimitive(value);
    }
}
