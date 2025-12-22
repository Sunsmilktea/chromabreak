package com.github.chromabreak.system;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * 染料颜色映射器
 * 将Minecraft的染料映射到韧性颜色
 * <p>
 * Dye Color Mapper
 * Maps Minecraft dyes to toughness colors
 */
public class DyeColorMapper {
    private static final Map<Item, ToughnessColor> DYE_TO_COLOR_MAP = new HashMap<>();

    static {
        // 初始化染料到颜色的映射（7种颜色与7种染料一一对应）
        // Initialize dye to color mapping (7 colors correspond to 7 dyes one-to-one)
        // 红色染料 -> 红色韧性条
        // Red Dye -> Red Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.RED_DYE, ToughnessColor.RED);

        // 蓝色染料 -> 蓝色韧性条
        // Blue Dye -> Blue Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.BLUE_DYE, ToughnessColor.BLUE);

        // 绿色染料 -> 绿色韧性条
        // Green Dye -> Green Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.GREEN_DYE, ToughnessColor.GREEN);

        // 黄色染料 -> 黄色韧性条
        // Yellow Dye -> Yellow Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.YELLOW_DYE, ToughnessColor.YELLOW);

        // 白色染料 -> 白色韧性条
        // White Dye -> White Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.WHITE_DYE, ToughnessColor.WHITE);

        // 黑色染料 -> 黑色韧性条
        // Black Dye -> Black Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.BLACK_DYE, ToughnessColor.BLACK);

        // 橙色染料 -> 橙色韧性条
        // Orange Dye -> Orange Toughness Bar
        DyeColorMapper.DYE_TO_COLOR_MAP.put(Items.ORANGE_DYE, ToughnessColor.ORANGE);
    }

    private DyeColorMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * 根据染料物品获取对应的韧性颜色
     * Get toughness color from dye item
     *
     * @param dyeItem 染料物品
     * @return 对应的韧性颜色，如果不是染料或未找到则返回null
     */
    public static ToughnessColor getColorFromDye(final Item dyeItem) {
        if (null == dyeItem) {
            return null;
        }
        return DyeColorMapper.DYE_TO_COLOR_MAP.get(dyeItem);
    }

    /**
     * 检查物品是否是有效的染料
     * Check if item is a valid dye
     *
     * @param item 物品
     * @return 是否是有效的染料
     */
    public static boolean isValidDye(final Item item) {
        return null != item && DyeColorMapper.DYE_TO_COLOR_MAP.containsKey(item);
    }
}

