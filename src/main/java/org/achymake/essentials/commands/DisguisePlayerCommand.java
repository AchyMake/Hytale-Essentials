package org.achymake.essentials.commands;

import org.achymake.essentials.pages.DisguisePlayerPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class DisguisePlayerCommand extends AbstractPlayerCommand {
    public DisguisePlayerCommand() {
        super("player", "Disguise into other player");
        addUsageVariant(new DisguisePlayerNamedCommand());
        requirePermission("essentials.command.disguise.player");
    }
    protected void execute(@Nonnull CommandContext context,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        store.getComponent(ref, Player.getComponentType()).getPageManager().openCustomPage(ref, store, new DisguisePlayerPage(playerRef));
    }
}