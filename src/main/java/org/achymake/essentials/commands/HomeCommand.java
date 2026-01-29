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
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.*;

public class HomeCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public HomeCommand() {
        super("home", "teleport home", false);
        addUsageVariant(new HomeNamedCommand());
        requirePermission("essentials.command.home");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var homeTP = getPlayerHandler().getHome(playerRef.getUuid(), "home");
        if (homeTP != null) {
            var message = Message.join(
                    Message.raw("Teleporting to ").color(Color.ORANGE),
                    Message.raw("Home")
            );
            getTeleportHandler().teleport(playerRef, homeTP, message, 3);
        } else playerRef.sendMessage(Message.join(
                Message.raw("Seems like ").color(Color.RED),
                Message.raw("Home "),
                Message.raw("doesn't exists").color(Color.RED)
        ));
    }
}