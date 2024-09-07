package simpleshopgui.commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;
import simpleshopgui.managers.ShopDatabaseManager;
import simpleshopgui.utils.colors.ChatColorTranslator;
import simpleshopgui.utils.shop.ShopUtils;

public class SellCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack itemInHand = player.getInventory().getItemInMainHand();

            if (itemInHand == null || itemInHand.getType() == Material.AIR) {
                player.sendMessage(ChatColorTranslator
                        .translate(Plugin.config.getString("messages.commands.sell.no_item_in_hand")));
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(ChatColorTranslator
                        .translate(Plugin.config.getString("messages.commands.sell.no_price_provided")));
                return true;
            }

            try {
                if (ShopUtils.parseFromStringToDouble(args[0]) <= 0) {
                    player.sendMessage(ChatColorTranslator
                            .translate(Plugin.config.getString("messages.commands.sell.price_error")));
                    return true;
                }
            } catch (NumberFormatException err) {
                player.sendMessage(ChatColorTranslator
                        .translate(Plugin.config.getString("messages.commands.sell.price_error")));
                return true;
            }

            double price = ShopUtils.parseFromStringToDouble(args[0]);

            if (price > Plugin.config.getDouble("shop.max_item_price")) {
                player.sendMessage(ChatColorTranslator
                        .translate(Plugin.config.getString("messages.commands.sell.price_over_max").replace(
                                "%max_price%",
                                ShopUtils.parseFromDoubleToString(Plugin.config.getDouble("shop.max_item_price")))));
                return true;
            }

            ItemStack itemStack = itemInHand.clone();

            ShopDatabaseManager.addItemToShop(player, itemStack, price);

            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

            player.sendMessage(ChatColorTranslator
                    .translate(Plugin.config.getString("messages.commands.sell.item_sold")
                            .replace("%item_amount%", "" + itemStack.getAmount())
                            .replace("%item_name%", ShopUtils.userFriendlyItemName(itemStack.getType().name()))));

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (args.length == 1) {
                double parsed = ShopUtils.parseFromStringToDouble(args[0]);

                if (parsed <= 0) {
                    return Lists.newArrayList();
                }

                return Lists.newArrayList(ShopUtils.parseFromDoubleToString(parsed).replace(",", "."));
            }

            return Lists.newArrayList();
        } catch (NumberFormatException err) {
            return Lists.newArrayList();
        }
    }
}
