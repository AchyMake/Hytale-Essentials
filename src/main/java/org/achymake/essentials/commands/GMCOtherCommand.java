package org.achymake.essentials.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class GMCOtherCommand extends CommandBase {
    private final RequiredArg<PlayerRef> targetRef;
    public GMCOtherCommand() {
        super("changes others game mode to creative");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.gmc.other");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        if (targetRef != null && targetRef.isValid()) {
            var ref = targetRef.getReference();
            assert ref != null;
            var store = ref.getStore();
            var target = store.getComponent(ref, Player.getComponentType());
            if (target != null) {
                if (target.getGameMode().equals(GameMode.Creative)) {
                    commandContext.sendMessage(Message.join(
                            Message.raw("Seems like ").color(Color.RED),
                            Message.raw(target.getDisplayName() + " "),
                            Message.raw("is already are in ").color(Color.RED),
                            Message.raw("Creative "),
                            Message.raw("mode").color(Color.RED)
                    ));
                } else Player.setGameMode(ref, GameMode.Creative, store);
            }
        }
    }
}