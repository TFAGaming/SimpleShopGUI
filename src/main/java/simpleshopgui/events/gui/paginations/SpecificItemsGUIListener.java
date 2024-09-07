package simpleshopgui.events.gui.paginations;

import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import simpleshopgui.gui.ShopGUI;
import simpleshopgui.gui.ShopGUISpecificItem;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.gui.PaginationGUI;

public class SpecificItemsGUIListener {
    public static void listen(InventoryClickEvent event, Player player, PaginationGUI pagegui, String category) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }

        int clicked_item = event.getSlot();

        if (clicked_item > (9 * 6) - 1) {
            return;
        }

        List<List<Object>> listed_items = ShopDatabaseManager.getListedItemsByCategory(category, false);

        listed_items.sort(Comparator.comparingDouble((List<Object> list) -> (double) list.get(4)).reversed());

        int pageIndex = pagegui.getPage();
        int slotIndex = event.getSlot();

        if (pageIndex >= 0 && slotIndex >= 0) {
            int itemsPerPage = 36;
            int itemIndex = pageIndex * itemsPerPage + slotIndex;

            int startIndex = pageIndex * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, listed_items.size());

            List<List<Object>> itemsForCurrentPage = listed_items.subList(startIndex, endIndex);

            if (itemIndex < listed_items.size() && event.getSlot() < itemsForCurrentPage.size()) {
                List<Object> item_indexed = listed_items.get(itemIndex);

                ItemStack item = (ItemStack) item_indexed.get(2);

                ShopGUI.playerCurrentMaterial.put(player.getUniqueId(), item.getType());

                ShopGUISpecificItem gui = new ShopGUISpecificItem();

                gui.create(player);
            }
        }
    }
}