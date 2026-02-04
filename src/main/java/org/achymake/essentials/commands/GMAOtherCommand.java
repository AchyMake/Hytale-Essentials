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

public class GMAOtherCommand extends CommandBase {
    private final RequiredArg<PlayerRef> targetRef;
    public GMAOtherCommand() {
        super("changes others game mode to adventure");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.gma.other");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var ref = targetRef.getReference();
        if (targetRef.isValid() && ref != null) {
            var store = ref.getStore();
            var world = store.getExternalData().getWorld();
            world.execute(() -> {
                var target = store.getComponent(ref, Player.getComponentType());
                if (target != null) {
                    if (!target.getGameMode().equals(GameMode.Adventure)) {
                        Player.setGameMode(ref, GameMode.Adventure, store);
                        commandContext.sendMessage(Message.join(
                                Message.raw("You changed ").color(Color.ORANGE),
                                Message.raw(targetRef.getUsername() + " "),
                                Message.raw("game mode to ").color(Color.ORANGE),
                                Message.raw("Adventure")
                        ));
                    } else commandContext.sendMessage(Message.join(
                            Message.raw("Seems like ").color(Color.RED),
                            Message.raw(targetRef.getUsername() + " "),
                            Message.raw("is already are in ").color(Color.RED),
                            Message.raw("Adventure "),
                            Message.raw("mode").color(Color.RED)
                    ));
                }
            });
        }
    }
}