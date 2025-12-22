package com.github.chromabreak.system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * 韧性条系统管理器
 * 负责管理实体的韧性值和相关游戏机制
 * <p>
 * Toughness System Manager
 * Responsible for managing entity toughness values and related game mechanics
 */
public class ToughnessSystem {

    // 韧性值NBT标签键
    // Toughness NBT tag keys
    private static final String TOUGHNESS_TAG = "chromabreak_toughness";
    private static final String MAX_TOUGHNESS_TAG = "chromabreak_max_toughness";
    private static final String IS_TOUGHNESS_BROKEN_TAG = "chromabreak_toughness_broken";

    // 私有构造器防止实例化
    // Private constructor to prevent instantiation
    private ToughnessSystem() {
    }

    /**
     * 初始化实体的韧性系统
     * Initialize toughness system for entity
     *
     * @param entity 目标实体
     */
    public static void initializeToughness(final LivingEntity entity) {
        if (ToughnessSystem.shouldHaveToughness(entity)) {
            final CompoundTag tag = entity.getPersistentData();

            // 获取最大韧性值（优先使用自定义值）
            // Get max toughness (prefer custom value)
            final float maxToughness = ToughnessSystem.getDefaultMaxToughness(entity);

            if (!tag.contains(ToughnessSystem.MAX_TOUGHNESS_TAG)) {
                tag.putFloat(ToughnessSystem.MAX_TOUGHNESS_TAG, maxToughness);
            } else {
                // 如果已有值，但存在自定义值，则更新它
                // If value exists but custom value is set, update it
                if (0 < maxToughness) {
                    tag.putFloat(ToughnessSystem.MAX_TOUGHNESS_TAG, maxToughness);
                }
            }

            if (!tag.contains(ToughnessSystem.TOUGHNESS_TAG)) {
                tag.putFloat(ToughnessSystem.TOUGHNESS_TAG, maxToughness);
            } else {
                // 如果已有值，但存在自定义值，则更新它
                // If value exists but custom value is set, update it
                if (0 < maxToughness) {
                    final float currentToughness = tag.getFloat(ToughnessSystem.TOUGHNESS_TAG);
                    final float currentMaxToughness = tag.getFloat(ToughnessSystem.MAX_TOUGHNESS_TAG);
                    // 按比例调整当前韧性值
                    // Adjust current toughness proportionally
                    if (0 < currentMaxToughness) {
                        final float ratio = currentToughness / currentMaxToughness;
                        tag.putFloat(ToughnessSystem.TOUGHNESS_TAG, maxToughness * ratio);
                    } else {
                        tag.putFloat(ToughnessSystem.TOUGHNESS_TAG, maxToughness);
                    }
                }
            }

            if (!tag.contains(ToughnessSystem.IS_TOUGHNESS_BROKEN_TAG)) {
                tag.putBoolean(ToughnessSystem.IS_TOUGHNESS_BROKEN_TAG, false);
            }

            // 初始化颜色分布 - 必须每次都检查并更新，确保使用最新的自定义颜色
            // Initialize color distribution - must check and update every time to ensure latest custom color is used
            final com.github.chromabreak.system.ToughnessColorDistribution customDistribution =
                    com.github.chromabreak.system.EntityHealthManager.getEntityColorDistribution(entity);

            if (null != customDistribution) {
                final java.util.Map<com.github.chromabreak.system.ToughnessColor, Float> colorMap = customDistribution.getColorMap();
                if (null != colorMap && !colorMap.isEmpty()) {
                    // 使用自定义颜色分布，无论NBT中是否已有，都要更新以确保使用最新配置
                    // Use custom color distribution, update regardless of whether NBT already has it to ensure latest config is used
                    customDistribution.toNbt(tag);
                } else {
                    // 自定义颜色分布为空，使用默认白色
                    // Custom color distribution is empty, use default white
                    if (!tag.contains(ToughnessColorDistribution.COLORS_TAG) ||
                            !tag.contains(ToughnessColorDistribution.PERCENTAGES_TAG)) {
                        final ToughnessColorDistribution defaultDistribution = ToughnessColorDistribution.singleColor(ToughnessColor.WHITE);
                        defaultDistribution.toNbt(tag);
                    }
                }
            } else if (!tag.contains(ToughnessColorDistribution.COLORS_TAG) ||
                    !tag.contains(ToughnessColorDistribution.PERCENTAGES_TAG)) {
                // 如果没有自定义颜色分布且NBT中也没有，使用默认白色
                // If no custom color distribution and no NBT, use default white
                final ToughnessColorDistribution defaultDistribution = ToughnessColorDistribution.singleColor(ToughnessColor.WHITE);
                defaultDistribution.toNbt(tag);
            }
            // 如果NBT中已有颜色分布且没有自定义颜色分布，则保持NBT中的值
            // If NBT already has color distribution and no custom distribution, keep NBT value
        }
    }

