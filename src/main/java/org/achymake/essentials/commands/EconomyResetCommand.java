package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class EconomyResetCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final RequiredArg<PlayerRef> targetRef;
    public EconomyResetCommand() {
        super("reset", "economy reset command");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.economy.reset");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var ref = targetRef.getReference();
        if (ref != null && ref.isValid()) {
            var username = targetRef.getUsername();
            var store = ref.getStore();
            var world = getInstance().getUniverseHandler().getWorld(targetRef.getWorldUuid());
            world.execute(() -> {
                var account = store.getComponent(ref, getInstance().getAccountComponentType());
                if (account != null) {
                    account.set(0.0);
                    commandContext.sendMessage(Message.join(
                            Message.raw("You reset ").color(Color.ORANGE),
                            Message.raw(username),
                            Message.raw("'s account").color(Color.ORANGE)
                    ));
                } else commandContext.sendMessage(Message.raw("Seems like target do not have Account Component").color(Color.RED));
            });
        }
    }
}