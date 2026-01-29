package org.achymake.essentials.handlers;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.Spawn;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TeleportHandler {
    private final Set<PlayerRef> ignored = new HashSet<>();
    private final HashMap<PlayerRef, PlayerRef> tpa = new HashMap<>();
    private final HashMap<PlayerRef, PlayerRef> reversed = new HashMap<>();
    private final HashMap<PlayerRef, ScheduledFuture<?>> tpScheduled = new HashMap<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private ScheduleHandler getScheduleHandler() {
        return getInstance().getScheduleHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public boolean isInRequest(PlayerRef playerRef) {
        return getTPA().containsValue(playerRef) || getTPA().containsKey(playerRef) || getTpScheduled().containsKey(playerRef);
    }
    public boolean isIgnored(PlayerRef playerRef) {
        return getIgnored().contains(playerRef);
    }
    public ScheduledFuture<?> getTPScheduled(PlayerRef playerRef) {
        return getTpScheduled().getOrDefault(playerRef, null);
    }
    public void teleport(PlayerRef playerRef, Teleport teleport, Message message, int delay) {
        var ref = playerRef.getReference();
        if (ref == null)return;
        if (!isInRequest(playerRef)) {
            var clonedPos = playerRef.getTransform().getPosition().clone();
            var clonedX = (int) clonedPos.x;
            var clonedY = (int) clonedPos.y;
            var clonedZ = (int) clonedPos.z;
            var scheduledTask = getScheduleHandler().schedule(() -> {
                getTpScheduled().remove(playerRef);
                var pos = playerRef.getTransform().getPosition();
                if ((int) pos.x == clonedX && (int) pos.y == clonedY && (int) pos.z == clonedZ) {
                    teleport(playerRef, teleport, message);
                } else playerRef.sendMessage(Message.raw("Seems like you have moved").color(Color.RED));
            }, delay, TimeUnit.SECONDS);
            playerRef.sendMessage(Message.join(
                    Message.raw("Teleporting in ").color(Color.ORANGE),
                    Message.raw(delay + " "),
                    Message.raw("sec").color(Color.ORANGE)
            ));
            getTpScheduled().put(playerRef, scheduledTask);
        } else playerRef.sendMessage(Message.raw("Seems like you already are in a request").color(Color.RED));
    }
    public void teleport(PlayerRef playerRef, Teleport teleport, Message message) {
        var ref = playerRef.getReference();
        if (ref == null)return;
        var store = ref.getStore();
        var playerRefWorld = store.getExternalData().getWorld();
        playerRef.sendMessage(message);
        playerRefWorld.execute(() -> store.addComponent(ref, Teleport.getComponentType(), teleport));
    }
    public void cancel(PlayerRef playerRef) {
        if (getTpScheduled().containsKey(playerRef)) {
            var scheduledTask = getTPScheduled(playerRef);
            if (scheduledTask != null) {
                scheduledTask.cancel(true);
            }
        }
        if (getTPA().containsKey(playerRef)) {
            var targetRef = getTPA().get(playerRef);
            getTpScheduled().remove(targetRef);
            getTPA().remove(targetRef);
            getReversed().remove(targetRef);
        } else if (getTPA().containsValue(playerRef)) {
            var targetRef = getReversed().get(playerRef);
            getTpScheduled().remove(targetRef);
            getTPA().remove(targetRef);
            getReversed().remove(targetRef);
        }
        getTPA().remove(playerRef);
        getReversed().remove(playerRef);
        getTpScheduled().remove(playerRef);
    }
    public Teleport createForPlayer(World world, Transform transform) {
        return Teleport.createForPlayer(world, transform);
    }
    public Teleport getSpawn() {
        if (getFileHandler().getFile("mods/Essentials/spawn.json").exists()) {
            try (var writer = getFileHandler().getFileReader("mods/Essentials/spawn.json")) {
                var json = getFileHandler().getGson().fromJson(writer, Spawn.class);
                var world = getUniverseHandler().getWorld(json.WorldName());
                var posX = json.PosX();
                var posY = json.PosY();
                var posZ = json.PosZ();
                var pitch = json.Pitch();
                var yaw = json.Yaw();
                var roll = json.Roll();
                return Teleport.createForPlayer(world, new Transform(posX, posY, posZ, pitch, yaw, roll));
            } catch (IOException e) {
                return null;
            }
        } else return null;
    }
    public HashMap<PlayerRef, ScheduledFuture<?>> getTpScheduled() {
        return tpScheduled;
    }
    public HashMap<PlayerRef, PlayerRef> getReversed() {
        return reversed;
    }
    public HashMap<PlayerRef, PlayerRef> getTPA() {
        return tpa;
    }
    public Set<PlayerRef> getIgnored() {
        return ignored;
    }
}