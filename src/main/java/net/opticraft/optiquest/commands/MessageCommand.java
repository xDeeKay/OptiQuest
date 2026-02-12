package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;
import java.util.List;

public class MessageCommand extends CommandBase {

    private final OptiQuest plugin;
    @Nonnull private final RequiredArg<PlayerRef> playerArg;
    @Nonnull private final RequiredArg<String> messageArg;

    public MessageCommand(OptiQuest plugin) {
        super("message", "Send a private message to a player", false);
        this.plugin = plugin;
        this.playerArg = this.withRequiredArg("player", "net.opticraft.optiquest.commands.message.arg.player", ArgTypes.PLAYER_REF);
        this.messageArg = this.withRequiredArg("message", "net.opticraft.optiquest.commands.message.arg.message", ArgTypes.STRING);
        this.setAllowsExtraArguments(true);
        this.addAliases("msg");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        CommandSender sender = context.sender();
        PlayerRef targetRef = (PlayerRef) context.get(this.playerArg);

        String senderUsername = sender.getDisplayName();
        OptiPlayer optiSender = plugin.getPlayerManager().getByUsername(senderUsername);
        OptiPlayer optiTarget = plugin.getPlayerManager().get(targetRef);
        if (optiSender == null || optiTarget == null) return;

        String firstWord = (String) context.get(this.messageArg);
        String fullInput = context.getInputString();
        int targetPos = fullInput.indexOf(targetRef.getUsername());
        int messageStart = fullInput.indexOf(firstWord, targetPos + targetRef.getUsername().length());
        String finalMessageContent = fullInput.substring(messageStart).trim();

        List<String> formats = plugin.getConfig().getPrivateMessageFormat();

        String senderTemplate = formats.get(0);
        String receiverTemplate = formats.get(1);

        String toSender = senderTemplate
                .replace("%to%", optiTarget.getDisplayName())
                .replace("%from%", optiSender.getDisplayName())
                .replace("%message%", finalMessageContent);

        String toReceiver = receiverTemplate
                .replace("%to%", optiTarget.getDisplayName())
                .replace("%from%", optiSender.getDisplayName())
                .replace("%message%", finalMessageContent);

        sender.sendMessage(plugin.getColorUtils().colorize(toSender));
        targetRef.sendMessage(plugin.getColorUtils().colorize(toReceiver));
    }
}
