package simpleshopgui.events.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import simpleshopgui.Plugin;
import simpleshopgui.gui.BuyGUI;
import simpleshopgui.gui.ShopGUI;
import simpleshopgui.gui.ShopGUIBuildingBlocks;
import simpleshopgui.gui.ShopGUIFood;
import simpleshopgui.gui.ShopGUIMinerals;
import simpleshopgui.gui.ShopGUIMiscellaneous;
import simpleshopgui.gui.ShopGUINatural;
import simpleshopgui.gui.ShopGUIRedstone;
import simpleshopgui.gui.ShopGUITools;
import simpleshopgui.managers.PlayerGUIManager;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.chat.ChatColorTranslator;
import simpleshopgui.utils.gui.GUIIdentity;
import simpleshopgui.utils.player.PlayerUtils;
import simpleshopgui.utils.shop.ShopUtils;

public class GUIListener implements Listener {
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

        switch (PlayerGUIManager.getCurrentInventory(player)) {
            case GUIIdentity.SHOP_GUI:
                event.setCancelled(true);

                switch (event.getSlot()) {
                    case 10:
                        ShopGUIBuildingBlocks.create(player);
                        break;
                    case 11:
                        ShopGUITools.create(player);
                        break;
                    case 12:
                        ShopGUIFood.create(player);
                        break;
                    case 13:
                        ShopGUIMinerals.create(player);
                        break;
                    case 14:
                        ShopGUINatural.create(player);
                        break;
                    case 15:
                        ShopGUIRedstone.create(player);
                        break;
                    case 16:
                        ShopGUIMiscellaneous.create(player);
                        break;
                    default:
                        break;
                }

                PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), false);

                break;
            case GUIIdentity.BUY_GUI:
                event.setCancelled(true);

                boolean isShulkerBox = BuyGUI.playerShulkerBoxInventory.get(player.getUniqueId());

                if (!isShulkerBox) {
                    switch (event.getSlot()) {
                        case 10:
                            ShopGUI.playerSelectedCategory.remove(player.getUniqueId());

                            ShopGUI gui = new ShopGUI(player);

                            gui.openInventory();

                            break;
                        case 16:
                            List<Object> selectedItem = BuyGUI.playerSelectedItem.get(player.getUniqueId());

                            deletePlayerData(player);
                            player.closeInventory();

                            int itemId = (int) selectedItem.get(0);

                            if (!ShopDatabaseManager.itemExists(itemId)) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.invalid_item")));
                                return;
                            }

                            if (ShopDatabaseManager.getExpiresAt(itemId) < System.currentTimeMillis()) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.item_expired")));
                                return;
                            }

                            if (!PlayerUtils.hasAvailableSlot(player)) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.inventory_full")));
                                return;
                            }

                            OfflinePlayer seller = ShopDatabaseManager.getSeller(itemId);
                            double price = ShopDatabaseManager.getPrice(itemId);

                            if (seller.getUniqueId().equals(player.getUniqueId())) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.player_is_item_seller")));
                                return;
                            }

                            if (PlayerUtils.getPlayerBalance(player) < price) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.too_expensive")
                                                .replace("%player_balance%", ShopUtils
                                                        .parseFromDoubleToString(
                                                                PlayerUtils.getPlayerBalance(player)))));
                                return;
                            }

                            ItemStack itemStack = ShopDatabaseManager.getItem(itemId);

                            if (seller != null) {
                                PlayerUtils.addMoneyToPlayer(seller, price);

                                if (seller.isOnline() && Plugin.config.getBoolean("shop.show_buy_notification")) {
                                    Bukkit.getPlayer(seller.getUniqueId()).sendMessage(ChatColorTranslator
                                            .translate(Plugin.config.getString("messages.notification.player_buy")
                                                    .replace("%buyer_name%", player.getName())
                                                    .replace("%item_amount%", "" + itemStack.getAmount())
                                                    .replace("%item_name%",
                                                            ShopUtils.userFriendlyItemName(itemStack.getType().name()))
                                                    .replace("%item_price%",
                                                            ShopUtils.parseFromDoubleToString(price))));
                                }
                            }

                            PlayerUtils.removeMoneyFromPlayer(player, price);

                            ShopDatabaseManager.removeItemFromShop(itemId);
                            player.getInventory().addItem(itemStack);

                            break;
                        default:
                            break;
                    }
                } else {
                    switch (event.getSlot()) {
                        case 37:
                            ShopGUI.playerSelectedCategory.remove(player.getUniqueId());

                            ShopGUI gui = new ShopGUI(player);

                            gui.openInventory();

                            break;
                        case 43:
                            List<Object> selectedItem = BuyGUI.playerSelectedItem.get(player.getUniqueId());

                            deletePlayerData(player);
                            player.closeInventory();

                            int itemId = (int) selectedItem.get(0);

                            if (!ShopDatabaseManager.itemExists(itemId)) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.invalid_item")));
                                return;
                            }

                            if (ShopDatabaseManager.getExpiresAt(itemId) < System.currentTimeMillis()) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.item_expired")));
                                return;
                            }

                            if (!PlayerUtils.hasAvailableSlot(player)) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.inventory_full")));
                                return;
                            }

                            OfflinePlayer seller = ShopDatabaseManager.getSeller(itemId);
                            double price = ShopDatabaseManager.getPrice(itemId);

                            if (seller.getUniqueId().equals(player.getUniqueId())) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.player_is_item_seller")));
                                return;
                            }

                            if (PlayerUtils.getPlayerBalance(player) < price) {
                                player.sendMessage(ChatColorTranslator
                                        .translate(Plugin.config.getString("messages.guis.buy.too_expensive")
                                                .replace("%player_balance%", ShopUtils
                                                        .parseFromDoubleToString(
                                                                PlayerUtils.getPlayerBalance(player)))));
                                return;
                            }

                            ItemStack itemStack = ShopDatabaseManager.getItem(itemId);

                            if (seller != null) {
                                PlayerUtils.addMoneyToPlayer(seller, price);

                                if (seller.isOnline() && Plugin.config.getBoolean("shop.show_buy_notification")) {
                                    Bukkit.getPlayer(seller.getUniqueId()).sendMessage(ChatColorTranslator
                                            .translate(Plugin.config.getString("messages.notification.player_buy")
                                                    .replace("%buyer_name%", player.getName())
                                                    .replace("%item_amount%", "" + itemStack.getAmount())
                                                    .replace("%item_name%",
                                                            ShopUtils.userFriendlyItemName(itemStack.getType().name()))
                                                    .replace("%item_price%",
                                                            ShopUtils.parseFromDoubleToString(price))));
                                }
                            }

                            PlayerUtils.removeMoneyFromPlayer(player, price);
                            ShopDatabaseManager.removeItemFromShop(itemId);

                            player.getInventory().addItem(itemStack);

                            break;
                        default:
                            break;
                    }
                }

                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        PlayerGUIManager.removeCurrentInventory(player);
        PlayerGUIManager.playerTriggerEvent.remove(player.getUniqueId());
    }

    private void deletePlayerData(Player player) {
        if (BuyGUI.playerShulkerBoxInventory.containsKey(player.getUniqueId())) {
            BuyGUI.playerShulkerBoxInventory.remove(player.getUniqueId());
            BuyGUI.playerSelectedItem.remove(player.getUniqueId());
        }
    }
}