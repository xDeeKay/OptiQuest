package net.opticraft.optiquest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.opticraft.optiquest.commands.*;
import net.opticraft.optiquest.listeners.ChatListener;
import net.opticraft.optiquest.player.OptiPlayerListener;
import net.opticraft.optiquest.player.OptiPlayerManager;
import net.opticraft.optiquest.util.ColorUtils;
import net.opticraft.optiquest.util.DataManager;
import net.opticraft.optiquest.util.PluginConfig;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class OptiQuest extends JavaPlugin {

    private static OptiQuest instance;
    private PluginConfig pluginConfig;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final OptiPlayerManager playerManager;
    private final DataManager dataManager;
    private final ColorUtils colorUtils;

    public OptiQuest(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        setupConfig();
        this.playerManager = new OptiPlayerManager();
        this.dataManager = new DataManager(this);
        this.colorUtils = new ColorUtils(this);
        getLogger().at(Level.INFO).log("[OptiQuest] Plugin loaded!");
    }

    public static OptiQuest getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        getLogger().at(Level.INFO).log("[OptiQuest] Plugin setting up...");
        registerEvents();
        registerCommands();
    }

    @Override
    protected void start() {
        getLogger().at(Level.INFO).log("[OptiQuest] Plugin enabled!");
    }

    @Override
    public void shutdown() {
        getLogger().at(Level.INFO).log("[OptiQuest] Plugin disabled!");
        saveConfig();
    }

    private void registerEvents() {
        OptiPlayerListener optiPlayerListener = new OptiPlayerListener(this);
        this.getEventRegistry().registerGlobal(AddPlayerToWorldEvent.class, optiPlayerListener::onAddPlayerToWorld);
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, optiPlayerListener::onPlayerConnect);
        this.getEventRegistry().registerGlobal(PlayerDisconnectEvent.class, optiPlayerListener::onPlayerDisconnect);
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, new ChatListener(this)::onPlayerChat);
    }

    private void registerCommands() {
        this.getCommandRegistry().registerCommand(new AFKCommand(this));
        this.getCommandRegistry().registerCommand(new BroadcastCommand(this));
        this.getCommandRegistry().registerCommand(new ListCommand(this));
        this.getCommandRegistry().registerCommand(new MessageCommand(this));
        this.getCommandRegistry().registerCommand(new MOTDCommand(this));
        this.getCommandRegistry().registerCommand(new MuteCommand(this));
        this.getCommandRegistry().registerCommand(new NearCommand(this));
        this.getCommandRegistry().registerCommand(new NicknameCommand(this));
        this.getCommandRegistry().registerCommand(new PositionCommand(this));
        this.getCommandRegistry().registerCommand(new RulesCommand(this));
    }

    private void setupConfig() {
        try {
            Path folder = Paths.get("mods", "Opticraft_OptiQuest");
            if (!Files.exists(folder)) Files.createDirectories(folder);

            Path configFile = folder.resolve("config.json");

            if (Files.exists(configFile)) {
                pluginConfig = gson.fromJson(Files.readString(configFile), PluginConfig.class);
            } else {
                pluginConfig = new PluginConfig();
                saveConfig();
            }

        } catch (IOException e) {
            getLogger().at(Level.SEVERE).log("[OptiQuest] Failed to load or save config", e);
        }
    }

    public void saveConfig() {
        try {
            Path configFile = Paths.get("mods", "Opticraft_OptiQuest", "config.json");
            Files.writeString(configFile, gson.toJson(pluginConfig));
        } catch (IOException e) {
            getLogger().at(Level.SEVERE).log("[OptiQuest] Failed to save config", e);
        }
    }

    public PluginConfig getConfig() {
        return pluginConfig;
    }

    public OptiPlayerManager getPlayerManager() {
        return playerManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ColorUtils getColorUtils() {
        return colorUtils;
    }
}
