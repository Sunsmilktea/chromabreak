package com.github.chromabreak.system;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * WeaponColorHelper - 武器颜色助手类
 * Weapon Color Helper Class
 * <p>
 * 负责管理武器上的颜色标记，用于韧性系统的颜色匹配和破坏逻辑
 * Responsible for managing color markers on weapons, used for color matching and breaking logic in toughness system
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 颜色标记管理：在武器上添加、移除、检查颜色标记
 * Color marker management: Add, remove, check color markers on weapons
 * - DataComponents存储：使用Minecraft 1.21.1的DataComponents系统存储颜色信息
 * DataComponents storage: Uses Minecraft 1.21.1 DataComponents system to store color information
 * - 韧性破坏检查：检查武器是否可以破坏特定颜色或颜色分布的韧性
 * Toughness breaking check: Checks if weapon can break toughness of specific color or color distribution
 * - 反射兼容：使用反射处理DataComponents API，确保版本兼容性
 * Reflection compatibility: Uses reflection to handle DataComponents API, ensuring version compatibility
 * <p>
 * 颜色标记系统：
 * Color marker system:
 * - 颜色存储：颜色信息存储在DataComponents的CUSTOM_DATA组件中
 * Color storage: Color information stored in CUSTOM_DATA component of DataComponents
 * - NBT结构：使用NBT列表存储颜色名称字符串
 * NBT structure: Uses NBT list to store color name strings
 * - 标签标识：使用"chromabreak_weapon_colors"作为颜色标记的标识符
 * Tag identifier: Uses "chromabreak_weapon_colors" as identifier for color markers
 * <p>
 * 韧性系统集成：
 * Toughness system integration:
 * - 颜色匹配：武器必须拥有与实体韧性颜色匹配的颜色才能造成伤害
 * Color matching: Weapon must have colors matching entity toughness colors to deal damage
 * - 分布检查：支持检查武器是否拥有颜色分布中的所有颜色
 * Distribution check: Supports checking if weapon has all colors in color distribution
 * - 多颜色支持：武器可以拥有多个颜色标记
 * Multi-color support: Weapon can have multiple color markers
 * <p>
 * 设计特点：
 * Design features:
 * - 枚举单例模式：使用枚举确保单例，所有方法都是静态方法
 * Enum singleton pattern: Uses enum to ensure singleton, all methods are static methods
 * - 反射兼容：通过反射处理DataComponents API，避免直接依赖特定版本
 * Reflection compatibility: Handles DataComponents API through reflection to avoid direct version dependency
 * - 错误处理：完善的错误处理机制，避免崩溃
 * Error handling: Comprehensive error handling mechanism to avoid crashes
 * - 性能优化：避免不必要的计算和内存分配
 * Performance optimization: Avoids unnecessary calculations and memory allocations
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 武器染色：玩家可以通过染料为武器添加颜色标记
 * Weapon dyeing: Players can add color markers to weapons using dyes
 * - 韧性系统：武器颜色决定其可以破坏的韧性类型
 * Toughness system: Weapon colors determine which toughness types it can break
 * - 游戏平衡：通过颜色系统实现复杂的伤害计算逻辑
 * Game balance: Implements complex damage calculation logic through color system
 * - 模组集成：与其他模组配合，提供颜色标记功能
 * Mod integration: Works with other mods to provide color marking functionality
 * <p>
 * 技术实现细节：
 * Technical implementation details:
 * - DataComponents API：使用Minecraft 1.21.1的新数据组件系统
 * DataComponents API: Uses Minecraft 1.21.1's new data component system
 * - 反射调用：通过反射调用DataComponents相关方法，确保版本兼容性
 * Reflection calls: Calls DataComponents related methods through reflection for version compatibility
 * - NBT序列化：颜色信息通过NBT格式序列化存储
 * NBT serialization: Color information serialized and stored in NBT format
 * - 异常处理：完善的异常处理，确保系统稳定性
 * Exception handling: Comprehensive exception handling ensures system stability
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
