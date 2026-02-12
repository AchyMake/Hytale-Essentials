package org.achymake.essentials.handlers;

import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.EssentialsConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PlayerHandler {
    private final ArrayList<PlayerRef> PVP = new ArrayList<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public int getMaxHomes(PlayerRef playerRef) {
        var listed = new ArrayList<Integer>();
        var uuid = playerRef.getUuid();
        try (var reader = getFileHandler().getFileReader("mods/Essentials/config.json")) {
            var json = getFileHandler().getGson().fromJson(reader, EssentialsConfig.class);
            var maxHomes = json.MaxHomes();
            for (var string : maxHomes.keySet()) {
                if (PermissionsModule.get().hasPermission(uuid, "essentials.command.sethome." + string)) {
                    listed.add(maxHomes.get(string));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listed.sort(Integer::compareTo);
        return listed.getLast();
    }
    public void setPVP(PlayerRef playerRef, boolean value) {
        if (value) {
            getPVP().add(playerRef);
        } else getPVP().remove(playerRef);
    }
    public boolean isPVP(PlayerRef playerRef) {
        return getPVP().contains(playerRef);
    }
    public String getUsername(UUID uuid) {
        var loaded = getUniverseHandler().getPlayerStorage().load(uuid);
        try {
            var holder = loaded.get();
            var namePlate = holder.getComponent(Nameplate.getComponentType());
            if (namePlate != null) {
                return namePlate.getText();
            } else return null;
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }
    public ArrayList<PlayerRef> getPVP() {
        return PVP;
    }
}