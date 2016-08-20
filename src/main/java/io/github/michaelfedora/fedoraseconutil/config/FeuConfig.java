package io.github.michaelfedora.fedoraseconutil.config;

import io.github.michaelfedora.fedoraseconutil.PluginInfo;
import io.github.michaelfedora.fedoraseconutil.FedorasEconUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Michael on 3/22/2016.
 */
public class FeuConfig implements FeuConfigurable {

    public static final FeuConfig instance = new FeuConfig();

    private final ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode root;

    public static String initValue = null;

    private String value;

    private FeuConfig() {

        this.value = initValue;

        Path path = FedorasEconUtil.getSharedConfigDir().resolve(PluginInfo.DATA_ROOT + ".cfg");
        this.loader = HoconConfigurationLoader.builder().setPath(path).build();
    }

    public static void initialize() {
        instance.value = initValue;

        instance.load();
        instance.setupValues();
        instance.save();
    }

    public String getValue() { return this.value; }

    public void setValue(String value) {
        this.value = value;
        this.getNode("value").setValue(this.value);
    }

    private void setupValues() {
        if(this.getNode("value").getValue() == null)
            this.getNode("value").setComment("The value").setValue(this.value);
        else
            this.value = this.getNode("value").getString();
    }

    @Override
    public void load() {
        try {
            this.root = loader.load();
        } catch(IOException e) {
            FedorasEconUtil.getLogger().error("Could not load configuration!", e);
        }
    }

    @Override
    public void save() {
        try {
            this.loader.save(root);
        } catch(IOException e) {
            FedorasEconUtil.getLogger().error("Could not save configuration!", e);
        }
    }

    @Override
    public CommentedConfigurationNode root() { return this.root; }

    @Override
    public CommentedConfigurationNode getNode(Object... path) { return this.root.getNode(path); }
}
