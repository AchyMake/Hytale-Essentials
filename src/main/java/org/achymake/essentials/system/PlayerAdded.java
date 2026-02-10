package org.achymake.essentials.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.player.HiddenPlayersManager;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.components.Account;
import org.achymake.essentials.components.Death;
import org.achymake.essentials.components.Homes;
import org.achymake.essentials.handlers.PlayerHandler;
import org.achymake.essentials.handlers.UniverseHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.Color;

public class PlayerAdded extends RefSystem<EntityStore> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    @Override
    public void onEntityAdded(@NonNullDecl Ref<EntityStore> ref,
                              @NonNullDecl AddReason addReason,
                              @NonNullDecl Store<EntityStore> store,
                              @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var username = commandBuffer.getComponent(ref, Nameplate.getComponentType()).getText();
        if (commandBuffer.getComponent(ref, getInstance().getAccountComponentType()) == null) {
            commandBuffer.addComponent(ref, getInstance().getAccountComponentType(), new Account());
            HytaleLogger.getLogger().atInfo().log(username + " Account Component Added");
        }
        if (commandBuffer.getComponent(ref, getInstance().getDeathComponentType()) == null) {
            commandBuffer.addComponent(ref, getInstance().getDeathComponentType(), new Death());
            HytaleLogger.getLogger().atInfo().log(username + " Death Location Component Added");
        }
        if (commandBuffer.getComponent(ref, getInstance().getHomesComponentType()) == null) {
            commandBuffer.addComponent(ref, getInstance().getHomesComponentType(), new Homes());
            HytaleLogger.getLogger().atInfo().log(username + " Homes Component Added");
        }
    }
    @Override
    public void onEntityRemove(@NonNullDecl Ref<EntityStore> ref,
                               @NonNullDecl RemoveReason removeReason,
                               @NonNullDecl Store<EntityStore> store,
                               @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var uuid = commandBuffer.getComponent(ref, UUIDComponent.getComponentType()).getUuid();
        var hiddenManager = new HiddenPlayersManager();
        if (hiddenManager.isPlayerHidden(uuid))return;
        var username = commandBuffer.getComponent(ref, Nameplate.getComponentType()).getText();
        if (PermissionsModule.get().hasPermission(uuid, "essentials.event.quit.message")) {
            getUniverseHandler().sendAll(Message.join(
                    Message.raw(username + " "),
                    Message.raw("has left the Server [").color(Color.ORANGE),
                    Message.raw("-").color(Color.RED),
                    Message.raw("]").color(Color.ORANGE)
            ));
        } else getUniverseHandler().sendAll(Message.join(
                Message.raw(username + " "),
                Message.raw("has left the Server").color(Color.GRAY)
        ), "essentials.event.quit.notify");
    }
    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}