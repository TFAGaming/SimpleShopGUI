package simpleshopgui.utils.players;

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

    public static void removeMoneyToPlayer(OfflinePlayer player, double amount) {
        Plugin.vaultapi.getEconomy().withdrawPlayer(player, amount);
    }
}
