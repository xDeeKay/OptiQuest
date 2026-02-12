package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class ListCommand extends CommandBase {

    private final OptiQuest plugin;

    public ListCommand(OptiQuest plugin) {
        super("list", "View all online players", false);
        this.plugin = plugin;
        this.addAliases("online", "players", "playerlist", "plist", "who");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        Collection<OptiPlayer> onlinePlayers = plugin.getPlayerManager().getAll();
        int count = onlinePlayers.size();

        String headerTemplate = plugin.getConfig().getPlayerListMessages().get(0);
        String playerTemplate = plugin.getConfig().getPlayerListMessages().get(1);

        List<String> formattedNames = onlinePlayers.stream()
                .map(optiPlayer -> {
                    String color = plugin.getConfig().getGroupColor(optiPlayer.getPrimaryGroup());
                    String afkPrefix = optiPlayer.isAfk() ? plugin.getConfig().getAfkPrefix() : "";

                    return playerTemplate
                            .replace("%afk%", afkPrefix)
                            .replace("%groupcolor%", color)
                            .replace("%displayname%", optiPlayer.getDisplayName())
                            .replace("%username%", optiPlayer.getUsername());
                })
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();

        String header = headerTemplate.replace("%count%", String.valueOf(count));
        context.sender().sendMessage(plugin.getColorUtils().colorize(header));

        if (formattedNames.isEmpty()) {
            context.sender().sendMessage(plugin.getColorUtils().colorize("&7There are no players online."));
        } else {
            String playerList = String.join("&f, ", formattedNames);
            context.sender().sendMessage(plugin.getColorUtils().colorize(playerList));
        }
    }
}
