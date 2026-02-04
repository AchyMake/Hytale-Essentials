package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.VanishHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

class VanishOtherCommand extends CommandBase {
    @Nonnull
    private final RequiredArg<PlayerRef> playerArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    VanishOtherCommand() {
        super("vanish other player");
        playerArg = withRequiredArg("player", "server.commands.argtype.player.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.vanish.other");
    }
    protected void executeSync(@Nonnull CommandContext commandContext) {
        var playerRef = playerArg.get(commandContext);
        var ref = playerRef.getReference();
        if (ref != null && ref.isValid()) {
            getVanishHandler().setVanish(playerRef, !getVanishHandler().isVanished(playerRef));
            if (getVanishHandler().isVanished(playerRef)) {
                playerRef.sendMessage(Message.raw("You are now vanished from players").color(Color.ORANGE));
                commandContext.sendMessage(Message.join(
                        Message.raw(playerRef.getUsername()),
                        Message.raw(" is now vanished from players").color(Color.ORANGE)
                ));
            } else {
                playerRef.sendMessage(Message.raw("You are no longer vanished from players").color(Color.ORANGE));
                commandContext.sendMessage(Message.join(
                        Message.raw(playerRef.getUsername()),
                        Message.raw(" is no longer vanished from players").color(Color.ORANGE)
                ));
            }
        }
    }
}