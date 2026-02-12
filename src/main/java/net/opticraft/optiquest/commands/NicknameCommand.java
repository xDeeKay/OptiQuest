package net.opticraft.optiquest.commands;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;

public class NicknameCommand extends CommandBase {

    private final OptiQuest plugin;
    private final OptionalArg<String> nameArg;

    public NicknameCommand(OptiQuest plugin) {
        super("nickname", "Change your display name in chat", false);
        this.plugin = plugin;
        this.nameArg = this.withOptionalArg("name", "net.opticraft.optiquest.commands.nick.arg.name", ArgTypes.STRING);
        this.addAliases("nick");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String senderName = context.sender().getDisplayName();
        OptiPlayer optiPlayer = plugin.getPlayerManager().getByUsername(senderName);
        if (optiPlayer == null) return;

        PlayerRef senderRef = optiPlayer.getPlayerRef();

        String newName = context.get(this.nameArg);

        if (newName == null || newName.equalsIgnoreCase("off")) {
            optiPlayer.setNickname(null);
            senderRef.sendMessage(plugin.getColorUtils().colorize("&6Your nickname has been cleared."));
        } else {
            if (newName.length() > 16) {
                senderRef.sendMessage(plugin.getColorUtils().colorize("&cNickname is too long! Max 16 characters."));
                return;
            }

            optiPlayer.setNickname(newName);
            senderRef.sendMessage(plugin.getColorUtils().colorize("&6Your nickname is now set to: &f" + newName));
        }

        plugin.getDataManager().savePlayer(optiPlayer);
    }
}
