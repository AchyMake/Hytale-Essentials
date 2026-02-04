package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;
import java.util.ArrayList;

public class BalanceTopCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public BalanceTopCommand() {
        super("top", "balance top command");
        requirePermission("essentials.command.balance.top");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var topAccounts = new ArrayList<>(getEconomyHandler().getTopAccounts());
        if (!topAccounts.isEmpty()) {
            commandContext.sendMessage(Message.raw("Top 10 Balance").color(Color.ORANGE));
            for (int i = 0; i < topAccounts.size(); i++) {
                var listed = topAccounts.get(i);
                var value = i + 1;
                var username = listed.getKey();
                var account = getEconomyHandler().format(listed.getValue());
                commandContext.sendMessage(Message.join(
                        Message.raw(value + " ").color(Color.ORANGE),
                        Message.raw(username + " "),
                        Message.raw(account)

                ));
            }
        } else commandContext.sendMessage(Message.raw("Seems like top accounts is empty").color(Color.RED));
    }
}