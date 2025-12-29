package com.github.chromabreak.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * EntityHealthManager - 实体健康管理器
 * Entity Health Manager
 * <p>
 * 负责管理实体的自定义健康相关设置，包括最大生命值、韧性值和韧性颜色分布
 * Responsible for managing custom health-related settings for entities, including maximum health, toughness values, and toughness color distributions
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 自定义最大生命值的管理（设置、获取、移除）
 * Management of custom maximum health (set, get, remove)
 * - 自定义韧性值的管理（设置、获取、移除）
 * Management of custom toughness values (set, get, remove)
 * - 自定义韧性颜色分布的管理（设置、获取、移除）
 * Management of custom toughness color distributions (set, get, remove)
 * - 实体类型字符串的标准化处理
 * Standardized processing of entity type strings
 * - 自定义设置的批量应用和清理
 * Batch application and cleanup of custom settings
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 * <p>
 * 支持通过KubeJS脚本和配置文件自定义实体健康行为
 * Supports custom entity health behavior through KubeJS scripts and configuration files
 */
public enum EntityHealthManager {
    ;

    // 存储自定义最大生命值的映射
    // Map for storing custom maximum health values
    private static final Map<String, Float> CUSTOM_MAX_HEALTH_MAP = new HashMap<>();

    // 存储自定义韧性值的映射
    // Map for storing custom toughness values
    private static final Map<String, Float> CUSTOM_TOUGHNESS_MAP = new HashMap<>();

    // 存储自定义韧性颜色分布的映射
    // Map for storing custom toughness color distributions
    private static final Map<String, ToughnessColorDistribution> CUSTOM_COLOR_DISTRIBUTION_MAP = new HashMap<>();

    /**
     * 设置生物的自定义最大生命值
     * Set custom maximum health for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     * @param maxHealth  最大生命值
     */
    public static void setCustomMaxHealth(final String entityType, final float maxHealth) {
        if (0 >= maxHealth) {
            EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.remove(entityType);
        } else {
            EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.put(entityType, maxHealth);
        }
    }

    /**
     * 获取生物的自定义最大生命值
     * Get custom maximum health for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     * @return 自定义最大生命值，如果没有设置则返回-1
     */
    public static float getCustomMaxHealth(final String entityType) {
        return EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.getOrDefault(entityType, -1.0f);
    }

    /**
     * 移除生物的自定义最大生命值设置
     * Remove custom maximum health setting for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     */
    public static void removeCustomMaxHealth(final String entityType) {
        EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.remove(entityType);
    }

    /**
     * 设置生物的自定义韧性值
     * Set custom toughness value for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     * @param toughness  韧性值
     */
    public static void setCustomToughness(final String entityType, final float toughness) {
        if (0 > toughness) {
            EntityHealthManager.CUSTOM_TOUGHNESS_MAP.remove(entityType);
        } else {
            EntityHealthManager.CUSTOM_TOUGHNESS_MAP.put(entityType, toughness);
        }
    }

    /**
     * 获取生物的自定义韧性值
     * Get custom toughness value for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     * @return 自定义韧性值，如果没有设置则返回-1
     */
    public static float getCustomToughness(final String entityType) {
        return EntityHealthManager.CUSTOM_TOUGHNESS_MAP.getOrDefault(entityType, -1.0f);
    }

    /**
     * 移除生物的自定义韧性值设置
     * Remove custom toughness setting for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     */
    public static void removeCustomToughness(final String entityType) {
        EntityHealthManager.CUSTOM_TOUGHNESS_MAP.remove(entityType);
    }

    /**
     * 检查实体是否有自定义最大生命值设置
     * Check if entity has custom maximum health setting
     *
     * @param entity 目标实体
     * @return 是否有自定义设置
     */
    public static boolean hasCustomMaxHealth(final LivingEntity entity) {
        if (entity instanceof Player) {
            return false; // 玩家没有自定义生命值设置
        }

        final String entityType = EntityHealthManager.getEntityTypeString(entity);
        return EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.containsKey(entityType);
    }

    /**
     * 检查实体是否有自定义韧性值设置
     * Check if entity has custom toughness setting
     *
     * @param entity 目标实体
     * @return 是否有自定义设置
     */
    public static boolean hasCustomToughness(final LivingEntity entity) {
        if (entity instanceof Player) {
            return false; // 玩家没有自定义韧性值设置
        }

        final String entityType = EntityHealthManager.getEntityTypeString(entity);
        return EntityHealthManager.CUSTOM_TOUGHNESS_MAP.containsKey(entityType);
    }

    /**
     * 获取实体的自定义最大生命值
     * Get custom maximum health for entity
     *
     * @param entity 目标实体
     * @return 自定义最大生命值，如果没有设置则返回实体的原始最大生命值
     */
    public static float getEntityMaxHealth(final LivingEntity entity) {
        if (entity instanceof Player) {
            return entity.getMaxHealth(); // 玩家使用原始最大生命值
        }

        final String entityType = EntityHealthManager.getEntityTypeString(entity);
        final float customMaxHealth = EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.getOrDefault(entityType, -1.0f);

        if (0 < customMaxHealth) {
            return customMaxHealth;
        }

        return entity.getMaxHealth(); // 返回原始最大生命值
    }

