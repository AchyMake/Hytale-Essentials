package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.ModelAssetHandler;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class UndisguiseOtherCommand extends CommandBase {
    private static final Message MESSAGE_COMMANDS_ERRORS_PLAYER_NOT_IN_WORLD = Message.translation("server.commands.errors.playerNotInWorld");
    @Nonnull
    private final RequiredArg<PlayerRef> playerArg;
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ModelAssetHandler getModelAssetHandler() {
        return getInstance().getModelAssetHandler();
    }
    UndisguiseOtherCommand() {
        super("Undisguises others");
        playerArg = withRequiredArg("player", "server.commands.argtype.player.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.disguise.other");
    }
    protected void executeSync(@Nonnull CommandContext commandContext) {
        var targetPlayerRef = playerArg.get(commandContext);
        var targetRef = targetPlayerRef.getReference();
        if (targetRef != null && targetRef.isValid()) {
            getModelAssetHandler().unDisguise(targetRef);
        } else commandContext.sendMessage(MESSAGE_COMMANDS_ERRORS_PLAYER_NOT_IN_WORLD);
    }
}