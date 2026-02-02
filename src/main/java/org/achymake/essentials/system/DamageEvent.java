package org.achymake.essentials.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.RootDependency;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.PlayerHandler;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;
import java.util.Collections;
import java.util.Set;

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
    @NonNullDecl
    public Set<Dependency<EntityStore>> getDependencies() {
        return Collections.singleton(RootDependency.first());
    }
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }
    @Override
    public void handle(int index,
                       @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk,
                       @NonNullDecl Store<EntityStore> store,
                       @NonNullDecl CommandBuffer<EntityStore> commandBuffer,
                       @NonNullDecl Damage event) {
        if (!store.getExternalData().getWorld().getWorldConfig().isPvpEnabled())return;
        var ref = archetypeChunk.getReferenceTo(index);
        var victimPlayerRef = store.getComponent(ref, PlayerRef.getComponentType());
        if (victimPlayerRef == null)return;
        if (event.getSource() instanceof Damage.EntitySource entitySource) {
            var attackerRef = entitySource.getRef();
            if (!attackerRef.isValid())return;
            var attackerStore = attackerRef.getStore();
            var attackerPlayerRef = attackerStore.getComponent(attackerRef, PlayerRef.getComponentType());
            if (attackerPlayerRef == null)return;
            if (!getPlayerHandler().isPVP(attackerPlayerRef)) {
                event.setCancelled(true);
                attackerPlayerRef.sendMessage(Message.raw("Seems like you have disabled PVP").color(Color.RED));
            } else if (!getPlayerHandler().isPVP(victimPlayerRef)) {
                event.setCancelled(true);
                attackerPlayerRef.sendMessage(Message.join(
                        Message.raw("Seems like ").color(Color.RED),
                        Message.raw(victimPlayerRef.getUsername() + " "),
                        Message.raw("has disabled PVP").color(Color.RED)
                ));
            }
        }
    }
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getFilterDamageGroup();
    }
}