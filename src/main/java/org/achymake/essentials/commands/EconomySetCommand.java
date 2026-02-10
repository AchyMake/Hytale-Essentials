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

class EconomySetCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    private final RequiredArg<Integer> integerRequiredArg;
    public EconomySetCommand() {
        super("set", "economy set command");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        integerRequiredArg = withRequiredArg("double", "server.commands.argtype.string.desc", ArgTypes.INTEGER);
        requirePermission("essentials.command.economy.set");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var value = this.integerRequiredArg.get(commandContext);
        var ref = targetRef.getReference();
        if (ref != null && ref.isValid()) {
            var username = targetRef.getUsername();
            var store = ref.getStore();
            var world = getInstance().getUniverseHandler().getWorld(targetRef.getWorldUuid());
            world.execute(() -> {
                var account = store.getComponent(ref, getInstance().getAccountComponentType());
                if (account != null) {
                    account.set(value);
                    commandContext.sendMessage(Message.join(
                            Message.raw("You set ").color(Color.ORANGE),
                            Message.raw(getEconomyHandler().format(value) + " ").color(Color.GREEN),
                            Message.raw("to ").color(Color.ORANGE),
                            Message.raw(username),
                            Message.raw("'s account").color(Color.ORANGE)
                    ));
                } else commandContext.sendMessage(Message.raw("Seems like target do not have Account Component").color(Color.RED));
            });
        }
    }
}