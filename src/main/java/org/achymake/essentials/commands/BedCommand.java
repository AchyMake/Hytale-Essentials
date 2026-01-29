package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class BedCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public BedCommand() {
        super("bed", "Teleports to Bed or Spawn", false);
        addUsageVariant(new BedOtherCommand());
        requirePermission("essentials.command.bed");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var message = Message.join(
                Message.raw("Teleporting to ").color(Color.ORANGE),
                Message.raw("Bed")
        );
        Player.getRespawnPosition(ref, world.getName(), store).thenAccept(transform ->
                getTeleportHandler().teleport(playerRef, getTeleportHandler().createForPlayer(world, transform), message, 3));
    }
}