package com.github.chromabreak.system;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

/**
 * DyeColorMapper - 染料颜色映射器
 * Dye Color Mapper
 * <p>
 * 负责将Minecraft的染料物品映射到韧性颜色系统
 * Responsible for mapping Minecraft dye items to toughness color system
 * <p>
 * 建立7种Minecraft染料与7种韧性颜色的一一对应关系：
 * Establishes one-to-one correspondence between 7 Minecraft dyes and 7 toughness colors:
 * - 红色染料 (RED_DYE) -> 红色韧性条 (ToughnessColor.RED)
 * Red Dye -> Red Toughness Bar
 * - 蓝色染料 (BLUE_DYE) -> 蓝色韧性条 (ToughnessColor.BLUE)
 * Blue Dye -> Blue Toughness Bar
 * - 绿色染料 (GREEN_DYE) -> 绿色韧性条 (ToughnessColor.GREEN)
 * Green Dye -> Green Toughness Bar
 * - 黄色染料 (YELLOW_DYE) -> 黄色韧性条 (ToughnessColor.YELLOW)
 * Yellow Dye -> Yellow Toughness Bar
 * - 白色染料 (WHITE_DYE) -> 白色韧性条 (ToughnessColor.WHITE)
 * White Dye -> White Toughness Bar
 * - 黑色染料 (BLACK_DYE) -> 黑色韧性条 (ToughnessColor.BLACK)
 * Black Dye -> Black Toughness Bar
 * - 橙色染料 (ORANGE_DYE) -> 橙色韧性条 (ToughnessColor.ORANGE)
 * Orange Dye -> Orange Toughness Bar
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 染料物品到韧性颜色的映射查询
 * Dye item to toughness color mapping lookup
 * - 染料物品有效性验证
 * Dye item validity verification
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 * <p>
 * 在静态初始化块中建立映射关系，确保线程安全
 * Establishes mapping relationship in static initialization block, ensuring thread safety
 */
public enum DyeColorMapper {
    ;

    /**
     * 染料到韧性颜色的映射表
     * Dye to toughness color mapping table
     * <p>
     * 存储Minecraft染料物品与韧性颜色的对应关系
     * Stores correspondence between Minecraft dye items and toughness colors
     */
    private static final Map<Item, ToughnessColor> DYE_TO_COLOR_MAP = new HashMap<>();

    /**
     * 静态初始化块 - 建立染料到颜色的映射关系
     * Static initialization block - Establish dye to color mapping relationship
     *
     * 在类加载时初始化映射表，确保线程安全
     * Initialize mapping table when class is loaded, ensuring thread safety
     */
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

    /**
     * 根据染料物品获取对应的韧性颜色
     * Get toughness color from dye item
     *
     * @param dyeItem 染料物品（支持：RED_DYE, BLUE_DYE, GREEN_DYE, YELLOW_DYE, WHITE_DYE, BLACK_DYE, ORANGE_DYE）
     *                Dye item (supported: RED_DYE, BLUE_DYE, GREEN_DYE, YELLOW_DYE, WHITE_DYE, BLACK_DYE, ORANGE_DYE)
     * @return 对应的韧性颜色，如果染料物品为null或未找到对应关系则返回null
     * Corresponding toughness color, returns null if dye item is null or mapping not found
     * <p>
     * 示例：传入Items.RED_DYE返回ToughnessColor.RED
     * Example: Pass Items.RED_DYE returns ToughnessColor.RED
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
     * @param item 要检查的物品
     *             Item to check
     * @return 如果物品是有效的染料（在映射表中存在）则返回true，否则返回false
     * Returns true if item is a valid dye (exists in mapping table), false otherwise
     * <p>
     * 用于验证物品是否可以作为染料使用
     * Used to verify if an item can be used as a dye
     */
    public static boolean isValidDye(final Item item) {
        return null != item && DyeColorMapper.DYE_TO_COLOR_MAP.containsKey(item);
    }
}

