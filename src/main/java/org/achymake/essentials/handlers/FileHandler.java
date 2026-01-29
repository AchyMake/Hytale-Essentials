package org.achymake.essentials.handlers;

import com.google.gson.Gson;
import org.achymake.essentials.files.EssentialsConfig;
import org.achymake.essentials.files.Vanished;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandler {
    public File getFile(String path) {
        return new File(path);
    }
    public File getFolder() {
        return getFile("mods/Essentials");
    }
    public void createFolder(String path) {
        var file = getFile(path);
        if (file.exists())return;
        file.mkdir();
    }
    public void createFile(String path, Object object) {
        try (var writer = getFileWriter(path)) {
            getGson().toJson(object, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteFile(File file) {
        if (!file.exists())return;
        file.delete();
    }
    public FileReader getFileReader(String path) throws FileNotFoundException {
        return new FileReader(path);
    }
    public FileWriter getFileWriter(String path) throws IOException {
        return new FileWriter(path);
    }
    public Gson getGson() {
        return new Gson();
    }
    private void createConfig() {
        if (getFile("mods/Essentials/config.json").exists())return;
        var maxHomes = new HashMap<String, Integer>();
        maxHomes.put("default", 3);
        maxHomes.put("vip", 6);
        var protection = new ArrayList<String>();
        protection.add("spawn");
        protection.add("pvp");
        var censored = new ArrayList<String>();
        censored.add("fuck");
        censored.add("pussy");
        censored.add("nigger");
        censored.add("nigga");
        try (var writer = getFileWriter("mods/Essentials/config.json")) {
            getGson().toJson(new EssentialsConfig("default", censored, "#,##0", "$", true, maxHomes), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void createVanished() {
        if (getFile("mods/Essentials/vanished.json").exists())return;
        try (var writer = getFileWriter("mods/Essentials/vanished.json")) {
            getGson().toJson(new Vanished(new ArrayList<>()), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setup() {
        createFolder("mods/Essentials");
        createFolder("mods/Essentials/userdata");
        createConfig();
        createVanished();
    }
}