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
import org.achymake.essentials.handlers.TeleportHandler;
import org.achymake.essentials.handlers.UniverseHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class SpawnCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public SpawnCommand() {
        super("spawn", "teleports to spawn", false);
        requirePermission("essentials.command.spawn");
        addUsageVariant(new SpawnOtherCommand());
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var spawnTp = getUniverseHandler().getSpawn();
        if (spawnTp != null) {
            var message = Message.join(
                    Message.raw("Teleporting to ").color(Color.ORANGE),
                    Message.raw("Spawn")
            );
            getTeleportHandler().teleport(playerRef, spawnTp, message, 3);
        } else playerRef.sendMessage(Message.raw("Seem like spawn has not been set").color(Color.RED));
    }
}