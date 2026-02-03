package org.achymake.essentials.handlers;

import com.hypixel.hytale.server.core.universe.PlayerRef;

public class VanishHandler {
    public void setVanish(PlayerRef playerRef, boolean value) {
        var hiddenManager = playerRef.getHiddenPlayersManager();
        var uuid = playerRef.getUuid();
        if (value) {
            hiddenManager.hidePlayer(uuid);
        } else hiddenManager.showPlayer(uuid);
    }
    public boolean isVanished(PlayerRef playerRef) {
        return playerRef.getHiddenPlayersManager().isPlayerHidden(playerRef.getUuid());
    }
}