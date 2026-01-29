package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class EconomyCommand extends AbstractCommandCollection {
    public EconomyCommand() {
        super("economy", "economy command");
        requirePermission("essentials.command.economy");
        addSubCommand(new EconomyAddCommand());
        addSubCommand(new EconomyRemoveCommand());
        addSubCommand(new EconomyResetCommand());
        addSubCommand(new EconomySetCommand());
        addAliases("eco");
    }
}