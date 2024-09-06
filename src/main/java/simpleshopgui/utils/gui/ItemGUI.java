package simpleshopgui.utils.gui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import simpleshopgui.Plugin;
import simpleshopgui.utils.colors.ChatColorTranslator;
import simpleshopgui.utils.shop.ShopUtils;

public class ItemGUI {
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4");

    public static ItemStack getItem(String displayname, List<String> lore, String material,
            List<List<Object>> replacements) {
        if (replacements != null) {
            for (List<Object> replacement : replacements) {
                String replaced = displayname.replace((String) replacement.get(0), "" + replacement.get(1));

                displayname = replaced;
            }

            if (lore != null) {
                List<String> updated_lore = new ArrayList<>(lore);

                for (List<Object> replacement : replacements) {
                    String target = (String) replacement.get(0);
                    String replace = "" + replacement.get(1);

                    for (int i = 0; i < updated_lore.size(); i++) {
                        String line = updated_lore.get(i);
                        String replaced = line.replace(target, replace);

                        updated_lore.set(i, replaced);
                    }
                }

                lore = updated_lore;
            }
        }

        if (material.startsWith("PLAYERHEAD-")) {
            ItemStack playerhead = getCustomHeadTexture(Arrays.asList(material.split("-")).get(1));

            ItemMeta meta = playerhead.getItemMeta();

            meta.setDisplayName(ChatColorTranslator.translate(displayname));

            if (lore != null) {
                ArrayList<String> lorelist = new ArrayList<String>();

                for (String each : lore) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lorelist.add(ChatColorTranslator.translate(each));
                }

                meta.setLore(lorelist);
            }

            playerhead.setItemMeta(meta);

            return playerhead;
        } else {
            ItemStack item = new ItemStack(
                    Material.getMaterial(material) == null ? Material.BARRIER : Material.getMaterial(material));

            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColorTranslator.translate(displayname));

            if (lore != null) {
                ArrayList<String> lorelist = new ArrayList<String>();

                for (String each : lore) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lorelist.add(ChatColorTranslator.translate(each));
                }

                meta.setLore(lorelist);
            }

            item.setItemMeta(meta);

            return item;
        }
    }

    public static ItemStack customizedItemShopMeta(ItemStack itemStack, List<Object> itemData, int type,
            boolean... isInListedGUI) {
        ItemStack itemStackClone = itemStack.clone();

        ItemMeta itemMeta = itemStackClone.getItemMeta();

        if (itemMeta != null) {
            List<String> lore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

            if (type == 1) {
                /*
                 * lore.add("");
                 * lore.add(ChatColorTranslator.translate(
                 * "&aSeller: &f" + Bukkit.getOfflinePlayer(UUID.fromString((String)
                 * itemData.get(1))).getName()));
                 * lore.add(ChatColorTranslator.translate(
                 * "&aPrice: &f$" + ShopUtils.parseFromDoubleToString((double)
                 * itemData.get(3))));
                 * lore.add(ChatColorTranslator.translate(
                 * "&aExpires: &f" + ShopUtils.getTimeRemaining((double) itemData.get(5))));
                 * lore.add("");
                 * lore.add(ChatColorTranslator.translate(
                 * isInListedGUI.length > 0 && isInListedGUI[0] ? "&7Click to Remove" :
                 * "&7Click to Buy"));
                 */
                if (isInListedGUI.length > 0 && isInListedGUI[0]) {
                    List<String> lorelist = Plugin.config.getStringList("gui.listed_items.contents.ITEM.lore");

                    for (String each : lorelist) {
                        if (!each.contains("&")) {
                            each = "&f" + each;
                        }

                        lore.add(ChatColorTranslator.translate(
                                each.replace("%seller_name%",
                                        Bukkit.getOfflinePlayer(UUID.fromString((String) itemData.get(1))).getName())
                                        .replace("%item_price%",
                                                ShopUtils.parseFromDoubleToString((double) itemData.get(3)))
                                        .replace("%item_expires%",
                                                ShopUtils.getTimeRemaining((double) itemData.get(5)))));
                    }
                } else {
                    List<String> lorelist = Plugin.config.getStringList("gui.shop_category.contents.ITEM.lore");

                    for (String each : lorelist) {
                        if (!each.contains("&")) {
                            each = "&f" + each;
                        }

                        lore.add(ChatColorTranslator.translate(
                                each.replace("%seller_name%",
                                        Bukkit.getOfflinePlayer(UUID.fromString((String) itemData.get(1))).getName())
                                        .replace("%item_price%",
                                                ShopUtils.parseFromDoubleToString((double) itemData.get(3)))
                                        .replace("%item_expires%",
                                                ShopUtils.getTimeRemaining((double) itemData.get(5)))));
                    }
                }
            } else if (type == 2) {
                List<String> lorelist = Plugin.config.getStringList("gui.shop_buy.contents.ITEM.lore");

                for (String each : lorelist) {
                    if (!each.contains("&")) {
                        each = "&f" + each;
                    }

                    lore.add(ChatColorTranslator.translate(
                            each.replace("%item_price%", ShopUtils.parseFromDoubleToString((double) itemData.get(3)))));
                }
            }

            itemMeta.setLore(lore);

            itemStackClone.setItemMeta(itemMeta);
        }

        return itemStackClone;
    }

    public static ItemStack getCustomHeadTexture(String texture_url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwnerProfile(
                getProfile("https://textures.minecraft.net/texture/" + texture_url));
        head.setItemMeta(meta);

        return head;
    }

    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = Bukkit.createPlayerProfile(RANDOM_UUID);
        PlayerTextures textures = profile.getTextures();

        URL urlobject;

        try {
            urlobject = new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }

        textures.setSkin(urlobject);
        profile.setTextures(textures);

        return profile;
    }
}