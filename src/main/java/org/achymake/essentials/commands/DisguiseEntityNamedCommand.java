package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MessageHandler;
import org.achymake.essentials.handlers.ModelAssetHandler;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.Color;

public class DisguiseEntityNamedCommand extends AbstractPlayerCommand {
    @Nonnull
    private final RequiredArg<ModelAsset> modelAssetArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MessageHandler getMessageHandler() {
        return getInstance().getMessageHandler();
    }
    private ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    public DisguiseEntityNamedCommand() {
        super("Disguise into other entities");
        modelAssetArg = withRequiredArg("model", "an EntityType/Model_Asset", ArgTypes.MODEL_ASSET);
        requirePermission("essentials.command.disguise");
    }
    protected void execute(@Nonnull CommandContext context,
                           @Nonnull Store<EntityStore> store,
                           @Nonnull Ref<EntityStore> ref,
                           @Nonnull PlayerRef playerRef,
                           @Nonnull World world) {
        var modelAsset = modelAssetArg.get(context);
        if (context.sender().hasPermission("essentials.command.disguise." + modelAsset.getId().toLowerCase())) {
            getModelAssetHandler().setDisguised(ref, modelAsset);
            playerRef.sendMessage(Message.join(
                    Message.raw("You are now disguised as ").color(Color.ORANGE),
                    Message.raw(getMessageHandler().toTitleCase(modelAsset.getId()))
            ));
        } else playerRef.sendMessage(Message.join(
                Message.raw("Seems like you don't have permission to disguise as ").color(Color.RED),
                Message.raw(getMessageHandler().toTitleCase(modelAsset.getId()))
        ));
    }
}