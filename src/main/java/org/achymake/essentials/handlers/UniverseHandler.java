package org.achymake.essentials.handlers;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.playerdata.PlayerStorage;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.EssentialsConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UniverseHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    public Universe getUniverse() {
        return Universe.get();
    }
    public Collection<World> getWorlds() {
        return getUniverse().getWorlds().values();
    }
    public World getWorld(String worldName) {
        return getUniverse().getWorld(worldName);
    }
    public World getWorld(UUID uuid) {
        return getUniverse().getWorld(uuid);
    }
    public Collection<PlayerRef> getPlayers() {
        return getUniverse().getPlayers();
    }
    public PlayerStorage getPlayerStorage() {
        return getUniverse().getPlayerStorage();
    }
    public void sendAll(Message message) {
        getUniverse().sendMessage(message);
    }
    public void sendAll(Message message, String permission) {
        var players = getPlayers();
        if (players.isEmpty())return;
        for (var playerRef : players) {
            var uuid = playerRef.getUuid();
            if (PermissionsModule.get().hasPermission(uuid, permission)) {
                playerRef.sendMessage(message);
            }
        }
    }
    public World getSpawnWorld() {
        try (var writer = getFileHandler().getFileReader("mods/Essentials/config.json")) {
            var json = getFileHandler().getGson().fromJson(writer, EssentialsConfig.class);
            return getWorld(json.SpawnWorld());
        } catch (IOException e) {
            return null;
        }
    }
    public Transform getSpawnPoint() {
        return getSpawnWorld().getWorldConfig().getSpawnProvider().getSpawnPoint(getSpawnWorld(), getSpawnWorld().getWorldConfig().getUuid());
    }
    public Teleport getSpawn() {
        var world = getSpawnWorld();
        if (world != null) {
            var worldConfig = world.getWorldConfig();
            var spawnProvider = worldConfig.getSpawnProvider();
            if (spawnProvider != null) {
                return Teleport.createForPlayer(world, getSpawnPoint());
            } else return null;
        } else return null;
    }
    public void playSound(PlayerRef playerRef, String soundName, float volume, float pitch) {
        var sound = SoundEvent.getAssetMap().getIndex(soundName);
        assert playerRef.getWorldUuid() != null;
        getScheduleHandler().schedule(() -> {
            Objects.requireNonNull(getUniverse().getWorld(playerRef.getWorldUuid())).execute(() -> SoundUtil.playSoundEvent2dToPlayer(playerRef, sound, SoundCategory.UI, volume, pitch));
        }, 100, TimeUnit.MILLISECONDS);
    }
    public void playSound(PlayerRef playerRef, String soundName) {
        var sound = SoundEvent.getAssetMap().getIndex(soundName);
        assert playerRef.getWorldUuid() != null;
        getScheduleHandler().schedule(() -> {
            Objects.requireNonNull(getUniverse().getWorld(playerRef.getWorldUuid())).execute(() -> SoundUtil.playSoundEvent2dToPlayer(playerRef, sound, SoundCategory.UI));
        }, 100, TimeUnit.MILLISECONDS);
    }
    public void playSound(World world, Vector3d position, String soundName) {
        var sound = SoundEvent.getAssetMap().getIndex(soundName);
        var store = world.getEntityStore().getStore();
        getScheduleHandler().schedule(() -> world.execute(() -> SoundUtil.playSoundEvent3d(sound, SoundCategory.UI, position, store)), 100, TimeUnit.MILLISECONDS);
    }
}