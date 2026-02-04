package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.concurrent.TimeUnit;

class TPATargetCommand extends AbstractPlayerCommand {
    @Nonnull
    private final RequiredArg<PlayerRef> playerArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    TPATargetCommand() {
        super("tpa request target!");
        playerArg = withRequiredArg("player", "server.commands.argtype.player.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.tpa");
    }
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var targetPlayerRef = playerArg.get(commandContext);
        var targetRef = targetPlayerRef.getReference();
        if (targetRef != null && targetRef.isValid()) {
            if (targetPlayerRef != playerRef) {
                if (!getTeleportHandler().isInRequest(playerRef)) {
                    if (!getTeleportHandler().isIgnored(targetPlayerRef)) {
                        var scheduledTask = getScheduleHandler().schedule(() -> {
                            var expired = Message.raw("Teleport request has been expired").color(Color.RED);
                            playerRef.sendMessage(expired);
                            targetPlayerRef.sendMessage(expired);
                            getTeleportHandler().cancel(playerRef);
                        },15, TimeUnit.SECONDS);
                        getTeleportHandler().getTPA().put(playerRef, targetPlayerRef);
                        getTeleportHandler().getReversed().put(targetPlayerRef, playerRef);
                        getTeleportHandler().getTpScheduled().put(playerRef, scheduledTask);
                        getTeleportHandler().getTpScheduled().put(targetPlayerRef, scheduledTask);
                        playerRef.sendMessage(Message.join(
                                Message.raw("You have sent an tpa request to ").color(Color.ORANGE),
                                Message.raw(targetPlayerRef.getUsername()).color(Color.WHITE),
                                Message.raw("!").color(Color.ORANGE)
                        ));
                        playerRef.sendMessage(Message.join(
                                Message.raw("Type ").color(Color.ORANGE),
                                Message.raw("/tpcancel ").color(Color.RED),
                                Message.raw("to cancel the tpa request").color(Color.ORANGE)
                        ));
                        targetPlayerRef.sendMessage(Message.join(
                                Message.raw(playerRef.getUsername() + " ").color(Color.WHITE),
                                Message.raw("has sent an tpa request to you").color(Color.ORANGE)
                        ));
                        targetPlayerRef.sendMessage(Message.join(
                                Message.raw("Type ").color(Color.ORANGE),
                                Message.raw("/tpaccept ").color(Color.GREEN),
                                Message.raw("or ").color(Color.ORANGE),
                                Message.raw("/tpdeny ").color(Color.RED),
                                Message.raw("within ").color(Color.ORANGE),
                                Message.raw("15 "),
                                Message.raw("sec").color(Color.ORANGE)
                        ));
                    } else playerRef.sendMessage(Message.raw("Seems like the player does not accept any tp requests").color(Color.RED));
                } else playerRef.sendMessage(Message.raw("Seems like you already are in a request").color(Color.RED));
            } else playerRef.sendMessage(Message.raw("Seems like you tried to sent request to your self").color(Color.RED));
        }
    }
}