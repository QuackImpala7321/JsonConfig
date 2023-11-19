package net.quackimpala7321;

import com.google.gson.Gson;
import net.quackimpala7321.json.ConfigElement;
import net.quackimpala7321.json.ConfigObject;

import java.io.File;

public class DynamicConfig extends JsonConfig{
    DynamicConfig(ConfigObject root, ConfigObject defaultRoot, File file, Gson gson, char delimiter) {
        super(root, defaultRoot, file, gson, delimiter);
        this.load();
    }

    @Override
    public void setRoot(ConfigObject root) {
        super.setRoot(root);
        if (this.getDefaultRoot() != null)
            this.save();
    }

    @Override
    public void setValue(String fullPath, ConfigElement<?> value) {
        super.setValue(fullPath, value);
        this.save();
    }

    @Override
    public void resolveMissingContents() {
        super.resolveMissingContents();
        this.save();
    }
}
