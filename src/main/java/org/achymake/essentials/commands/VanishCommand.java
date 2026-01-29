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
import org.achymake.essentials.handlers.VanishHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class VanishCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private VanishHandler getVanishHandler() {
        return getInstance().getVanishHandler();
    }
    public VanishCommand() {
        super("vanish", "makes you invisible from players", false);
        addUsageVariant(new VanishOtherCommand());
        requirePermission("essentials.command.vanish");
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        getVanishHandler().setVanish(playerRef, !getVanishHandler().isVanished(playerRef));
        if (getVanishHandler().isVanished(playerRef)) {
            commandContext.sendMessage(Message.raw("You are now vanished from players").color(Color.ORANGE));
        } else commandContext.sendMessage(Message.raw("You are no longer vanished from players").color(Color.ORANGE));
    }
}