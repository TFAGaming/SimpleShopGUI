package simpleshopgui.commands;

import java.util.List;

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
import simpleshopgui.utils.colors.ChatColorTranslator;
import simpleshopgui.utils.players.PlayerUtils;
import simpleshopgui.utils.shop.ShopUtils;

public class ShopCommand implements TabExecutor {
    private List<String> availableCategories = Lists.newArrayList("Blocks", "Tools", "Food", "Minerals", "Natural",
            "Redstone", "Miscellaneous");

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
                if (!availableCategories.contains(args[0])) {
                    player.sendMessage(ChatColorTranslator
                            .translate(Plugin.config.getString("messages.commands.shop.invalid_category")));
                    return true;
                }

                switch (args[0]) {
                    case "Blocks":
                        ShopGUIBuildingBlocks.create(player);
                        break;
                    case "Tools":
                        ShopGUITools.create(player);
                        break;
                    case "Food":
                        ShopGUIFood.create(player);
                        break;
                    case "Minerals":
                        ShopGUIMinerals.create(player);
                        break;
                    case "Natural":
                        ShopGUINatural.create(player);
                        break;
                    case "Redstone":
                        ShopGUIRedstone.create(player);
                        break;
                    case "Miscellaneous":
                        ShopGUIMiscellaneous.create(player);
                        break;
                    default:
                        break;
                }

                ShopUtils.playerTriggerEvent.put(player.getUniqueId(), true);
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
        if (args.length == 1) {
            if (args[0].length() <= 0) {
                return availableCategories;
            }

            List<String> finalList = Lists.newArrayList();

            for (String category : availableCategories) {
                if (category.toLowerCase().startsWith(args[0].toLowerCase())) {
                    finalList.add(category);
                }
            }

            return finalList;
        }

        return Lists.newArrayList();
    }
}
