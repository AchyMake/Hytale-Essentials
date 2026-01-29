package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.files.EssentialsConfig;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.util.EventTitleUtil;

import java.io.IOException;

public class MessageHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    public void sendTitle(PlayerRef playerRef, Message primary, Message secondary, boolean major) {
        EventTitleUtil.showEventTitleToPlayer(playerRef, primary, secondary, major);
    }
    public String toTitleCase(String string) {
        if (string.contains(" ")) {
            var builder = getBuilder();
            for (var strings : string.split(" ")) {
                builder.append(strings.toUpperCase().charAt(0)).append(strings.substring(1).toLowerCase());
                builder.append(" ");
            }
            return builder.toString().strip();
        } else if (string.contains("_")) {
            var builder = getBuilder();
            for (var strings : string.split("_")) {
                builder.append(strings.toUpperCase().charAt(0)).append(strings.substring(1).toLowerCase());
                builder.append(" ");
            }
            return builder.toString().strip();
        } else return string.toUpperCase().charAt(0) + string.substring(1).toLowerCase();
    }
    public StringBuilder getBuilder() {
        return new StringBuilder();
    }
    public String censor(String message) {
        try (var reader = getFileHandler().getFileReader("mods/Essentials/config.json")) {
            var json = getFileHandler().getGson().fromJson(reader, EssentialsConfig.class);
            for (var censored : json.Censored()) {
                if (message.toLowerCase().contains(censored)) {
                    return message.replace(censored, "*".repeat(censored.length()));
                }
            }
            return message;
        } catch (IOException e) {
            return message;
        }
    }
}