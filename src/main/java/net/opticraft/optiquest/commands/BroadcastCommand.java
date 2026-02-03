package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;
import java.awt.*;

public class BroadcastCommand extends CommandBase {

    private final OptiQuest plugin;

    @Nonnull private final RequiredArg<String> messageArg;

    public BroadcastCommand(OptiQuest plugin) {
        this.plugin = plugin;

        super("broadcast", "Broadcast a message to all online players", false);

        this.messageArg = this.withRequiredArg("message", "net.opticraft.optiquest.commands.broadcast.arg.message",
                ArgTypes.STRING);

        this.setAllowsExtraArguments(true);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String firstWord = (String) context.get(this.messageArg);
        String fullInput = context.getInputString();

        int messageStart = fullInput.indexOf(firstWord, 1);

        if (messageStart == -1) {
            return;
        }

        String finalMessage = fullInput.substring(messageStart).trim();

        for (OptiPlayer onlinePlayer : plugin.getPlayerManager().getAll()) {
            onlinePlayer.getPlayerRef().sendMessage(plugin.getColorUtils().colorize(plugin.getConfig().getBroadcastMessage().replace("%message%", finalMessage)));
        }
    }
}
