package simpleshopgui.events.gui.paginations;

import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import simpleshopgui.Plugin;
import simpleshopgui.gui.ListedItemsGUI;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.chat.ChatColorTranslator;
import simpleshopgui.utils.gui.PaginationGUI;
import simpleshopgui.utils.player.PlayerUtils;

public class ListedItemsGUIListener {
    public static void listen(InventoryClickEvent event, Player player, PaginationGUI pagegui) {
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

        List<List<Object>> listedItems = ShopDatabaseManager.getListedItemsByPlayer(player);

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

                if (!PlayerUtils.hasAvailableSlot(player)) {
                    player.closeInventory();

                    player.sendMessage(ChatColorTranslator
                                    .translate(Plugin.config.getString("messages.guis.listed.inventory_full")));
                    return;
                }

                ShopDatabaseManager.removeItemFromShop((int) item_indexed.get(0));
                player.getInventory().addItem((ItemStack) item_indexed.get(2));

                if (listedItems.size() - 1 > 0) {
                    ListedItemsGUI.create(player);
                } else {
                    player.closeInventory();
                }
            }
        }
    }
}