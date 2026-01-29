package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.event.events.player.PlayerSetupConnectEvent;

public class PlayerSetupConnect {
    public static void onPlayerSetupConnect(PlayerSetupConnectEvent event) {
        if (!event.isCancelled())return;
        if (!PermissionsModule.get().hasPermission(event.getUuid(), "essentials.event.join.full"))return;
        event.setCancelled(false);
    }
}