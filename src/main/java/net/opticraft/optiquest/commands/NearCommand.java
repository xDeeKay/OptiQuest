package net.opticraft.optiquest.commands;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NearCommand extends CommandBase {

    private final OptiQuest plugin;

    public NearCommand(OptiQuest plugin) {
        super("near", "See which players are nearby", false);
        this.plugin = plugin;
        this.addAliases("nearby");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {
        String senderName = context.sender().getDisplayName();
        OptiPlayer optiSender = plugin.getPlayerManager().getByUsername(senderName);

        if (optiSender == null) return;

        PlayerRef senderRef = optiSender.getPlayerRef();
        Vector3d sPos = senderRef.getTransform().getPosition();

        int radius = plugin.getConfig().getNearRadius();

        List<String> formats = plugin.getConfig().getNearMessages();
        String headerTemplate = formats.get(0);
        String playerTemplate = formats.get(1);

        List<String> nearbyNames = getNearbyPlayerStrings(optiSender, sPos, radius, playerTemplate);

        if (nearbyNames.isEmpty()) {
            senderRef.sendMessage(plugin.getColorUtils().colorize("&eNo players within " + radius + "m."));
        } else {
            String header = headerTemplate.replace("%radius%", String.valueOf(radius));
            String list = String.join("&f, ", nearbyNames);

            senderRef.sendMessage(plugin.getColorUtils().colorize(header));
            senderRef.sendMessage(plugin.getColorUtils().colorize(list));
        }
    }

    private List<String> getNearbyPlayerStrings(OptiPlayer sender, Vector3d sPos, int radius, String playerTemplate) {
        List<String> nearbyNames = new ArrayList<>();

        for (OptiPlayer target : plugin.getPlayerManager().getAll()) {
            if (target.getUuid().equals(sender.getUuid())) continue;

            PlayerRef targetRef = target.getPlayerRef();
            Vector3d tPos = targetRef.getTransform().getPosition();

            double distance = Math.sqrt(
                    Math.pow(sPos.x - tPos.x, 2) +
                            Math.pow(sPos.y - tPos.y, 2) +
                            Math.pow(sPos.z - tPos.z, 2)
            );

            if (distance <= radius) {
                String color = plugin.getConfig().getGroupColor(target.getPrimaryGroup());

                String entry = playerTemplate
                        .replace("%groupcolor%", color)
                        .replace("%displayname%", target.getDisplayName())
                        .replace("%username%", target.getUsername())
                        .replace("%distance%", String.valueOf((int) distance));

                nearbyNames.add(entry);
            }
        }
        return nearbyNames;
    }
}
