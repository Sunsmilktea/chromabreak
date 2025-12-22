package com.github.chromabreak.system;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 武器颜色辅助类
 * 处理武器上的颜色标记（存储在DataComponents中）
 * <p>
 * Weapon Color Helper Class
 * Handles color markers on weapons (stored in DataComponents)
 */
public enum WeaponColorHelper {
    ;

    private static final String COLORS_TAG = "chromabreak_weapon_colors";

    /**
     * 检查武器是否有指定颜色
     * Check if weapon has specified color
     *
     * @param itemStack 物品堆栈
     * @param color     颜色
     * @return 是否有该颜色
     */
    public static boolean hasColor(final ItemStack itemStack, final ToughnessColor color) {
        if (itemStack.isEmpty() || null == color) {
            return false;
        }

        final Set<ToughnessColor> colors = WeaponColorHelper.getColors(itemStack);
        return colors.contains(color);
    }

    /**
     * 获取武器上的所有颜色
     * Get all colors on weapon
     *
     * @param itemStack 物品堆栈
     * @return 颜色集合
     */
    public static Set<ToughnessColor> getColors(final ItemStack itemStack) {
        final Set<ToughnessColor> colors = new HashSet<>();

        if (itemStack.isEmpty()) {
            return colors;
        }

        try {
            // 从DataComponents读取颜色
            // Read colors from DataComponents
            final DataComponentMap components = itemStack.getComponents();
            if (null != components) {
                final var customData = components.get(DataComponents.CUSTOM_DATA);
                if (null != customData) {
                    // 使用正确的API获取NBT标签
                    // Use correct API to get NBT tag
                    final CompoundTag tag = customData.copyTag();
                    if (null != tag && tag.contains(WeaponColorHelper.COLORS_TAG)) {
                        final net.minecraft.nbt.ListTag colorsTag = tag.getList(WeaponColorHelper.COLORS_TAG, net.minecraft.nbt.Tag.TAG_STRING);
                        for (int i = 0; i < colorsTag.size(); i++) {
                            final String colorName = colorsTag.getString(i);
                            final ToughnessColor color = ToughnessColor.byName(colorName);
                            if (null != color) {
                                colors.add(color);
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
            // 忽略异常
        }

        return colors;
    }

    /**
     * 添加颜色到武器
     * Add color to weapon
     *
     * @param itemStack 物品堆栈
     * @param color     颜色
     * @return 是否成功添加
     */
    public static boolean addColor(final ItemStack itemStack, final ToughnessColor color) {
        if (itemStack.isEmpty() || null == color) {
            return false;
        }

        final Set<ToughnessColor> colors = WeaponColorHelper.getColors(itemStack);
        if (colors.contains(color)) {
            return true; // Already has this color
        }

        colors.add(color);

        try {
            // 使用正确的DataComponents API设置颜色
            // Use correct DataComponents API to set colors
            final CompoundTag protectionTag = new CompoundTag();
            final net.minecraft.nbt.ListTag colorsTag = new net.minecraft.nbt.ListTag();
            for (final ToughnessColor c : colors) {
                colorsTag.add(net.minecraft.nbt.StringTag.valueOf(c.getName()));
            }
            protectionTag.put(WeaponColorHelper.COLORS_TAG, colorsTag);

            // 使用反射创建CustomData对象
            // Use reflection to create CustomData object
            final Class<?> customDataClass = Class.forName("net.minecraft.core.component.CustomData");
            final java.lang.reflect.Method ofMethod = customDataClass.getMethod("of", CompoundTag.class);
            final Object customData = ofMethod.invoke(null, protectionTag);

            // 使用反射调用set方法
            // Use reflection to call set method
            final java.lang.reflect.Method setMethod = itemStack.getClass().getMethod("set",
                    net.minecraft.core.component.DataComponentType.class, Object.class);
            setMethod.invoke(itemStack, DataComponents.CUSTOM_DATA, customData);
            return true;
        } catch (final Exception e) {
            // 如果API调用失败，返回false
            // If API call fails, return false
            return false;
        }
    }

    /**
     * 移除武器上的颜色
     * Remove color from weapon
     *
     * @param itemStack 物品堆栈
     * @param color     颜色
     * @return 是否成功移除
     */
    public static boolean removeColor(final ItemStack itemStack, final ToughnessColor color) {
        if (itemStack.isEmpty() || null == color) {
            return false;
        }

        final Set<ToughnessColor> colors = WeaponColorHelper.getColors(itemStack);
        if (!colors.contains(color)) {
            return true; // Already doesn't have this color
        }

        colors.remove(color);

        try {
            // 使用正确的DataComponents API设置颜色
            // Use correct DataComponents API to set colors
            final CompoundTag protectionTag = new CompoundTag();
            if (!colors.isEmpty()) {
                final net.minecraft.nbt.ListTag colorsTag = new net.minecraft.nbt.ListTag();
                for (final ToughnessColor c : colors) {
                    colorsTag.add(net.minecraft.nbt.StringTag.valueOf(c.getName()));
                }
                protectionTag.put(WeaponColorHelper.COLORS_TAG, colorsTag);
            }

            // 使用反射创建CustomData对象
            // Use reflection to create CustomData object
            final Class<?> customDataClass = Class.forName("net.minecraft.core.component.CustomData");
            final java.lang.reflect.Method ofMethod = customDataClass.getMethod("of", CompoundTag.class);
            final Object customData = ofMethod.invoke(null, protectionTag);

            // 使用反射调用set方法
            // Use reflection to call set method
            final java.lang.reflect.Method setMethod = itemStack.getClass().getMethod("set",
                    net.minecraft.core.component.DataComponentType.class, Object.class);
            setMethod.invoke(itemStack, DataComponents.CUSTOM_DATA, customData);
            return true;
        } catch (final Exception e) {
            // 如果API调用失败，返回false
            // If API call fails, return false
            return false;
        }
    }

    /**
     * 检查武器是否可以破坏指定颜色的韧性
     * Check if weapon can break toughness of specified color
     *
     * @param itemStack 物品堆栈
     * @param color     颜色
     * @return 是否可以破坏
     */
    public static boolean canBreakColor(final ItemStack itemStack, final ToughnessColor color) {
        return WeaponColorHelper.hasColor(itemStack, color);
    }

    /**
     * 检查武器是否可以破坏指定颜色分布的韧性
     * Check if weapon can break toughness of specified color distribution
     *
     * @param itemStack    物品堆栈
     * @param distribution 颜色分布
     * @return 是否可以破坏（武器必须拥有分布中的所有颜色）
     */
    public static boolean canBreakDistribution(final ItemStack itemStack, final ToughnessColorDistribution distribution) {
        if (null == distribution) {
            return false;
        }

        final Set<ToughnessColor> weaponColors = WeaponColorHelper.getColors(itemStack);
        final Map<ToughnessColor, Float> distributionColors = distribution.getColorMap();

        // 武器必须拥有分布中的所有颜色
        // Weapon must have all colors in the distribution
        for (final ToughnessColor color : distributionColors.keySet()) {
            if (!weaponColors.contains(color)) {
                return false;
            }
        }

        return true;
    }
}
