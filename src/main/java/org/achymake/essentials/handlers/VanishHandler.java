package org.achymake.essentials.handlers;

import com.hypixel.hytale.server.core.Options;
import com.hypixel.hytale.server.core.entity.entities.player.HiddenPlayersManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.Vanished;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VanishHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public void setVanish(PlayerRef playerRef, boolean value) {
        var uuid = playerRef.getUuid();
        getUniverseHandler().getWorlds().forEach(world -> {
            for (var refs : world.getPlayerRefs()) {
                if (refs != playerRef) {
                    var hiddenPlayersManager = refs.getHiddenPlayersManager();
                    if (value) {
                        hiddenPlayersManager.showPlayer(uuid);
                    } else hiddenPlayersManager.hidePlayer(uuid);
                }
            }
        });
        if (value) {
            addListed(playerRef);
        } else removeListed(playerRef);
    }
    public boolean isVanished(PlayerRef playerRef) {
        var uuidString = playerRef.getUuid().toString();
        return listed().contains(uuidString);
    }
    public void addListed(PlayerRef playerRef) {
        var listed = listed();
        var uuidString = playerRef.getUuid().toString();
        if (!listed.contains(uuidString)) {
            listed.add(uuidString);
            try (var writer = getFileHandler().getFileWriter("mods/Essentials/vanished.json")) {
                getFileHandler().getGson().toJson(new Vanished(listed), writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void removeListed(PlayerRef playerRef) {
        var listed = listed();
        var uuidString = playerRef.getUuid().toString();
        if (listed.contains(uuidString)) {
            listed.remove(uuidString);
            try (var writer = getFileHandler().getFileWriter("mods/Essentials/vanished.json")) {
                getFileHandler().getGson().toJson(new Vanished(listed), writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<String> listed() {
        try (var writer = getFileHandler().getFileReader("mods/Essentials/vanished.json")) {
            var vanished = getFileHandler().getGson().fromJson(writer, Vanished.class);
            return vanished.Vanished();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<PlayerRef> getVanished() {
        var vanished = new ArrayList<PlayerRef>();
        if (!listed().isEmpty()) {
            for (var uuidString : listed()) {
                var playerRef = getUniverseHandler().getUniverse().getPlayer(new Options.UUIDConverter().convert(uuidString));
                if (playerRef != null) {
                    vanished.add(playerRef);
                }
            }
        }
        return vanished;
    }
}