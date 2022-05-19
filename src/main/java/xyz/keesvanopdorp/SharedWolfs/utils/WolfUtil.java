package xyz.keesvanopdorp.SharedWolfs.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class WolfUtil {
    private String basePath;

    public WolfUtil(String basePath) {
        this.basePath = basePath;
    }

    public File getWolfFile(UUID uuid) throws IOException {
        String filePath = this.getFilePath(uuid);
        File wolfFile = new File(filePath);
        if(!wolfFile.exists()) {
            wolfFile.createNewFile();
        }
        return wolfFile;
    }

    public YamlConfiguration getWolfConfiguration(UUID uuid) throws IOException, InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        File wolfFile = this.getWolfFile(uuid);
        configuration.load(wolfFile);
        return configuration;
    }

    public void writeToWolfFile(UUID uuid, YamlConfiguration configuration) throws IOException {
        String filePath = this.getFilePath(uuid);
        File wolfFile = new File(filePath);
        if(!wolfFile.exists()) {
            wolfFile.createNewFile();
        }
        configuration.save(wolfFile);
    }

    public void deleteWolfFile(UUID uuid){
        String filePath = this.getFilePath(uuid);
        File wolfFile = new File(filePath);
        if(!wolfFile.exists()) {
            return;
        }
        wolfFile.delete();
    }

    public String getFilePath(UUID uuid) {
        return this.basePath + "/" + uuid + ".yml";
    }
}
