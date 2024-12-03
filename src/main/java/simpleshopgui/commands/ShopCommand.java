package simpleshopgui.commands;

import java.util.List;
import java.util.Locale.Category;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;
import simpleshopgui.gui.ShopGUI;
import simpleshopgui.gui.ShopGUIBuildingBlocks;
import simpleshopgui.gui.ShopGUIFood;
import simpleshopgui.gui.ShopGUIMinerals;
import simpleshopgui.gui.ShopGUIMiscellaneous;
import simpleshopgui.gui.ShopGUINatural;
import simpleshopgui.gui.ShopGUIRedstone;
import simpleshopgui.gui.ShopGUITools;
import simpleshopgui.managers.PlayerGUIManager;
import simpleshopgui.utils.chat.ChatColorTranslator;
import simpleshopgui.utils.player.PlayerUtils;

public class ShopCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!PlayerUtils.hasPermission(player, "shop")) {
                player.sendMessage(ChatColorTranslator
                        .translate(Plugin.config.getString("messages.permission_error")));
                return true;
            }

            if (args.length == 1) {
                if (!simpleshopgui.utils.shop.Category.getAllCategories(true).contains(args[0].toLowerCase())) {
                    player.sendMessage(ChatColorTranslator
                            .translate(Plugin.config.getString("messages.commands.shop.invalid_category")));
                    return true;
                }

                switch (args[0].toLowerCase()) {
                    case "blocks":
                        ShopGUIBuildingBlocks.create(player);
                        break;
                    case "tools":
                        ShopGUITools.create(player);
                        break;
                    case "food":
                        ShopGUIFood.create(player);
                        break;
                    case "minerals":
                        ShopGUIMinerals.create(player);
                        break;
                    case "natural":
                        ShopGUINatural.create(player);
                        break;
                    case "redstone":
                        ShopGUIRedstone.create(player);
                        break;
                    case "miscellaneous":
                        ShopGUIMiscellaneous.create(player);
                        break;
                    default:
                        break;
                }

                PlayerGUIManager.playerTriggerEvent.put(player.getUniqueId(), true);
            } else {
                ShopGUI gui = new ShopGUI(player);

                gui.openInventory();
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> categories = simpleshopgui.utils.shop.Category.getAllCategories(false);

        if (args.length == 1) {
            if (args[0].length() <= 0) {
                return categories;
            }

            List<String> returnList = Lists.newArrayList();

            for (String category : categories) {
                if (category.toLowerCase().startsWith(args[0].toLowerCase())) {
                    returnList.add(category);
                }
            }

            return returnList;
        }

        return Lists.newArrayList();
    }
}
