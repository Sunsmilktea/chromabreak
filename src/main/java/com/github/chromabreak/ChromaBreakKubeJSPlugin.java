package com.github.chromabreak;

import com.github.chromabreak.system.EntityHealthManager;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * KubeJS Plugin for ChromaBreak mod
 * Provides JavaScript bindings and events for ChromaBreak functionality
 * <p>
 * ChromaBreak模组的KubeJS插件
 * 为ChromaBreak功能提供JavaScript绑定和事件支持
 * <p>
 * 这个插件允许在KubeJS脚本中访问和操作ChromaBreak模组的功能，
 * 通过JavaScript绑定机制将模组功能暴露给脚本开发者使用。
 */
public class ChromaBreakKubeJSPlugin implements KubeJSPlugin {
    /**
     * 日志记录器，用于记录插件运行状态和调试信息
     * Logger for recording plugin status and debugging information
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("ChromaBreakKubeJSPlugin");

    /**
     * 静态初始化块，确保插件类被加载
     * Static initializer block to ensure plugin class is loaded
     */
    static {
        ChromaBreakKubeJSPlugin.LOGGER.info("ChromaBreakKubeJSPlugin class loaded");
    }

    /**
     * 插件初始化方法
     * 在插件加载时被调用，用于执行基本的初始化操作
     * <p>
     * Plugin initialization method
     * Called when the plugin is loaded, used for basic initialization operations
     */
    @Override
    public void init() {
        ChromaBreakKubeJSPlugin.LOGGER.info("ChromaBreak KubeJS plugin initialized");
        ChromaBreakKubeJSPlugin.LOGGER.info("ChromaBreak KubeJS plugin class: {}", this.getClass().getName());
    }

    /**
     * 插件完全初始化后的回调方法
     * 在所有插件都初始化完成后被调用
     * <p>
     * Callback method after plugin is fully initialized
     * Called after all plugins have been initialized
     */
    @Override
    public void afterInit() {
        ChromaBreakKubeJSPlugin.LOGGER.info("ChromaBreak KubeJS plugin fully initialized");
    }

    /**
     * 注册KubeJS绑定
     * 将ChromaBreak功能暴露给JavaScript
     * <p>
     * Register KubeJS bindings
     * Expose ChromaBreak functionality to JavaScript
     */
    @Override
    public void registerBindings(final BindingRegistry event) {
        ChromaBreakKubeJSPlugin.LOGGER.info("ChromaBreak KubeJS registerBindings called");
        try {
            event.add("chromaBreak", ChromaBreakBindings.class);
            ChromaBreakKubeJSPlugin.LOGGER.info("ChromaBreak KubeJS bindings registered successfully");
        } catch (final Exception e) {
            ChromaBreakKubeJSPlugin.LOGGER.error("Failed to register ChromaBreak KubeJS bindings", e);
        }
    }

    /**
     * Bindings class that exposes ChromaBreak functionality to JavaScript
     * <p>
     * 绑定类，将ChromaBreak功能暴露给JavaScript
     * 这个类包含了所有可以在KubeJS脚本中使用的ChromaBreak相关方法
     */
    public static class ChromaBreakBindings {
        /**
         * 获取模组ID
         * 返回ChromaBreak模组的唯一标识符
         * <p>
         * Get mod ID
         * Returns the unique identifier of the ChromaBreak mod
         *
         * @return 模组ID字符串
         * Mod ID string
         */
        public String getModId() {
            return ChromaBreak.MODID;
        }

        /**
         * 获取模组名称
         * 返回ChromaBreak模组的显示名称
         * <p>
         * Get mod name
         * Returns the display name of the ChromaBreak mod
         *
         * @return 模组名称字符串
         * Mod name string
         */
        public String getModName() {
            return "ChromaBreak";
        }

        /**
         * 获取模组版本
         * 返回ChromaBreak模组的版本号
         * <p>
         * Get mod version
         * Returns the version number of the ChromaBreak mod
         *
         * @return 模组版本字符串
         * Mod version string
         */
        public String getModVersion() {
            return "1.0.0";
        }

        /**
         * 记录信息日志
         * 在KubeJS脚本中记录信息级别的日志
         * <p>
         * Log info message
         * Records info-level log messages in KubeJS scripts
         *
         * @param message 要记录的日志消息
         *                Log message to record
         */
        public void logInfo(final String message) {
            ChromaBreakKubeJSPlugin.LOGGER.info("[KubeJS] {}", message);
        }

