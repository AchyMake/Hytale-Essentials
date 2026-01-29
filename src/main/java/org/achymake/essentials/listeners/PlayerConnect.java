package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.UniverseHandler;
import org.achymake.essentials.handlers.VanishHandler;

import java.awt.*;

public class PlayerConnect {
    private static Essentials getInstance() {
        return Essentials.getInstance();
    }
    private static PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private static VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private static UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public static void onPlayerConnect(PlayerConnectEvent event) {
        var playerRef = event.getPlayerRef();
        getPlayerHandler().setup(playerRef);
        if (!getPlayerHandler().hasJoined(playerRef.getUuid())) {
            event.setWorld(getUniverseHandler().getSpawnWorld());
            getUniverseHandler().sendAll(Message.join(
                    Message.raw(playerRef.getUsername()),
                    Message.raw(" has joined the server for the first time!").color(Color.ORANGE)
            ));
        } else if (!getVanishHandler().isVanished(playerRef)) {
            if (!getVanishHandler().getVanished().isEmpty()) {
                var hiddenManager = playerRef.getHiddenPlayersManager();
                var vanished = getVanishHandler().getVanished();
                vanished.forEach(playerRef1 -> hiddenManager.hidePlayer(playerRef1.getUuid()));
            }
            if (PermissionsModule.get().hasPermission(playerRef.getUuid(), "essentials.event.join.message")) {
                getUniverseHandler().sendAll(Message.join(
                        Message.raw(playerRef.getUsername() + " "),
                        Message.raw("has joined the Server [").color(Color.ORANGE),
                        Message.raw("+").color(Color.GREEN),
                        Message.raw("]").color(Color.ORANGE)
                ));
            } else getUniverseHandler().sendAll(Message.join(
                    Message.raw(playerRef.getUsername() + " "),
                    Message.raw("has joined the Server").color(Color.GRAY)
            ));
        } else getVanishHandler().setVanish(playerRef, true);
    }
}