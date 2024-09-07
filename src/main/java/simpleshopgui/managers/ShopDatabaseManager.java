package simpleshopgui.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import simpleshopgui.Plugin;
import simpleshopgui.database.ItemSerializer;
import simpleshopgui.utils.shop.ItemTypeCategorizer;

public class ShopDatabaseManager {
    private static final Map<Integer, List<Object>> cache = new HashMap<>();

    public static void updateCache() {
        String sql = "SELECT * FROM sold_items";

        try {
            if (!cache.isEmpty()) {
                cache.clear();
            }

            Connection connection = Plugin.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                String player_uuid = result.getString("player_uuid");
                String item_data = result.getString("item_data");
                double price = result.getDouble("price");
                double created_at = result.getDouble("created_at");
                double expires = result.getDouble("expires");
                String category = result.getString("category");

                List<Object> cache_data = new ArrayList<Object>();

                cache_data.add(id);
                cache_data.add(player_uuid);
                cache_data.add(item_data);
                cache_data.add(price);
                cache_data.add(created_at);
                cache_data.add(expires);
                cache_data.add(category);

                cache.put(id, cache_data);
            }

            statement.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public static void addItemToShop(Player player, ItemStack item, double price) {
        String sql = "INSERT INTO sold_items ( " +
                "player_uuid, " +
                "item_data, " +
                "price, " +
                "created_at, " +
                "expires," +
                "category" +
                ") VALUES (?,?,?,?,?,?)";

        try {
            Connection connection = Plugin.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, ItemSerializer.serialize(item));
            statement.setDouble(3, price);
            statement.setDouble(4, System.currentTimeMillis());
            statement.setDouble(5, System.currentTimeMillis() + Plugin.config.getDouble("shop.max_items_duration"));
            statement.setString(6, ItemTypeCategorizer.getCategory(item.getType()));
            
            statement.execute();
            statement.close();

            updateCache();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeItemFromShop(int itemId) {
        String sql = "DELETE FROM sold_items WHERE id = ?";

        try {
            Connection connection = Plugin.database.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, itemId);

            statement.execute();
            statement.close();

            cache.remove(itemId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<List<Object>> getListedItemsByPlayer(OfflinePlayer player) {
        List<List<Object>> arraylist = new ArrayList<List<Object>>();

        for (Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(1)).equals(player.getUniqueId().toString())) {
                List<Object> sub_arraylist = new ArrayList<Object>();

                sub_arraylist.add(data.get(0));
                sub_arraylist.add(data.get(1));
                sub_arraylist.add(ItemSerializer.deserialize((String) data.get(2)));
                sub_arraylist.add(data.get(3));
                sub_arraylist.add(data.get(4));
                sub_arraylist.add(data.get(5));
                sub_arraylist.add(data.get(6));

                arraylist.add(sub_arraylist);
            }
        }

        return arraylist;
    }

    public static List<List<Object>> getListedItemsBySpecificMaterial(Material material, boolean showExpired) {
        List<List<Object>> arraylist = new ArrayList<List<Object>>();

        for (Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (ItemSerializer.deserialize((String) data.get(2)).getType().equals(material)) {
                List<Object> sub_arraylist = new ArrayList<Object>();

                sub_arraylist.add(data.get(0));
                sub_arraylist.add(data.get(1));
                sub_arraylist.add(ItemSerializer.deserialize((String) data.get(2)));
                sub_arraylist.add(data.get(3));
                sub_arraylist.add(data.get(4));
                sub_arraylist.add(data.get(5));
                sub_arraylist.add(data.get(6));

                arraylist.add(sub_arraylist);
            }
        }

        if (showExpired) {
            return arraylist;
        } else {
            List<List<Object>> filteredList = new ArrayList<List<Object>>();

            for (List<Object> sublist : arraylist) {
                if (!sublist.isEmpty()) {
                    double time = (double) sublist.get(5);

                    if (time >= System.currentTimeMillis()) {
                        filteredList.add(sublist);
                    }
                }
            }

            return filteredList;
        }
    }

    public static List<List<Object>> getListedItemsByCategory(String category, boolean showExpired) {
        List<List<Object>> arraylist = new ArrayList<List<Object>>();
        List<Material> materialsList = new ArrayList<Material>();

        for (Entry<Integer, List<Object>> entry : cache.entrySet()) {
            List<Object> data = entry.getValue();

            if (((String) data.get(6)).equals(category)) {
                List<Object> sub_arraylist = new ArrayList<Object>();

                ItemStack item = ItemSerializer.deserialize((String) data.get(2));
                item.setAmount(1);

                if (materialsList.contains(item.getType())) {
                    continue;
                }

                materialsList.add(item.getType());

                sub_arraylist.add(data.get(0));
                sub_arraylist.add(data.get(1));
                sub_arraylist.add(item);
                sub_arraylist.add(data.get(3));
                sub_arraylist.add(data.get(4));
                sub_arraylist.add(data.get(5));
                sub_arraylist.add(data.get(6));

                arraylist.add(sub_arraylist);
            }
        }

        if (showExpired) {
            return arraylist;
        } else {
            List<List<Object>> filteredList = new ArrayList<List<Object>>();

            for (List<Object> sublist : arraylist) {
                if (!sublist.isEmpty()) {
                    double time = (double) sublist.get(5);

                    if (time >= System.currentTimeMillis()) {
                        filteredList.add(sublist);
                    }
                }
            }

            return filteredList;
        }
    }

    public static boolean itemExists(int id) {
        return cache.containsKey(id);
    }

    public static OfflinePlayer getSeller(int id) {
        if (cache.containsKey(id)) {
            List<Object> data = cache.get(id);

            return Bukkit.getOfflinePlayer(UUID.fromString((String) data.get(1)));
        }

        return null;
    }

    public static ItemStack getItem(int id) {
        if (cache.containsKey(id)) {
            List<Object> data = cache.get(id);

            return ItemSerializer.deserialize((String) data.get(2));
        }

        return null;
    }

    public static double getPrice(int id) {
        if (cache.containsKey(id)) {
            List<Object> data = cache.get(id);

            return (double) data.get(3);
        }

        return -1;
    }

    public static double getCreatedAt(int id) {
        if (cache.containsKey(id)) {
            List<Object> data = cache.get(id);

            return (double) data.get(4);
        }

        return -1;
    }

    public static double getExpiresAt(int id) {
        if (cache.containsKey(id)) {
            List<Object> data = cache.get(id);

            return (double) data.get(5);
        }

        return -1;
    }

    public static String getCategory(int id) {
        if (cache.containsKey(id)) {
            List<Object> data = cache.get(id);

            return (String) data.get(6);
        }

        return null;
    }
}
