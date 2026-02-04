package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.Color;

public class PayPlayerAmountCommand extends AbstractPlayerCommand {
    private final RequiredArg<PlayerRef> playerArg;
    @Nonnull
    private final RequiredArg<Integer> integerArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    PayPlayerAmountCommand() {
        super("pay player");
        playerArg = withRequiredArg("player", "server.commands.argtype.player.desc", ArgTypes.PLAYER_REF);
        integerArg = withRequiredArg("integer", "server.commands.give.quantity.desc", ArgTypes.INTEGER);
        requirePermission("essentials.command.pay");
    }
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var targetPlayerRef = playerArg.get(commandContext);
        var amount = integerArg.get(commandContext);
        var targetRef = targetPlayerRef.getReference();
        if (targetRef != null && targetRef.isValid()) {
            if (targetPlayerRef != playerRef) {
                if (amount > 0) {
                    var uuid = playerRef.getUuid();
                    var targetUuid = targetPlayerRef.getUuid();
                    var formatted = getEconomyHandler().format(amount);
                    if (getEconomyHandler().has(uuid, amount)) {
                        if (getEconomyHandler().remove(uuid, amount) && getEconomyHandler().add(targetUuid, amount)) {
                            playerRef.sendMessage(Message.join(
                                    Message.raw("You sent ").color(Color.ORANGE),
                                    Message.raw(formatted + " "),
                                    Message.raw("to ").color(Color.ORANGE),
                                    Message.raw(targetPlayerRef.getUsername())
                            ));
                            targetPlayerRef.sendMessage(Message.join(
                                    Message.raw(playerRef.getUsername() + " "),
                                    Message.raw("has sent you ").color(Color.ORANGE),
                                    Message.raw(formatted)
                            ));
                        } else playerRef.sendMessage(Message.raw("Seems like there was an error while saving the files").color(Color.RED));
                    } else playerRef.sendMessage(Message.join(
                            Message.raw("Seems like you don't have ").color(Color.RED),
                            Message.raw(formatted)
                    ));
                } else playerRef.sendMessage(Message.raw("Seems like you were trying to put negative integer").color(Color.RED));
            } else playerRef.sendMessage(Message.raw("Seems like you tried to pay your self").color(Color.RED));
        }
    }
}