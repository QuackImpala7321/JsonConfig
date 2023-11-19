package net.quackimpala7321.json;

import com.google.gson.JsonObject;

public interface JsonConvertible {
    JsonObject toJsonObject();
    String toJson();
}
