package simpleshopgui.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;
import simpleshopgui.utils.colors.ChatColorTranslator;
import simpleshopgui.utils.gui.ItemGUI;
import simpleshopgui.utils.players.PlayerUtils;
import simpleshopgui.utils.shop.ShopUtils;

public class ShopGUI {
	private final Player player;
	private final Inventory inventory;
	public static Map<UUID, String> playerCurrentCategory = new HashMap<UUID, String>();
	public static Map<UUID, Material> playerCurrentMaterial = new HashMap<UUID, Material>();

	public ShopGUI(Player player) {
		this.player = player;
		inventory = Bukkit.createInventory(null, 9 * 4,
				ChatColorTranslator.translate(Plugin.config.getString("gui.shop.title")));

		initializeItems();
	}

	public void initializeItems() {
		inventory.setItem(10, ItemGUI.getItem(getConfigString("BUILDING_BLOCKS.displayname"),
				getConfigStringList("BUILDING_BLOCKS.lore"),
				getConfigString("BUILDING_BLOCKS.material"), null));

		inventory.setItem(11, ItemGUI.getItem(getConfigString("TOOLS.displayname"),
				getConfigStringList("TOOLS.lore"), getConfigString("TOOLS.material"), null));

		inventory.setItem(12, ItemGUI.getItem(getConfigString("FOOD.displayname"),
				getConfigStringList("FOOD.lore"), getConfigString("FOOD.material"), null));

		inventory.setItem(13, ItemGUI.getItem(getConfigString("MINERALS.displayname"),
				getConfigStringList("MINERALS.lore"), getConfigString("MINERALS.material"), null));

		inventory.setItem(14, ItemGUI.getItem(getConfigString("NATURAL.displayname"),
				getConfigStringList("NATURAL.lore"), getConfigString("NATURAL.material"), null));

		inventory.setItem(15, ItemGUI.getItem(getConfigString("REDSTONE.displayname"),
				getConfigStringList("REDSTONE.lore"), getConfigString("REDSTONE.material"), null));

		inventory.setItem(16, ItemGUI.getItem(getConfigString("MISCELLANEOUS.displayname"),
				getConfigStringList("MISCELLANEOUS.lore"), getConfigString("MISCELLANEOUS.material"),
				null));

		inventory.setItem(27, ItemGUI.getItem(getConfigString("MY_PROFILE.displayname"),
				getConfigStringList("MY_PROFILE.lore"), getConfigString("MY_PROFILE.material"),
				Lists.newArrayList(Lists.newArrayList("%player_name%", player.getName()),
						Lists.newArrayList("%player_balance%",
								ShopUtils.parseFromDoubleToString(PlayerUtils
										.getPlayerBalance(player))))));

		inventory.setItem(35, ItemGUI.getItem(getConfigString("HELP.displayname"),
				getConfigStringList("HELP.lore"), getConfigString("HELP.material"), null));
	}

	public void openInventory() {
		player.openInventory(inventory);

		ShopUtils.setCurrentInventoryId(player, 1);
	}

	private String getConfigString(String path) {
		return Plugin.config.getString("gui.shop.contents." + path);
	}

	private List<String> getConfigStringList(String path) {
		return Plugin.config.getStringList("gui.shop.contents." + path);
	}
}
