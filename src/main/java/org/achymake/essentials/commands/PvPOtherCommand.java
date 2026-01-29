package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class PvPOtherCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    public PvPOtherCommand() {
        super("toggles others pvp");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.pvp.other");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        getPlayerHandler().setPVP(targetRef, !getPlayerHandler().isPVP(targetRef));
        if (getPlayerHandler().isPVP(targetRef)) {
            commandContext.sendMessage(Message.join(
                    Message.raw("You ").color(Color.ORANGE),
                    Message.raw("enabled ").color(Color.GREEN),
                    Message.raw(targetRef.getUsername()),
                    Message.raw("'s PVP").color(Color.ORANGE)
            ));
        } else commandContext.sendMessage(Message.join(
                Message.raw("You ").color(Color.ORANGE),
                Message.raw("disabled ").color(Color.RED),
                Message.raw(targetRef.getUsername()),
                Message.raw("'s PVP").color(Color.ORANGE)
        ));
    }
}