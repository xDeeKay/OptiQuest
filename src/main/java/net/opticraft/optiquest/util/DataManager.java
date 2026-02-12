package net.opticraft.optiquest.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class DataManager {

    private final OptiQuest plugin;
    private final Gson gson;
    private final Path userDataFolder;

    public DataManager(OptiQuest plugin) {
        this.plugin = plugin;
        this.userDataFolder = Paths.get("mods", "Opticraft_OptiQuest", "userdata");
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        setupFolder();
    }

    private void setupFolder() {
        try {
            if (!Files.exists(userDataFolder)) {
                Files.createDirectories(userDataFolder);
            }
        } catch (IOException e) {
            plugin.getLogger().at(Level.SEVERE).log("[OptiQuest] Could not create userdata folder", e);
        }
    }

    public void savePlayer(OptiPlayer player) {
        Path playerFile = userDataFolder.resolve(player.getUuid().toString() + ".json");

        try {
            String json = gson.toJson(player.saveData());
            Files.writeString(playerFile, json);
        } catch (IOException e) {
            plugin.getLogger().at(Level.SEVERE).log("[OptiQuest] Failed to save data for " + player.getUsername(), e);
        }
    }

    public void loadPlayer(OptiPlayer player) {
        Path playerFile = userDataFolder.resolve(player.getUuid().toString() + ".json");

        if (!Files.exists(playerFile)) return;

        try {
            String json = Files.readString(playerFile);
            OptiPlayer.PlayerData data = gson.fromJson(json, OptiPlayer.PlayerData.class);
            player.loadData(data);
        } catch (IOException e) {
            plugin.getLogger().at(Level.SEVERE).log("[OptiQuest] Failed to load data for " + player.getUsername(), e);
        }
    }
}
