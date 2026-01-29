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
import org.achymake.essentials.handlers.PlayerHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class SetHomeCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    public SetHomeCommand() {
        super("sethome", "sets home", false);
        addUsageVariant(new SetHomeNamedCommand());
        requirePermission("essentials.command.sethome");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var maxHomes = getPlayerHandler().getMaxHomes(playerRef);
        var homes = getPlayerHandler().getHomes(playerRef.getUuid());
        if (homes.contains("home") || maxHomes > homes.size()) {
            getPlayerHandler().setHome(playerRef, world, "home");
            playerRef.sendMessage(Message.join(
                    Message.raw("You have set a home named ").color(Color.ORANGE),
                    Message.raw("home")
            ));
        } else playerRef.sendMessage(Message.join(
                Message.raw("You have reached limit of ").color(Color.RED),
                Message.raw(maxHomes + " "),
                Message.raw("homes").color(Color.RED)
        ));
    }
}