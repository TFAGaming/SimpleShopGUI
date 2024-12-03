package simpleshopgui.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerGUIManager {
    public static Map<UUID, Boolean> playerTriggerEvent = new HashMap<UUID, Boolean>();
    public static Map<UUID, Integer> playerInventoryId = new HashMap<UUID, Integer>();

    public static void setCurrentInventoryId(Player player, int id) {
        playerInventoryId.put(player.getUniqueId(), id);
    }

    public static void removeCurrentInventory(Player player) {
        playerInventoryId.remove(player.getUniqueId());
    }

    public static boolean hasCurrentInventoryOpened(Player player) {
        return playerInventoryId.containsKey(player.getUniqueId());
    }

    public static int getCurrentInventory(Player player) {
        Object value = playerInventoryId.get(player.getUniqueId());

        if (value == null) {
            return -1;
        }

        return (int) value;
    }
}
