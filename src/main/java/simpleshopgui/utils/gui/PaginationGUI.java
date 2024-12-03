package simpleshopgui.utils.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import simpleshopgui.Plugin;

public class PaginationGUI {
    public static Map<UUID, Inventory> playerInventory = new HashMap<>();
    public static Map<UUID, PaginationGUI> playerPaginationInstance = new HashMap<>();

    private Player player;
    private Map<Integer, List<ItemStack>> data = new HashMap<>();
    private Inventory inventory;
    private int inventory_lines;
    private int total_pages;

    private int current_index = 0;

    public PaginationGUI(Player player, int lines, String title, int total_pages) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 9 * lines, title);
        this.inventory_lines = lines;
        this.total_pages = total_pages;
    }

    public void addPage(int page_index, List<ItemStack> items) {
        data.put(page_index, items);
    }

    public Map<Integer, List<ItemStack>> getPages() {
        return data;
    }

    public void setPage(int page_index) {
        current_index = page_index;
    }

    public int getPage() {
        return current_index;
    }

    public void nextPage() {
        int new_index = current_index + 1;

        if (new_index > (total_pages - 1)) {
            return;
        }

        updatePage(new_index, true);

        current_index = new_index;
    }

    public void previousPage() {
        int new_index = current_index - 1;

        if (new_index < 0) {
            return;
        }

        updatePage(new_index, true);

        current_index = new_index;
    }

    public void openInventory(PaginationGUI pagegui) {
        updatePage(0, false);

        player.openInventory(inventory);

        playerInventory.put(player.getUniqueId(), inventory);
        playerPaginationInstance.put(player.getUniqueId(), pagegui);
    }

    private void updatePage(int page_index, boolean clear_inventory) {
        if (clear_inventory) {
            inventory.clear();
        }

        List<ItemStack> items = data.get(page_index);

        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(i, items.get(i));
        }

        updateMenuButtons(page_index);
    }

    private void updateMenuButtons(int page_index) {
        int first_index_last_line = 9 * (inventory_lines - 1);
        int last_index_last_line = (9 * inventory_lines) - 1;

        ItemStack previous_page = ItemGUI.getItem(getConfigString("PREVIOUS_PAGE.displayname"),
                getConfigStringList("PREVIOUS_PAGE.lore"), getConfigString("PREVIOUS_PAGE.material"), null);

        ItemStack page_information = ItemGUI.getItem(getConfigString("CENTERED_INFO_ITEM.displayname"),
                getConfigStringList("CENTERED_INFO_ITEM.lore"), getConfigString("CENTERED_INFO_ITEM.material"),
                Lists.newArrayList(
                        Lists.newArrayList("%current_page%", page_index + 1),
                        Lists.newArrayList("%total_pages%", total_pages)));

        ItemStack next_page = ItemGUI.getItem(getConfigString("NEXT_PAGE.displayname"),
                getConfigStringList("NEXT_PAGE.lore"), getConfigString("NEXT_PAGE.material"), null);

        inventory.setItem(last_index_last_line, next_page);
        inventory.setItem(first_index_last_line + 4, page_information);
        inventory.setItem(first_index_last_line, previous_page);
    }

    private String getConfigString(String path) {
        return Plugin.config.getString("gui.__CONFIG_PAGINATION__." + path);
    }

    private List<String> getConfigStringList(String path) {
        return Plugin.config.getStringList("gui.__CONFIG_PAGINATION__." + path);
    }
}