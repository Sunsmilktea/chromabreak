package com.github.chromabreak.system;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * 武器辅助类
 * 判断物品是否是武器
 */
public enum WeaponHelper {
    ;

    /**
     * 检查物品是否是武器
     *
     * @param itemStack 物品堆栈
     * @return 是否是武器
     */
    public static boolean isWeapon(final ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        return WeaponHelper.isWeapon(itemStack.getItem());
    }

    /**
     * 检查物品是否是武器
     *
     * @param item 物品
     * @return 是否是武器
     */
    public static boolean isWeapon(final Item item) {
        if (null == item) {
            return false;
        }

        // 检查原版武器
        if (item == Items.WOODEN_SWORD || item == Items.STONE_SWORD || item == Items.IRON_SWORD ||
                item == Items.GOLDEN_SWORD || item == Items.DIAMOND_SWORD || item == Items.NETHERITE_SWORD ||
                item == Items.WOODEN_AXE || item == Items.STONE_AXE || item == Items.IRON_AXE ||
                item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.NETHERITE_AXE ||
                item == Items.TRIDENT) {
            return true;
        }

        // 检查模组武器（通过ID模式匹配）
        final String itemId = item.toString();
        return itemId.contains("sword") || itemId.contains("axe") || itemId.contains("weapon");
    }
}

