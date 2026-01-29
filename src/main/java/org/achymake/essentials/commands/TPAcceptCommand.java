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
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class TPAcceptCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerRefHandler() {
        return getInstance().getPlayerHandler();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public TPAcceptCommand() {
        super("tpaccept", "accept teleport requests", false);
        requirePermission("essentials.command.tpaccept");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        if (getTeleportHandler().isInRequest(playerRef)) {
            var requester = getTeleportHandler().getReversed().get(playerRef);
            if (requester != null) {
                var targetRefWorld = getPlayerRefHandler().getWorld(requester);
                if (targetRefWorld != null) {
                    var message = Message.join(
                            Message.raw("Teleporting to ").color(Color.ORANGE),
                            Message.raw(requester.getUsername())
                    );
                    var tp = getTeleportHandler().createForPlayer(targetRefWorld, requester.getTransform());
                    getTeleportHandler().teleport(playerRef, tp, message);
                }
            }
            getTeleportHandler().cancel(playerRef);
        } else playerRef.sendMessage(Message.raw("Seems like you don't have any tpa request").color(Color.RED));
    }
}