package net.opticraft.optiquest.player;

import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.UUID;

public class OptiPlayer {

    private final PlayerRef playerRef;
    private boolean afk;
    private boolean socialSpy;

    public OptiPlayer(PlayerRef playerRef) {
        this.playerRef = playerRef;
        this.afk = false;
        this.socialSpy = false;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public boolean isAfk() {
        return afk;
    }

    public void setAfk(boolean afk) {
        this.afk = afk;
    }

    public boolean isSocialSpy() {
        return socialSpy;
    }

    public void setSocialSpy(boolean socialSpy) {
        this.socialSpy = socialSpy;
    }

    public String getUsername() {
        return playerRef.getUsername();
    }

    public UUID getUUID() {
        return playerRef.getUuid();
    }
}
