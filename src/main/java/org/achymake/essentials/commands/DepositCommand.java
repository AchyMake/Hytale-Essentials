package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.HashMap;

public class DepositCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public DepositCommand() {
        super("deposit", "deposit to bank", false);
        requirePermission("essentials.command.deposit");
        addUsageVariant(new DepositOtherCommand());
        addAliases("depo");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var player = store.getComponent(ref, Player.getComponentType());
        if (player != null) {
            var inventory = player.getInventory();
            var listed = new HashMap<String, Integer>();
            var coinID = "Coins";
            var storage = inventory.getStorage();
            var hotbar = inventory.getHotbar();
            var account = store.getComponent(ref, getInstance().getAccountComponentType());
            if (account != null) {
                storage.forEach((i, itemStack) -> {
                    var id = itemStack.getItemId();
                    if (id.equalsIgnoreCase(coinID)) {
                        var amount = itemStack.getQuantity();
                        account.add(amount);
                        if (listed.containsKey(coinID)) {
                            listed.put(coinID, listed.get(coinID) + amount);
                        } else listed.put(coinID, amount);
                        storage.removeItemStackFromSlot(i, itemStack, amount);
                    }
                });
                hotbar.forEach((i, itemStack) -> {
                    var id = itemStack.getItemId();
                    if (id.equalsIgnoreCase(coinID)) {
                        var amount = itemStack.getQuantity();
                        account.add(amount);
                        if (listed.containsKey(coinID)) {
                            listed.put(coinID, listed.get(coinID) + amount);
                        } else listed.put(coinID, amount);
                        hotbar.removeItemStackFromSlot(i, itemStack, amount);
                    }
                });
                if (!listed.isEmpty()) {
                    var formatted = getEconomyHandler().format(listed.get(coinID));
                    playerRef.sendMessage(Message.join(
                            Message.raw("You deposit ").color(Color.ORANGE),
                            Message.raw(formatted)
                    ));
                } else playerRef.sendMessage(Message.raw("Seems like you don't have any coins to deposit").color(Color.RED));
            }
        }
    }
}