package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

class BedOtherCommand extends AbstractPlayerCommand {
    @Nonnull
    private final RequiredArg<PlayerRef> playerArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    BedOtherCommand() {
        super("teleports to others bed");
        playerArg = withRequiredArg("player", "server.commands.argtype.player.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.bed.other");
    }
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var targetPlayerRef = playerArg.get(commandContext);
        var targetRef = targetPlayerRef.getReference();
        if (targetRef != null && targetRef.isValid()) {
            var targetStore = targetRef.getStore();
            var message = Message.join(
                    Message.raw("Teleporting to ").color(Color.ORANGE),
                    Message.raw(targetPlayerRef.getUsername()),
                    Message.raw("'s Bed").color(Color.ORANGE)
            );
            Player.getRespawnPosition(targetRef, world.getName(), targetStore).thenAccept(transform ->
                    getTeleportHandler().teleport(playerRef, getTeleportHandler().createForPlayer(world, transform), message));
        }
    }
}