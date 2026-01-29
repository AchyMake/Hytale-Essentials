package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;

public class AddPlayerToWorld {
    public static void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        event.setBroadcastJoinMessage(false);
    }
}
