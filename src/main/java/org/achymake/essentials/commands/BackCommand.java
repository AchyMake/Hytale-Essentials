package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.TeleportHandler;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.*;

public class BackCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public BackCommand() {
        super("back", "teleports back to death location", false);
        requirePermission("essentials.command.back");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var deathTP = getPlayerHandler().getDeath(playerRef.getUuid());
        if (deathTP != null) {
            getTeleportHandler().teleport(playerRef, deathTP, Message.join(
                    Message.raw("Teleporting to ").color(Color.ORANGE),
                    Message.raw("Death Location")
            ), 3);
        } else playerRef.sendMessage(Message.raw("Seems like your death location doesn't exist").color(Color.RED));
    }
}