package org.achymake.essentials.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.TeleportHandler;
import org.achymake.essentials.handlers.UniverseHandler;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.Color;

public class SpawnOtherCommand extends CommandBase {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private TeleportHandler getTeleportHandler() {
        return getInstance().getTeleportHandler();
    }
    private UniverseHandler getUniverseHandler() {
        return getInstance().getUniverseHandler();
    }
    private final RequiredArg<PlayerRef> targetRef;
    public SpawnOtherCommand() {
        super("spawn other");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.spawn.other");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var spawnTp = getUniverseHandler().getSpawn();
        if (spawnTp != null) {
            var message = Message.join(
                    Message.raw("Teleporting to ").color(Color.ORANGE),
                    Message.raw("Spawn")
            );
            getTeleportHandler().teleport(targetRef, spawnTp, message);
        } else commandContext.sendMessage(Message.raw("Seem like spawn has not been set").color(Color.RED));
    }
}