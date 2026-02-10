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

class EconomyAddCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    private final RequiredArg<Double> doubleRequiredArg;
    public EconomyAddCommand() {
        super("add", "economy add command");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        doubleRequiredArg = withRequiredArg("double", "server.commands.argtype.string.desc", ArgTypes.DOUBLE);
        requirePermission("essentials.command.economy.add");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var value = this.doubleRequiredArg.get(commandContext);
        var ref = targetRef.getReference();
        if (ref != null && ref.isValid()) {
            var store = ref.getStore();
            if (value > 0) {
                var world = getInstance().getUniverseHandler().getWorld(targetRef.getWorldUuid());
                world.execute(() -> {
                    var account = store.getComponent(ref, getInstance().getAccountComponentType());
                    if (account != null) {
                        account.add(value);
                        commandContext.sendMessage(Message.join(
                                Message.raw("You added ").color(Color.ORANGE),
                                Message.raw(getEconomyHandler().format(value) + " ").color(Color.GREEN),
                                Message.raw("to ").color(Color.ORANGE),
                                Message.raw(targetRef.getUsername()),
                                Message.raw("'s account").color(Color.ORANGE)
                        ));
                        commandContext.sendMessage(Message.join(
                                Message.raw("New balance ").color(Color.ORANGE),
                                Message.raw(getEconomyHandler().format(account.getBalance()))
                        ));
                    } else commandContext.sendMessage(Message.raw("Seems like target do not have Account Component").color(Color.RED));
                });
            } else commandContext.sendMessage(Message.raw("Seems like you were trying to put a negative integer").color(Color.RED));
        }
    }
}