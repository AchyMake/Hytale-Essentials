package org.achymake.essentials.commands;

import org.achymake.essentials.pages.WithdrawValuePage;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;


public class WithdrawOtherCommand extends CommandBase {
    private final RequiredArg<PlayerRef> targetRef;
    public WithdrawOtherCommand() {
        super("withdraw other");
        targetRef = withRequiredArg("player", "server.commands.argtype.string.desc", ArgTypes.PLAYER_REF);
        requirePermission("essentials.command.withdraw.other");
    }
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var targetRef = this.targetRef.get(commandContext);
        var ref = targetRef.getReference();
        assert ref != null;
        var target = ref.getStore().getComponent(ref, Player.getComponentType());
        if (target != null) {
            target.getPageManager().openCustomPage(ref, ref.getStore(), new WithdrawValuePage(targetRef));
        }
    }
}