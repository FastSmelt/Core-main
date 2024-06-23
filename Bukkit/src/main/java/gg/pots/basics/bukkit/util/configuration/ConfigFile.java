package gg.pots.basics.bukkit.util.configuration;


import gg.pots.basics.bukkit.util.logger.LoggerUtility;
import lombok.Data;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Data
public class ConfigFile {

    private final File configFile;
    private YamlConfiguration configuration;

    /**
     * Constructor to create a new {@link ConfigFile}
     *
     * @param plugin The {@link JavaPlugin} instance.
     * @param fileName The name of the file to create.
     */

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

    /**
     * Save a {@link ConfigFile} to disk.
     */

    public void saveFile() {
        try {
            this.configuration.save(this.configFile);
        } catch (Exception exception) {
            LoggerUtility.sendMessage("&7[&9&lConfig&7] &cAn error has occurred while saving the file, " + this.configuration);
        }
    }

    /**
     * Reload a {@link ConfigFile} from disk.
     */

    public void reloadFile(boolean save) {
        if (save) {
            this.saveFile();
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @param value The value of the file to get.
     */

    public void set(String path, Object value) {
        this.configuration.set(path, value);
        this.saveFile();
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @return The value of the file to get.
     */

    public String getString(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getString(path);
        }

        return null;
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @return The value of the file to get.
     */

    public String[] getStringList(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getStringList(path).toArray(new String[0]);
        }

        return null;
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @return The value of the file to get.
     */

    public int getInt(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }

        return 0;
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @return The value of the file to get.
     */

    public boolean getBoolean(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getBoolean(path);
        }

        return false;
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @return The value of the file to get.
     */

    public double getDouble(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }

        return 0;
    }

    /**
     * Get a {@link ConfigFile} from a {@link File}.
     *
     * @param path The path of the file to get.
     * @return The value of the file to get.
     */

    public long getLong(String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getLong(path);
        }

        return 0;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public File getConfigFile() {
        return configFile;
    }
}