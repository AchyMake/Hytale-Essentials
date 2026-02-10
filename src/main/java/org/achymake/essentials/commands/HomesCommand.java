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

public class HomesCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public HomesCommand() {
        super("homes", "check home names", false);
        requirePermission("essentials.command.homes");
    }
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var homes = store.getComponent(ref, getInstance().getHomesComponentType());
        if (homes != null) {
            var listed = homes.getHomes();
            if (!listed.isEmpty()) {
                playerRef.sendMessage(Message.raw("Homes:").color(Color.ORANGE));
                for (var homeName : listed) {
                    playerRef.sendMessage(Message.raw("- " + homeName));
                }
            } else playerRef.sendMessage(Message.raw("Seems like you haven't set any homes yet").color(Color.RED));
        } else playerRef.sendMessage(Message.raw("Seems like you do not have Homes Component").color(Color.RED));
    }
}