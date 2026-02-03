package net.opticraft.optiquest.listeners;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import java.util.List;

public class PlayerConnectionListener {

    private final OptiQuest plugin;

    public PlayerConnectionListener(OptiQuest plugin) {
        this.plugin = plugin;
    }

    public void onAddPlayerToWorld(AddPlayerToWorldEvent event) {
        event.setBroadcastJoinMessage(false);
    }

    public void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        String username = playerRef.getUsername();

        plugin.getPlayerManager().add(playerRef);

        List<String> joinMessages = plugin.getConfig().getJoinMessages();
        if (joinMessages != null && !joinMessages.isEmpty()) {
            sendMessageListToOnlinePlayers(joinMessages, username);
        }
    }

    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        String username = playerRef.getUsername();

        plugin.getPlayerManager().remove(playerRef);

        List<String> leaveMessages = plugin.getConfig().getLeaveMessages();
        if (leaveMessages != null && !leaveMessages.isEmpty()) {
            sendMessageListToOnlinePlayers(leaveMessages, username);
        }
    }

    public void sendMessageListToOnlinePlayers(List<String> messagelist, String username) {
        for (OptiPlayer onlinePlayer : plugin.getPlayerManager().getAll()) {
            Message combined = Message.raw("");
            for (String line : messagelist) {
                String formatted = line.replace("%username%", username);
                combined = Message.join(combined, plugin.getColorUtils().colorize(formatted), Message.raw("\n"));
            }
            onlinePlayer.getPlayerRef().sendMessage(combined);
        }
    }
}
