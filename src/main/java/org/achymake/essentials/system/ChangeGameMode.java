package org.achymake.essentials.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.ecs.ChangeGameModeEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MessageHandler;
import org.achymake.essentials.handlers.UniverseHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class ChangeGameMode extends EntityEventSystem<EntityStore, ChangeGameModeEvent> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MessageHandler getMessageHandler() {
        return getInstance().getMessageHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public ChangeGameMode() {
        super(ChangeGameModeEvent.class);
    }
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
    @Override
    public void handle(int index,
                       @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
                       @NonNullDecl Store<EntityStore> store,
                       @NonNullDecl CommandBuffer<EntityStore> commandBuffer,
                       @NonNullDecl ChangeGameModeEvent event) {
        var ref = archetypeChunk.getReferenceTo(index);
        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null)return;
        var mode = getMessageHandler().toTitleCase(event.getGameMode().name());
        var message = Message.join(
                Message.raw(playerRef.getUsername()),
                Message.raw(" has changed mode to ").color(Color.ORANGE),
                Message.raw(mode)
        );
        getUniverseHandler().sendAll(message, "essentials.event.gamemode.notify");
        getMessageHandler().sendTitle(playerRef, Message.raw(mode), Message.raw("Game Mode"), false);
    }
}