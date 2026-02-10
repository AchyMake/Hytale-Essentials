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

import javax.annotation.Nonnull;
import java.awt.Color;

public class DelHomeCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public DelHomeCommand() {
        super("delhome", "deletes home", false);
        addUsageVariant(new DelHomeNamedCommand());
        requirePermission("essentials.command.delhome");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var homes = store.getComponent(ref, getInstance().getHomesComponentType());
        if (homes != null) {
            if (homes.exists("home")) {
                homes.delHome("home");
                playerRef.sendMessage(Message.join(
                        Message.raw("You have deleted a home named ").color(Color.ORANGE),
                        Message.raw("home")
                ));
            } else playerRef.sendMessage(Message.join(
                    Message.raw("Seems like ").color(Color.RED),
                    Message.raw("home "),
                    Message.raw("does not exist").color(Color.RED)
            ));
        } else playerRef.sendMessage(Message.raw("Seems like you do not have Homes Component").color(Color.RED));
    }
}
