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
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.awt.Color;

public class DisguisePlayerPage extends InteractiveCustomUIPage<DisguisePlayerPage.DisguisePlayerEventData> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    public static class DisguisePlayerEventData {
        public String username;
        public static final BuilderCodec<DisguisePlayerEventData> CODEC =
                BuilderCodec.builder(DisguisePlayerEventData.class, DisguisePlayerEventData::new)
                        .append(
                                new KeyedCodec<>("@Username", Codec.STRING),
                                (DisguisePlayerEventData obj, String val) -> obj.username = val,
                                obj -> obj.username
                        )
                        .add()
                        .build();
    }
    public DisguisePlayerPage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, DisguisePlayerEventData.CODEC);
    }
    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/DisguisePlayerPage.ui");
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#DisguiseButton",
                new EventData().append("@Username", "#NameInput.Value")
        );
    }
    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull DisguisePlayerEventData data) {
        var player = store.getComponent(ref, Player.getComponentType());
        assert player != null;
        var entered = data.username;
        if (entered != null && !entered.isEmpty()) {
            if (!entered.equalsIgnoreCase("reset")) {
                var targetPlayerRef = Universe.get().getPlayer(entered, NameMatching.EXACT_IGNORE_CASE);
                if (targetPlayerRef != null) {
                    var targetRef = targetPlayerRef.getReference();
                    assert targetRef != null;
                    getModelAssetHandler().setDisguised(ref, targetRef);
                } else player.sendMessage(Message.join(
                        Message.raw("Seems like ").color(Color.RED),
                        Message.raw(entered + " "),
                        Message.raw("is not online").color(Color.RED)
                ));
            } else getModelAssetHandler().unDisguise(ref);
        } else player.sendMessage(Message.raw("Seems like you didn't type anything").color(Color.RED));
        player.getPageManager().setPage(ref, store, Page.None);
    }
}