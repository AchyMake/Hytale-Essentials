package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;
import java.util.HashMap;

public class DepositOtherCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    public DepositOtherCommand() {
        super("deposit other");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.deposit.other");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var ref = targetRef.getReference();
        assert ref != null;
        var target = ref.getStore().getComponent(ref, Player.getComponentType());
        if (target != null) {
            var inventory = target.getInventory();
            var listed = new HashMap<String, Integer>();
            var coinID = "coins";
            var storage = inventory.getStorage();
            var hotbar = inventory.getHotbar();
            var account = ref.getStore().getComponent(ref, getInstance().getAccountComponentType());
            storage.forEach((i, itemStack) -> {
                var id = itemStack.getItemId();
                if (id.equalsIgnoreCase(coinID)) {
                    var amount = itemStack.getQuantity();
                    if (account != null) {
                        account.add(amount);
                        if (listed.containsKey(coinID)) {
                            listed.put(coinID, listed.get(coinID) + amount);
                        } else listed.put(coinID, amount);
                        storage.removeItemStackFromSlot(i, itemStack, amount);
                    }
                }
            });
            hotbar.forEach((i, itemStack) -> {
                var id = itemStack.getItemId();
                if (id.equalsIgnoreCase(coinID)) {
                    var amount = itemStack.getQuantity();
                    if (account != null) {
                        account.add(amount);
                        if (listed.containsKey(coinID)) {
                            listed.put(coinID, listed.get(coinID) + amount);
                        } else listed.put(coinID, amount);
                        hotbar.removeItemStackFromSlot(i, itemStack, amount);
                    }
                }
            });
            if (!listed.isEmpty()) {
                var amount = listed.get(coinID);
                var formatted = getEconomyHandler().format(amount);
                target.sendMessage(Message.join(
                        Message.raw("You deposit ").color(Color.ORANGE),
                        Message.raw(formatted + " ")
                ));
            } else target.sendMessage(Message.raw("Seems like you don't have any coins to deposit").color(Color.RED));
        }
    }
}