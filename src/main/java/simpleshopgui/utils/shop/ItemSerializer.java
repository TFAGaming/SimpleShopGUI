package simpleshopgui.utils.shop;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemSerializer {
    public static String serialize(ItemStack item) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(byteStream);

            bukkitStream.writeObject(item);
            bukkitStream.close();

            return Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack deserialize(String serializedItem) {
        try {
            byte[] itemData = Base64.getDecoder().decode(serializedItem);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(itemData);
            BukkitObjectInputStream bukkitStream = new BukkitObjectInputStream(byteStream);

            ItemStack item = (ItemStack) bukkitStream.readObject();
            bukkitStream.close();

            return item;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
