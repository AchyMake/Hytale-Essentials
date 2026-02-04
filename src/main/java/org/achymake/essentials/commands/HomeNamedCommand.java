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
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.TeleportHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

class HomeNamedCommand extends AbstractPlayerCommand {
    @Nonnull
    private final RequiredArg<String> value;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    public HomeNamedCommand() {
        super("teleports to named home");
        value = withRequiredArg("string", "server.commands.argtype.string.desc", ArgTypes.STRING);
        requirePermission("essentials.command.home");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var homeName = value.get(commandContext);
        var uuid = playerRef.getUuid();
        var homeTP = getPlayerHandler().getHome(uuid, homeName);
        if (homeTP != null) {
            var message = Message.join(
                    Message.raw("Teleporting to ").color(Color.ORANGE),
                    Message.raw(homeName)
            );
            getTeleportHandler().teleport(playerRef, homeTP, message, 3);
        } else playerRef.sendMessage(Message.join(
                Message.raw("Seems like ").color(Color.RED),
                Message.raw(homeName + " "),
                Message.raw("doesn't exists").color(Color.RED)
        ));
    }
}