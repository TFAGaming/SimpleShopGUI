package simpleshopgui.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import simpleshopgui.Plugin;
import simpleshopgui.managers.PlayerGUIManager;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.chat.ChatColorTranslator;
import simpleshopgui.utils.gui.GUIIdentity;
import simpleshopgui.utils.gui.ItemGUI;
import simpleshopgui.utils.gui.PaginationGUI;
import simpleshopgui.utils.shop.ItemRarityCategorizer;
import simpleshopgui.utils.shop.ShopUtils;

public class SpecificMaterialGUI {
    public void create(Player player) {
        Material currenMaterial = ShopGUI.playerSelectedMaterial.get(player.getUniqueId());

        List<List<Object>> listed_items = ShopDatabaseManager.getListedItemsBySpecificMaterial(currenMaterial, false);

        listed_items.sort(Comparator.comparingDouble((List<Object> list) -> (double) list.get(4)).reversed());

        int count = listed_items.size();

        int total_pages = (int) Math.ceil((float) count / 36.0);

        if (total_pages <= 0) {
            total_pages = 1;
        }

        PaginationGUI pagegui = new PaginationGUI(player, 6,
                ChatColorTranslator.translate(Plugin.config.getString("gui.shop_specific_material.title")
                        .replace("%item_rarity_color%", ItemRarityCategorizer.getColor(currenMaterial))
                        .replace("%item_name%", ShopUtils.userFriendlyItemName(currenMaterial.name()))),
                total_pages);

        List<List<ItemStack>> pages = new ArrayList<>();

        for (int index = 0; index < total_pages; index++) {
            List<ItemStack> page = new ArrayList<>();

            int startIndex = index * 36;
            int endIndex = Math.min(startIndex + 36, count);

            for (int i = startIndex; i < endIndex; i++) {
                List<Object> each = listed_items.get(i);

                ItemStack item = (ItemStack) each.get(2);

                page.add(ItemGUI.customizedItemShopMeta(item, each, 1, false));
            }

            pages.add(page);
        }

        for (int i = 0; i < pages.size(); i++) {
            pagegui.addPage(i, pages.get(i));
        }

        pagegui.openInventory(pagegui);

        PlayerGUIManager.setCurrentInventoryId(player, GUIIdentity.SPECIFIC_MATERIAL_GUI);
    }
}
