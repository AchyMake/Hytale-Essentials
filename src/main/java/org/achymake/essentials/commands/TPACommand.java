package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class TPACommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public TPACommand() {
        super("tpa", "teleport request players", false);
        addUsageVariant(new TPATargetCommand());
        requirePermission("essentials.command.tpa");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        if (getTeleportHandler().isIgnored(playerRef)) {
            getTeleportHandler().getIgnored().remove(playerRef);
        } else getTeleportHandler().getIgnored().add(playerRef);
        if (getTeleportHandler().isIgnored(playerRef)) {
            playerRef.sendMessage(Message.raw("You are now ignoring tpa requests!").color(Color.ORANGE));
        } else playerRef.sendMessage(Message.raw("You are now accepting tpa requests!").color(Color.ORANGE));
    }
}