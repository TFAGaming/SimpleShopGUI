package simpleshopgui.utils.shop;

import java.util.List;

import com.google.common.collect.Lists;

public class Category {
    public static final int BUILDING_BLOCKS = 1;
    public static final int FOOD = 1 << 1;
    public static final int MINERALS = 1 << 2;
    public static final int MISCELLANEOUS = 1 << 3;
    public static final int NATURAL = 1 << 4;
    public static final int REDSTONE = 1 << 5;
    public static final int TOOLS = 1 << 6;

    public static List<String> getAllCategories(boolean lower) {
        if (lower) {
            return Lists.newArrayList("blocks", "tools", "food", "minerals", "natural",
                "redstone", "miscellaneous");
        } else {
            return Lists.newArrayList("Blocks", "Tools", "Food", "Minerals", "Natural",
                "Redstone", "Miscellaneous");
        }
    }
}