    /**
     * 获取实体的当前韧性值
     * Get current toughness value for entity
     *
     * @param entity 目标实体
     * @return 当前韧性值
     */
    public static float getToughness(final LivingEntity entity) {
        if (!ToughnessSystem.shouldHaveToughness(entity)) {
            return 0.0f;
        }

        final CompoundTag tag = entity.getPersistentData();
        return tag.getFloat(ToughnessSystem.TOUGHNESS_TAG);
    }

    /**
     * 获取实体的最大韧性值
     * Get maximum toughness value for entity
     *
     * @param entity 目标实体
     * @return 最大韧性值
     */
    public static float getMaxToughness(final LivingEntity entity) {
        if (!ToughnessSystem.shouldHaveToughness(entity)) {
            return 0.0f;
        }

        final CompoundTag tag = entity.getPersistentData();
        return tag.getFloat(ToughnessSystem.MAX_TOUGHNESS_TAG);
    }

    /**
     * 获取韧性值百分比
     * Get toughness percentage
     *
     * @param entity 目标实体
     * @return 韧性值百分比 (0.0 - 1.0)
     */
    public static float getToughnessPercentage(final LivingEntity entity) {
        if (!ToughnessSystem.shouldHaveToughness(entity)) {
            return 0.0f;
        }

        final float maxToughness = ToughnessSystem.getMaxToughness(entity);
        if (0 >= maxToughness) {
            return 0.0f;
        }

        return Math.min(Math.max(ToughnessSystem.getToughness(entity) / maxToughness, 0.0f), 1.0f);
    }

