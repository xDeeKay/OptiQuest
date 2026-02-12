package net.opticraft.optiquest.util;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    // Player List Messages
                    .append(new KeyedCodec<String[]>("PlayerListMessages", Codec.STRING_ARRAY),
                            (config, value, info) -> config.playerListMessages = value,
                            (config, info) -> config.playerListMessages)
                    .add()
                    // Player List Messages
                    .append(new KeyedCodec<String[]>("PrivateMessageFormat", Codec.STRING_ARRAY),
                            (config, value, info) -> config.privateMessageFormat = value,
                            (config, info) -> config.privateMessageFormat)
                    .add()
                    // AFK Prefix
                    .append(new KeyedCodec<String>("AFKPrefix", Codec.STRING),
                            (config, value, info) -> config.afkPrefix = value,
                            (config, info) -> config.afkPrefix)
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
                    // Near Messages
                    .append(new KeyedCodec<String[]>("NearMessages", Codec.STRING_ARRAY),
                            (config, value, info) -> config.nearMessages = value,
                            (config, info) -> config.nearMessages)
                    .add()
                    // Near Radius
                    .append(new KeyedCodec<Integer>("NearRadius", Codec.INTEGER),
                            (config, value, info) -> config.nearRadius = value,
                            (config, info) -> config.nearRadius)
                    .add()
                    // Default Group
                    .append(new KeyedCodec<String>("DefaultGroup", Codec.STRING),
                            (config, value, info) -> config.defaultGroup = value,
                            (config, info) -> config.defaultGroup)
                    .add()
                    // Player List Messages
                    .append(new KeyedCodec<String[]>("GroupColors", Codec.STRING_ARRAY),
                            (config, value, info) -> config.groupColors = value,
                            (config, info) -> config.groupColors)
                    .add()
                    // Chat Format
                    .append(new KeyedCodec<String>("ChatFormat", Codec.STRING),
                            (config, value, info) -> config.chatFormat = value,
                            (config, info) -> config.chatFormat)
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

    public String[] playerListMessages = new String[] {
            "&6Online Players (&e%count%&6):", // Element [0]: The Header
            "%afk%%groupcolor%%displayname%"  // Element [1]: The Player Format
    };

    public String[] privateMessageFormat = new String[] {
            "&d[MSG] &fYou > %to%: %message%",   // Element [0]: What the sender sees
            "&d[MSG] &f%from% > You: %message%"    // Element [1]: What the receiver sees
    };

    private String afkPrefix = "&7[AFK] ";
    private String afkMessageActive = "%username% is now AFK";
    private String afkMessageInactive = "%username% is no longer AFK";

    private String broadcastMessage = "&f[&cAnnouncement&f] %message%";

    public String[] nearMessages = new String[] {
            "&6Nearby Players (&e%radius%m&6):",           // Element [0]: Header
            "%groupcolor%%displayname% &7(%distance%m)"    // Element [1]: Player format
    };

    private int nearRadius = 200;

    private String defaultGroup = "Adventurer";

    // This is what the GSON codec will see
    public String[] groupColors = new String[] {
            "Owner:&c",
            "Admin:&9",
            "Moderator:&b",
            "Adventurer:&a"
    };

    // Internal map for fast lookups during chat
    private Map<String, String> colorMap = null;
    public String getGroupColor(String groupName) {
        if (colorMap == null) {
            parseColors();
        }
        return colorMap.getOrDefault(groupName, "&f");
    }

    public void parseColors() {
        colorMap = new HashMap<>();
        for (String entry : groupColors) {
            if (entry.contains(":")) {
                String[] parts = entry.split(":", 2);
                colorMap.put(parts[0], parts[1]);
            }
        }
    }

    private String chatFormat = "&f[%groupcolor%%groupname%&f] %groupcolor%%displayname%&f: %message%";

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

    public List<String> getPlayerListMessages() {
        return Arrays.asList(playerListMessages);
    }

    public List<String> getPrivateMessageFormat() {
        return Arrays.asList(privateMessageFormat);
    }

    public String getAfkPrefix() {
        return afkPrefix;
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

    public List<String> getNearMessages() {
        return Arrays.asList(nearMessages);
    }

    public int getNearRadius() {
        return nearRadius;
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public String getChatFormat() {
        return chatFormat;
    }
}
