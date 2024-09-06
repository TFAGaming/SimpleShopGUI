package simpleshopgui.events.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import simpleshopgui.events.gui.paginations.BuyItemGUIListener;
import simpleshopgui.events.gui.paginations.ListedItemsGUIListener;
import simpleshopgui.gui.ShopGUI;
import simpleshopgui.utils.gui.PaginationGUI;
import simpleshopgui.utils.shop.ShopUtils;

public class PaginationGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (PaginationGUI.playerInventory.containsKey(player.getUniqueId())) {
            switch (ShopUtils.getCurrentInventoryId(player)) {
                case 2:
                    event.setCancelled(true);

                    Inventory inventory = PaginationGUI.playerInventory.get(player.getUniqueId());
                    int inventory_size = inventory.getSize() / 36;

                    int last_index_last_line = (9 * 6 * inventory_size) - 1;
                    int first_index_last_line = last_index_last_line - 8;
                    int centered_index_last_line = last_index_last_line - 4;

                    PaginationGUI pagegui = PaginationGUI.instance.get(player.getUniqueId());

                    if (event.getSlot() == centered_index_last_line) {
                        return;
                    }

                    if (event.getSlot() == first_index_last_line) {
                        if (pagegui.getPage() == 0) {
                            ShopGUI.playerCurrentCategory.remove(player.getUniqueId());

                            ShopGUI gui = new ShopGUI(player);

                            gui.openInventory();

                            ShopUtils.triggerBuy = false;
                        } else {
                            pagegui.previousPage();
                        }
                    } else if (event.getSlot() == last_index_last_line) {
                        pagegui.nextPage();
                    } else {
                        if (!ShopUtils.triggerBuy) {
                            ShopUtils.triggerBuy = true;
                        } else {
                            ShopUtils.triggerBuy = false;

                            BuyItemGUIListener.listen(event, player, pagegui,
                                    ShopGUI.playerCurrentCategory.get(player.getUniqueId()));
                        }

                    }
                    break;
                case 4:
                    event.setCancelled(true);

                    Inventory inventory_4 = PaginationGUI.playerInventory.get(player.getUniqueId());
                    int inventory_size_4 = inventory_4.getSize() / 36;

                    int last_index_last_line_4 = (9 * 6 * inventory_size_4) - 1;
                    int first_index_last_line_4 = last_index_last_line_4 - 8;
                    int centered_index_last_line_4 = last_index_last_line_4 - 4;

                    PaginationGUI pagegui_4 = PaginationGUI.instance.get(player.getUniqueId());

                    if (event.getSlot() == centered_index_last_line_4) {
                        return;
                    }

                    if (event.getSlot() == first_index_last_line_4) {
                        if (pagegui_4.getPage() == 0) {
                            player.closeInventory();
                        } else {
                            pagegui_4.previousPage();
                        }
                    } else if (event.getSlot() == last_index_last_line_4) {
                        pagegui_4.nextPage();
                    } else {
                        ListedItemsGUIListener.listen(event, player, pagegui_4);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (PaginationGUI.playerInventory.containsKey(player.getUniqueId())) {
            PaginationGUI.playerInventory.remove(player.getUniqueId());
            PaginationGUI.instance.remove(player.getUniqueId());

            ShopGUI.playerCurrentCategory.remove(player.getUniqueId());

            ShopUtils.triggerBuy = false;
        }
    }
}