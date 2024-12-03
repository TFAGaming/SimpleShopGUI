package simpleshopgui.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;
import simpleshopgui.managers.PlayerGUIManager;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.chat.ChatColorTranslator;
import simpleshopgui.utils.gui.GUIIdentity;
import simpleshopgui.utils.gui.ItemGUI;
import simpleshopgui.utils.gui.PaginationGUI;
import simpleshopgui.utils.shop.Category;
import simpleshopgui.utils.shop.ShopUtils;

public class ShopGUIRedstone {
    public static void create(Player player) {
        List<List<Object>> listedItems = ShopDatabaseManager.getListedItemsByCategory(Category.REDSTONE, false);

        listedItems.sort(Comparator.comparingDouble((List<Object> list) -> (double) list.get(4)).reversed());

        int count = listedItems.size();

        int total_pages = (int) Math.ceil((float) count / 36.0);

        if (total_pages <= 0) {
            total_pages = 1;
        }

        PaginationGUI pagegui = new PaginationGUI(player, 6,
                ChatColorTranslator.translate(Plugin.config.getString("gui.shop_category.titles.REDSTONE")),
                total_pages);

        ShopGUI.playerSelectedCategory.put(player.getUniqueId(), Category.REDSTONE);

        List<List<ItemStack>> pages = new ArrayList<>();

        for (int index = 0; index < total_pages; index++) {
            List<ItemStack> page = new ArrayList<>();

            int startIndex = index * 36;
            int endIndex = Math.min(startIndex + 36, count);

            for (int i = startIndex; i < endIndex; i++) {
                List<Object> each = listedItems.get(i);

                ItemStack item = (ItemStack) each.get(2);

                int itemsCount = ShopDatabaseManager.getListedItemsBySpecificMaterial(item.getType(), false).size();

                page.add(ItemGUI.getItem("&f&r" + ShopUtils.userFriendlyItemName(item.getType().name()),
                        Plugin.config.getStringList("gui.shop_category.contents.ITEM.lore"),
                        item.getType().toString(),
                        Lists.newArrayList(Lists.newArrayList("%items_count%", "" + itemsCount), null)));
            }

            pages.add(page);
        }

        for (int i = 0; i < pages.size(); i++) {
            pagegui.addPage(i, pages.get(i));
        }

        pagegui.openInventory(pagegui);

        PlayerGUIManager.setCurrentInventoryId(player, GUIIdentity.CATEGORY_GUI);
    }
}
