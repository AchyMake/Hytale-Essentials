package org.achymake.essentials.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Homes implements Component<EntityStore> {
    private Map<String, String> worldMap;
    private Map<String, Double> doubleMap;
    private Map<String, Float> floatMap;
    public static final BuilderCodec<Homes> CODEC =
            BuilderCodec.builder(Homes.class, Homes::new)
                    .append(new KeyedCodec<>("WorldMap", new MapCodec<>(Codec.STRING, HashMap::new, false)),
                            (homes, stringStringMap) -> homes.worldMap = stringStringMap,
                            homes -> homes.worldMap)
                    .add()
                    .append(new KeyedCodec<>("DoubleMap", new MapCodec<>(Codec.DOUBLE, HashMap::new, false)),
                            (homes, stringDoubleMap) -> homes.doubleMap = stringDoubleMap,
                            homes -> homes.doubleMap)
                    .add()
                    .append(new KeyedCodec<>("FloatMap", new MapCodec<>(Codec.FLOAT, HashMap::new, false)),
                            (homes, stringFloatMap) -> homes.floatMap = stringFloatMap,
                            homes -> homes.floatMap)
                    .add()
                    .build();
    public Homes() {
        worldMap = new HashMap<>();
        doubleMap = new HashMap<>();
        floatMap = new HashMap<>();
    }
    public Homes(Homes clone) {
        worldMap = clone.worldMap;
        doubleMap = clone.doubleMap;
        floatMap = clone.floatMap;
    }
    public Set<String> getHomes() {
        return worldMap.keySet();
    }
    public boolean exists(String homeName) {
        return worldMap.containsKey(homeName);
    }
    public void setHome(String homeName, World world, Transform transform) {
        var pos = transform.getPosition();
        var rot = transform.getRotation();
        worldMap.put(homeName, world.getName());
        doubleMap.put(homeName + "-x", pos.x);
        doubleMap.put(homeName + "-y", pos.y);
        doubleMap.put(homeName + "-z", pos.z);
        floatMap.put(homeName + "-x", rot.x);
        floatMap.put(homeName + "-y", rot.y);
        floatMap.put(homeName + "-z", rot.z);
    }
    public void delHome(String homeName) {
        if (exists(homeName)) {
            worldMap.remove(homeName);
            doubleMap.remove(homeName + "-x");
            doubleMap.remove(homeName + "-y");
            doubleMap.remove(homeName + "-z");
            floatMap.remove(homeName + "-x");
            floatMap.remove(homeName + "-y");
            floatMap.remove(homeName + "-z");
        }
    }
    public Teleport getHome(String homeName) {
        if (exists(homeName)) {
            var world = Universe.get().getWorld(worldMap.get(homeName));
            var x = doubleMap.get(homeName + "-x");
            var y = doubleMap.get(homeName + "-y");
            var z = doubleMap.get(homeName + "-z");
            var pitch = floatMap.get(homeName + "-x");
            var yaw = floatMap.get(homeName + "-y");
            var roll = floatMap.get(homeName + "-z");
            return Teleport.createForPlayer(world, new Vector3d(x, y, z), new Vector3f(pitch, yaw, roll));
        } else return null;
    }
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new Homes(this);
    }
}
