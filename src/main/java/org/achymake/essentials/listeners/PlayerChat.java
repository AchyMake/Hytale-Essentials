package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MessageHandler;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;

public class PlayerChat {
    private static Essentials getInstance() {
        return Essentials.getInstance();
    }
    private static MessageHandler getMessageHandler() {
        return getInstance().getMessageHandler();
    }
    public static void onPlayerChat(PlayerChatEvent event) {
        event.setContent(getMessageHandler().censor(event.getContent()));
    }
}