package org.achymake.essentials.handlers;

import org.achymake.essentials.files.*;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import org.achymake.essentials.Essentials;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHandler {
    private final ArrayList<PlayerRef> PVP = new ArrayList<>();
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public World getWorld(PlayerRef playerRef) {
        var ref = playerRef.getReference();
        if (ref != null) {
            return ref.getStore().getExternalData().getWorld();
        } else return null;
    }
    public File getUserdataFolder() {
        return getFileHandler().getFile("mods/Essentials/userdata");
    }
    public File getFolder(UUID uuid) {
        return getFileHandler().getFile("mods/Essentials/userdata/" + uuid);
    }
    public File getHomesFolder(UUID uuid) {
        return getFileHandler().getFile("mods/Essentials/userdata/" + uuid + "/homes");
    }
    public File getHomeFile(UUID uuid, String homeName) {
        return getFileHandler().getFile("mods/Essentials/userdata/" + uuid + "/homes/" + homeName + ".json");
    }
    public void setDeath(PlayerRef playerRef, World world) {
        var worldName = world.getName();
        var pos = playerRef.getTransform().getPosition();
        var posX = pos.x;
        var posY = pos.y;
        var posZ = pos.z;
        var headRotation = playerRef.getHeadRotation();
        var pitch = headRotation.getPitch();
        var yaw = headRotation.getYaw();
        var roll = headRotation.getRoll();
        try (var writer = getFileHandler().getFileWriter(getFolder(playerRef.getUuid()) + "/death.json")) {
            getFileHandler().getGson().toJson(new Death(worldName, posX, posY, posZ, pitch, yaw, roll), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Teleport getDeath(UUID uuid) {
        var deathFile = getFileHandler().getFile(getFolder(uuid) + "/death.json");
        if (deathFile.exists()) {
            try (var reader = getFileHandler().getFileReader(getFolder(uuid) + "/death.json")) {
                var json = getFileHandler().getGson().fromJson(reader, Death.class);
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
    public int getMaxHomes(PlayerRef playerRef) {
        var ref = playerRef.getReference();
        var listed = new ArrayList<Integer>();
        if (ref != null) {
            var store = ref.getStore();
            var player = store.getComponent(ref, Player.getComponentType());
            if (player != null) {
                try (var reader = getFileHandler().getFileReader("mods/Essentials/config.json")) {
                    var json = getFileHandler().getGson().fromJson(reader, EssentialsConfig.class).MaxHomes();
                    for (var string : json.keySet()) {
                        if (player.hasPermission("essentials.command.sethome." + string)) {
                            listed.add(json.get(string));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        listed.sort(Integer::compareTo);
        return listed.getLast();
    }
    public void setHome(PlayerRef playerRef, World world, String homeName) {
        var worldName = world.getName();
        var pos = playerRef.getTransform().getPosition();
        var posX = pos.x;
        var posY = pos.y;
        var posZ = pos.z;
        var headRotation = playerRef.getHeadRotation();
        var pitch = headRotation.getPitch();
        var yaw = headRotation.getYaw();
        var roll = headRotation.getRoll();
        try (var writer = getFileHandler().getFileWriter(getHomesFolder(playerRef.getUuid()) + "/" + homeName + ".json")) {
            getFileHandler().getGson().toJson(new SetHome(worldName, posX, posY, posZ, pitch, yaw, roll), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean homeExists(UUID uuid, String homeName) {
        return getHomeFile(uuid, homeName).exists();
    }
    public Teleport getHome(UUID uuid, String homeName) {
        var homesFolder = getHomesFolder(uuid);
        if (homeExists(uuid, homeName)) {
            try (var writer = getFileHandler().getFileReader(homesFolder + "/" + homeName + ".json")) {
                var json = getFileHandler().getGson().fromJson(writer, SetHome.class);
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
    public List<String> getHomes(UUID uuid) {
        var listed = new ArrayList<String>();
        var folder = getHomesFolder(uuid);
        if (folder.exists() && folder.isDirectory()) {
            var files = folder.listFiles();
            if (files != null) {
                for (var file : files) {
                    var fileName = file.getName().replace(".json", "");
                    listed.add(fileName);
                }
            }
        }
        return listed;
    }
    public void setPVP(PlayerRef playerRef, boolean value) {
        if (value) {
            getPVP().add(playerRef);
        } else getPVP().remove(playerRef);
    }
    public boolean isPVP(PlayerRef playerRef) {
        return !getPVP().contains(playerRef);
    }
    public boolean hasJoined(UUID uuid) {
        return getFolder(uuid).exists();
    }
    public String getUsername(UUID uuid) {
        if (getFileHandler().getFile("mods/Essentials/userdata/" + uuid + "/username.json").exists()) {
            try (var reader = getFileHandler().getFileReader("mods/Essentials/userdata/" + uuid + "/username.json")) {
                var json = getFileHandler().getGson().fromJson(reader, Username.class);
                return json.Username();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else return null;
    }
    public String getUsername(String uuidString) {
        if (getFileHandler().getFile("mods/Essentials/userdata/" + uuidString + "/username.json").exists()) {
            try (var reader = getFileHandler().getFileReader("mods/Essentials/userdata/" + uuidString + "/username.json")) {
                var json = getFileHandler().getGson().fromJson(reader, Username.class);
                return json.Username();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else return null;
    }
    public void setup(PlayerRef playerRef) {
        createFolders(playerRef.getUuid());
        createFiles(playerRef);
    }
    public void createFolders(UUID uuid) {
        var folder = getFolder(uuid);
        if (!folder.exists()) {
            folder.mkdir();
        }
        var homesFolder = getHomesFolder(uuid);
        if (!homesFolder.exists()) {
            homesFolder.mkdir();
        }
    }
    public void createFiles(PlayerRef playerRef) {
        var uuid = playerRef.getUuid();
        if (!getFileHandler().getFile("mods/Essentials/userdata/" + uuid + "/account.json").exists()) {
            try (var writer = getFileHandler().getFileWriter("mods/Essentials/userdata/" + uuid + "/account.json")) {
                getFileHandler().getGson().toJson(new Account(0), writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void update(PlayerRef playerRef) {
        var uuid = playerRef.getUuid();
        try (var writer = getFileHandler().getFileWriter("mods/Essentials/userdata/" + uuid + "/username.json")) {
            getFileHandler().getGson().toJson(new Username(playerRef.getUsername()), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<String> getOffliners() {
        var folder = getUserdataFolder();
        var filesFolders = folder.listFiles();
        var listed = new ArrayList<String>();
        if (filesFolders != null) {
            for (var uuidFolder : filesFolders) {
                var uuidString = uuidFolder.getName();
                listed.add(uuidString);
            }
        }
        return listed;
    }
    public ArrayList<PlayerRef> getPVP() {
        return PVP;
    }
}