    /**
     * 减少实体的韧性值
     * Reduce entity toughness
     *
     * @param entity 目标实体
     * @param amount 减少的量
     */
    /**
     * 根据生物防御值计算韧性削减幅度
     * Calculate toughness reduction percentage based on entity defense
     *
     * @param entity 目标实体
     * @return 削减幅度（0.0 - 1.0）
     */
    private static float calculateReductionPercentage(final LivingEntity entity) {
        // 获取配置的最小值和最大值
        // Get configured min and max values
        final double minReduction = com.github.chromabreak.Config.TOUGHNESS_REDUCTION_MIN.get();
        final double maxReduction = com.github.chromabreak.Config.TOUGHNESS_REDUCTION_MAX.get();

        // 获取实体的防御值
        // Get entity defense value
        float defense = 0.0f;
        try {
            final var defenseAttributeHolder = net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.getHolder(
                    net.minecraft.resources.ResourceLocation.parse("minecraft:generic.armor"));
            if (defenseAttributeHolder.isPresent()) {
                final var defenseAttribute = defenseAttributeHolder.get();
                if (entity.getAttributes().hasAttribute(defenseAttribute)) {
                    defense = (float) entity.getAttributeValue(defenseAttribute);
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions
        }

        // 根据防御值计算削减幅度
        // Calculate reduction percentage based on defense
        // 防御值越高，削减幅度越大（在配置的范围内）
        // Higher defense = higher reduction (within configured range)
        // 使用对数函数来平滑过渡（防御值0-20映射到min-max）
        // Use logarithmic function for smooth transition (defense 0-20 maps to min-max)
        final float normalizedDefense = Math.min(defense / 20.0f, 1.0f); // 归一化到0-1
        final float reduction = (float) (minReduction + (maxReduction - minReduction) * normalizedDefense);

        return Math.max((float) minReduction, Math.min((float) maxReduction, reduction));
    }

    /**
     * 减少实体的韧性值（根据防御值计算削减幅度）
     * Reduce entity toughness (calculate reduction percentage based on defense)
     *
     * @param entity 目标实体
     * @param amount 基础削减量
     */
    public static void reduceToughness(final LivingEntity entity, final float amount) {
        if (!ToughnessSystem.shouldHaveToughness(entity) || 0 >= amount) {
            return;
        }

        // 根据防御值计算削减幅度
        // Calculate reduction percentage based on defense
        final float reductionPercentage = ToughnessSystem.calculateReductionPercentage(entity);

        // 应用削减幅度
        // Apply reduction percentage
        final float actualReduction = amount * reductionPercentage;

        final CompoundTag tag = entity.getPersistentData();
        final float currentToughness = tag.getFloat(ToughnessSystem.TOUGHNESS_TAG);
        final float newToughness = Math.max(0.0f, currentToughness - actualReduction);

        tag.putFloat(ToughnessSystem.TOUGHNESS_TAG, newToughness);

        // Check if toughness is broken
        if (0 >= newToughness && 0 < currentToughness) {
            tag.putBoolean(ToughnessSystem.IS_TOUGHNESS_BROKEN_TAG, true);
            ToughnessSystem.onToughnessBroken(entity);
        }
    }

    /**
     * 检查韧性是否已被破坏
     * Check if toughness is broken
     *
     * @param entity 目标实体
     * @return 韧性是否已被破坏
     */
    public static boolean isToughnessBroken(final LivingEntity entity) {
        if (!ToughnessSystem.shouldHaveToughness(entity)) {
            return true; // Entities without toughness are considered "broken"
        }

        final CompoundTag tag = entity.getPersistentData();
        return tag.getBoolean(ToughnessSystem.IS_TOUGHNESS_BROKEN_TAG);
    }

    /**
     * 韧性被破坏时的回调
     * Callback when toughness is broken
     *
     * @param entity 目标实体
     */
    private static void onToughnessBroken(final LivingEntity entity) {
        // 预留接口，未来可以添加视觉效果/音效
        // Reserved interface for future visual/audio effects
    }

    /**
     * 检查实体是否应该有韧性条
     * Check if entity should have toughness bar
     *
     * @param entity 目标实体
     * @return 是否应该有韧性条
     */
    private static boolean shouldHaveToughness(final LivingEntity entity) {
        // Players don't have toughness
        // 玩家没有韧性条
        if (entity instanceof Player) {
            return false;
        }

        // Dead entities don't have toughness
        // 死亡的实体没有韧性条
        if (!entity.isAlive()) {
            return false;
        }

        // 如果设置了自定义韧性值，则应该有韧性条
        // If custom toughness is set, should have toughness bar
        if (com.github.chromabreak.system.EntityHealthManager.hasCustomToughness(entity)) {
            final float customToughness = com.github.chromabreak.system.EntityHealthManager.getEntityToughness(entity);
            if (0 < customToughness) {
                return true;
            }
        }

        // Boss, hostile entities, and neutral entities have toughness
        // Boss、敌对生物和中立生物有韧性条
        final net.minecraft.world.entity.MobCategory category = entity.getType().getCategory();
        return net.minecraft.world.entity.MobCategory.MONSTER == category ||
                net.minecraft.world.entity.MobCategory.CREATURE == category ||
                entity.getType().toString().toLowerCase().contains("boss") ||
                entity.getType().toString().toLowerCase().contains("ender_dragon") ||
                entity.getType().toString().toLowerCase().contains("wither");
    }

    /**
     * 获取实体的默认最大韧性值
     * Get default max toughness for entity
     * 优先使用自定义韧性值，如果没有则使用默认值
     * Prefer custom toughness value, use default if not set
     *
     * @param entity 目标实体
     * @return 最大韧性值（自定义或默认）
     */
    static float getDefaultMaxToughness(final LivingEntity entity) {
        // 优先检查是否有自定义韧性值
        // First check if there's a custom toughness value
        if (com.github.chromabreak.system.EntityHealthManager.hasCustomToughness(entity)) {
            final float customToughness = com.github.chromabreak.system.EntityHealthManager.getEntityToughness(entity);
            if (0 < customToughness) {
                return customToughness;
            }
        }

        // Boss entities have higher toughness
        // Boss生物有更高的韧性
        if (entity.getType().toString().toLowerCase().contains("boss") ||
                entity.getType().toString().toLowerCase().contains("ender_dragon") ||
                entity.getType().toString().toLowerCase().contains("wither")) {
            return 200.0f;
        }

        // Hostile entities have standard toughness
        // 敌对生物有标准韧性
        if (net.minecraft.world.entity.MobCategory.MONSTER == entity.getType().getCategory()) {
            return 100.0f;
        }

        return 0.0f;
    }

    /**
     * 获取实体的韧性颜色分布
     * Get entity toughness color distribution
     *
     * @param entity 目标实体
     * @return 韧性颜色分布，如果不存在则返回默认白色
     */
    public static ToughnessColorDistribution getColorDistribution(final LivingEntity entity) {
        if (!ToughnessSystem.shouldHaveToughness(entity)) {
            return ToughnessColorDistribution.singleColor(ToughnessColor.WHITE);
        }

        // 优先检查EntityHealthManager中的自定义颜色分布（实时检查，确保获取最新配置）
        // Prefer checking custom color distribution in EntityHealthManager (real-time check to ensure latest config)
        final com.github.chromabreak.system.ToughnessColorDistribution customDistribution =
                com.github.chromabreak.system.EntityHealthManager.getEntityColorDistribution(entity);
        if (null != customDistribution) {
            final java.util.Map<com.github.chromabreak.system.ToughnessColor, Float> colorMap = customDistribution.getColorMap();
            if (null != colorMap && !colorMap.isEmpty()) {
                // 将自定义颜色分布保存到实体NBT中，以便后续使用
                // Save custom color distribution to entity NBT for future use
                final CompoundTag tag = entity.getPersistentData();
                customDistribution.toNbt(tag);

                // 调试日志：记录找到的自定义颜色分布
                // Debug log: record found custom color distribution
                final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("ToughnessSystem");
                logger.debug("Found custom color distribution for {}: {}", entity.getType().toString(), colorMap.keySet());

                return customDistribution;
            }
        }

        // 如果没有自定义颜色分布，从NBT读取（支持KubeJS脚本直接设置的NBT数据）
        // If no custom color distribution, read from NBT (supports NBT data set directly by KubeJS scripts)
        final CompoundTag tag = entity.getPersistentData();

        // 检查NBT中是否有颜色数据（支持KubeJS脚本直接设置的格式）
        // Check if NBT has color data (supports format set directly by KubeJS scripts)
        if (tag.contains(ToughnessColorDistribution.COLORS_TAG) &&
                tag.contains(ToughnessColorDistribution.PERCENTAGES_TAG)) {
            final ToughnessColorDistribution distribution = ToughnessColorDistribution.fromNbt(tag);
            if (null != distribution) {
                final java.util.Map<com.github.chromabreak.system.ToughnessColor, Float> colorMap = distribution.getColorMap();
                if (null != colorMap && !colorMap.isEmpty()) {
                    // 调试日志：记录从NBT读取的颜色分布
                    // Debug log: record color distribution read from NBT
                    final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("ToughnessSystem");
                    logger.debug("Found color distribution from NBT for {}: {}", entity.getType().toString(), colorMap.keySet());

                    return distribution;
                }
            }
        }

        // 默认返回白色
        // Default to white
        final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("ToughnessSystem");
        logger.debug("Using default white color for {}", entity.getType().toString());

        return ToughnessColorDistribution.singleColor(ToughnessColor.WHITE);
    }

    /**
     * 设置实体的韧性颜色分布
     * Set entity toughness color distribution
     *
     * @param entity       目标实体
     * @param distribution 颜色分布
     */
    public static void setColorDistribution(final LivingEntity entity, final ToughnessColorDistribution distribution) {
        if (!ToughnessSystem.shouldHaveToughness(entity) || null == distribution) {
            return;
        }

        final CompoundTag tag = entity.getPersistentData();
        distribution.toNbt(tag);
    }

    /**
     * 计算韧性存在时的伤害减免
     * Calculate damage reduction when toughness is present
     *
     * @param originalDamage      原始伤害
     * @param toughnessPercentage 韧性值百分比
     * @return 减免后的伤害
     */
    public static float calculateReducedDamage(final float originalDamage, final float toughnessPercentage) {
        if (0 >= toughnessPercentage) {
            return originalDamage; // No toughness, full damage
        }

        // When toughness is present, damage is reduced to 2%-5% of original damage
        // 韧性存在时，伤害减少到原始伤害的2%-5%
        final float reductionFactor = 0.02f + (float) Math.random() * 0.03f; // 2% to 5%
        return originalDamage * reductionFactor;
    }
}