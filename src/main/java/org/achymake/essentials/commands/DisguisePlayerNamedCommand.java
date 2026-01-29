package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ModelAssetHandler;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;

public class DisguisePlayerNamedCommand extends AbstractPlayerCommand {
    private static final Message MESSAGE_COMMANDS_ERRORS_PLAYER_NOT_IN_WORLD = Message.translation("server.commands.errors.playerNotInWorld");
    @Nonnull
    private final RequiredArg<PlayerRef> playerRefRequiredArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    public DisguisePlayerNamedCommand() {
        super("Disguise into other player");
        playerRefRequiredArg = withRequiredArg("player", "Disguise as other player", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.disguise.player");
    }
    protected void execute(@Nonnull CommandContext context,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var targetPlayerRef = playerRefRequiredArg.get(context);
        var player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        var targetRef = targetPlayerRef.getReference();
        if (targetRef != null && targetRef.isValid()) {
            getModelAssetHandler().setDisguised(ref, targetRef);
        } else context.sendMessage(MESSAGE_COMMANDS_ERRORS_PLAYER_NOT_IN_WORLD);
    }
}