package com.github.chromabreak.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * ToughnessColorDistribution - 韧性颜色分布类
 * Toughness Color Distribution Class
 * <p>
 * 表示ChromaBreak模组中韧性条的颜色分布，支持单一颜色或多色百分比分布
 * Represents color distribution of toughness bar in ChromaBreak mod,
 * supports single color or multi-color percentage distribution
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 单一颜色分布：整个韧性条使用单一颜色
 * Single color distribution: Entire toughness bar uses a single color
 * - 多色百分比分布：韧性条按百分比使用多种颜色
 * Multi-color percentage distribution: Toughness bar uses multiple colors by percentage
 * - NBT序列化：将颜色分布保存到实体NBT数据中
 * NBT serialization: Save color distribution to entity NBT data
 * - NBT反序列化：从实体NBT数据读取颜色分布
 * NBT deserialization: Read color distribution from entity NBT data
 * - 颜色分布管理：查询、修改和归一化颜色分布
 * Color distribution management: Query, modify, and normalize color distribution
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 实体韧性条颜色渲染
 * Entity toughness bar color rendering
 * - 染料混合系统（多色韧性条）
 * Dye mixing system (multi-color toughness bar)
 * - 配置持久化（保存到实体NBT）
 * Configuration persistence (save to entity NBT)
 * <p>
 * 设计特点：
 * Design features:
 * - 百分比自动归一化（确保总和为1.0）
 * Automatic percentage normalization (ensures sum is 1.0)
 * - 容错处理（无效颜色和百分比的处理）
 * Error handling (handling of invalid colors and percentages)
 * - 线程安全（只读操作，内部状态不可变）
 * Thread safety (read-only operations, internal state immutable)
 */
public class ToughnessColorDistribution {
    /**
     * NBT标签名称 - 颜色列表
     * NBT tag name - Color list
     * <p>
     * 用于在NBT数据中存储颜色名称列表
     * Used to store color name list in NBT data
     */
    public static final String COLORS_TAG = "chromabreak_toughness_colors";

    /**
     * NBT标签名称 - 百分比列表
     * NBT tag name - Percentage list
     * <p>
     * 用于在NBT数据中存储颜色百分比列表
     * Used to store color percentage list in NBT data
     */
    public static final String PERCENTAGES_TAG = "chromabreak_toughness_percentages";

    /**
     * 颜色和百分比映射表
     * Color and percentage mapping table
     * <p>
     * 存储韧性颜色与对应的百分比（0.0-1.0）
     * Stores toughness colors with corresponding percentages (0.0-1.0)
     * 百分比总和始终为1.0（经过归一化处理）
     * Percentage sum is always 1.0 (after normalization)
     */
    private final Map<ToughnessColor, Float> colorMap;

    /**
     * 私有构造器
     * Private Constructor
     * <p>
     * 用于内部创建实例，外部通过静态工厂方法创建
     * Used for internal instance creation, external creation through static factory methods
     */
    private ToughnessColorDistribution() {
        this.colorMap = new HashMap<>();
    }

    /**
     * 创建单一颜色的韧性分布
     * Create single color toughness distribution
     *
     * @param color 要使用的单一颜色
     *              Single color to use
     * @return 单一颜色的韧性颜色分布实例
     * Single color toughness color distribution instance
     * <p>
     * 示例：singleColor(ToughnessColor.RED) 创建红色韧性条
     * Example: singleColor(ToughnessColor.RED) creates red toughness bar
     */
    public static ToughnessColorDistribution singleColor(final ToughnessColor color) {
        final ToughnessColorDistribution distribution = new ToughnessColorDistribution();
        distribution.colorMap.put(color, 1.0f);
        return distribution;
    }

    /**
     * 创建多色百分比分布的韧性分布
     * Create multi-color percentage distribution toughness distribution
     *
     * @param colors 颜色和百分比映射（百分比总和应该为1.0，会自动归一化）
     *               Color and percentage mapping (percentage sum should be 1.0, will be auto-normalized)
     * @return 多色百分比分布的韧性颜色分布实例
     * Multi-color percentage distribution toughness color distribution instance
     * <p>
     * 示例：创建红蓝各50%的分布
     * Example: Create 50% red and 50% blue distribution
     * Map<ToughnessColor, Float> colors = new HashMap<>();
     * colors.put(ToughnessColor.RED, 0.5f);
     * colors.put(ToughnessColor.BLUE, 0.5f);
     * multiColor(colors);
     */
    public static ToughnessColorDistribution multiColor(final Map<ToughnessColor, Float> colors) {
        final ToughnessColorDistribution distribution = new ToughnessColorDistribution();
        distribution.colorMap.putAll(colors);
        // 归一化百分比
        // Normalize percentages
        distribution.normalize();
        return distribution;
    }

