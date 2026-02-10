package org.achymake.essentials.commands;

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
import org.achymake.essentials.Essentials;

import javax.annotation.Nonnull;
import java.awt.Color;

class DelHomeNamedCommand extends AbstractPlayerCommand {
    private final RequiredArg<String> value;
    @Nonnull
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    DelHomeNamedCommand() {
        super("deletes named home");
        value = withRequiredArg("string", "server.commands.argtype.string.desc", ArgTypes.STRING);
        requirePermission("essentials.command.delhome");
    }
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var homeName = value.get(commandContext);
        var homes = store.getComponent(ref, getInstance().getHomesComponentType());
        if (homes != null) {
            if (homes.exists(homeName)) {
                homes.delHome(homeName);
                playerRef.sendMessage(Message.join(
                        Message.raw("You have deleted a home named ").color(Color.ORANGE),
                        Message.raw(homeName)
                ));
            } else playerRef.sendMessage(Message.join(
                    Message.raw("Seems like you haven't set ").color(Color.RED),
                    Message.raw(homeName + " "),
                    Message.raw("as home yet").color(Color.RED)
            ));
        } else playerRef.sendMessage(Message.raw("Seems like you do not have Homes Component").color(Color.RED));
    }
}