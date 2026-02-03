package net.opticraft.optiquest.util;

import com.hypixel.hytale.server.core.Message;
import net.opticraft.optiquest.OptiQuest;

import java.awt.Color;

public class ColorUtils {

    private final OptiQuest plugin;

    public ColorUtils(OptiQuest plugin) {
        this.plugin = plugin;
    }

    public Message colorize(String text) {
        if (text == null || text.isEmpty()) return Message.raw("");

        String[] parts = text.split("&");
        Message finalMessage = Message.raw("");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) continue;

            if (i == 0 && !text.startsWith("&")) {
                finalMessage = Message.join(finalMessage, Message.raw(part));
                continue;
            }

            if (part.startsWith("#") && part.length() >= 7) {
                String hexCode = part.substring(1, 7);
                String remainingText = part.substring(7);
                finalMessage = Message.join(finalMessage, Message.raw(remainingText).color(getHexColor(hexCode)));
            } else {
                char code = part.charAt(0);
                String remainingText = part.substring(1);
                finalMessage = Message.join(finalMessage, Message.raw(remainingText).color(getColorFromCode(code)));
            }
        }
        return finalMessage;
    }

    private Color getColorFromCode(char code) {
        return switch (Character.toLowerCase(code)) {
            case '0' -> Color.BLACK;
            case '1' -> Color.BLUE;
            case '2' -> Color.GREEN;
            case '3' -> Color.CYAN;
            case '4' -> Color.RED;
            case '5' -> Color.MAGENTA;
            case '6' -> Color.ORANGE;
            case '7' -> Color.GRAY;
            case '8' -> Color.DARK_GRAY;
            case '9' -> Color.BLUE;
            case 'a' -> Color.GREEN;
            case 'b' -> Color.CYAN;
            case 'c' -> Color.RED;
            case 'd' -> Color.PINK;
            case 'e' -> Color.YELLOW;
            case 'f' -> Color.WHITE;
            case 'x' -> Color.LIGHT_GRAY;
            default -> Color.WHITE;
        };
    }

    private Color getHexColor(String hex) {
        try {
            // Parse the hex string (e.g., "FF5555") into an integer
            int r = Integer.valueOf(hex.substring(0, 2), 16);
            int g = Integer.valueOf(hex.substring(2, 4), 16);
            int b = Integer.valueOf(hex.substring(4, 6), 16);
            return new Color(r, g, b);
        } catch (Exception e) {
            // If the user typed a bad hex (like &#GGGGGG), fallback to white
            return Color.WHITE;
        }
    }
}
