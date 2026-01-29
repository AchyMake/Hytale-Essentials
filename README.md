JitPack.io Check Repository = https://jitpack.io/#AchyMake/Hytale-Essentials
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.AchyMake</groupId>
            <artifactId>Hytale-Essentials</artifactId>
            <version>LATEST</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```
Example for getting Userdata

```java
package org.example.yourplugin;

import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class YourPlugin extends JavaPlugin {
    private static YourPlugin instance;
    
    public YourPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getEssentials().getUserdata().isPVP(offlinePlayer);
    }

    public void setPVP(PlayerRef playerRef, boolean value) {
        getEssentials().getPlayerHandler().setPVP(playerRef, value);
    }

    public boolean isVanished(PlayerRef playerRef) {
        return getEssentials().getUserdata().isVanished(playerRef);
    }

    public void toggleVanish(PlayerRef playerRef) {
        getEssentials().getVanishHandler().setVanish(playerRef, !isVanished(playerRef));
    }

    public void setVanished(PlayerRef playerRef, boolean value) {
        getEssentials().getVanishHandler().setVanish(playerRef, value);
    }

    public Essentials getEssentials() {
        return Essentials.getInstance();
    }

    public static YourPlugin getInstance() {
        return instance;
    }
}
```