        /**
         * 记录警告日志
         * 在KubeJS脚本中记录警告级别的日志
         * <p>
         * Log warning message
         * Records warning-level log messages in KubeJS scripts
         *
         * @param message 要记录的警告消息
         *                Warning message to record
         */
        public void logWarning(final String message) {
            ChromaBreakKubeJSPlugin.LOGGER.warn("[KubeJS] {}", message);
        }

        /**
         * 记录错误日志
         * 在KubeJS脚本中记录错误级别的日志
         * <p>
         * Log error message
         * Records error-level log messages in KubeJS scripts
         *
         * @param message 要记录的错误消息
         *                Error message to record
         */
        public void logError(final String message) {
            ChromaBreakKubeJSPlugin.LOGGER.error("[KubeJS] {}", message);
        }

        /**
         * 检查血条HUD功能是否启用
         * 返回当前血条HUD功能的启用状态
         * <p>
         * Check if health bar HUD feature is enabled
         * Returns the current enabled status of the health bar HUD feature
         *
         * @return 布尔值，表示血条HUD功能是否启用
         * Boolean indicating if the health bar HUD feature is enabled
         */
        public boolean isHealthBarHUDEnabled() {
            return com.github.chromabreak.Config.SHOW_ENTITY_HEALTH_BAR.getAsBoolean();
        }

        /**
         * 获取血条HUD的尺寸信息
         * 返回包含血条宽度和高度的数组
         * <p>
         * Get health bar HUD dimensions
         * Returns an array containing health bar width and height
         *
         * @return 包含宽度和高度的数组 [width, height]
         * Array containing width and height [width, height]
         */
        public double[] getHealthBarDimensions() {
            return new double[]{
                    com.github.chromabreak.Config.ENTITY_HEALTH_BAR_WIDTH.getAsDouble(),
                    com.github.chromabreak.Config.ENTITY_HEALTH_BAR_HEIGHT.getAsDouble()
            };
        }

        /**
         * 设置生物的最大生命值
         * Set maximum health for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @param maxHealth  最大生命值
         */
        public void setEntityMaxHealth(final String entityType, final float maxHealth) {
            if (0 >= maxHealth) {
                ChromaBreakKubeJSPlugin.LOGGER.warn("Invalid max health value: {} for entity: {}", maxHealth, entityType);
                return;
            }
            EntityHealthManager.setCustomMaxHealth(entityType, maxHealth);
            ChromaBreakKubeJSPlugin.LOGGER.info("Set max health for {} to {}", entityType, maxHealth);
        }

        /**
         * 获取生物的自定义最大生命值
         * Get custom maximum health for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @return 自定义最大生命值，如果没有设置则返回-1
         */
        public float getEntityMaxHealth(final String entityType) {
            return EntityHealthManager.getCustomMaxHealth(entityType);
        }

        /**
         * 移除生物的自定义最大生命值设置
         * Remove custom maximum health setting for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         */
        public void removeEntityMaxHealth(final String entityType) {
            EntityHealthManager.removeCustomMaxHealth(entityType);
            ChromaBreakKubeJSPlugin.LOGGER.info("Removed custom max health for {}", entityType);
        }

        /**
         * 设置生物的韧性值
         * Set toughness value for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @param toughness  韧性值
         */
        public void setEntityToughness(final String entityType, final float toughness) {
            if (0 > toughness) {
                ChromaBreakKubeJSPlugin.LOGGER.warn("Invalid toughness value: {} for entity: {}", toughness, entityType);
                return;
            }
            EntityHealthManager.setCustomToughness(entityType, toughness);
            ChromaBreakKubeJSPlugin.LOGGER.info("Set toughness for {} to {}", entityType, toughness);
        }

        /**
         * 获取生物的自定义韧性值
         * Get custom toughness value for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @return 自定义韧性值，如果没有设置则返回-1
         */
        public float getEntityToughness(final String entityType) {
            return EntityHealthManager.getCustomToughness(entityType);
        }

        /**
         * 移除生物的自定义韧性值设置
         * Remove custom toughness setting for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         */
        public void removeEntityToughness(final String entityType) {
            EntityHealthManager.removeCustomToughness(entityType);
            ChromaBreakKubeJSPlugin.LOGGER.info("Removed custom toughness for {}", entityType);
        }

