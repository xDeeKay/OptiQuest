package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import net.opticraft.optiquest.OptiQuest;

import javax.annotation.Nonnull;
import java.awt.*;

public class RulesCommand extends CommandBase {

    private final OptiQuest plugin;

    public RulesCommand(OptiQuest plugin) {
        this.plugin = plugin;
        super("rules", "Display the server rules.", false);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        for (String rule : plugin.getConfig().getRules()) {
            context.sendMessage(plugin.getColorUtils().colorize(rule));
        }
    }
}
