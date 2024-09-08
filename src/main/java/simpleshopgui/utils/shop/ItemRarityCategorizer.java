package simpleshopgui.utils.shop;

import org.bukkit.Material;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemRarityCategorizer {
    private static final Map<Material, String> map = new HashMap<Material, String>();

    static {
        // UNCOMMON
        List<Material> uncommonList = Lists.newArrayList(
            Material.EXPERIENCE_BOTTLE,
            Material.DRAGON_BREATH,
            Material.ELYTRA,
            Material.CREEPER_HEAD,
            Material.ZOMBIE_HEAD,
            Material.SKELETON_SKULL,
            Material.WITHER_SKELETON_SKULL,
            Material.PIGLIN_HEAD,
            Material.PLAYER_HEAD,
            Material.HEART_OF_THE_SEA,
            Material.NETHER_STAR,
            Material.DRAGON_HEAD,
            Material.TOTEM_OF_UNDYING,
            Material.ENCHANTED_BOOK
        );
        
        // RARE
        List<Material> rareList = Lists.newArrayList(
            Material.BEACON,
            Material.CONDUIT,
            Material.END_CRYSTAL,
            Material.GOLDEN_APPLE
        );

        for (Material material : Material.values()) {
            if (material.name().startsWith("MUSIC_DISC_")) {
                rareList.add(material);
            }
        }

        // EPIC
        List<Material> epicList = Lists.newArrayList(
            Material.MACE,
            Material.DRAGON_EGG,
            Material.ENCHANTED_GOLDEN_APPLE,
            Material.HEAVY_CORE,
            // These ones below are unobtainable in survival or in vanilla
            Material.COMMAND_BLOCK,
            Material.COMMAND_BLOCK_MINECART,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.JIGSAW,
            Material.LIGHT,
            Material.BARRIER,
            Material.DEBUG_STICK,
            Material.KNOWLEDGE_BOOK
        );

        for (Material item : uncommonList) {
            map.put(item, "&e");
        }
        
        for (Material item : rareList) {
            map.put(item, "&b");
        }

        for (Material item : epicList) {
            map.put(item, "&d");
        }
    }

    public static String getColor(Material material) {
        return map.getOrDefault(material, "&f");
    }
}