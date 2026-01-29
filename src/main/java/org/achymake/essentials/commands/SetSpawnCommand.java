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
import org.achymake.essentials.handlers.UniverseHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class SetSpawnCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public SetSpawnCommand() {
        super("setspawn", "sets spawn", true);
        requirePermission("essentials.command.setspawn");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        getUniverseHandler().setSpawn(world, playerRef.getTransform());
        playerRef.sendMessage(Message.raw("Spawn has been set").color(Color.ORANGE));
    }
}