    /**
     * 获取实体的自定义韧性值
     * Get custom toughness for entity
     *
     * @param entity 目标实体
     * @return 自定义韧性值，如果没有设置则返回默认韧性值
     */
    public static float getEntityToughness(final LivingEntity entity) {
        if (entity instanceof Player) {
            return 0.0f; // 玩家没有韧性值
        }

        final String entityType = EntityHealthManager.getEntityTypeString(entity);
        final float customToughness = EntityHealthManager.CUSTOM_TOUGHNESS_MAP.getOrDefault(entityType, -1.0f);

        if (0 <= customToughness) {
            return customToughness;
        }

        // 返回默认韧性值（基于生物类型）
        return ToughnessSystem.getDefaultMaxToughness(entity);
    }

    /**
     * 应用自定义生命值和韧性值到实体
     * Apply custom health and toughness values to entity
     *
     * @param entity 目标实体
     */
    public static void applyCustomValues(final LivingEntity entity) {
        if (entity instanceof Player) {
            return; // 不修改玩家的生命值和韧性值
        }

        // 应用自定义最大生命值
        if (EntityHealthManager.hasCustomMaxHealth(entity)) {
            final float customMaxHealth = EntityHealthManager.getEntityMaxHealth(entity);
            // 注意：直接修改实体的最大生命值可能需要特殊处理
            // 这里我们通过NBT或其他机制来存储和应用自定义值
            entity.getPersistentData().putFloat("chromabreak_custom_max_health", customMaxHealth);
        }

        // 应用自定义韧性值
        if (EntityHealthManager.hasCustomToughness(entity)) {
            final float customToughness = EntityHealthManager.getEntityToughness(entity);
            // 初始化韧性系统时使用自定义值
            ToughnessSystem.initializeToughness(entity);
        }
    }

    /**
     * 获取生物类型的字符串表示
     * Get string representation of entity type
     *
     * @param entity 目标实体
     * @return 生物类型字符串（如"minecraft:zombie"）
     */
    private static String getEntityTypeString(final LivingEntity entity) {
        final ResourceLocation entityId = net.minecraft.world.entity.EntityType.getKey(entity.getType());
        return entityId.toString();
    }

    /**
     * 获取所有自定义最大生命值设置的生物类型
     * Get all entity types with custom maximum health settings
     *
     * @return 生物类型数组
     */
    public static String[] getCustomMaxHealthEntities() {
        return EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.keySet().toArray(new String[0]);
    }

    /**
     * 获取所有自定义韧性值设置的生物类型
     * Get all entity types with custom toughness settings
     *
     * @return 生物类型数组
     */
    public static String[] getCustomToughnessEntities() {
        return EntityHealthManager.CUSTOM_TOUGHNESS_MAP.keySet().toArray(new String[0]);
    }

    /**
     * 设置生物的自定义韧性颜色分布
     * Set custom toughness color distribution for entity type
     *
     * @param entityType   生物类型（字符串格式，如"minecraft:zombie"）
     * @param distribution 颜色分布（为null则移除）
     */
    public static void setCustomColorDistribution(final String entityType, final ToughnessColorDistribution distribution) {
        if (null == distribution) {
            EntityHealthManager.CUSTOM_COLOR_DISTRIBUTION_MAP.remove(entityType);
        } else {
            EntityHealthManager.CUSTOM_COLOR_DISTRIBUTION_MAP.put(entityType, distribution);
        }
    }

    /**
     * 获取生物的自定义韧性颜色分布
     * Get custom toughness color distribution for entity type
     *
     * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
     * @return 颜色分布，如果没有设置则返回null
     */
    public static ToughnessColorDistribution getCustomColorDistribution(final String entityType) {
        return EntityHealthManager.CUSTOM_COLOR_DISTRIBUTION_MAP.get(entityType);
    }

    /**
     * 检查实体是否有自定义韧性颜色分布设置
     * Check if entity has custom toughness color distribution setting
     *
     * @param entity 目标实体
     * @return 是否有自定义设置
     */
    public static boolean hasCustomColorDistribution(final LivingEntity entity) {
        if (entity instanceof Player) {
            return false; // 玩家没有自定义颜色分布设置
        }

        final String entityType = EntityHealthManager.getEntityTypeString(entity);
        return EntityHealthManager.CUSTOM_COLOR_DISTRIBUTION_MAP.containsKey(entityType);
    }

    /**
     * 获取实体的自定义韧性颜色分布
     * Get custom toughness color distribution for entity
     *
     * @param entity 目标实体
     * @return 颜色分布，如果没有设置则返回null
     */
    public static ToughnessColorDistribution getEntityColorDistribution(final LivingEntity entity) {
        if (entity instanceof Player) {
            return null; // 玩家没有颜色分布
        }

        final String entityType = EntityHealthManager.getEntityTypeString(entity);
        return EntityHealthManager.CUSTOM_COLOR_DISTRIBUTION_MAP.get(entityType);
    }

    /**
     * 清除所有自定义设置
     * Clear all custom settings
     */
    public static void clearAllCustomSettings() {
        EntityHealthManager.CUSTOM_MAX_HEALTH_MAP.clear();
        EntityHealthManager.CUSTOM_TOUGHNESS_MAP.clear();
        EntityHealthManager.CUSTOM_COLOR_DISTRIBUTION_MAP.clear();
    }
}
