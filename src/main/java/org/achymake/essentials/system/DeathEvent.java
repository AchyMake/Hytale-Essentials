package org.achymake.essentials.system;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.components.Death;
import org.achymake.essentials.handlers.UniverseHandler;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.Objects;

public class DeathEvent extends DeathSystems.OnDeathSystem {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return PlayerRef.getComponentType();
    }
    @Override
    public void onComponentAdded(@Nonnull Ref ref,
                                 @Nonnull DeathComponent component,
                                 @Nonnull Store store,
                                 @Nonnull CommandBuffer commandBuffer) {
        var playerRef = (PlayerRef) store.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null)return;
        var cause = component.getDeathCause();
        if (cause == null)return;
        var username = playerRef.getUsername();
        if (component.getDeathInfo().getSource() instanceof Damage.EntitySource entitySource) {
            var playerAttacker = (PlayerRef) store.getComponent(entitySource.getRef(), PlayerRef.getComponentType());
            if (playerAttacker != null) {
                getUniverseHandler().sendAll(Message.join(
                        Message.raw(username + " "),
                        Message.raw("got killed by ").color(Color.RED),
                        Message.raw(playerAttacker.getUsername())
                ));
            } else {
                var attacker = (NPCEntity) store.getComponent(entitySource.getRef(), Objects.requireNonNull(NPCEntity.getComponentType()));
                if (attacker != null) {
                    getUniverseHandler().sendAll(Message.join(
                            Message.raw(username + " "),
                            Message.raw("got killed by ").color(Color.RED),
                            Message.raw(attacker.getNPCTypeId())
                    ));
                } else getUniverseHandler().sendAll(Message.join(
                        Message.raw(username + " "),
                        Message.raw("died from ").color(Color.RED),
                        Message.raw(cause.getId())
                ));
            }
        } else getUniverseHandler().sendAll(Message.join(
                Message.raw(username + " "),
                Message.raw("died from ").color(Color.RED),
                Message.raw(cause.getId())
        ));
        var death = (Death) store.getComponent(ref, getInstance().getDeathComponentType());
        if (death != null) {
            var pos = playerRef.getTransform().getPosition();
            death.setWorldName(getUniverseHandler().getWorld(playerRef.getWorldUuid()).getName());
            death.setX(pos.x);
            death.setY(pos.y);
            death.setZ(pos.z);
        }
    }
}