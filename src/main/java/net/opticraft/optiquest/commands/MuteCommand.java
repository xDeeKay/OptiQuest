package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;

public class MuteCommand extends CommandBase {

    private final OptiQuest plugin;
    private final RequiredArg<String> playerArg;
    private final OptionalArg<String> reasonArg;

    public MuteCommand(OptiQuest plugin) {
        super("mute", "Prevent a player from speaking in chat", false);
        this.plugin = plugin;
        this.playerArg = this.withRequiredArg("player", "net.opticraft.optiquest.commands.mute.arg.player", ArgTypes.STRING);
        this.reasonArg = this.withOptionalArg("reason", "net.opticraft.optiquest.commands.mute.arg.reason", ArgTypes.STRING);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String targetName = context.get(this.playerArg);
        String reason = context.get(this.reasonArg);
        if (reason == null) reason = "No reason specified";

        OptiPlayer optiTarget = plugin.getPlayerManager().getByUsername(targetName);
        String senderName = context.sender().getDisplayName();
        PlayerRef sender = plugin.getPlayerManager().getByUsername(senderName).getPlayerRef();

        if (optiTarget == null) {
            sender.sendMessage(plugin.getColorUtils().colorize("&cPlayer '" + targetName + "' not found."));
            return;
        }

        boolean isMutedNow = !optiTarget.isMuted();
        optiTarget.setMuted(isMutedNow);

        String status = isMutedNow ? "&crendered mute" : "&aunmuted";
        sender.sendMessage(plugin.getColorUtils().colorize("&6Player &e" + optiTarget.getDisplayName() + " &6is now " + status + "&6."));

        if (isMutedNow) {
            optiTarget.getPlayerRef().sendMessage(plugin.getColorUtils().colorize("&cYou have been muted by an admin. Reason: &f" + reason));
        } else {
            optiTarget.getPlayerRef().sendMessage(plugin.getColorUtils().colorize("&aYou have been unmuted."));
        }
    }
}
