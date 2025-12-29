package com.github.chromabreak.system;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * WeaponHelper - 武器助手类
 * Weapon Helper Class
 * <p>
 * 负责判断物品是否是武器，支持原版武器和模组武器的识别
 * Responsible for determining if an item is a weapon, supports identification of vanilla and modded weapons
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 物品堆栈是否是武器的判断
 * Determine if an item stack is a weapon
 * - 物品是否是武器的判断
 * Determine if an item is a weapon
 * - 原版武器和模组武器的识别
 * Identification of vanilla and modded weapons
 * <p>
 * 支持的原版武器类型：
 * Supported vanilla weapon types:
 * - 剑类武器（木剑、石剑、铁剑、金剑、钻石剑、下界合金剑）
 * Sword weapons (wooden, stone, iron, golden, diamond, netherite)
 * - 斧类武器（木斧、石斧、铁斧、金斧、钻石斧、下界合金斧）
 * Axe weapons (wooden, stone, iron, golden, diamond, netherite)
 * - 三叉戟
 * Trident
 * <p>
 * 支持的模组武器识别模式：
 * Supported modded weapon identification patterns:
 * - 物品ID包含"sword"（剑）
 * Item ID contains "sword"
 * - 物品ID包含"axe"（斧）
 * Item ID contains "axe"
 * - 物品ID包含"weapon"（武器）
 * Item ID contains "weapon"
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 */
public enum WeaponHelper {
    ;

    /**
     * 检查物品堆栈是否是武器
     * Check if item stack is a weapon
     *
     * @param itemStack 要检查的物品堆栈
     *                  Item stack to check
     * @return 如果物品堆栈是武器则返回true，否则返回false
     * Returns true if item stack is a weapon, false otherwise
     * <p>
     * 首先检查物品堆栈是否为空，然后调用物品级别的武器检查
     * First checks if item stack is empty, then calls item-level weapon check
     */
    public static boolean isWeapon(final ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        return WeaponHelper.isWeapon(itemStack.getItem());
    }

    /**
     * 检查物品是否是武器
     * Check if item is a weapon
     *
     * @param item 要检查的物品
     *             Item to check
     * @return 如果物品是武器则返回true，否则返回false
     * Returns true if item is a weapon, false otherwise
     * <p>
     * 检查逻辑：
     * Checking logic:
     * 1. 检查是否为null
     * Check if null
     * 2. 检查原版武器类型
     * Check vanilla weapon types
     * 3. 检查模组武器（通过物品ID模式匹配）
     * Check modded weapons (via item ID pattern matching)
     */
    public static boolean isWeapon(final Item item) {
        if (null == item) {
            return false;
        }

        // 检查原版武器
        // Check vanilla weapons
        if (item == Items.WOODEN_SWORD || item == Items.STONE_SWORD || item == Items.IRON_SWORD ||
                item == Items.GOLDEN_SWORD || item == Items.DIAMOND_SWORD || item == Items.NETHERITE_SWORD ||
                item == Items.WOODEN_AXE || item == Items.STONE_AXE || item == Items.IRON_AXE ||
                item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.NETHERITE_AXE ||
                item == Items.TRIDENT) {
            return true;
        }

        // 检查模组武器（通过ID模式匹配）
        // Check modded weapons (via ID pattern matching)
        final String itemId = item.toString();
        return itemId.contains("sword") || itemId.contains("axe") || itemId.contains("weapon");
    }
}

