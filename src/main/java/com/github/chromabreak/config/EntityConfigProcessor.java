package com.github.chromabreak.config;

import com.github.chromabreak.system.EntityHealthManager;
import com.github.chromabreak.system.ToughnessColor;
import com.github.chromabreak.system.ToughnessColorDistribution;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * EntityConfigProcessor - 实体配置处理器
 * Entity Configuration Processor
 * <p>
 * 负责解析和处理ChromaBreak模组的实体配置，将JSON配置转换为EntityHealthManager的设置
 * Responsible for parsing and processing entity configurations for ChromaBreak mod,
 * converting JSON configurations to EntityHealthManager settings
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - JSON配置解析：解析实体配置的JSON字符串
 * JSON configuration parsing: Parse JSON strings of entity configurations
 * - 配置验证：验证配置的完整性和有效性
 * Configuration validation: Validate configuration completeness and validity
 * - 配置处理：将配置应用到EntityHealthManager
 * Configuration processing: Apply configurations to EntityHealthManager
 * - 错误处理：处理配置解析和处理过程中的错误
 * Error handling: Handle errors during configuration parsing and processing
 * <p>
 * 支持的配置类型：
 * Supported configuration types:
 * - 最大生命值配置 (maxHealth)
 * Maximum health configuration (maxHealth)
 * - 最大韧性值配置 (maxToughness)
 * Maximum toughness configuration (maxToughness)
 * - 韧性颜色配置 (toughnessColor - 单色)
 * Toughness color configuration (toughnessColor - single color)
 * - 多色分布配置 (toughnessColors - 多色百分比分布)
 * Multi-color distribution configuration (toughnessColors - multi-color percentage distribution)
 * <p>
 * 配置格式示例：
 * Configuration format examples:
 * 单色配置：
 * Single color configuration:
 * {
 * "entityType": "minecraft:zombie",
 * "maxHealth": 30.0,
 * "maxToughness": 20.0,
 * "toughnessColor": "red"
 * }
 * <p>
 * 多色配置：
 * Multi-color configuration:
 * {
 * "entityType": "minecraft:skeleton",
 * "maxHealth": 25.0,
 * "maxToughness": 15.0,
 * "toughnessColors": {
 * "red": 0.5,
 * "blue": 0.5
 * }
 * }
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 */
public enum EntityConfigProcessor {
    ;
    private static final Logger LOGGER = LoggerFactory.getLogger("EntityConfigProcessor");
    private static final Gson GSON = new Gson();

    /**
     * 处理单个实体配置
     * Process a single entity configuration
     *
     * @param configStr 配置字符串（JSON格式）
     * @return 处理结果（成功/失败）
     */
    public static boolean processEntityConfig(final String configStr) {
        if (null == configStr || configStr.trim().isEmpty()) {
            return false;
        }

        try {
            final JsonObject config = EntityConfigProcessor.GSON.fromJson(configStr, JsonObject.class);
            if (null == config) {
                EntityConfigProcessor.LOGGER.warn("Invalid entity config (null): {}", configStr);
                return false;
            }

            // 获取实体类型
            if (!config.has("entityType")) {
                EntityConfigProcessor.LOGGER.warn("Entity config missing 'entityType': {}", configStr);
                return false;
            }

            final String entityType = config.get("entityType").getAsString();
            if (null == entityType || entityType.trim().isEmpty()) {
                EntityConfigProcessor.LOGGER.warn("Entity config has empty 'entityType': {}", configStr);
                return false;
            }

            // 处理最大生命值配置
            EntityConfigProcessor.processMaxHealthConfig(config, entityType);

            // 处理韧性值配置
            EntityConfigProcessor.processToughnessConfig(config, entityType);

            // 处理韧性颜色配置
            EntityConfigProcessor.processToughnessColorConfig(config, entityType);

            EntityConfigProcessor.LOGGER.debug("Successfully processed entity config for {}", entityType);
            return true;

        } catch (final Exception e) {
            EntityConfigProcessor.LOGGER.error("Failed to parse entity config: {}", configStr, e);
            return false;
        }
    }

    /**
     * 处理最大生命值配置
     * Process max health configuration
     */
    private static void processMaxHealthConfig(final JsonObject config, final String entityType) {
        if (config.has("maxHealth")) {
            try {
                final float maxHealth = config.get("maxHealth").getAsFloat();
                if (0 < maxHealth) {
                    EntityHealthManager.setCustomMaxHealth(entityType, maxHealth);
                    EntityConfigProcessor.LOGGER.debug("Set max health for {}: {}", entityType, maxHealth);
                }
            } catch (final Exception e) {
                EntityConfigProcessor.LOGGER.warn("Failed to parse maxHealth for {}: {}", entityType, e.getMessage());
            }
        }
    }

