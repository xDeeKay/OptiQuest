package net.opticraft.optiquest.player;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class OptiPlayerListener {

    private final OptiQuest plugin;

    public OptiPlayerListener(OptiQuest plugin) {
        this.plugin = plugin;
    }

    public void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        event.setBroadcastJoinMessage(false);
    }

    public void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        UUID uuid = playerRef.getUuid();

        plugin.getPlayerManager().add(playerRef);
        OptiPlayer optiPlayer = plugin.getPlayerManager().get(playerRef);

        if (optiPlayer != null) {
            plugin.getDataManager().loadPlayer(optiPlayer);

            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    plugin.getLogger().at(Level.SEVERE).log("[OptiQuest] Concurrent async delay failed!", e);
                    Thread.currentThread().interrupt();
                }

                var perms = PermissionsModule.get();
                Set<String> groups = perms.getGroupsForUser(uuid);

                if (groups.isEmpty() || (groups.size() == 1 && groups.contains("Adventure"))) {
                    String defaultGroup = plugin.getConfig().getDefaultGroup();
                    perms.addUserToGroup(uuid, defaultGroup);
                    perms.removeUserFromGroup(uuid, "Adventure");

                    plugin.getLogger().at(Level.INFO).log("[OptiQuest] Auto-assigned group " + defaultGroup + " to " + playerRef.getUsername());
                }
            });
        }

        List<String> joinMessages = plugin.getConfig().getJoinMessages();
        if (joinMessages != null && !joinMessages.isEmpty()) {
            sendMessageListToOnlinePlayers(joinMessages, optiPlayer);
        }
    }

    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        OptiPlayer optiPlayer = plugin.getPlayerManager().get(playerRef);

        if (optiPlayer == null) return;

        List<String> leaveMessages = plugin.getConfig().getLeaveMessages();
        if (leaveMessages != null && !leaveMessages.isEmpty()) {
            sendMessageListToOnlinePlayers(leaveMessages, optiPlayer);
        }

        plugin.getDataManager().savePlayer(optiPlayer);
        plugin.getPlayerManager().remove(playerRef);
    }

    public void sendMessageListToOnlinePlayers(List<String> messagelist, OptiPlayer optiPlayer) {
        if (optiPlayer == null) return;

        String groupColor = plugin.getConfig().getGroupColor(optiPlayer.getPrimaryGroup());
        String displayName = optiPlayer.getDisplayName();
        String username = optiPlayer.getUsername();

        for (OptiPlayer onlinePlayer : plugin.getPlayerManager().getAll()) {
            Message combined = Message.raw("");

            for (int i = 0; i < messagelist.size(); i++) {
                String line = messagelist.get(i);

                String formatted = line
                        .replace("%groupcolor%", groupColor)
                        .replace("%displayname%", displayName)
                        .replace("%username%", username);

                combined = Message.join(combined, plugin.getColorUtils().colorize(formatted));
                if (i < messagelist.size() - 1) {
                    combined = Message.join(combined, Message.raw("\n"));
                }
            }
            onlinePlayer.getPlayerRef().sendMessage(combined);
        }
    }
}
