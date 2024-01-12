package net.quackimpala7321;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.quackimpala7321.json.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class JsonConfig implements JsonConvertible {
    public static final Gson DEFAULT_GSON = new GsonBuilder().create();

    private final Gson gson;
    private final File file;
    private final char delimiter;

    private ConfigObject root;
    private final ConfigObject defaultRoot;

    JsonConfig(ConfigObject root, ConfigObject defaultRoot, File file, Gson gson, char delimiter) {
        this.setRoot(root);
        this.defaultRoot = defaultRoot;
        this.file = file;
        this.gson = gson;
        this.delimiter = delimiter;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(this.file)) {
            writer.write(this.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        try (FileReader reader = new FileReader(this.file)) {
            this.setRoot( ConfigObject.fromJsonObject(JsonParser.parseReader(reader).getAsJsonObject()) );
            reader.close();

            this.resolveMissingContents();
        } catch (IOException e) {
            this.setRoot(this.defaultRoot);
        }
    }

    // same thing as the last one
    public void resolveMissingContents() {
        this.root.resolveMissingContents(this.defaultRoot);
    }

    public ConfigObject getRoot() {
        return this.root;
    }

    public ConfigObject getDefaultRoot() {
        return this.defaultRoot;
    }

    public void setRoot(ConfigObject root) {
        this.root.setParent(null);
        this.root = root;
        this.root.setParent(this);
    }

    public boolean getBoolean(String path) {
        return ((BooleanConfigElement) this.getValue(path)).getValue();
    }

    public Number getNumber(String path) {
        return ((NumberConfigElement) this.getValue(path)).getValue();
    }

    public int getInt(String path) {
        return this.getNumber(path).intValue();
    }

    public long getLong(String path) {
        return this.getNumber(path).longValue();
    }

    public float getFloat(String path) {
        return this.getNumber(path).floatValue();
    }

    public double getDouble(String path) {
        return this.getNumber(path).doubleValue();
    }

    public byte getByte(String path) {
        return this.getNumber(path).byteValue();
    }

    public short getShort(String path) {
        return this.getNumber(path).shortValue();
    }

    public String getString(String path) {
        return ((StringConfigElement) this.getValue(path)).getValue();
    }

    public ConfigElement<?> getValue(String path) {
        final String[] paths = path.split(String.format("\\%c", this.delimiter));

        ConfigElement<?> at = this.root;
        for (String p : paths) {
            if (at instanceof ConfigObject e)
                at = e.getJsonValue(p);
            else if (at instanceof ConfigArray e)
                at = e.getElementAt(Integer.parseInt(p));
            else
                return at;
        }

        return at;
    }

    public Gson getGson() {
        return this.gson;
    }

    public void setValue(String fullPath, boolean value) {
        this.setValue(fullPath, new BooleanConfigElement(value));
    }

    public void setValue(String fullPath, Number value) {
        this.setValue(fullPath, new NumberConfigElement(value));
    }

    public void setValue(String fullPath, String value) {
        this.setValue(fullPath, new StringConfigElement(value));
    }

    public void setValue(String fullPath, ConfigElement<?> value) {
        final int splitIdx = fullPath.lastIndexOf(this.delimiter);
        if (splitIdx < 0) {
            this.getRoot().setJsonValue(fullPath, value);
            return;
        }

        final String beforePath = fullPath.substring(0, splitIdx);
        final String afterPath = fullPath.substring(splitIdx + 1);

        final ConfigElement<?> element = this.getValue(beforePath);
        if (element instanceof ConfigObject e)
            e.setJsonValue(afterPath, value);
        else if (element instanceof ConfigArray e)
            e.setElementAt(Integer.parseInt(afterPath), value);
    }

    @Override
    public JsonObject toJsonObject() {
        return this.root.toJsonObject();
    }

    @Override
    public String toJson() {
        return this.root.toJson();
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    public static class Builder {
        private ConfigObject root;
        private ConfigObject defaultRoot;
        private String path;
        private String name;
        private Gson gson;
        private char delimiter;

        public Builder() {
            this.setRoot(new ConfigObject());
            this.setDefault(new ConfigObject());
            this.setName("config.json");
            this.setGson(DEFAULT_GSON);
            this.setDelimiter('.');
        }

        public static Builder create() {
            return new Builder();
        }

        public JsonConfig build() {
            return new JsonConfig(
                    this.root,
                    this.defaultRoot,
                    Path.of(this.path, this.name).toFile(),
                    this.gson,
                    this.delimiter
            );
        }

        public DynamicConfig buildDynamic() {
            return new DynamicConfig(
                    this.root,
                    this.defaultRoot,
                    Path.of(this.path, this.name).toFile(),
                    this.gson,
                    this.delimiter
            );
        }

        public Builder setRoot(ConfigObject root) {
            this.root = root;
            return this;
        }

        public Builder setDefault(ConfigObject defaultRoot) {
            this.defaultRoot = defaultRoot;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        public Builder setDelimiter(char delimiter) {
            this.delimiter = delimiter;
            return this;
        }
    }
}
