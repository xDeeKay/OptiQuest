package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.opticraft.optiquest.OptiQuest;

import javax.annotation.Nonnull;

public class MOTDCommand extends CommandBase {

    private final OptiQuest plugin;

    public MOTDCommand(OptiQuest plugin) {
        this.plugin = plugin;
        super("motd", "Display the Message of the Day.", false);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        for (String message : plugin.getConfig().getMOTD()) {
            context.sendMessage(plugin.getColorUtils().colorize(message));
        }
    }
}
