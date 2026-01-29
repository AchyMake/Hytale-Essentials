package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.Color;

public class GMCCommand extends AbstractPlayerCommand {
    public GMCCommand() {
        super("gmc", "change game mode to creative", false);
        addUsageVariant(new GMCOtherCommand());
        requirePermission("essentials.command.gmc");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var player = store.getComponent(ref, Player.getComponentType());
        if (player != null) {
            if (player.getGameMode().equals(GameMode.Creative)) {
                playerRef.sendMessage(Message.join(
                        Message.raw("Seems like you already are in ").color(Color.RED),
                        Message.raw("Creative "),
                        Message.raw("mode").color(Color.RED)
                ));
            } else Player.setGameMode(ref, GameMode.Creative, store);
        }
    }
}