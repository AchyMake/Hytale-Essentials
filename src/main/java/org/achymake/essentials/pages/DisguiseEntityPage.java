package org.achymake.essentials.pages;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ModelAssetHandler;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.Color;

public class DisguiseEntityPage extends InteractiveCustomUIPage<DisguiseEntityPage.DisguiseEntityEventData> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    public static class DisguiseEntityEventData {
        public String entityModelID;
        public static final BuilderCodec<DisguiseEntityEventData> CODEC =
                BuilderCodec.builder(DisguiseEntityEventData.class, DisguiseEntityEventData::new)
                        .append(
                                new KeyedCodec<>("@EntityID", Codec.STRING),
                                (DisguiseEntityEventData obj, String val) -> obj.entityModelID = val,
                                obj -> obj.entityModelID
                        )
                        .add()
                        .build();
    }
    public DisguiseEntityPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, DisguiseEntityEventData.CODEC);
    }
    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/DisguiseEntityPage.ui");
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#DisguiseButton",
                new EventData().append("@EntityID", "#NameInput.Value")
        );
    }
    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull DisguiseEntityEventData data) {
        var player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        var entered = data.entityModelID;
        if (entered != null && !entered.isEmpty()) {
            if (!entered.equalsIgnoreCase("reset")) {
                var modelAsset = getModelAssetHandler().getAsset(entered.replace(" ", "_"));
                if (modelAsset != null) {
                    if (player.hasPermission("essentials.command.disguise." + modelAsset.getId().toLowerCase())) {
                        getModelAssetHandler().setDisguised(ref, modelAsset);
                    } else player.sendMessage(Message.join(
                            Message.raw("Seems like you don't have permission to disguise as ").color(Color.RED),
                            Message.raw(entered.replace("_", " "))
                    ));
                } else player.sendMessage(Message.join(
                        Message.raw("Seems like ").color(Color.RED),
                        Message.raw(entered.replace("_", " ") + " "),
                        Message.raw("does not exists").color(Color.RED)
                ));
            } else getModelAssetHandler().unDisguise(ref);
        } else player.sendMessage(Message.raw("Seems like you didn't type anything").color(Color.RED));
        player.getPageManager().setPage(ref, store, Page.None);
    }
}