package org.achymake.essentials.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.pages.WithdrawValuePage;

import javax.annotation.Nonnull;

public class WithdrawCommand extends AbstractPlayerCommand {
    public WithdrawCommand() {
        super("withdraw", "withdraw from bank", false);
        requirePermission("essentials.command.withdraw");
        addUsageVariant(new WithdrawOtherCommand());
    }
    @Override
    protected void execute(@Nonnull CommandContext commandContext,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        store.getComponent(ref, Player.getComponentType()).getPageManager().openCustomPage(ref, store, new WithdrawValuePage(playerRef));
    }
}