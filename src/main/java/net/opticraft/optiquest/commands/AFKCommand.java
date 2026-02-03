package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;
import java.awt.*;

public class AFKCommand extends CommandBase {

    private final OptiQuest plugin;

    public AFKCommand(OptiQuest plugin) {
        this.plugin = plugin;
        super("afk", "Toggle your AFK status.", false);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        CommandSender sender = context.sender();
        String senderName = sender.getDisplayName();

        OptiPlayer optiPlayer = plugin.getPlayerManager().getByUsername(senderName);

        if (optiPlayer == null) {
            sender.sendMessage(Message.raw("Error: Your player data could not be found.").color(Color.RED));
            return;
        }

        boolean nowAfk = !optiPlayer.isAfk();

        optiPlayer.setAfk(nowAfk);

        String messageTemplate = nowAfk
                ? plugin.getConfig().getAfkMessageActive()
                : plugin.getConfig().getAfkMessageInactive();

        String afkMessage = messageTemplate.replace("%username%", senderName);

        for (OptiPlayer onlinePlayer : plugin.getPlayerManager().getAll()) {
            onlinePlayer.getPlayerRef().sendMessage(plugin.getColorUtils().colorize(afkMessage));
        }
    }
}
