package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

public class BalanceOtherCommand extends AbstractCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    public BalanceOtherCommand() {
        super("balance other command");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.balance.other");
    }
    @NullableDecl
    @Override
    protected CompletableFuture<Void> execute(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var account = getEconomyHandler().get(targetRef.getUuid());
        commandContext.sendMessage(Message.join(
                Message.raw(targetRef.getUsername() + " "),
                Message.raw("has ").color(Color.ORANGE),
                Message.raw(getEconomyHandler().format(account))
        ));
        return null;
    }
}