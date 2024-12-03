package simpleshopgui.utils.shop;

import org.bukkit.Material;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.Map;

public class MaterialCategorizer {
    private static final Map<Material, Integer> map = new HashMap<>();

    static {
        for (Material material : Material.values()) {
            if (isTool(material)) {
                map.put(material, Category.TOOLS);
            } else if (isOre(material)) {
                map.put(material, Category.MINERALS);
            } else if (isNatural(material)) {
                map.put(material, Category.NATURAL);
            } else if (isRedstone(material)) {
                map.put(material, Category.REDSTONE);
            } else if (material.isEdible()) {
                map.put(material, Category.FOOD);
            } else if (material.isBlock()) {
                map.put(material, Category.BUILDING_BLOCKS);
            } else {
                map.put(material, Category.MISCELLANEOUS);
            }
        }
    }

    public static int getCategory(Material material) {
        return map.getOrDefault(material, Category.MISCELLANEOUS);
    }

    private static boolean isTool(Material material) {
        return material.name().endsWith("_SWORD") || material.name().endsWith("_AXE") ||
                material.name().endsWith("_PICKAXE") || material.name().endsWith("_SHOVEL") ||
                material.name().endsWith("_HOE") || material.name().contains("HELMET") ||
                material.name().contains("CHESTPLATE") || material.name().contains("LEGGINGS") ||
                material.name().contains("BOOTS") || material.name().endsWith("_HORSE_ARMOR") ||
                material.equals(Material.MACE) || material.equals(Material.SHIELD) ||
                material.equals(Material.BRUSH) || material.equals(Material.SHEARS) ||
                material.equals(Material.FISHING_ROD) || material.equals(Material.CARROT_ON_A_STICK) ||
                material.equals(Material.WARPED_FUNGUS_ON_A_STICK) || material.equals(Material.WIND_CHARGE) ||
                material.equals(Material.COMPASS) || material.equals(Material.RECOVERY_COMPASS) ||
                material.equals(Material.ELYTRA) || material.equals(Material.FLINT_AND_STEEL) ||
                material.equals(Material.LEAD) || material.equals(Material.WOLF_ARMOR) ||
                material.equals(Material.TRIDENT) || material.equals(Material.CROSSBOW) ||
                material.equals(Material.BOW) ;
    }

    private static boolean isOre(Material material) {
        return Lists.newArrayList(
                Material.DIAMOND,
                Material.DIAMOND_ORE,
                Material.DEEPSLATE_DIAMOND_ORE,
                Material.EMERALD,
                Material.EMERALD_ORE,
                Material.DEEPSLATE_EMERALD_ORE,
                Material.LAPIS_LAZULI,
                Material.LAPIS_ORE,
                Material.DEEPSLATE_LAPIS_ORE,
                Material.IRON_INGOT,
                Material.IRON_INGOT,
                Material.IRON_ORE,
                Material.DEEPSLATE_IRON_ORE,
                Material.COPPER_INGOT,
                Material.COPPER_ORE,
                Material.DEEPSLATE_COPPER_ORE,
                Material.GOLD_INGOT,
                Material.GOLD_NUGGET,
                Material.GOLD_ORE,
                Material.DEEPSLATE_GOLD_ORE,
                Material.NETHERITE_SCRAP,
                Material.NETHERITE_INGOT,
                Material.ANCIENT_DEBRIS,
                Material.COAL,
                Material.COAL_ORE,
                Material.DEEPSLATE_COAL_ORE,
                Material.CHARCOAL,
                Material.QUARTZ,
                Material.NETHER_QUARTZ_ORE,
                Material.AMETHYST_SHARD,
                Material.RAW_COPPER,
                Material.RAW_COPPER_BLOCK,
                Material.RAW_GOLD,
                Material.RAW_IRON,
                Material.RAW_IRON_BLOCK).contains(material);
    }

    private static boolean isNatural(Material material) {
        if (Lists.newArrayList(
                Material.CLAY,
                Material.PODZOL,
                Material.BAMBOO,
                Material.CACTUS,
                Material.SUGAR_CANE,
                Material.BROWN_MUSHROOM,
                Material.RED_MUSHROOM,
                Material.VINE,
                Material.SEAGRASS,
                Material.TALL_SEAGRASS,
                Material.SHORT_GRASS,
                Material.SEA_PICKLE,
                Material.KELP,
                Material.DEAD_BUSH,
                Material.FLOWERING_AZALEA,
                Material.AZALEA,
                Material.COBWEB,
                Material.LILY_PAD,
                Material.SPORE_BLOSSOM,
                Material.PINK_PETALS,
                Material.SCULK_VEIN,
                Material.LARGE_FERN,
                Material.NETHER_WART,
                Material.COCOA_BEANS,
                // Flowers ↓
                Material.DANDELION,
                Material.POPPY,
                Material.BLUE_ORCHID,
                Material.ALLIUM,
                Material.AZURE_BLUET,
                Material.RED_TULIP,
                Material.ORANGE_TULIP,
                Material.WHITE_TULIP,
                Material.PINK_TULIP,
                Material.OXEYE_DAISY,
                Material.CORNFLOWER,
                Material.LILY_OF_THE_VALLEY,
                Material.WITHER_ROSE,
                Material.SUNFLOWER,
                Material.LILAC,
                Material.ROSE_BUSH,
                Material.PEONY).contains(material)) {
            return true;
        }

        if (material.name().endsWith("_PLANT") ||
                material.name().endsWith("_FLOWER") ||
                material.name().contains("MUSHROOM") ||
                material.name().contains("LEAVES") ||
                material.name().contains("SAPLING") ||
                material.name().contains("VINE") ||
                material.name().contains("FUNGUS") ||
                material.name().contains("SPROUT") ||
                material.name().contains("HANGING_ROOTS") ||
                material.name().contains("MOSS") ||
                material.name().contains("CORAL") ||
                material.name().contains("FAN") ||
                material.name().contains("LILY") || material.name().endsWith("_SEEDS")) {
            return true;
        }

        return false;
    }

    private static boolean isRedstone(Material material) {
        return Lists.newArrayList(
                Material.REDSTONE,
                Material.REDSTONE_BLOCK,
                Material.REDSTONE_TORCH,
                Material.REPEATER,
                Material.COMPARATOR,
                Material.TARGET,
                Material.LEVER,
                Material.TRIPWIRE_HOOK,
                Material.DAYLIGHT_DETECTOR,
                Material.CRAFTER,
                Material.DISPENSER,
                Material.DROPPER,
                Material.PISTON,
                Material.STICKY_PISTON,
                Material.TRAPPED_CHEST,
                Material.OBSERVER,
                Material.NOTE_BLOCK,
                Material.REDSTONE_LAMP,
                Material.TRIPWIRE_HOOK,
                Material.HOPPER,
                Material.SCULK_SENSOR,
                Material.CALIBRATED_SCULK_SENSOR).contains(material) || material.name().endsWith("_PRESSURE_PLATE")
                || material.name().endsWith("_BUTTON") || material.name().endsWith("_COPPER_BULB");
    }

}