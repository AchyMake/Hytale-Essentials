package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ModelAssetHandler;
import org.achymake.essentials.handlers.UniverseHandler;
import org.achymake.essentials.handlers.VanishHandler;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;

import java.awt.Color;

public class PlayerDisconnect {
    private static Essentials getInstance() {
        return Essentials.getInstance();
    }
    private static ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    private static VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    private static UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public static void onPlayerDisconnect(PlayerDisconnectEvent event) {
        var playerRef = event.getPlayerRef();
        if (getModelAssetHandler().isDisguised(playerRef.getReference())) {
            getModelAssetHandler().unDisguise(playerRef.getReference());
        }
        if (getVanishHandler().isVanished(playerRef))return;
        if (!PermissionsModule.get().hasPermission(playerRef.getUuid(), "essentials.event.quit.message"))return;
        getUniverseHandler().sendAll(Message.join(
                Message.raw(playerRef.getUsername()),
                Message.raw(" has left the Server").color(Color.ORANGE)
        ));
    }
}