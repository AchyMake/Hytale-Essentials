package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandUtil;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.UniverseHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class AnnouncementCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    public AnnouncementCommand() {
        super("announcement","announcement command");
        requirePermission("essentials.command.announcement");
        addAliases("announce");
        setAllowsExtraArguments(true);
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        getUniverseHandler().sendAll(Message.join(
                Message.raw("Server").color(Color.ORANGE),
                Message.raw(": " + CommandUtil.stripCommandName(commandContext.getInputString()).trim())
        ));
    }
}