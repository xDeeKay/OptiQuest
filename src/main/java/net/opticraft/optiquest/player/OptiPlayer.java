package net.opticraft.optiquest.player;

import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OptiPlayer {

    private final PlayerRef playerRef;

    private String nickname;
    private boolean muted;
    private boolean afk;
    private boolean socialSpy;
    private boolean staffChat;

    public OptiPlayer(PlayerRef playerRef) {
        this.playerRef = playerRef;
    }

    public static class PlayerData {
        public String nickname;
        public boolean muted;
        public boolean afk;
        public boolean socialSpy;
        public boolean staffChat;
    }

    public void loadData(PlayerData data) {
        if (data == null) return;
        this.nickname = data.nickname;
        this.muted = data.muted;
        this.afk = data.afk;
        this.socialSpy = data.socialSpy;
        this.staffChat = data.staffChat;
    }

    public PlayerData saveData() {
        PlayerData data = new PlayerData();
        data.nickname = this.nickname;
        data.muted = this.muted;
        data.afk = this.afk;
        data.socialSpy = this.socialSpy;
        data.staffChat = this.staffChat;
        return data;
    }

    public PlayerRef getPlayerRef() {
        return playerRef;
    }

    public UUID getUuid() {
        return playerRef.getUuid();
    }

    public String getUsername() {
        return playerRef.getUsername();
    }

    public String getNickname() {
        return nickname;
    }

    public OptiPlayer setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getDisplayName() {
        if (nickname != null && !nickname.trim().isEmpty()) {
            return nickname;
        }
        return playerRef.getUsername();
    }

    public String getPrimaryGroup() {
        Set<String> groups = PermissionsModule.get().getGroupsForUser(this.playerRef.getUuid());

        if (groups.isEmpty()) {
            return "Player";
        }

        List<String> priority = Arrays.asList("Owner", "Moderator", "Adventure");

        for (String rank : priority) {
            if (groups.contains(rank)) {
                return rank;
            }
        }

        return groups.iterator().next();
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
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

    public boolean isStaffChat() {
        return staffChat;
    }

    public void setStaffChat(boolean staffChat) {
        this.staffChat = staffChat;
    }
}
