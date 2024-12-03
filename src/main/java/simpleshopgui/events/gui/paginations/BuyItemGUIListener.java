package simpleshopgui.events.gui.paginations;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import simpleshopgui.gui.BuyGUI;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.gui.PaginationGUI;

public class BuyItemGUIListener {
    public static void listen(InventoryClickEvent event, Player player, PaginationGUI pagegui, Material material) {
        if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() != null && event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }

        int clicked_item = event.getSlot();

        if (clicked_item > (9 * 6) - 1) {
            return;
        }

        List<List<Object>> listedItems = ShopDatabaseManager.getListedItemsBySpecificMaterial(material, false);

        listedItems.sort(Comparator.comparingDouble((List<Object> list) -> (double) list.get(4)).reversed());

        int pageIndex = pagegui.getPage();
        int slotIndex = event.getSlot();

        if (pageIndex >= 0 && slotIndex >= 0) {
            int itemsPerPage = 36;
            int itemIndex = pageIndex * itemsPerPage + slotIndex;

            int startIndex = pageIndex * itemsPerPage;
            int endIndex = Math.min(startIndex + itemsPerPage, listedItems.size());

            List<List<Object>> itemsForCurrentPage = listedItems.subList(startIndex, endIndex);

            if (itemIndex < listedItems.size() && event.getSlot() < itemsForCurrentPage.size()) {
                List<Object> item_indexed = listedItems.get(itemIndex);

                BuyGUI gui = new BuyGUI(player, item_indexed);

                gui.openInventory();
            }
        }
    }
}