        /**
         * 设置生物的韧性颜色（单一颜色）
         * Set toughness color for entity type (single color)
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @param colorName  颜色名称（"red", "black", "orange", "green", "white", "blue", "yellow"）
         */
        public void setEntityToughnessColor(final String entityType, final String colorName) {
            final com.github.chromabreak.system.ToughnessColor color = com.github.chromabreak.system.ToughnessColor.byName(colorName);
            if (null == color) {
                ChromaBreakKubeJSPlugin.LOGGER.warn("Invalid color name: {} for entity: {}", colorName, entityType);
                return;
            }
            final com.github.chromabreak.system.ToughnessColorDistribution distribution =
                    com.github.chromabreak.system.ToughnessColorDistribution.singleColor(color);
            EntityHealthManager.setCustomColorDistribution(entityType, distribution);
            ChromaBreakKubeJSPlugin.LOGGER.info("Set toughness color for {} to {}", entityType, colorName);
        }

        /**
         * 设置生物的韧性颜色分布（多色百分比）
         * Set toughness color distribution for entity type (multi-color percentage)
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @param colorMap   颜色和百分比映射对象
         *                   格式: {red: 0.5, blue: 0.3, green: 0.2}
         */
        public void setEntityToughnessColorDistribution(final String entityType, final Map<String, Object> colorMap) {
            if (null == colorMap || colorMap.isEmpty()) {
                ChromaBreakKubeJSPlugin.LOGGER.warn("Empty color map for entity: {}", entityType);
                return;
            }

            final java.util.Map<com.github.chromabreak.system.ToughnessColor, Float> distributionMap = new java.util.HashMap<>();
            for (final Map.Entry<String, Object> entry : colorMap.entrySet()) {
                final com.github.chromabreak.system.ToughnessColor color = com.github.chromabreak.system.ToughnessColor.byName(entry.getKey());
                if (null != color) {
                    try {
                        final float percentage = Float.parseFloat(entry.getValue().toString());
                        if (0 < percentage) {
                            distributionMap.put(color, percentage);
                        }
                    } catch (final NumberFormatException e) {
                        ChromaBreakKubeJSPlugin.LOGGER.warn("Invalid percentage value: {} for color: {}", entry.getValue(), entry.getKey());
                    }
                } else {
                    ChromaBreakKubeJSPlugin.LOGGER.warn("Invalid color name: {} for entity: {}", entry.getKey(), entityType);
                }
            }

            if (distributionMap.isEmpty()) {
                ChromaBreakKubeJSPlugin.LOGGER.warn("No valid colors in distribution for entity: {}", entityType);
                return;
            }

            final com.github.chromabreak.system.ToughnessColorDistribution distribution =
                    com.github.chromabreak.system.ToughnessColorDistribution.multiColor(distributionMap);
            EntityHealthManager.setCustomColorDistribution(entityType, distribution);
            ChromaBreakKubeJSPlugin.LOGGER.info("Set toughness color distribution for {} with {} colors", entityType, distributionMap.size());
        }

        /**
         * 获取生物的韧性颜色分布
         * Get toughness color distribution for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         * @return 颜色分布对象，如果没有设置则返回null
         * 格式: {colors: ["red", "blue"], percentages: [0.5, 0.5]}
         */
        public Map<String, Object> getEntityToughnessColorDistribution(final String entityType) {
            final com.github.chromabreak.system.ToughnessColorDistribution distribution =
                    EntityHealthManager.getCustomColorDistribution(entityType);
            if (null == distribution) {
                return null;
            }

            final java.util.Map<String, Object> result = new java.util.HashMap<>();
            final java.util.List<String> colors = new java.util.ArrayList<>();
            final java.util.List<Float> percentages = new java.util.ArrayList<>();

            for (final java.util.Map.Entry<com.github.chromabreak.system.ToughnessColor, Float> entry : distribution.getColorMap().entrySet()) {
                colors.add(entry.getKey().getName());
                percentages.add(entry.getValue());
            }

            result.put("colors", colors.toArray(new String[0]));
            result.put("percentages", percentages.toArray(new Float[0]));
            return result;
        }

        /**
         * 移除生物的自定义韧性颜色分布设置
         * Remove custom toughness color distribution setting for entity type
         *
         * @param entityType 生物类型（字符串格式，如"minecraft:zombie"）
         */
        public void removeEntityToughnessColorDistribution(final String entityType) {
            EntityHealthManager.setCustomColorDistribution(entityType, null);
            ChromaBreakKubeJSPlugin.LOGGER.info("Removed custom toughness color distribution for {}", entityType);
        }

