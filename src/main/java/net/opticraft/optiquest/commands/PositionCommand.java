package net.opticraft.optiquest.commands;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;
import java.awt.*;

public class PositionCommand extends CommandBase {

    private final OptiQuest plugin;
    private final OptionalArg<PlayerRef> playerArg;

    public PositionCommand(OptiQuest plugin) {
        super("position", "Get a players position in the world", false);
        this.plugin = plugin;
        this.playerArg = this.withOptionalArg("player", "net.opticraft.optiquest.commands.position.arg.player", ArgTypes.PLAYER_REF);
        this.addAliases("pos", "getpos");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String senderName = context.sender().getDisplayName();
        OptiPlayer optiSender = plugin.getPlayerManager().getByUsername(senderName);
        if (optiSender == null) return;

        OptiPlayer targetPlayer;

        PlayerRef argRef = context.get(this.playerArg);

        if (argRef != null) {
            targetPlayer = plugin.getPlayerManager().get(argRef);

            if (targetPlayer == null) {
                optiSender.getPlayerRef().sendMessage(plugin.getColorUtils().colorize("&cThat player is not online."));
                return;
            }
        } else {
            targetPlayer = optiSender;
        }

        PlayerRef targetRef = targetPlayer.getPlayerRef();
        Vector3d pos = targetRef.getTransform().getPosition();

        String formatted = String.format("&6Position of &e%s&6: &fX: %.1f, Y: %.1f, Z: %.1f",
                targetPlayer.getDisplayName(), pos.x, pos.y, pos.z);

        optiSender.getPlayerRef().sendMessage(plugin.getColorUtils().colorize(formatted));
    }
}
