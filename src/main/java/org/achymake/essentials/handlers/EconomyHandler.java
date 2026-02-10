package org.achymake.essentials.handlers;

import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.universe.Universe;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.EssentialsConfig;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class EconomyHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    public double get(UUID uuid) {
        var loaded = Universe.get().getPlayerStorage().load(uuid);
        try {
            var account = loaded.get().getComponent(getInstance().getAccountComponentType());
            if (account != null) {
                return account.getBalance();
            } else return 0;
        } catch (InterruptedException | ExecutionException e) {
            return 0;
        }
    }
    public boolean has(UUID uuid, double value) {
        return get(uuid) >= value;
    }
    public List<Map.Entry<String, Double>> getTopAccounts() {
        var accounts = new HashMap<String, Double>();
        try {
            Universe.get().getPlayerStorage().getPlayers().forEach(uuid -> {
                var loaded = Universe.get().getPlayerStorage().load(uuid);
                try {
                    var holder = loaded.get();
                    var namePlate = holder.getComponent(Nameplate.getComponentType());
                    var account = holder.getComponent(getInstance().getAccountComponentType());
                    if (account != null && namePlate != null) {
                        var balance = account.getBalance();
                        if (balance > 0) {
                            accounts.put(namePlate.getText(), balance);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var listed = new ArrayList<>(accounts.entrySet());
        listed.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        return listed.stream().limit(10).toList();
    }
    public String format(double value) {
        try (var reader = getFileHandler().getFileReader("mods/Essentials/config.json")) {
            var json = getFileHandler().getGson().fromJson(reader, EssentialsConfig.class);
            var formatted = new DecimalFormat(json.Format()).format(value);
            if (json.CurrencyPrefix()) {
                return json.Currency() + formatted;
            } else return formatted + json.Currency();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}