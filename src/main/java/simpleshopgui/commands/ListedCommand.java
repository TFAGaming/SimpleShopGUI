package simpleshopgui.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;
import simpleshopgui.gui.ListedItemsGUI;
import simpleshopgui.utils.colors.ChatColorTranslator;
import simpleshopgui.utils.players.PlayerUtils;

public class ListedCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!PlayerUtils.hasPermission(player, "listed")) {
               player.sendMessage(ChatColorTranslator
                        .translate(Plugin.config.getString("messages.permission_error")));
                return true; 
            }

            ListedItemsGUI.create(player);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Lists.newArrayList();
    }
}
