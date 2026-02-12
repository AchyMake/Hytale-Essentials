package org.achymake.essentials.pages;

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
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EconomyHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class WithdrawValuePage extends InteractiveCustomUIPage<WithdrawValuePage.WithdrawValueEventData> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EconomyHandler getEconomyHandler() {
        return getInstance().getEconomyHandler();
    }
    public static class WithdrawValueEventData {
        public String integer;
        public static final BuilderCodec<WithdrawValueEventData> CODEC =
                BuilderCodec.builder(WithdrawValueEventData.class, WithdrawValueEventData::new)
                        .append(
                                new KeyedCodec<>("@NumberID", Codec.STRING),
                                (WithdrawValueEventData obj, String val) -> obj.integer = val,
                                obj -> obj.integer
                        )
                        .add()
                        .build();
    }
    public WithdrawValuePage(@Nonnull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, WithdrawValueEventData.CODEC);
    }
    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        cmd.append("Pages/WithdrawValuePage.ui");
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#WithdrawButton",
                new EventData().append("@NumberID", "#NameInput.Value")
        );
    }
    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull WithdrawValueEventData data) {
        var player = store.getComponent(ref, Player.getComponentType());
        if (player != null) {
            var inventory = player.getInventory();
            var enteredString = data.integer;
            if (enteredString != null && !enteredString.isEmpty()) {
                try {
                    var entered = Integer.parseInt(data.integer);
                    if (entered > 0 && 1000 >= entered) {
                        var formatted = getEconomyHandler().format(entered);
                        if (getEconomyHandler().has(playerRef, entered)) {
                            getEconomyHandler().remove(playerRef, entered);
                            inventory.getCombinedHotbarFirst().addItemStack(new ItemStack("Coins", entered));
                            playerRef.sendMessage(Message.join(
                                    Message.raw("You withdrew ").color(Color.ORANGE),
                                    Message.raw(formatted)
                            ));
                        } else playerRef.sendMessage(Message.join(
                                Message.raw("Seems like you don't have ").color(Color.RED),
                                Message.raw(formatted)
                        ));
                    } else player.sendMessage(Message.raw("Seems like you tried to input an inadequate amount").color(Color.RED));
                } catch (NumberFormatException e) {
                    playerRef.sendMessage(Message.raw("Seems like you input string instead of integer").color(Color.RED));
                    player.getPageManager().setPage(ref, store, Page.None);
                }
            } else playerRef.sendMessage(Message.raw("Seems like you didn't type anything").color(Color.RED));
            player.getPageManager().setPage(ref, store, Page.None);
        }
    }
}