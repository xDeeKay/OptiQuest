package net.opticraft.optiquest.listeners;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import net.opticraft.optiquest.OptiQuest;
import net.opticraft.optiquest.player.OptiPlayer;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class ChatListener {

    private final OptiQuest plugin;
    private static final String HAND_PLACEHOLDER = "%hand%";

    public ChatListener(OptiQuest plugin) {
        this.plugin = plugin;
    }

    public void onPlayerChat(PlayerChatEvent event) {
        PlayerRef senderRef = event.getSender();
        OptiPlayer optiSender = plugin.getPlayerManager().getByUsername(senderRef.getUsername());

        if (optiSender == null) return;

        if (optiSender.isMuted()) {
            senderRef.sendMessage(plugin.getColorUtils().colorize("&cYou are muted."));
            event.setCancelled(true);
            return;
        }

        AtomicReference<String> itemIdBox = new AtomicReference<>("");
        String processedContent = resolvePlaceholders(optiSender, event.getContent(), itemIdBox);

        String groupName = optiSender.getPrimaryGroup();
        String groupColor = plugin.getConfig().getGroupColor(groupName);

        String format = plugin.getConfig().getChatFormat();

        String formattedPrefix = format
                .replace("%groupcolor%", groupColor)
                .replace("%groupname%", groupName)
                .replace("%displayname%", optiSender.getDisplayName())
                .replace("%message%", ""); // We leave %message% empty here because it's handled below

        Message finalMessage;
        String itemId = itemIdBox.get();

        if (!itemId.isEmpty() && processedContent.contains(HAND_PLACEHOLDER)) {
            String[] parts = processedContent.split(HAND_PLACEHOLDER, 2);

            Message prefixPart = plugin.getColorUtils().colorize(formattedPrefix + parts[0]);
            Message itemTranslation = Message.translation("server.items." + itemId + ".name");

            if (parts.length > 1 && !parts[1].isEmpty()) {
                finalMessage = Message.join(prefixPart, itemTranslation, plugin.getColorUtils().colorize(parts[1]));
            } else {
                finalMessage = Message.join(prefixPart, itemTranslation);
            }
        } else {
            finalMessage = plugin.getColorUtils().colorize(formattedPrefix + processedContent.replace(HAND_PLACEHOLDER, "Nothing"));
        }

        for (PlayerRef target : event.getTargets()) {
            target.sendMessage(finalMessage);
        }

        event.setCancelled(true);
    }

    private String resolvePlaceholders(OptiPlayer player, String message, AtomicReference<String> itemIdBox) {
        PlayerRef senderRef = player.getPlayerRef();
        Ref<EntityStore> entityRef = senderRef.getReference();
        if (entityRef == null) return message;

        final AtomicReference<Float> health = new AtomicReference<>(0f);
        final AtomicReference<Float> stamina = new AtomicReference<>(0f);
        final AtomicReference<Float> mana = new AtomicReference<>(0f);

        final CountDownLatch latch = new CountDownLatch(1);
        World world = entityRef.getStore().getExternalData().getWorld();

        world.execute(() -> {
            try {
                Store<EntityStore> store = entityRef.getStore();

                EntityStatMap statMap = store.getComponent(entityRef, EntityStatMap.getComponentType());
                if (statMap != null) {
                    health.set(Objects.requireNonNull(statMap.get(DefaultEntityStatTypes.getHealth())).get());
                    stamina.set(Objects.requireNonNull(statMap.get(DefaultEntityStatTypes.getStamina())).get());
                    mana.set(Objects.requireNonNull(statMap.get(DefaultEntityStatTypes.getMana())).get());
                }

                Player playerComp = store.getComponent(entityRef, Player.getComponentType());
                if (playerComp != null && playerComp.getInventory() != null) {
                    ItemStack handItem = playerComp.getInventory().getItemInHand();
                    if (handItem != null && !handItem.isEmpty()) {
                        itemIdBox.set(handItem.getItemId());
                    }
                }
            } finally {
                latch.countDown();
            }
        });

        try {
            if (!latch.await(30, TimeUnit.MILLISECONDS)) {
                plugin.getLogger().at(Level.WARNING).log("[OptiQuest] Thread timeout for {0}", senderRef.getUsername());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Vector3d pos = senderRef.getTransform().getPosition();

        return message
                .replace("%location%", String.format("(%.0f, %.0f, %.0f)", pos.x, pos.y, pos.z))
                .replace("%health%", String.format("%.0f", health.get()))
                .replace("%stamina%", String.format("%.0f", stamina.get()))
                .replace("%mana%", String.format("%.0f", mana.get()))
                .replace("%name%", senderRef.getUsername());
    }
}
