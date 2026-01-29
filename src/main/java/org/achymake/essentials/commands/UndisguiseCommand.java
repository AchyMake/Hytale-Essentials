package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ModelAssetHandler;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class UndisguiseCommand extends AbstractPlayerCommand {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    public UndisguiseCommand() {
        super("undisguise", "Undisguises", false);
        addUsageVariant(new UndisguiseOtherCommand());
        addAliases("und");
        requirePermission("essentials.command.disguise");
    }
    protected void execute(@Nonnull CommandContext context,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        getModelAssetHandler().unDisguise(ref);
    }
}