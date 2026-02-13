package org.achymake.essentials.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.components.Account;
import org.achymake.essentials.components.Death;
import org.achymake.essentials.components.Homes;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerAdded extends RefSystem<EntityStore> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    @Override
    public void onEntityAdded(@NonNullDecl Ref<EntityStore> ref,
                              @NonNullDecl AddReason addReason,
                              @NonNullDecl Store<EntityStore> store,
                              @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        String username = "";
        if (playerRef != null) {
            username = playerRef.getUsername();
        }
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
        var playerRef = commandBuffer.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef != null) {
            var packetHandler = playerRef.getPacketHandler();
            packetHandler.setQueuePackets(false);
            packetHandler.tryFlush();
        }
    }
    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
}