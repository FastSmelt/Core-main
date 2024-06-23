package gg.pots.basics.core.util;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigFile {
    private final File configFile;
    private YamlConfiguration configuration;

    public ConfigFile(JavaPlugin plugin, String fileName) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        this.configFile = new File(plugin.getDataFolder(), fileName);
        if (!this.configFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveFile() {
        try {
            this.configuration.save(this.configFile);
        } catch (Exception var2) {
        }

    }

    public void reloadFile(boolean save) {
        if (save) {
            this.saveFile();
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void set(String path, Object value) {
        this.configuration.set(path, value);
        this.saveFile();
    }

    public String getString(String path) {
        return this.configuration.contains(path) ? this.configuration.getString(path) : null;
    }

    public String[] getStringList(String path) {
        return this.configuration.contains(path) ? (String[])this.configuration.getStringList(path).toArray(new String[0]) : null;
    }

    public int getInt(String path) {
        return this.configuration.contains(path) ? this.configuration.getInt(path) : 0;
    }

    public boolean getBoolean(String path) {
        return this.configuration.contains(path) ? this.configuration.getBoolean(path) : false;
    }

    public double getDouble(String path) {
        return this.configuration.contains(path) ? this.configuration.getDouble(path) : 0.0;
    }

    public long getLong(String path) {
        return this.configuration.contains(path) ? this.configuration.getLong(path) : 0L;
    }

    public YamlConfiguration getConfiguration() {
        return this.configuration;
    }

    public File getConfigFile() {
        return this.configFile;
    }

    public void setConfiguration(final YamlConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ConfigFile)) {
            return false;
        } else {
            ConfigFile other = (ConfigFile)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$configFile = this.getConfigFile();
                Object other$configFile = other.getConfigFile();
                if (this$configFile == null) {
                    if (other$configFile != null) {
                        return false;
                    }
                } else if (!this$configFile.equals(other$configFile)) {
                    return false;
                }

                Object this$configuration = this.getConfiguration();
                Object other$configuration = other.getConfiguration();
                if (this$configuration == null) {
                    if (other$configuration != null) {
                        return false;
                    }
                } else if (!this$configuration.equals(other$configuration)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ConfigFile;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $configFile = this.getConfigFile();
        result = result * 59 + ($configFile == null ? 43 : $configFile.hashCode());
        Object $configuration = this.getConfiguration();
        result = result * 59 + ($configuration == null ? 43 : $configuration.hashCode());
        return result;
    }

    public String toString() {
        return "ConfigFile(configFile=" + this.getConfigFile() + ", configuration=" + this.getConfiguration() + ")";
    }
}