        /**
         * 批量设置多个生物的生命值和韧性值
         * Batch set health and toughness for multiple entities
         *
         * @param entityConfigs 生物配置对象数组
         *                      格式: [{entityType: "minecraft:zombie", maxHealth: 50, toughness: 100}, ...]
         */
        public void setEntityConfigs(final Object[] entityConfigs) {
            if (null == entityConfigs || 0 == entityConfigs.length) {
                ChromaBreakKubeJSPlugin.LOGGER.warn("Empty entity configs provided");
                return;
            }

            for (final Object configObj : entityConfigs) {
                try {
                    if (configObj instanceof final Map<?, ?> config) {
                        final String entityType = config.get("entityType").toString();

                        if (config.containsKey("maxHealth")) {
                            final float maxHealth = Float.parseFloat(config.get("maxHealth").toString());
                            this.setEntityMaxHealth(entityType, maxHealth);
                        }

                        if (config.containsKey("toughness")) {
                            final float toughness = Float.parseFloat(config.get("toughness").toString());
                            this.setEntityToughness(entityType, toughness);
                        }

                        // 支持单一颜色设置
                        // Support single color setting
                        if (config.containsKey("toughnessColor")) {
                            final String colorName = config.get("toughnessColor").toString();
                            this.setEntityToughnessColor(entityType, colorName);
                        }

                        // 支持多色百分比分布设置
                        // Support multi-color percentage distribution setting
                        if (config.containsKey("toughnessColors")) {
                            final Object colorsObj = config.get("toughnessColors");
                            if (colorsObj instanceof final Map<?, ?> colorsMap) {
                                final Map<String, Object> colorMap = new java.util.HashMap<>();
                                for (final Map.Entry<?, ?> entry : colorsMap.entrySet()) {
                                    colorMap.put(entry.getKey().toString(), entry.getValue());
                                }
                                this.setEntityToughnessColorDistribution(entityType, colorMap);
                            }
                        }
                    }
                } catch (final Exception e) {
                    ChromaBreakKubeJSPlugin.LOGGER.error("Error processing entity config: {}", e.getMessage());
                }
            }
        }

        /**
         * 添加一个绕过韧性系统的伤害类型
         * Add a damage type that bypasses the toughness system
         *
         * @param damageType 伤害类型ID（例如 "minecraft:out_of_world"）
         */
        public void addBypassDamageType(final String damageType) {
            com.github.chromabreak.system.ModCompatibilityManager.addBypassDamageType(damageType);
            ChromaBreakKubeJSPlugin.LOGGER.info("Added bypass damage type: {}", damageType);
        }

        /**
         * 移除一个绕过韧性系统的伤害类型
         * Remove a damage type that bypasses the toughness system
         *
         * @param damageType 伤害类型ID
         */
        public void removeBypassDamageType(final String damageType) {
            com.github.chromabreak.system.ModCompatibilityManager.removeBypassDamageType(damageType);
            ChromaBreakKubeJSPlugin.LOGGER.info("Removed bypass damage type: {}", damageType);
        }

        /**
         * 获取所有绕过韧性系统的伤害类型
         * Get all damage types that bypass the toughness system
         *
         * @return 伤害类型ID数组
         */
        public String[] getBypassDamageTypes() {
            return com.github.chromabreak.system.ModCompatibilityManager.getBypassDamageTypes().toArray(new String[0]);
        }

        /**
         * 添加一个绕过韧性系统的模组ID
         * Add a mod ID that bypasses the toughness system
         *
         * @param modId 模组ID（例如 "avaritia"）
         */
        public void addBypassModId(final String modId) {
            com.github.chromabreak.system.ModCompatibilityManager.addBypassModId(modId);
            ChromaBreakKubeJSPlugin.LOGGER.info("Added bypass mod ID: {}", modId);
        }

        /**
         * 移除一个绕过韧性系统的模组ID
         * Remove a mod ID that bypasses the toughness system
         *
         * @param modId 模组ID
         */
        public void removeBypassModId(final String modId) {
            com.github.chromabreak.system.ModCompatibilityManager.removeBypassModId(modId);
            ChromaBreakKubeJSPlugin.LOGGER.info("Removed bypass mod ID: {}", modId);
        }

        /**
         * 获取所有绕过韧性系统的模组ID
         * Get all mod IDs that bypass the toughness system
         *
         * @return 模组ID数组
         */
        public String[] getBypassModIds() {
            return com.github.chromabreak.system.ModCompatibilityManager.getBypassModIds().toArray(new String[0]);
        }

