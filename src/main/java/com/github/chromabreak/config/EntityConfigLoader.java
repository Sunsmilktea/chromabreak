package com.github.chromabreak.config;

import com.github.chromabreak.Config;
import com.github.chromabreak.system.EntityHealthManager;
import com.github.chromabreak.system.ToughnessColor;
import com.github.chromabreak.system.ToughnessColorDistribution;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体配置加载器
 * 从配置文件加载实体配置并应用到EntityHealthManager
 * <p>
 * Entity Configuration Loader
 * Load entity configurations from config file and apply to EntityHealthManager
 */
public class EntityConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("EntityConfigLoader");
    private static final Gson GSON = new Gson();
    private static boolean loaded = false;

    /**
     * 从配置文件加载实体配置
     * Load entity configurations from config file
     */
    public static void loadEntityConfigs() {
        if (EntityConfigLoader.loaded) {
            return;
        }

        EntityConfigLoader.LOGGER.info("Loading entity configurations from config file...");
        EntityConfigLoader.LOGGER.info("从配置文件加载实体配置...");

        try {
            @SuppressWarnings("unchecked") final List<String> configList = (List<String>) (List<?>) Config.ENTITY_CONFIGS.get();
            if (null == configList || configList.isEmpty()) {
                EntityConfigLoader.LOGGER.info("No entity configurations found in config file");
                EntityConfigLoader.LOGGER.info("配置文件中没有找到实体配置");
                EntityConfigLoader.loaded = true;
                return;
            }

            int successCount = 0;
            int failCount = 0;

            for (final String configStr : configList) {
                if (null == configStr || configStr.trim().isEmpty()) {
                    continue;
                }

                try {
                    final JsonObject config = EntityConfigLoader.GSON.fromJson(configStr, JsonObject.class);
                    if (null == config) {
                        EntityConfigLoader.LOGGER.warn("Invalid entity config (null): {}", configStr);
                        failCount++;
                        continue;
                    }

                    // 获取实体类型
                    // Get entity type
                    if (!config.has("entityType")) {
                        EntityConfigLoader.LOGGER.warn("Entity config missing 'entityType': {}", configStr);
                        failCount++;
                        continue;
                    }

                    final String entityType = config.get("entityType").getAsString();
                    if (null == entityType || entityType.trim().isEmpty()) {
                        EntityConfigLoader.LOGGER.warn("Entity config has empty 'entityType': {}", configStr);
                        failCount++;
                        continue;
                    }

                    // 设置最大生命值（如果配置了）
                    // Set max health (if configured)
                    if (config.has("maxHealth")) {
                        try {
                            final float maxHealth = config.get("maxHealth").getAsFloat();
                            if (0 < maxHealth) {
                                EntityHealthManager.setCustomMaxHealth(entityType, maxHealth);
                                EntityConfigLoader.LOGGER.debug("Set max health for {}: {}", entityType, maxHealth);
                            }
                        } catch (final Exception e) {
                            EntityConfigLoader.LOGGER.warn("Failed to parse maxHealth for {}: {}", entityType, e.getMessage());
                        }
                    }

                    // 设置最大韧性值（如果配置了）
                    // Set max toughness (if configured)
                    if (config.has("maxToughness")) {
                        try {
                            final float maxToughness = config.get("maxToughness").getAsFloat();
                            if (0 <= maxToughness) {
                                EntityHealthManager.setCustomToughness(entityType, maxToughness);
                                EntityConfigLoader.LOGGER.debug("Set max toughness for {}: {}", entityType, maxToughness);
                            }
                        } catch (final Exception e) {
                            EntityConfigLoader.LOGGER.warn("Failed to parse maxToughness for {}: {}", entityType, e.getMessage());
                        }
                    }

                    // 设置韧性颜色（单一颜色或多色分布）
                    // Set toughness color (single color or multi-color distribution)
                    if (config.has("toughnessColor")) {
                        // 单一颜色
                        // Single color
                        try {
                            final String colorName = config.get("toughnessColor").getAsString();
                            final ToughnessColor color = ToughnessColor.byName(colorName);
                            if (null != color) {
                                final ToughnessColorDistribution distribution = ToughnessColorDistribution.singleColor(color);
                                EntityHealthManager.setCustomColorDistribution(entityType, distribution);
                                EntityConfigLoader.LOGGER.debug("Set single color for {}: {}", entityType, colorName);
                            } else {
                                EntityConfigLoader.LOGGER.warn("Invalid color name '{}' for {}", colorName, entityType);
                            }
                        } catch (final Exception e) {
                            EntityConfigLoader.LOGGER.warn("Failed to parse toughnessColor for {}: {}", entityType, e.getMessage());
                        }
                    } else if (config.has("toughnessColors")) {
                        // 多色分布
                        // Multi-color distribution
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
                                            EntityConfigLoader.LOGGER.warn("Invalid percentage for color '{}' in {}: {}", colorName, entityType, e.getMessage());
                                        }
                                    } else {
                                        EntityConfigLoader.LOGGER.warn("Invalid color name '{}' in toughnessColors for {}", colorName, entityType);
                                    }
                                }

                                if (!colorMap.isEmpty()) {
                                    // 归一化百分比
                                    // Normalize percentages
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
                                    EntityConfigLoader.LOGGER.debug("Set multi-color distribution for {}: {}", entityType, colorMap);
                                } else {
                                    EntityConfigLoader.LOGGER.warn("No valid colors found in toughnessColors for {}", entityType);
                                }
                            }
                        } catch (final Exception e) {
                            EntityConfigLoader.LOGGER.warn("Failed to parse toughnessColors for {}: {}", entityType, e.getMessage());
                        }
                    }

                    successCount++;
                } catch (final Exception e) {
                    EntityConfigLoader.LOGGER.error("Failed to parse entity config: {}", configStr, e);
                    failCount++;
                }
            }

            EntityConfigLoader.LOGGER.info("Entity configuration loading completed: {} succeeded, {} failed", successCount, failCount);
            EntityConfigLoader.LOGGER.info("实体配置加载完成：{} 成功，{} 失败", successCount, failCount);
            EntityConfigLoader.loaded = true;
        } catch (final Exception e) {
            EntityConfigLoader.LOGGER.error("Failed to load entity configurations", e);
            EntityConfigLoader.loaded = true; // 标记为已加载，避免重复尝试
        }
    }

    /**
     * 重新加载实体配置（用于配置热重载）
     * Reload entity configurations (for config hot reload)
     */
    public static void reloadEntityConfigs() {
        EntityConfigLoader.loaded = false;
        EntityHealthManager.clearAllCustomSettings();
        EntityConfigLoader.loadEntityConfigs();
    }
}

