package simpleshopgui.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import simpleshopgui.Plugin;
import simpleshopgui.utils.colors.ChatColorTranslator;
import simpleshopgui.utils.gui.ItemGUI;
import simpleshopgui.utils.shop.ShopUtils;

public class BuyGUI {
    private final ItemStack item;
    private final Player player;
    private final Inventory inventory;
    private final boolean isShulkerBoxInventory;
    public static Map<UUID, Boolean> playerShulkerBoxInventory = new HashMap<UUID, Boolean>();
    public static Map<UUID, List<Object>> playerSelectedItem = new HashMap<UUID, List<Object>>();

    public BuyGUI(Player player, List<Object> itemData) {
        this.item = (ItemStack) itemData.get(2);
        this.player = player;

        boolean isShulkerBox = item.getType().name().endsWith("SHULKER_BOX");

        playerShulkerBoxInventory.put(player.getUniqueId(), isShulkerBox);
        this.isShulkerBoxInventory = isShulkerBox;

        playerSelectedItem.put(player.getUniqueId(), itemData);

        inventory = Bukkit.createInventory(null, isShulkerBox ? 9 * 6 : 9 * 3,
                ChatColorTranslator.translate(Plugin.config.getString("gui.shop_buy.title"))
                        .replace("%item_amount%", "" + item.getAmount())
                        .replace("%item_name%", ShopUtils.userFriendlyItemName(item.getType().toString())));

        initializeItems(item);
    }

    public void initializeItems(ItemStack item) {
        if (!isShulkerBoxInventory) {
            inventory.setItem(10,
                    ItemGUI.getItem(getConfigString("CANCEL.displayname"), getConfigStringList("CANCEL.lore"),
                            getConfigString("CANCEL.material"), null));

            inventory.setItem(13,
                    ItemGUI.customizedItemShopMeta(item, playerSelectedItem.get(player.getUniqueId()), 2));

            inventory.setItem(16, ItemGUI.getItem(getConfigString("BUY.displayname"), getConfigStringList("BUY.lore"),
                    getConfigString("BUY.material"), null));
        } else {
            List<List<Object>> shulkerBoxItemStacks = ShopUtils.getItemsFromShulkerBox(item);

            for (List<Object> each : shulkerBoxItemStacks) {
                inventory.setItem((int) each.get(1), (ItemStack) each.get(0));
            }

            inventory.setItem(37,
                    ItemGUI.getItem(getConfigString("CANCEL.displayname"), getConfigStringList("CANCEL.lore"),
                            getConfigString("CANCEL.material"), null));

            if (shulkerBoxItemStacks.size() > 0) {
                inventory.setItem(40,
                        ItemGUI.customizedItemShopMeta(item, playerSelectedItem.get(player.getUniqueId()), 2));
            } else {
                ItemStack newItem = ItemGUI.getItem("&f" + ShopUtils.userFriendlyItemName(item.getType().name()),
                        getConfigStringList("ITEM.__IF_SHULKER_IS_EMPTY__.lore"), item.getType().name(), null);

                inventory.setItem(40,
                        ItemGUI.customizedItemShopMeta(newItem, playerSelectedItem.get(player.getUniqueId()), 2));
            }

            inventory.setItem(43, ItemGUI.getItem(getConfigString("BUY.displayname"), getConfigStringList("BUY.lore"),
                    getConfigString("BUY.material"), null));
        }
    }

    public void openInventory() {
        player.openInventory(inventory);

        ShopUtils.setCurrentInventoryId(player, 3);
    }

    private String getConfigString(String path) {
        return Plugin.config.getString("gui.shop_buy.contents." + path);
    }

    private List<String> getConfigStringList(String path) {
        return Plugin.config.getStringList("gui.shop_buy.contents." + path);
    }
}
