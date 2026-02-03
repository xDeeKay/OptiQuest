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

        List<String> formattedNames = onlinePlayers.stream()
                .map(player -> player.isAfk() ? "&7[AFK] " + player.getUsername() : "&a" + player.getUsername())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();

        String playerList = String.join("&7, ", formattedNames);

        context.sender().sendMessage(plugin.getColorUtils().colorize("&6Online Players (&e" + count + "&6):"));

        if (formattedNames.isEmpty()) {
            context.sender().sendMessage(plugin.getColorUtils().colorize("&7There are no players online."));
        } else {
            context.sender().sendMessage(plugin.getColorUtils().colorize(playerList));
        }
    }
}