    /**
     * 从NBT标签读取韧性颜色分布
     * Read toughness color distribution from NBT tag
     *
     * @param tag 包含颜色分布数据的NBT标签
     *            NBT tag containing color distribution data
     * @return 读取到的韧性颜色分布，如果NBT数据无效则返回null
     * Read toughness color distribution, returns null if NBT data is invalid
     * <p>
     * NBT数据结构：
     * NBT data structure:
     * - COLORS_TAG: 字符串列表，存储颜色名称
     * COLORS_TAG: String list storing color names
     * - PERCENTAGES_TAG: 字符串列表，存储百分比值
     * PERCENTAGES_TAG: String list storing percentage values
     */
    public static ToughnessColorDistribution fromNbt(final CompoundTag tag) {
        if (!tag.contains(ToughnessColorDistribution.COLORS_TAG) ||
                !tag.contains(ToughnessColorDistribution.PERCENTAGES_TAG)) {
            return null;
        }

        final ToughnessColorDistribution distribution = new ToughnessColorDistribution();
        final ListTag colorsTag = tag.getList(ToughnessColorDistribution.COLORS_TAG, Tag.TAG_STRING);
        final ListTag percentagesTag = tag.getList(ToughnessColorDistribution.PERCENTAGES_TAG, Tag.TAG_STRING);

        if (colorsTag.size() != percentagesTag.size()) {
            return null;
        }

        for (int i = 0; i < colorsTag.size(); i++) {
            final String colorName = colorsTag.getString(i);
            final ToughnessColor color = ToughnessColor.byName(colorName);
            if (null != color) {
                try {
                    final float percentage = Float.parseFloat(percentagesTag.getString(i));
                    distribution.colorMap.put(color, percentage);
                } catch (final NumberFormatException e) {
                    // Ignore invalid percentage
                    // 忽略无效百分比
                }
            }
        }

        distribution.normalize();
        return distribution;
    }

    /**
     * 将韧性颜色分布保存到NBT标签
     * Save toughness color distribution to NBT tag
     *
     * @param tag 要保存到的NBT标签
     *            NBT tag to save to
     *            <p>
     *            保存的数据结构：
     *            Saved data structure:
     *            - COLORS_TAG: 颜色名称列表（小写）
     *            COLORS_TAG: Color name list (lowercase)
     *            - PERCENTAGES_TAG: 百分比值列表（字符串格式）
     *            PERCENTAGES_TAG: Percentage value list (string format)
     */
    public void toNbt(final CompoundTag tag) {
        final ListTag colorsTag = new ListTag();
        final ListTag percentagesTag = new ListTag();

        for (final Map.Entry<ToughnessColor, Float> entry : this.colorMap.entrySet()) {
            colorsTag.add(StringTag.valueOf(entry.getKey().getName()));
            percentagesTag.add(StringTag.valueOf(String.valueOf(entry.getValue())));
        }

        tag.put(ToughnessColorDistribution.COLORS_TAG, colorsTag);
        tag.put(ToughnessColorDistribution.PERCENTAGES_TAG, percentagesTag);
    }

    /**
     * 获取颜色映射的副本
     * Get copy of color map
     *
     * @return 颜色和百分比映射的副本（防止外部修改）
     * Copy of color and percentage mapping (prevents external modification)
     * <p>
     * 返回的是副本，确保内部状态的不可变性
     * Returns a copy to ensure immutability of internal state
     */
    public Map<ToughnessColor, Float> getColorMap() {
        return new HashMap<>(this.colorMap);
    }

    /**
     * 检查是否包含指定颜色
     * Check if contains specified color
     *
     * @param color 要检查的颜色
     *              Color to check
     * @return 如果包含该颜色且百分比大于0则返回true，否则返回false
     * Returns true if contains the color and percentage > 0, false otherwise
     */
    public boolean containsColor(final ToughnessColor color) {
        return this.colorMap.containsKey(color) && 0 < this.colorMap.get(color);
    }

    /**
     * 获取指定颜色的百分比
     * Get percentage of specified color
     *
     * @param color 要查询的颜色
     *              Color to query
     * @return 颜色的百分比（0.0-1.0），如果颜色不存在则返回0.0
     * Color percentage (0.0-1.0), returns 0.0 if color doesn't exist
     */
    public float getPercentage(final ToughnessColor color) {
        return this.colorMap.getOrDefault(color, 0.0f);
    }

    /**
     * 归一化百分比（确保总和为1.0）
     * Normalize percentages (ensure sum is 1.0)
     * <p>
     * 内部方法，用于确保颜色百分比总和为1.0
     * Internal method to ensure color percentage sum is 1.0
     * 如果总和为0或负数，则默认使用白色
     * If sum is 0 or negative, defaults to white
     */
    private void normalize() {
        float sum = 0.0f;
        for (final Float percentage : this.colorMap.values()) {
            sum += percentage;
        }

        if (0 < sum && 1.0f != sum) {
            // 归一化
            // Normalize
            for (final Map.Entry<ToughnessColor, Float> entry : this.colorMap.entrySet()) {
                entry.setValue(entry.getValue() / sum);
            }
        } else if (0 >= sum) {
            // 如果没有有效颜色，默认使用白色
            // If no valid colors, default to white
            this.colorMap.clear();
            this.colorMap.put(ToughnessColor.WHITE, 1.0f);
        }
    }

    /**
     * 获取颜色数量
     * Get number of colors
     *
     * @return 颜色分布中包含的颜色数量
     * Number of colors contained in the color distribution
     * <p>
     * 示例：单一颜色分布返回1，红蓝各50%分布返回2
     * Example: Single color distribution returns 1, 50% red and 50% blue distribution returns 2
     */
    public int getColorCount() {
        return this.colorMap.size();
    }
}
