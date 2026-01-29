package org.achymake.essentials.handlers;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.EssentialsConfig;
import org.achymake.essentials.files.Spawn;

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
    public World getDefault() {
        return getUniverse().getDefaultWorld();
    }
    public Transform getDefaultSpawn() {
        var defaultWorld = getDefault();
        assert defaultWorld != null;
        var worldConfig = defaultWorld.getWorldConfig();
        var spawnProvider = worldConfig.getSpawnProvider();
        assert spawnProvider != null;
        var worldUUID = worldConfig.getUuid();
        return spawnProvider.getSpawnPoint(defaultWorld, worldUUID);
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
    public Collection<PlayerRef> getPlayers() {
        return getUniverse().getPlayers();
    }
    public void sendAll(Message message) {
        getUniverse().sendMessage(message);
    }
    public void sendAll(Message message, String permission) {
        var players = getPlayers();
        if (players.isEmpty())return;
        for (var playerRef : players) {
            var ref = playerRef.getReference();
            assert ref != null;
            var store = ref.getStore();
            var player = store.getComponent(ref, Player.getComponentType());
            assert player != null;
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }
    public World getSpawnWorld() {
        try (var writer = getFileHandler().getFileReader("mods/Essentials/config.json")) {
            var json = getFileHandler().getGson().fromJson(writer, EssentialsConfig.class);
            return getWorld(json.SpawnWorld());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setSpawn(World world, Transform transform) {
        var worldName = world.getName();
        var pos = transform.getPosition();
        var posX = pos.x;
        var posY = pos.y;
        var posZ = pos.z;
        var headRotation = transform.getRotation();
        var pitch = headRotation.getPitch();
        var yaw = headRotation.getYaw();
        var roll = headRotation.getRoll();
        try (var writer = getInstance().getFileHandler().getFileWriter("mods/Essentials/spawn.json")) {
            getInstance().getFileHandler().getGson().toJson(new Spawn(worldName, posX, posY, posZ, pitch, yaw, roll), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}