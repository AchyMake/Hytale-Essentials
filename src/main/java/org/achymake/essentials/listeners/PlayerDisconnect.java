package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.UniverseHandler;
import org.achymake.essentials.handlers.VanishHandler;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

import java.awt.Color;

public class PlayerDisconnect {
    private static Essentials getInstance() {
        return Essentials.getInstance();
    }
    private static VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private static UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public static void onPlayerDisconnect(PlayerDisconnectEvent event) {
        var playerRef = event.getPlayerRef();
        var username = playerRef.getUsername();
        var uuid = playerRef.getUuid();
        var hiddenManager = playerRef.getHiddenPlayersManager();
        if (hiddenManager.isPlayerHidden(uuid))return;
        if (PermissionsModule.get().hasPermission(uuid, "essentials.event.quit.message")) {
            getUniverseHandler().sendAll(Message.join(
                    Message.raw(username + " "),
                    Message.raw("has left the Server [").color(Color.ORANGE),
                    Message.raw("-").color(Color.RED),
                    Message.raw("]").color(Color.ORANGE)
            ));
        } else getUniverseHandler().sendAll(Message.join(
                Message.raw(username + " "),
                Message.raw("has left the Server").color(Color.ORANGE)
        ), "essentials.event.quit.notify");
    }
}