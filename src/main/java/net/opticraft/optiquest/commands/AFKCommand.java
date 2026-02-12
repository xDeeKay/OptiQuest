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
        super("afk", "Toggle your AFK status.", false);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        CommandSender sender = context.sender();
        String senderName = sender.getDisplayName();

        OptiPlayer optiPlayer = plugin.getPlayerManager().getByUsername(senderName);
        if (optiPlayer == null) return;

        boolean nowAfk = !optiPlayer.isAfk();
        optiPlayer.setAfk(nowAfk);

        String messageTemplate = nowAfk
                ? plugin.getConfig().getAfkMessageActive()
                : plugin.getConfig().getAfkMessageInactive();

        String afkMessage = messageTemplate
                .replace("%username%", senderName)
                .replace("%displayname%", optiPlayer.getDisplayName());

        Message finalMessage = plugin.getColorUtils().colorize(afkMessage);
        for (OptiPlayer onlinePlayer : plugin.getPlayerManager().getAll()) {
            onlinePlayer.getPlayerRef().sendMessage(finalMessage);
        }

        plugin.getDataManager().savePlayer(optiPlayer);
    }
}
