package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

class EconomyRemoveCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    private final RequiredArg<Integer> integerRequiredArg;
    public EconomyRemoveCommand() {
        super("remove","economy remove command");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        integerRequiredArg = withRequiredArg("double", "server.commands.argtype.string.desc", ArgTypes.INTEGER);
        requirePermission("essentials.command.economy.remove");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var value = this.integerRequiredArg.get(commandContext);
        if (value > 0) {
            var uuid = targetRef.getUuid();
            if (getEconomyHandler().has(uuid, value)) {
                if (getEconomyHandler().remove(uuid, value)) {
                    commandContext.sendMessage(Message.join(
                            Message.raw("You removed ").color(Color.ORANGE),
                            Message.raw(getEconomyHandler().format(value) + " ").color(Color.RED),
                            Message.raw("from ").color(Color.ORANGE),
                            Message.raw(targetRef.getUsername()),
                            Message.raw("'s account").color(Color.ORANGE)
                    ));
                    commandContext.sendMessage(Message.join(
                            Message.raw("New balance ").color(Color.ORANGE),
                            Message.raw(getEconomyHandler().format(getEconomyHandler().get(uuid)))
                    ));
                } else commandContext.sendMessage(Message.raw("Seems like there was an error while saving the file").color(Color.RED));
            } else commandContext.sendMessage(Message.raw("Seems like result would be negative").color(Color.RED));
        } else commandContext.sendMessage(Message.raw("Seems like you were trying to put a negative integer").color(Color.RED));
    }
}