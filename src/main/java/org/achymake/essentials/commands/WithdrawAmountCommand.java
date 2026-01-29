package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class WithdrawAmountCommand extends AbstractPlayerCommand {
    private final RequiredArg<Integer> integerArg;
    @Nonnull
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public WithdrawAmountCommand() {
        super("withdraw from balance");
        integerArg = withRequiredArg("integer", "server.commands.give.quantity.desc", ArgTypes.INTEGER);
        requirePermission("essentials.command.withdraw");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var amount = this.integerArg.get(commandContext);
        var player = store.getComponent(ref, Player.getComponentType());
        if (player != null) {
            if (amount > 0 && 1000 >= amount) {
                var formatted = getEconomyHandler().format(amount);
                if (getEconomyHandler().has(playerRef.getUuid(), amount)) {
                    if (getEconomyHandler().remove(playerRef.getUuid(), amount)) {
                        player.getInventory().getCombinedHotbarFirst().addItemStack(new ItemStack("Coin", amount));
                        player.sendMessage(Message.join(
                                Message.raw("You withdraw ").color(Color.ORANGE),
                                Message.raw(formatted + " "),
                                Message.raw("from bank").color(Color.ORANGE)
                        ));
                        commandContext.sendMessage(Message.join(
                                Message.raw("New balance ").color(Color.ORANGE),
                                Message.raw(getEconomyHandler().format(getEconomyHandler().get(playerRef.getUuid())))
                        ));
                    }
                } else player.sendMessage(Message.join(
                        Message.raw("Seems like you don't have ").color(Color.RED),
                        Message.raw(formatted + " "),
                        Message.raw("in your bank").color(Color.RED)
                ));
            } else player.sendMessage(Message.raw("Seems like you tried to input an inadequate amount").color(Color.RED));
        }
    }
}