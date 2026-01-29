package org.achymake.essentials.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.console.ConsoleSender;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.TeleportHandler;
import org.achymake.essentials.handlers.UniverseHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;

public class CustomInteraction extends SimpleInstantInteraction {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public static final BuilderCodec<CustomInteraction> CODEC = BuilderCodec.builder(
            CustomInteraction.class, CustomInteraction::new, SimpleInstantInteraction.CODEC
    ).build();
    @Override
    protected void firstRun(@NonNullDecl InteractionType interactionType,
                            @NonNullDecl InteractionContext interactionContext,
                            @NonNullDecl CooldownHandler cooldownHandler) {
        var ref = interactionContext.getEntity();
        var store = ref.getStore();
        var player = store.getComponent(ref, Player.getComponentType());
        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        assert playerRef != null;
        var username = playerRef.getUsername();
        assert player != null;
        var slot = interactionContext.getHeldItemSlot();
        var itemStack = interactionContext.getHeldItem();
        assert itemStack != null;
        var itemID = itemStack.getItemId();
        var heldItemContainer = interactionContext.getHeldItemContainer();
        assert heldItemContainer != null;
        switch (itemID) {
            case "Deco_Scroll_Death_Location" -> {
                var deathTP = getPlayerHandler().getDeath(playerRef.getUuid());
                if (deathTP != null) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    var message = Message.join(
                            Message.raw("Teleporting to ").color(Color.ORANGE),
                            Message.raw("Death Location")
                    );
                    getTeleportHandler().teleport(playerRef, deathTP, message);
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like your death location doesn't exist").color(Color.RED));
            }
            case "Deco_Scroll_Bed_Location" -> {
                heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                var world = store.getExternalData().getWorld();
                var message = Message.join(
                        Message.raw("Teleporting to ").color(Color.ORANGE),
                        Message.raw("Bed")
                );
                Player.getRespawnPosition(ref, world.getName(), store).thenAccept(transform ->
                        getTeleportHandler().teleport(playerRef, getTeleportHandler().createForPlayer(world, transform), message));
                getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
            }
            case "Deco_Scroll_Disguise_Kweebec_Rootling" -> {
                if (!player.hasPermission("essentials.command.disguise.kweebec_rootling")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.disguise.kweebec_rootling true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Sethome_One" -> {
                if (!player.hasPermission("essentials.command.sethome.default_2")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.sethome.default_2 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                }
            }
            case "Deco_Scroll_Sethome_Two" -> {
                if (!player.hasPermission("essentials.command.sethome.default_3")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.sethome.default_3 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Sethome_Three" -> {
                if (!player.hasPermission("essentials.command.sethome.default_4")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.sethome.default_4 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Sethome_Four" -> {
                if (!player.hasPermission("essentials.command.sethome.default_5")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.sethome.default_5 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Sethome_Five" -> {
                if (!player.hasPermission("essentials.command.sethome.default_6")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.sethome.default_6 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Sethome_Six" -> {
                if (!player.hasPermission("essentials.command.sethome.default_7")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set essentials.command.sethome.default_7 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Claims_One" -> {
                if (!player.hasPermission("chunks.command.claim.default_2")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set chunks.command.claim.default_2 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Claims_Two" -> {
                if (!player.hasPermission("chunks.command.claim.default_3")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set chunks.command.claim.default_3 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
            case "Deco_Scroll_Claims_Three" -> {
                if (!player.hasPermission("chunks.command.claim.default_4")) {
                    heldItemContainer.removeItemStackFromSlot(slot, itemStack, 1);
                    CommandManager.get().handleCommand(ConsoleSender.INSTANCE, "lp user " + username + " permission set chunks.command.claim.default_4 true");
                    getUniverseHandler().playSound(playerRef, "SFX_Discovery_Z4_Short");
                } else playerRef.sendMessage(Message.raw("Seems like you already have the permission").color(Color.RED));
            }
        }
    }
}