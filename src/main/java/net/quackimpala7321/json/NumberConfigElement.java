package net.quackimpala7321.json;

import com.google.gson.JsonPrimitive;

public class NumberConfigElement extends ConfigElement<Number> {
    public NumberConfigElement(Number value) {
        super(value);
    }

    public int getAsInt() {
        return this.getValue().intValue();
    }

    public long getAsLong() {
        return this.getValue().longValue();
    }

    public float getAsFloat() {
        return this.getValue().floatValue();
    }

    public double getAsDouble() {
        return this.getValue().doubleValue();
    }

    public byte getAsByte() {
        return this.getValue().byteValue();
    }

    public short getAsShort() {
        return this.getValue().shortValue();
    }

    @Override
    public Number getValue() {
        return this.element.getAsNumber();
    }

    @Override
    public void setValue(Number value) {
        this.element = new JsonPrimitive(value);
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
