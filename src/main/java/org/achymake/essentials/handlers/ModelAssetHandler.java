package org.achymake.essentials.handlers;

import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.cosmetics.CosmeticsModule;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSkinComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;

public class ModelAssetHandler {
    private final ArrayList<Ref<EntityStore>> disguised = new ArrayList<>();
    public ModelAsset getAsset(String key) {
        return ModelAsset.getAssetMap().getAsset(key);
    }
    public PlayerSkinComponent getSkin(Ref<EntityStore> ref) {
        return ref.getStore().getComponent(ref, PlayerSkinComponent.getComponentType());
    }
    public void setDisguised(Ref<EntityStore> ref, ModelAsset modelAsset) {
        ref.getStore().putComponent(ref, ModelComponent.getComponentType(), new ModelComponent(Model.createUnitScaleModel(modelAsset)));
        getDisguised().add(ref);
    }
    public void setDisguised(Ref<EntityStore> ref, Ref<EntityStore> targetRef) {
        var store = ref.getStore();
        var playerSkinComponent = getSkin(targetRef);
        if (playerSkinComponent != null) {
            playerSkinComponent.setNetworkOutdated();
            store.putComponent(ref, ModelComponent.getComponentType(), new ModelComponent(CosmeticsModule.get().createModel(playerSkinComponent.getPlayerSkin())));
            getDisguised().add(ref);
        }
    }
    public void unDisguise(Ref<EntityStore> ref) {
        var playerSkinComponent = getSkin(ref);
        if (playerSkinComponent != null) {
            playerSkinComponent.setNetworkOutdated();
            ref.getStore().putComponent(ref, ModelComponent.getComponentType(), new ModelComponent(CosmeticsModule.get().createModel(playerSkinComponent.getPlayerSkin())));
            getDisguised().remove(ref);
        }
    }
    public boolean isDisguised(Ref<EntityStore> ref) {
        return getDisguised().contains(ref);
    }
    public ArrayList<Ref<EntityStore>> getDisguised() {
        return disguised;
    }
}