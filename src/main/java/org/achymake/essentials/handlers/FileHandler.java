package org.achymake.essentials.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.achymake.essentials.files.EssentialsConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class FileHandler {
    public File getFile(String path) {
        return new File(path);
    }
    public FileReader getFileReader(String path) throws FileNotFoundException {
        return new FileReader(path);
    }
    public FileWriter getFileWriter(String path) throws IOException {
        return new FileWriter(path);
    }
    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
    public File getFolder() {
        return getFile("mods/Essentials");
    }
    public void createFolder(String path) {
        var file = getFile(path);
        if (file.exists())return;
        file.mkdir();
    }
    public void deleteFile(File file) {
        if (!file.exists())return;
        file.delete();
    }
    private void createConfig() {
        if (getFile("mods/Essentials/config.json").exists())return;
        var maxHomes = new HashMap<String, Integer>();
        maxHomes.put("default", 3);
        maxHomes.put("vip", 6);
        var censored = new ArrayList<String>();
        censored.add("fuck");
        censored.add("pussy");
        censored.add("nigger");
        censored.add("nigga");
        try (var writer = getFileWriter("mods/Essentials/config.json")) {
            getGson().toJson(new EssentialsConfig("default", "https://store.yourStore.org/", censored, "#,##0", "$", true, maxHomes), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setup() {
        createFolder("mods/Essentials");
        createConfig();
    }
    public String getStore() {
        try (var reader = getFileReader("mods/Essentials/config.json")) {
            var json = getGson().fromJson(reader, EssentialsConfig.class);
            return json.Store();
        } catch (IOException e) {
            return null;
        }
    }
    public String format(double value) {
        try (var reader = getFileReader("mods/Essentials/config.json")) {
            var json = getGson().fromJson(reader, EssentialsConfig.class);
            var formatted = new DecimalFormat(json.Format()).format(value);
            if (json.CurrencyPrefix()) {
                return json.Currency() + formatted;
            } else return formatted + json.Currency();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}