    /**
     * 处理韧性值配置
     * Process toughness configuration
     */
    private static void processToughnessConfig(final JsonObject config, final String entityType) {
        if (config.has("maxToughness")) {
            try {
                final float maxToughness = config.get("maxToughness").getAsFloat();
                if (0 <= maxToughness) {
                    EntityHealthManager.setCustomToughness(entityType, maxToughness);
                    EntityConfigProcessor.LOGGER.debug("Set max toughness for {}: {}", entityType, maxToughness);
                }
            } catch (final Exception e) {
                EntityConfigProcessor.LOGGER.warn("Failed to parse maxToughness for {}: {}", entityType, e.getMessage());
            }
        }
    }

    /**
     * 处理韧性颜色配置
     * Process toughness color configuration
     */
    private static void processToughnessColorConfig(final JsonObject config, final String entityType) {
        // 单一颜色配置
        if (config.has("toughnessColor")) {
            EntityConfigProcessor.processSingleColorConfig(config, entityType);
        }
        // 多色分布配置
        else if (config.has("toughnessColors")) {
            EntityConfigProcessor.processMultiColorConfig(config, entityType);
        }
    }

    /**
     * 处理单一颜色配置
     * Process single color configuration
     */
    private static void processSingleColorConfig(final JsonObject config, final String entityType) {
        try {
            final String colorName = config.get("toughnessColor").getAsString();
            final ToughnessColor color = ToughnessColor.byName(colorName);
            if (null != color) {
                final ToughnessColorDistribution distribution = ToughnessColorDistribution.singleColor(color);
                EntityHealthManager.setCustomColorDistribution(entityType, distribution);
                EntityConfigProcessor.LOGGER.debug("Set single color for {}: {}", entityType, colorName);
            } else {
                EntityConfigProcessor.LOGGER.warn("Invalid color name '{}' for {}", colorName, entityType);
            }
        } catch (final Exception e) {
            EntityConfigProcessor.LOGGER.warn("Failed to parse toughnessColor for {}: {}", entityType, e.getMessage());
        }
    }

    /**
     * 处理多色分布配置
     * Process multi-color distribution configuration
     */
    private static void processMultiColorConfig(final JsonObject config, final String entityType) {
        try {
            final JsonObject colorsObj = config.getAsJsonObject("toughnessColors");
            if (null != colorsObj) {
                final Map<ToughnessColor, Float> colorMap = new HashMap<>();
                float totalPercentage = 0.0f;

                for (final Map.Entry<String, JsonElement> entry : colorsObj.entrySet()) {
                    final String colorName = entry.getKey();
                    final ToughnessColor color = ToughnessColor.byName(colorName);
                    if (null != color) {
                        try {
                            final float percentage = entry.getValue().getAsFloat();
                            if (0 < percentage) {
                                colorMap.put(color, percentage);
                                totalPercentage += percentage;
                            }
                        } catch (final Exception e) {
                            EntityConfigProcessor.LOGGER.warn("Invalid percentage for color '{}' in {}: {}", colorName, entityType, e.getMessage());
                        }
                    } else {
                        EntityConfigProcessor.LOGGER.warn("Invalid color name '{}' in toughnessColors for {}", colorName, entityType);
                    }
                }

                if (!colorMap.isEmpty()) {
                    // 归一化百分比
                    if (1.0f != totalPercentage && 0 < totalPercentage) {
                        final Map<ToughnessColor, Float> normalizedMap = new HashMap<>();
                        for (final Map.Entry<ToughnessColor, Float> entry : colorMap.entrySet()) {
                            normalizedMap.put(entry.getKey(), entry.getValue() / totalPercentage);
                        }
                        colorMap.clear();
                        colorMap.putAll(normalizedMap);
                    }

                    final ToughnessColorDistribution distribution = ToughnessColorDistribution.multiColor(colorMap);
                    EntityHealthManager.setCustomColorDistribution(entityType, distribution);
                    EntityConfigProcessor.LOGGER.debug("Set multi-color distribution for {}: {}", entityType, colorMap);
                } else {
                    EntityConfigProcessor.LOGGER.warn("No valid colors found in toughnessColors for {}", entityType);
                }
            }
        } catch (final Exception e) {
            EntityConfigProcessor.LOGGER.warn("Failed to parse toughnessColors for {}: {}", entityType, e.getMessage());
        }
    }

    /**
     * 从Map对象处理实体配置
     * Process entity configuration from Map object
     *
     * @param configMap 配置Map对象
     * @return 处理结果（成功/失败）
     */
    public static boolean processEntityConfigFromMap(final Map<?, ?> configMap) {
        if (null == configMap || configMap.isEmpty()) {
            return false;
        }

        try {
            // 将Map转换为JSON字符串，然后使用现有的处理逻辑
            final String configStr = EntityConfigProcessor.GSON.toJson(configMap);
            return EntityConfigProcessor.processEntityConfig(configStr);
        } catch (final Exception e) {
            EntityConfigProcessor.LOGGER.error("Failed to process entity config from Map: {}", e.getMessage());
            return false;
        }
    }
}
