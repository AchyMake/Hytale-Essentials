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

public class TPCancelCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public TPCancelCommand() {
        super("tpcancel", "cancel teleport request", false);
        requirePermission("essentials.command.tpcancel");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        if (getTeleportHandler().isInRequest(playerRef)) {
            var targetRef = getTeleportHandler().getTPA().get(playerRef);
            playerRef.sendMessage(Message.raw("You canceled your teleport request!").color(Color.ORANGE));
            if (targetRef != null && targetRef.isValid()) {
                targetRef.sendMessage(Message.join(
                        Message.raw(playerRef.getUsername() + " "),
                        Message.raw("canceled teleport request!").color(Color.ORANGE)
                ));
            }
            getTeleportHandler().cancel(playerRef);
        } else playerRef.sendMessage(Message.raw("Seems like you don't have any teleport request").color(Color.RED));
    }
}