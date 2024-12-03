package simpleshopgui.events.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import simpleshopgui.events.gui.paginations.BuyItemGUIListener;
import simpleshopgui.events.gui.paginations.ListedItemsGUIListener;
import simpleshopgui.events.gui.paginations.SpecificMaterialGUIListener;
import simpleshopgui.gui.ShopGUI;
import simpleshopgui.gui.ShopGUIBuildingBlocks;
import simpleshopgui.gui.ShopGUIFood;
import simpleshopgui.gui.ShopGUIMinerals;
import simpleshopgui.gui.ShopGUIMiscellaneous;
import simpleshopgui.gui.ShopGUINatural;
import simpleshopgui.gui.ShopGUIRedstone;
import simpleshopgui.gui.ShopGUITools;
import simpleshopgui.managers.PlayerGUIManager;
import simpleshopgui.utils.gui.GUIIdentity;
import simpleshopgui.utils.gui.PaginationGUI;
import simpleshopgui.utils.shop.Category;

public class PaginationGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() != null && event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }

        if (PaginationGUI.playerInventory.containsKey(player.getUniqueId())) {
            switch (PlayerGUIManager.getCurrentInventory(player)) {
                case GUIIdentity.CATEGORY_GUI:
                    event.setCancelled(true);

                    Inventory inventory = PaginationGUI.playerInventory.get(player.getUniqueId());
                    int inventory_size = inventory.getSize() / 36;

                    int last_index_last_line = (9 * 6 * inventory_size) - 1;
                    int first_index_last_line = last_index_last_line - 8;
                    int centered_index_last_line = last_index_last_line - 4;

                    PaginationGUI pagegui = PaginationGUI.playerPaginationInstance.get(player.getUniqueId());

                    if (event.getSlot() == centered_index_last_line) {
                        return;
                    }

                    if (event.getSlot() == first_index_last_line) {
                        if (pagegui.getPage() == 0) {
                            ShopGUI.playerSelectedCategory.remove(player.getUniqueId());
                            ShopGUI.playerSelectedMaterial.remove(player.getUniqueId());

                            ShopGUI gui = new ShopGUI(player);

                            gui.openInventory();

                            PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), false);
                        } else {
                            pagegui.previousPage();
                        }
                    } else if (event.getSlot() == last_index_last_line) {
                        pagegui.nextPage();
                    } else {
                        if (PlayerGUIManager.playerTriggerEvent.containsKey(player.getUniqueId()) && !PlayerGUIManager.playerTriggerEvent.get(player.getUniqueId())) {
                            PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), true);
                        } else {
                            PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), false);

                            SpecificMaterialGUIListener.listen(event, player, pagegui,
                                    ShopGUI.playerSelectedCategory.get(player.getUniqueId()));
                        }

                    }
                    break;
                case GUIIdentity.SPECIFIC_MATERIAL_GUI:
                    event.setCancelled(true);

                    Inventory inventory_3 = PaginationGUI.playerInventory.get(player.getUniqueId());
                    int inventory_size_3 = inventory_3.getSize() / 36;

                    int last_index_last_line_3 = (9 * 6 * inventory_size_3) - 1;
                    int first_index_last_line_3 = last_index_last_line_3 - 8;
                    int centered_index_last_line_3 = last_index_last_line_3 - 4;

                    PaginationGUI pagegui_3 = PaginationGUI.playerPaginationInstance.get(player.getUniqueId());

                    if (event.getSlot() == centered_index_last_line_3) {
                        return;
                    }

                    if (event.getSlot() == first_index_last_line_3) {
                        if (pagegui_3.getPage() == 0) {
                            if (ShopGUI.playerSelectedCategory.containsKey(player.getUniqueId())) {
                                PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), false);

                                ShopGUI.playerSelectedMaterial.remove(player.getUniqueId());

                                switch (ShopGUI.playerSelectedCategory.get(player.getUniqueId())) {
                                    case Category.BUILDING_BLOCKS:
                                        ShopGUIBuildingBlocks.create(player);
                                        break;
                                    case Category.TOOLS:
                                        ShopGUITools.create(player);
                                        break;
                                    case Category.FOOD:
                                        ShopGUIFood.create(player);
                                        break;
                                    case Category.MINERALS:
                                        ShopGUIMinerals.create(player);
                                        break;
                                    case Category.NATURAL:
                                        ShopGUINatural.create(player);
                                        break;
                                    case Category.REDSTONE:
                                        ShopGUIRedstone.create(player);
                                        break;
                                    case Category.MISCELLANEOUS:
                                        ShopGUIMiscellaneous.create(player);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), false);

                                ShopGUI.playerSelectedCategory.remove(player.getUniqueId());
                                ShopGUI.playerSelectedMaterial.remove(player.getUniqueId());

                                ShopGUI gui = new ShopGUI(player);

                                gui.openInventory();
                            }

                            // ShopUtils.playerTriggerEvent.put(player.getUniqueId(), false);
                        } else {
                            pagegui_3.previousPage();
                        }
                    } else if (event.getSlot() == last_index_last_line_3) {
                        pagegui_3.nextPage();
                    } else {
                        BuyItemGUIListener.listen(event, player, pagegui_3,
                                ShopGUI.playerSelectedMaterial.get(player.getUniqueId()));
                    }
                    break;
                case GUIIdentity.LISTED_ITEMS_GUI:
                    event.setCancelled(true);

                    Inventory inventory_5 = PaginationGUI.playerInventory.get(player.getUniqueId());
                    int inventory_size_5 = inventory_5.getSize() / 36;

                    int last_index_last_line_5 = (9 * 6 * inventory_size_5) - 1;
                    int first_index_last_line_5 = last_index_last_line_5 - 8;
                    int centered_index_last_line_5 = last_index_last_line_5 - 4;

                    PaginationGUI pagegui_5 = PaginationGUI.playerPaginationInstance.get(player.getUniqueId());

                    if (event.getSlot() == centered_index_last_line_5) {
                        return;
                    }

                    if (event.getSlot() == first_index_last_line_5) {
                        if (pagegui_5.getPage() == 0) {
                            player.closeInventory();
                        } else {
                            pagegui_5.previousPage();
                        }
                    } else if (event.getSlot() == last_index_last_line_5) {
                        pagegui_5.nextPage();
                    } else {
                        ListedItemsGUIListener.listen(event, player, pagegui_5);
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
            PaginationGUI.playerPaginationInstance.remove(player.getUniqueId());

            //ShopGUI.playerCurrentCategory.remove(player.getUniqueId());

            //ShopUtils.playerTriggerEvent.remove(player.getUniqueId());
        }
    }
}