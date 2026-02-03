package net.opticraft.optiquest.player;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OptiPlayerManager {

    private final Map<PlayerRef, OptiPlayer> playerMap = new HashMap<>();

    public void add(PlayerRef playerRef) {
        playerMap.put(playerRef, new OptiPlayer(playerRef));
    }

    public void remove(PlayerRef playerRef) {
        playerMap.remove(playerRef);
    }

    public OptiPlayer get(PlayerRef playerRef) {
        return playerMap.get(playerRef);
    }

    public Collection<OptiPlayer> getAll() {
        return playerMap.values();
    }

    public OptiPlayer getByUsername(String username) {
        for (OptiPlayer optiPlayer : playerMap.values()) {
            if (optiPlayer.getUsername().equalsIgnoreCase(username)) {
                return optiPlayer;
            }
        }
        return null;
    }
}
