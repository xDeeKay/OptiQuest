package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;
import java.awt.*;

public class MessageCommand extends CommandBase {

    @Nonnull private final RequiredArg<PlayerRef> playerArg;
    @Nonnull private final RequiredArg<String> messageArg;

    public MessageCommand() {
        super("message", "Send a private message to a player", false);

        this.playerArg = this.withRequiredArg("player", "net.opticraft.optiquest.commands.message.arg.player",
                ArgTypes.PLAYER_REF);
        this.messageArg = this.withRequiredArg("message", "net.opticraft.optiquest.commands.message.arg.message",
                ArgTypes.STRING);

        this.setAllowsExtraArguments(true);
        this.addAliases("msg");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        CommandSender sender = context.sender();
        PlayerRef target = (PlayerRef) context.get(this.playerArg);

        String senderName = sender.getDisplayName();
        String targetName = target.getUsername();

        String firstWord = (String) context.get(this.messageArg);
        String fullInput = context.getInputString();

        int targetPos = fullInput.indexOf(targetName);
        int messageStart = fullInput.indexOf(firstWord, targetPos + targetName.length());

        String finalMessage = fullInput.substring(messageStart).trim();

        sender.sendMessage(Message.join(
                Message.raw("[MSG] ").color(Color.MAGENTA),
                Message.raw("You > " + targetName + " : " + finalMessage).color(Color.WHITE)));

        target.sendMessage(Message.join(
                Message.raw("[MSG] ").color(Color.MAGENTA),
                Message.raw(senderName + " > You : " + finalMessage).color(Color.WHITE)));
    }
}
