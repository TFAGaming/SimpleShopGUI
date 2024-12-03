package simpleshopgui.utils.player;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import simpleshopgui.Plugin;

public class PlayerUtils {
    public static boolean hasAvailableSlot(Player player) {
        return player.getInventory().firstEmpty() == -1 ? false : true;
    }

    public static double getPlayerBalance(OfflinePlayer player) {
        return Plugin.vaultapi.getEconomy().getBalance(player);
    }

    public static void addMoneyToPlayer(OfflinePlayer player, double amount) {
        Plugin.vaultapi.getEconomy().depositPlayer(player, amount);
    }

    public static void removeMoneyFromPlayer(OfflinePlayer player, double amount) {
        Plugin.vaultapi.getEconomy().withdrawPlayer(player, amount);
    }

    public static boolean hasPermission(Player player, String command) {
        return player.hasPermission("simpleshopgui.commands." + command);
    }

    public static int getMaxListedItemsForPlayer(Player player) {
        String playerRank = getPlayerRank(player);

        String path = "ranks." + playerRank + ".max_listed_items";

        if (Plugin.config.contains(path)) {
            return Plugin.config.getInt(path);
        }

        return -1;
    }

    private static String getPlayerRank(Player player) {
        String[] groups = Plugin.vaultapi.getPermissions().getPlayerGroups(player);

        if (groups.length > 0) {
            return groups[0];
        }

        return "default";
    }
}
