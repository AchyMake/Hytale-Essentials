package org.achymake.essentials.handlers;

import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.components.Account;
import org.achymake.essentials.files.EssentialsConfig;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class EconomyHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public double get(PlayerRef playerRef) {
        var ref = playerRef.getReference();
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            var account = store.getComponent(ref, getInstance().getAccountComponentType());
            if (account != null) {
                return account.get();
            } else return 0;
        } else return 0;
    }
    public boolean has(PlayerRef playerRef, double value) {
        var ref = playerRef.getReference();
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            var account = store.getComponent(ref, getInstance().getAccountComponentType());
            if (account != null) {
                return account.get() >= value;
            } else return false;
        } else return false;
    }
    public void add(PlayerRef playerRef, double value) {
        var ref = playerRef.getReference();
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            var world = store.getExternalData().getWorld();
            world.execute(() -> {
                var account = store.getComponent(ref, getInstance().getAccountComponentType());
                if (account != null) {
                    account.add(value);
                }
            });
        }
    }
    public void remove(PlayerRef playerRef, double value) {
        var ref = playerRef.getReference();
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            var world = store.getExternalData().getWorld();
            world.execute(() -> {
                var account = store.getComponent(ref, getInstance().getAccountComponentType());
                if (account != null) {
                    account.remove(value);
                }
            });
        }
    }
    public void set(PlayerRef playerRef, double value) {
        var ref = playerRef.getReference();
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            var world = store.getExternalData().getWorld();
            world.execute(() -> {
                var account = store.getComponent(ref, getInstance().getAccountComponentType());
                if (account != null) {
                    account.set(value);
                }
            });
        }
    }
    public double get(UUID uuid) {
        var loaded = getUniverseHandler().getPlayerStorage().load(uuid);
        try {
            var holder = loaded.get();
            var account = holder.getComponent(getInstance().getAccountComponentType());
            if (account != null) {
                return account.get();
            } else return 0;
        } catch (InterruptedException | ExecutionException e) {
            return 0;
        }
    }
    public boolean has(UUID uuid, double value) {
        return get(uuid) >= value;
    }
    public boolean add(UUID uuid, double amount) {
        var loaded = getUniverseHandler().getPlayerStorage().load(uuid);
        try {
            var holder = loaded.get();
            var account = holder.getComponent(getInstance().getAccountComponentType());
            if (account != null) {
                account.add(amount);
                return true;
            } else return false;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
    public boolean remove(UUID uuid, double amount) {
        var loaded = getUniverseHandler().getPlayerStorage().load(uuid);
        try {
            var holder = loaded.get();
            var account = holder.getComponent(getInstance().getAccountComponentType());
            if (account != null) {
                account.remove(amount);
                return true;
            } else return false;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
    public boolean set(UUID uuid, double amount) {
        var loaded = getUniverseHandler().getPlayerStorage().load(uuid);
        try {
            var holder = loaded.get();
            var account = holder.getComponent(getInstance().getAccountComponentType());
            if (account != null) {
                account.set(amount);
                return true;
            } else return false;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }
    public List<Map.Entry<String, Double>> getTopAccounts() {
        var accounts = new HashMap<String, Double>();
        var playerStorage = getUniverseHandler().getPlayerStorage();
        try {
            playerStorage.getPlayers().forEach(uuid -> {
                try {
                    var loaded = playerStorage.load(uuid);
                    var holder = loaded.get();
                    var namePlate = holder.getComponent(Nameplate.getComponentType());
                    var account = holder.getComponent(getInstance().getAccountComponentType());
                    if (account != null && namePlate != null) {
                        var balance = account.get();
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
            return null;
        }
    }
}