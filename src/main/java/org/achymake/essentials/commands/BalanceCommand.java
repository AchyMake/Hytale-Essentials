package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class BalanceCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public BalanceCommand() {
        super("balance", "check balance", false);
        requirePermission("essentials.command.balance");
        addUsageVariant(new BalanceOtherCommand());
        addSubCommand(new BalanceTopCommand());
        addAliases("bal");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var account = store.getComponent(ref, getInstance().getAccountComponentType());
        if (account != null) {
            playerRef.sendMessage(Message.join(
                    Message.raw("Balance").color(Color.ORANGE),
                    Message.raw(": " + getEconomyHandler().format(account.getBalance()))
            ));
        }
    }
}