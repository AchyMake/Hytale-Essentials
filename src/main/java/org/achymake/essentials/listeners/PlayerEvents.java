package org.achymake.essentials.listeners;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.HiddenPlayersManager;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MessageHandler;
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.UniverseHandler;

import java.awt.Color;

public class PlayerEvents {
    private static Essentials getInstance() {
        return Essentials.getInstance();
    }
    private static PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private static UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    private static MessageHandler getMessageHandler() {
        return getInstance().getMessageHandler();
    }
    public static void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        event.setBroadcastJoinMessage(false);
        var isPVP = event.getWorld().getWorldConfig().isPvpEnabled();
        var playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
        getPlayerHandler().setPVP(playerRef, isPVP);
    }
    public static void onPlayerChat(PlayerChatEvent event) {
        event.setContent(getMessageHandler().censor(event.getContent()));
    }
    public static void onPlayerConnect(PlayerConnectEvent event) {
        var playerRef = event.getPlayerRef();
        var username = playerRef.getUsername();
        var uuid = playerRef.getUuid();
        if (PermissionsModule.get().hasPermission(uuid, "essentials.event.join.message")) {
            var hiddenManager = new HiddenPlayersManager();
            if (hiddenManager.isPlayerHidden(uuid))return;
            getUniverseHandler().sendAll(Message.join(
                    Message.raw(username + " "),
                    Message.raw("has joined the Server [").color(Color.ORANGE),
                    Message.raw("+").color(Color.GREEN),
                    Message.raw("]").color(Color.ORANGE)
            ));
        } else getUniverseHandler().sendAll(Message.join(
                        Message.raw(username + " "),
                        Message.raw("has joined the Server").color(Color.GRAY)),
                "essentials.event.join.notify");
    }
    public static void onPlayerDisconnect(PlayerDisconnectEvent event) {
        var playerRef = event.getPlayerRef();
        var username = playerRef.getUsername();
        var uuid = playerRef.getUuid();
        if (PermissionsModule.get().hasPermission(uuid, "essentials.event.quit.message")) {
            var hiddenManager = new HiddenPlayersManager();
            if (hiddenManager.isPlayerHidden(uuid))return;
            getUniverseHandler().sendAll(Message.join(
                    Message.raw(username + " "),
                    Message.raw("has left the Server [").color(Color.ORANGE),
                    Message.raw("-").color(Color.RED),
                    Message.raw("]").color(Color.ORANGE)
            ));
        } else getUniverseHandler().sendAll(Message.join(
                Message.raw(username + " "),
                Message.raw("has left the Server").color(Color.GRAY)
        ), "essentials.event.quit.notify");
    }
}
