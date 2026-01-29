package org.achymake.essentials.system;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class DamageEvent extends EntityEventSystem<EntityStore, Damage> {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private PlayerHandler getPlayerHandler() {
        return getInstance().getPlayerHandler();
    }
    public DamageEvent() {
        super(Damage.class);
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
                       @NonNullDecl Damage event) {
        var ref = archetypeChunk.getReferenceTo(index);
        var playerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null)return;
        if (event.getSource() instanceof Damage.EntitySource entitySource) {
            var attacker = store.getComponent(entitySource.getRef(), PlayerRef.getComponentType());
            if (attacker == null)return;
            if (!store.getExternalData().getWorld().getWorldConfig().isPvpEnabled())return;
            if (!attacker.isValid())return;
            if (!getPlayerHandler().isPVP(attacker)) {
                event.setCancelled(true);
                event.setAmount(0);
                attacker.sendMessage(Message.raw("Seems like you have disabled PVP").color(Color.RED));
            } else if (!getPlayerHandler().isPVP(playerRef)) {
                event.setCancelled(true);
                event.setAmount(0);
                attacker.sendMessage(Message.join(
                        Message.raw("Seems like ").color(Color.RED),
                        Message.raw(playerRef.getUsername() + " "),
                        Message.raw("has disabled PVP").color(Color.RED)
                ));
            }
        }
    }
}