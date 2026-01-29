package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.Account;
import org.achymake.essentials.files.EssentialsConfig;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class EconomyHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    public int get(String uuidString) {
        try (var writer = getFileHandler().getFileReader("mods/Essentials/userdata/" + uuidString + "/account.json")) {
            var json = getFileHandler().getGson().fromJson(writer, Account.class);
            return json.Coins();
        } catch (IOException e) {
            return 0;
        }
    }
    public int get(UUID uuid) {
        try (var writer = getFileHandler().getFileReader("mods/Essentials/userdata/" + uuid + "/account.json")) {
            var json = getFileHandler().getGson().fromJson(writer, Account.class);
            return json.Coins();
        } catch (IOException e) {
            return 0;
        }
    }
    public boolean has(UUID uuid, int value) {
        return get(uuid) >= value;
    }
    public boolean has(String uuidString, int value) {
        return get(uuidString) >= value;
    }
    public boolean add(UUID uuid, int value) {
        var result = get(uuid) + value;
        try (var writer = getFileHandler().getFileWriter("mods/Essentials/userdata/" + uuid + "/account.json")) {
            getFileHandler().getGson().toJson(new Account(result), writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean remove(UUID uuid, int value) {
        var result = get(uuid) - value;
        try (var writer = getFileHandler().getFileWriter("mods/Essentials/userdata/" + uuid + "/account.json")) {
            getFileHandler().getGson().toJson(new Account(result), writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean set(UUID uuid, int value) {
        try (var writer = getFileHandler().getFileWriter("mods/Essentials/userdata/" + uuid + "/account.json")) {
            getFileHandler().getGson().toJson(new Account(value), writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public List<Map.Entry<String, Integer>> getTopAccounts() {
        var accounts = new HashMap<String, Integer>();
        for (var uuidString : getPlayerHandler().getOffliners()) {
            if (has(uuidString, 1)) {
                accounts.put(getPlayerHandler().getUsername(uuidString), get(uuidString));
            }
        }
        var listed = new ArrayList<>(accounts.entrySet());
        listed.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        return listed.stream().limit(10).toList();
    }
    public String format(int value) {
        try (var writer = getFileHandler().getFileReader("mods/Essentials/config.json")) {
            var json = getFileHandler().getGson().fromJson(writer, EssentialsConfig.class);
            var formatted = new DecimalFormat(json.Format()).format(value);
            if (json.CurrencyPrefix()) {
                return json.Currency() + formatted;
            } else return formatted + json.Currency();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}