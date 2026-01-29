package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import fi.sulku.hytale.TinyMsg;

import javax.annotation.Nonnull;
import java.awt.Color;

public class StoreCommand extends CommandBase {
    public StoreCommand() {
        super("store", "check store", false);
        requirePermission("essentials.command.store");
    }
    @Override
    protected void executeSync(@Nonnull CommandContext commandContext) {
        commandContext.sendMessage(Message.join(
                Message.raw("Store: ").color(Color.ORANGE),
                TinyMsg.parse("<link:https://store.achymake.org/>Click Here!</link>").color(Color.GREEN)
        ));
    }
}