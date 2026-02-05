package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;

public class AddPlayerToWorld {
    private static Essentials getInstance() {
        return Essentials.getInstance();
    }
    private static PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    public static void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        event.setBroadcastJoinMessage(false);
        var isPVP = event.getWorld().getWorldConfig().isPvpEnabled();
        var playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
        getPlayerHandler().setPVP(playerRef, isPVP);
    }
}
