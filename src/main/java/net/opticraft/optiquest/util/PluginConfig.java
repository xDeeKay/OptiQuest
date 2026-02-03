package net.opticraft.optiquest.util;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.Arrays;
import java.util.List;

public class PluginConfig {

    public static final BuilderCodec<PluginConfig> CODEC =
            BuilderCodec.builder(PluginConfig.class, PluginConfig::new)
                    // First Join Message
                    .append(new KeyedCodec<String[]>("FirstJoinMessages", Codec.STRING_ARRAY),
                            (config, value, info) -> config.firstJoinMessages = value,
                            (config, info) -> config.firstJoinMessages)
                    .add()
                    // Join Message
                    .append(new KeyedCodec<String[]>("JoinMessages", Codec.STRING_ARRAY),
                            (config, value, info) -> config.joinMessages = value,
                            (config, info) -> config.joinMessages)
                    .add()
                    // Leave Message
                    .append(new KeyedCodec<String[]>("LeaveMessages", Codec.STRING_ARRAY),
                            (config, value, info) -> config.leaveMessages = value,
                            (config, info) -> config.leaveMessages)
                    .add()
                    // Message of the Day
                    .append(new KeyedCodec<String[]>("MOTD", Codec.STRING_ARRAY),
                            (config, value, info) -> config.motd = value,
                            (config, info) -> config.motd)
                    .add()
                    // Server Rules
                    .append(new KeyedCodec<String[]>("Rules", Codec.STRING_ARRAY),
                            (config, value, info) -> config.rules = value,
                            (config, info) -> config.rules)
                    .add()
                    // AFK Message Active
                    .append(new KeyedCodec<String>("AFKMessageActive", Codec.STRING),
                            (config, value, info) -> config.afkMessageActive = value,
                            (config, info) -> config.afkMessageActive)
                    .add()
                    // AFK Message Inactive
                    .append(new KeyedCodec<String>("AFKMessageInactive", Codec.STRING),
                            (config, value, info) -> config.afkMessageInactive = value,
                            (config, info) -> config.afkMessageInactive)
                    .add()
                    // Broadcast Message
                    .append(new KeyedCodec<String>("BroadcastMessage", Codec.STRING),
                            (config, value, info) -> config.broadcastMessage = value,
                            (config, info) -> config.broadcastMessage)
                    .add()
                    .append(new KeyedCodec<Integer>("NearRadius", Codec.INTEGER),
                            (config, value, info) -> config.nearRadius = value,
                            (config, info) -> config.nearRadius)
                    .add()
                    .build();

    private String[] firstJoinMessages = {
            "&6Welcome to the server, %username%!"
    };

    private String[] joinMessages = {
            "&6%username% has joined the game."
    };

    private String[] leaveMessages = {
            "&6%username% has left the game."
    };

    private String[] motd = {
            "&6Type /help for a list of available commands",
            "&6Please read and follow the /rules"
    };

    private String[] rules = {
            "&6No griefing or stealing",
            "&6No hacking or cheating",
            "&6Be respectful and friendly"
    };

    private String afkMessageActive = "%username% is now AFK";
    private String afkMessageInactive = "%username% is no longer AFK";

    private String broadcastMessage = "%message%";

    private int nearRadius = 200;

    public PluginConfig() {
    }

    public List<String> getFirstJoinMessages() {
        return Arrays.asList(firstJoinMessages);
    }

    public List<String> getJoinMessages() {
        return Arrays.asList(joinMessages);
    }

    public List<String> getLeaveMessages() {
        return Arrays.asList(leaveMessages);
    }

    public List<String> getMOTD() {
        return Arrays.asList(motd);
    }

    public List<String> getRules() {
        return Arrays.asList(rules);
    }

    public String getAfkMessageActive() {
        return afkMessageActive;
    }

    public String getAfkMessageInactive() {
        return afkMessageInactive;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }

    public int getNearRadius() {
        return nearRadius;
    }
}