        /**
         * 清除所有绕过韧性系统的配置（伤害类型和模组ID）
         * Clear all bypass configurations for the toughness system (damage types and mod IDs)
         */
        public void clearBypassConfigurations() {
            com.github.chromabreak.system.ModCompatibilityManager.clearBypassConfigurations();
            ChromaBreakKubeJSPlugin.LOGGER.info("Cleared all bypass configurations.");
        }

        /**
         * 添加颜色到武器物品
         * Add color to weapon item
         *
         * @param itemStack 武器物品堆栈（ItemStack对象）
         * @param colorName 颜色名称（"red", "blue", "green", "yellow", "white", "black", "orange"）
         * @return 是否成功添加颜色
         */
        public boolean addColorToWeapon(final Object itemStack, final String colorName) {
            if (null == itemStack || null == colorName) {
                return false;
            }
            try {
                if (itemStack instanceof net.minecraft.world.item.ItemStack stack) {
                    final com.github.chromabreak.system.ToughnessColor color = com.github.chromabreak.system.ToughnessColor.byName(colorName);
                    if (null == color) {
                        ChromaBreakKubeJSPlugin.LOGGER.warn("Invalid color name: {}", colorName);
                        return false;
                    }
                    return com.github.chromabreak.system.WeaponColorHelper.addColor(stack, color);
                }
            } catch (final Exception e) {
                ChromaBreakKubeJSPlugin.LOGGER.error("Error adding color to weapon: {}", e.getMessage());
            }
            return false;
        }

        /**
         * 检查武器是否有指定颜色
         * Check if weapon has specified color
         *
         * @param itemStack 武器物品堆栈（ItemStack对象）
         * @param colorName 颜色名称
         * @return 是否有该颜色
         */
        public boolean weaponHasColor(final Object itemStack, final String colorName) {
            if (null == itemStack || null == colorName) {
                return false;
            }
            try {
                if (itemStack instanceof net.minecraft.world.item.ItemStack stack) {
                    final com.github.chromabreak.system.ToughnessColor color = com.github.chromabreak.system.ToughnessColor.byName(colorName);
                    if (null == color) {
                        return false;
                    }
                    return com.github.chromabreak.system.WeaponColorHelper.hasColor(stack, color);
                }
            } catch (final Exception e) {
                ChromaBreakKubeJSPlugin.LOGGER.error("Error checking weapon color: {}", e.getMessage());
            }
            return false;
        }

        /**
         * 检查物品是否是武器
         * Check if item is a weapon
         *
         * @param itemStack 物品堆栈（ItemStack对象）
         * @return 是否是武器
         */
        public boolean isWeapon(final Object itemStack) {
            if (null == itemStack) {
                return false;
            }
            try {
                if (itemStack instanceof net.minecraft.world.item.ItemStack stack) {
                    return com.github.chromabreak.system.WeaponHelper.isWeapon(stack);
                }
            } catch (final Exception e) {
                ChromaBreakKubeJSPlugin.LOGGER.error("Error checking if item is weapon: {}", e.getMessage());
            }
            return false;
        }

        /**
         * 检查物品是否是有效的染料
         * Check if item is a valid dye
         *
         * @param itemStack 物品堆栈（ItemStack对象）
         * @return 是否是有效的染料
         */
        public boolean isValidDye(final Object itemStack) {
            if (null == itemStack) {
                return false;
            }
            try {
                if (itemStack instanceof net.minecraft.world.item.ItemStack stack) {
                    return com.github.chromabreak.system.DyeColorMapper.isValidDye(stack.getItem());
                }
            } catch (final Exception e) {
                ChromaBreakKubeJSPlugin.LOGGER.error("Error checking if item is dye: {}", e.getMessage());
            }
            return false;
        }

        /**
         * 从染料获取颜色名称
         * Get color name from dye
         *
         * @param itemStack 染料物品堆栈（ItemStack对象）
         * @return 颜色名称，如果不是染料则返回null
         */
        public String getColorFromDye(final Object itemStack) {
            if (null == itemStack) {
                return null;
            }
            try {
                if (itemStack instanceof net.minecraft.world.item.ItemStack stack) {
                    final com.github.chromabreak.system.ToughnessColor color = com.github.chromabreak.system.DyeColorMapper.getColorFromDye(stack.getItem());
                    return null == color ? null : color.getName();
                }
            } catch (final Exception e) {
                ChromaBreakKubeJSPlugin.LOGGER.error("Error getting color from dye: {}", e.getMessage());
            }
            return null;
        }
    }
}
