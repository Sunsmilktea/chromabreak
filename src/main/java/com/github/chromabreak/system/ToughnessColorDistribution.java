package com.github.chromabreak.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * 韧性颜色分布
 * 表示韧性条的颜色分布（可以是单一颜色或多色百分比分布）
 * <p>
 * Toughness Color Distribution
 * Represents color distribution of toughness bar (can be single color or multi-color percentage distribution)
 */
public class ToughnessColorDistribution {
    public static final String COLORS_TAG = "chromabreak_toughness_colors";
    public static final String PERCENTAGES_TAG = "chromabreak_toughness_percentages";

    private final Map<ToughnessColor, Float> colorMap;

    /**
     * 创建单一颜色的韧性分布
     * Create single color toughness distribution
     *
     * @param color 颜色
     * @return 韧性颜色分布
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
     * @param colors 颜色和百分比映射（百分比总和应该为1.0）
     * @return 韧性颜色分布
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
     * 私有构造器
     * Private constructor
     */
    private ToughnessColorDistribution() {
        this.colorMap = new HashMap<>();
    }

    /**
     * 从NBT读取韧性颜色分布
     * Read toughness color distribution from NBT
     *
     * @param tag NBT标签
     * @return 韧性颜色分布，如果读取失败则返回null
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
     * 保存到NBT
     * Save to NBT
     *
     * @param tag NBT标签
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
     * 获取颜色映射
     * Get color map
     *
     * @return 颜色和百分比映射
     */
    public Map<ToughnessColor, Float> getColorMap() {
        return new HashMap<>(this.colorMap);
    }

    /**
     * 检查是否包含指定颜色
     * Check if contains specified color
     *
     * @param color 颜色
     * @return 是否包含
     */
    public boolean containsColor(final ToughnessColor color) {
        return this.colorMap.containsKey(color) && 0 < this.colorMap.get(color);
    }

    /**
     * 获取指定颜色的百分比
     * Get percentage of specified color
     *
     * @param color 颜色
     * @return 百分比（0.0-1.0），如果不存在则返回0.0
     */
    public float getPercentage(final ToughnessColor color) {
        return this.colorMap.getOrDefault(color, 0.0f);
    }

    /**
     * 归一化百分比（确保总和为1.0）
     * Normalize percentages (ensure sum is 1.0)
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
     * @return 颜色数量
     */
    public int getColorCount() {
        return this.colorMap.size();
    }
}
