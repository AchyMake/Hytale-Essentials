package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.*;

public class PvPCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    public PvPCommand() {
        super("pvp", "toggle pvp", false);
        requirePermission("essentials.command.pvp");
        addUsageVariant(new PvPOtherCommand());
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        getPlayerHandler().setPVP(playerRef, !getPlayerHandler().isPVP(playerRef));
        if (getPlayerHandler().isPVP(playerRef)) {
            playerRef.sendMessage(Message.join(
                    Message.raw("You ").color(Color.ORANGE),
                    Message.raw("enabled ").color(Color.GREEN),
                    Message.raw("PVP").color(Color.ORANGE)
            ));
        } else playerRef.sendMessage(Message.join(
                    Message.raw("You ").color(Color.ORANGE),
                    Message.raw("disabled ").color(Color.RED),
                    Message.raw("PVP").color(Color.ORANGE)
            ));
    }
}