package net.opticraft.optiquest.commands;

import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

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
        List<String> nearbyNames = getStrings(senderName, sPos, radius);

        if (nearbyNames.isEmpty()) {
            senderRef.sendMessage(plugin.getColorUtils().colorize("&eNo players within " + radius + "m."));
        } else {
            String list = String.join("&7, ", nearbyNames);
            senderRef.sendMessage(plugin.getColorUtils().colorize("&6Nearby: " + list));
        }
    }

    @NonNullDecl
    private List<String> getStrings(String senderName, Vector3d sPos, int radius) {
        List<String> nearbyNames = new ArrayList<>();

        for (OptiPlayer target : plugin.getPlayerManager().getAll()) {
            if (target.getUsername().equalsIgnoreCase(senderName)) continue;

            PlayerRef targetRef = target.getPlayerRef();
            Vector3d tPos = targetRef.getTransform().getPosition();

            double distance = Math.sqrt(
                    Math.pow(sPos.x - tPos.x, 2) +
                            Math.pow(sPos.y - tPos.y, 2) +
                            Math.pow(sPos.z - tPos.z, 2)
            );

            if (distance <= radius) {
                nearbyNames.add("&a" + target.getUsername() + " &7(" + (int) distance + "m)");
            }
        }
        return nearbyNames;
    }
}
