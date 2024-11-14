package org.frags.changePassBungee;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public final class ChangePassBungee extends Plugin {

    private File file;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfig();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChangePass("passchange", this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Configuration getConfig() {
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reloadConfig() {
        if (this.file == null)
            this.file = new File(this.getDataFolder(), "config.yml");

        if (!this.file.exists()) {
            try {
                file.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(file)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
    }

    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        file = configFile;
    }
}
