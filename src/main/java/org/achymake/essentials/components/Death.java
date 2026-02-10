package org.achymake.essentials.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class Death implements Component<EntityStore> {
    private String worldName;
    private double x;
    private double y;
    private double z;
    public static final BuilderCodec<Death> CODEC =
            BuilderCodec.builder(Death.class, Death::new)
                    .append(new KeyedCodec<>("WorldName", Codec.STRING),
                            (death, string) -> death.worldName = string,
                            death -> death.worldName)
                    .add()
                    .append(new KeyedCodec<>("X", Codec.DOUBLE),
                            (death, Double) -> death.x = Double,
                            death -> death.x)
                    .add()
                    .append(new KeyedCodec<>("Y", Codec.DOUBLE),
                            (death, Double) -> death.y = Double,
                            death -> death.y)
                    .add()
                    .append(new KeyedCodec<>("Z", Codec.DOUBLE),
                            (death, Double) -> death.z = Double,
                            death -> death.z)
                    .add()
                    .build();
    public Death() {
        worldName = "default";
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }
    public Death(Death clone) {
        worldName = clone.worldName;
        x = clone.x;
        y = clone.y;
        z = clone.z;
    }
    public String getWorldName() {
        return worldName;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new Death(this);
    }
}