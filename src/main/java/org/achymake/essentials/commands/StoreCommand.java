package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import fi.sulku.hytale.TinyMsg;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.FileHandler;

import javax.annotation.Nonnull;
import java.awt.Color;

public class StoreCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileHandler getFileHandler() {
        return getInstance().getFileHandler();
    }
    public StoreCommand() {
        super("store", "check store", false);
        requirePermission("essentials.command.store");
    }
    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        commandContext.sendMessage(Message.join(
                Message.raw("Store: ").color(Color.ORANGE),
                TinyMsg.parse("<link:" + getFileHandler().getStore() + ">Click Here!</link>").color(Color.GREEN)
        ));
    }
}