package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class PayCommand extends AbstractCommandCollection {
    public PayCommand() {
        super("pay", "pay command");
        requirePermission("essentials.command.pay");
        addUsageVariant(new PayPlayerAmountCommand());
    }
}