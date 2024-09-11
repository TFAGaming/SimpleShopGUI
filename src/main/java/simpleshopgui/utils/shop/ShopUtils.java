package simpleshopgui.utils.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;

public class ShopUtils {
    public static Map<UUID, Boolean> playerTriggerEvent = new HashMap<UUID, Boolean>();
    public static Map<UUID, Integer> inventoryCache = new HashMap<UUID, Integer>();

    public static void setCurrentInventoryId(Player player, int id) {
        inventoryCache.put(player.getUniqueId(), id);
    }

    public static void removeCurrentInventoryId(Player player) {
        inventoryCache.remove(player.getUniqueId());
    }

    public static boolean hasCurrentInventoryIdOpened(Player player) {
        return inventoryCache.containsKey(player.getUniqueId());
    }

    public static int getCurrentInventoryId(Player player) {
        Object value = inventoryCache.get(player.getUniqueId());

        if (value == null) {
            return -1;
        }

        return (int) value;
    }

    public static String userFriendlyItemName(String input) {
        String output = "";

        for (String s : input.split("_")) {
            output += s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
        }

        return output.substring(0, output.length() - 1);
    }

    public static double parseFromStringToDouble(String value) {
        value = value.trim();

        if (value.matches("\\d+\\.?\\d*")) {
            return Double.parseDouble(value);
        }

        if (value.length() <= 0) {
            return -1;
        }

        char suffix = value.charAt(value.length() - 1);
        double multiplier = 1.0;

        switch (suffix) {
            case 'K':
            case 'k':
                multiplier = 1_000;
                break;
            case 'M':
            case 'm':
                multiplier = 1_000_000;
                break;
            case 'B':
            case 'b':
                multiplier = 1_000_000_000;
                break;
            case 'T':
            case 't':
                multiplier = 1_000_000_000_000L;
                break;
            case 'Q':
            case 'q':
                multiplier = 1_000_000_000_000_000L;
                break;
            default:
                return -1;
        }

        String numericPart = value.substring(0, value.length() - 1);

        try {
            return Double.parseDouble(numericPart) * multiplier;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String parseFromDoubleToString(double value) {
        String[] suffixes = { "", "k", "M", "B", "T", "Q" };
        int suffixIndex = 0;

        while (value >= 1000 && suffixIndex < suffixes.length - 1) {
            value /= 1000.0;
            suffixIndex++;
        }

        return String.format("%.1f%s", value, suffixes[suffixIndex]);
    }

    public static String getTimeRemaining(double futureTimeMillis) {
        double currentTimeMillis = System.currentTimeMillis();

        double timeDifference = futureTimeMillis - currentTimeMillis;

        if (timeDifference < 0) {
            return "Expired";
        }

        long days = (long) (timeDifference / (1000 * 60 * 60 * 24));
        timeDifference %= (1000 * 60 * 60 * 24);

        long hours = (long) (timeDifference / (1000 * 60 * 60));
        timeDifference %= (1000 * 60 * 60);

        long minutes = (long) (timeDifference / (1000 * 60));
        timeDifference %= (1000 * 60);

        long seconds = (long) (timeDifference / 1000);

        return Plugin.config.getString("shop.time_remaining_format")
                .replace("%seconds%", "" + seconds).replace("%minutes%", "" + minutes)
                .replace("%hours%", "" + hours).replace("%days%", "" + days);
    }

    public static List<List<Object>> getItemsFromShulkerBox(ItemStack shulkerBoxItem) {
        List<List<Object>> items = Lists.newArrayList();

        if (shulkerBoxItem == null || !shulkerBoxItem.getType().name().endsWith("SHULKER_BOX")) {
            return items;
        }

        if (!(shulkerBoxItem.getItemMeta() instanceof BlockStateMeta)) {
            return items;
        }

        BlockStateMeta blockStateMeta = (BlockStateMeta) shulkerBoxItem.getItemMeta();

        if (!(blockStateMeta.getBlockState() instanceof ShulkerBox)) {
            return items;
        }

        ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();

        ItemStack[] shulkerItems = shulkerBox.getInventory().getContents();
        for (int i = 0; i < shulkerItems.length; i++) {
            ItemStack item = shulkerItems[i];

            if (item != null) {
                List<Object> itemData = new ArrayList<>();

                itemData.add(item);
                itemData.add(i);

                items.add(itemData);
            }
        }

        return items;
    }
}
