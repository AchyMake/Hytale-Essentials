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

import javax.annotation.Nonnull;
import java.awt.Color;

class SetHomeNamedCommand extends AbstractPlayerCommand {
    @Nonnull
    private final RequiredArg<String> value;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    SetHomeNamedCommand() {
        super("sets named home");
        value = withRequiredArg("string", "server.commands.argtype.string.desc", ArgTypes.STRING);
        requirePermission("essentials.command.sethome");
    }
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var homeName = value.get(commandContext);
        var uuid = playerRef.getUuid();
        var homes = getPlayerHandler().getHomes(uuid);
        var maxHomes = getPlayerHandler().getMaxHomes(playerRef);
        if (homes.contains(homeName) || maxHomes > homes.size()) {
            var transform = playerRef.getTransform();
            if (getPlayerHandler().setHome(uuid, transform, world, homeName)) {
                playerRef.sendMessage(Message.join(
                        Message.raw("You have set a home named ").color(Color.ORANGE),
                        Message.raw(homeName)
                ));
            } else playerRef.sendMessage(Message.raw("Seems like there was an error while saving the file").color(Color.RED));
        } else playerRef.sendMessage(Message.join(
                Message.raw("You have reached limit of ").color(Color.RED),
                Message.raw(maxHomes + " "),
                Message.raw("homes").color(Color.RED)
        ));
    }
}