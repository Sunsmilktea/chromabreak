package com.github.chromabreak.config;

import com.github.chromabreak.Config;
import com.github.chromabreak.system.ModCompatibilityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ModCompatibilityConfigLoader - 模组兼容性配置加载器
 * Mod Compatibility Configuration Loader
 * <p>
 * 负责从ChromaBreak模组的配置文件加载模组兼容性设置，并将其应用到ModCompatibilityManager中
 * Responsible for loading mod compatibility settings from ChromaBreak mod's config file
 * and applying them to ModCompatibilityManager
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 配置解析：从Config.BYPASS_MOD_IDS和Config.BYPASS_ITEM_PATTERNS配置项读取设置
 * Configuration parsing: Read settings from Config.BYPASS_MOD_IDS and Config.BYPASS_ITEM_PATTERNS config items
 * - 配置应用：将配置设置应用到ModCompatibilityManager的绕过系统
 * Configuration application: Apply configuration settings to ModCompatibilityManager's bypass system
 * - 状态管理：管理配置加载状态，避免重复加载
 * State management: Manage configuration loading state to avoid duplicate loading
 * - 热重载支持：支持配置文件的动态重载
 * Hot reload support: Support dynamic reloading of configuration files
 * - 统计报告：记录配置加载的数量和结果
 * Statistics reporting: Record configuration loading counts and results
 * <p>
 * 加载的配置类型：
 * Loaded configuration types:
 * - 绕过模组ID列表：指定可以绕过韧性系统的模组ID
 * Bypass mod IDs list: Specifies mod IDs that can bypass the toughness system
 * - 绕过物品ID模式列表：指定可以绕过韧性系统的物品ID前缀模式
 * Bypass item ID patterns list: Specifies item ID prefix patterns that can bypass the toughness system
 * <p>
 * 工作流程：
 * Workflow:
 * 1. 检查是否已加载配置（避免重复加载）
 * Check if configurations are already loaded (avoid duplicate loading)
 * 2. 清除ModCompatibilityManager中的现有配置
 * Clear existing configurations in ModCompatibilityManager
 * 3. 从Config.BYPASS_MOD_IDS加载绕过模组ID列表
 * Load bypass mod IDs list from Config.BYPASS_MOD_IDS
 * 4. 从Config.BYPASS_ITEM_PATTERNS加载绕过物品ID模式列表
 * Load bypass item ID patterns list from Config.BYPASS_ITEM_PATTERNS
 * 5. 记录加载结果并标记为已加载
 * Log loading results and mark as loaded
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 */
public enum ModCompatibilityConfigLoader {
    ;

    /**
     * 日志记录器
     * Logger instance
     * <p>
     * 用于记录配置加载过程中的信息和错误
     * Used to log information and errors during configuration loading process
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("ModCompatibilityConfigLoader");

    /**
     * 配置加载状态标志
     * Configuration loading status flag
     * <p>
     * 用于避免重复加载配置，确保配置只加载一次
     * Used to avoid duplicate configuration loading, ensures configurations are loaded only once
     */
    private static boolean loaded;

    /**
     * 从配置文件加载模组兼容性配置
     * Load mod compatibility configurations from config file
     * <p>
     * 主配置加载方法，负责读取配置文件并处理所有模组兼容性配置
     * Main configuration loading method, responsible for reading config file and processing all mod compatibility configurations
     * <p>
     * 处理逻辑：
     * Processing logic:
     * 1. 检查配置是否已加载，避免重复处理
     * Check if configurations are already loaded to avoid duplicate processing
     * 2. 清除ModCompatibilityManager中的现有配置
     * Clear existing configurations in ModCompatibilityManager
     * 3. 从Config.BYPASS_MOD_IDS加载绕过模组ID列表
     * Load bypass mod IDs list from Config.BYPASS_MOD_IDS
     * 4. 从Config.BYPASS_ITEM_PATTERNS加载绕过物品ID模式列表
     * Load bypass item ID patterns list from Config.BYPASS_ITEM_PATTERNS
     * 5. 记录加载结果并标记为已加载
     * Log loading results and mark as loaded
     * <p>
     * 异常处理：如果发生异常，记录错误并标记为已加载（避免重复尝试）
     * Exception handling: If exception occurs, log error and mark as loaded (avoid repeated attempts)
     */
    public static void loadModCompatibilityConfigs() {
        if (ModCompatibilityConfigLoader.loaded) {
            return;
        }

        ModCompatibilityConfigLoader.LOGGER.info("Loading mod compatibility configurations from config file...");
        ModCompatibilityConfigLoader.LOGGER.info("从配置文件加载模组兼容性配置...");

        try {
            // 清除现有配置
            // Clear existing configurations
            ModCompatibilityManager.clearBypassConfigurations();

            // 加载绕过模组ID列表
            // Load bypass mod IDs list
            @SuppressWarnings("unchecked") final List<String> bypassModIds = (List<String>) Config.BYPASS_MOD_IDS.get();
            if (null != bypassModIds && !bypassModIds.isEmpty()) {
                for (final String modId : bypassModIds) {
                    if (null != modId && !modId.trim().isEmpty()) {
                        ModCompatibilityManager.addBypassModId(modId.trim());
                        ModCompatibilityConfigLoader.LOGGER.debug("Added bypass mod ID: {}", modId.trim());
                    }
                }
                ModCompatibilityConfigLoader.LOGGER.info("Loaded {} bypass mod IDs", bypassModIds.size());
                ModCompatibilityConfigLoader.LOGGER.info("已加载 {} 个绕过模组ID", bypassModIds.size());
            }

            // 加载绕过物品ID模式列表
            // Load bypass item ID patterns list
            @SuppressWarnings("unchecked") final List<String> bypassItemPatterns = (List<String>) Config.BYPASS_ITEM_PATTERNS.get();
            if (null != bypassItemPatterns && !bypassItemPatterns.isEmpty()) {
                for (final String pattern : bypassItemPatterns) {
                    if (null != pattern && !pattern.trim().isEmpty()) {
                        ModCompatibilityManager.addBypassItemPattern(pattern.trim());
                        ModCompatibilityConfigLoader.LOGGER.debug("Added bypass item pattern: {}", pattern.trim());
                    }
                }
                ModCompatibilityConfigLoader.LOGGER.info("Loaded {} bypass item patterns", bypassItemPatterns.size());
                ModCompatibilityConfigLoader.LOGGER.info("已加载 {} 个绕过物品ID模式", bypassItemPatterns.size());
            }

            ModCompatibilityConfigLoader.LOGGER.info("Mod compatibility configuration loading completed");
            ModCompatibilityConfigLoader.LOGGER.info("模组兼容性配置加载完成");
            ModCompatibilityConfigLoader.loaded = true;
        } catch (final Exception e) {
            ModCompatibilityConfigLoader.LOGGER.error("Failed to load mod compatibility configurations", e);
            ModCompatibilityConfigLoader.LOGGER.error("加载模组兼容性配置失败", e);
            ModCompatibilityConfigLoader.loaded = true; // 标记为已加载，避免重复尝试
        }
    }

    /**
     * 重新加载模组兼容性配置（用于配置热重载）
     * Reload mod compatibility configurations (for config hot reload)
     * <p>
     * 支持配置文件的动态重载，通常在配置修改后调用
     * Supports dynamic reloading of configuration files, typically called after config modifications
     * <p>
     * 处理步骤：
     * Processing steps:
     * 1. 重置加载状态标志，允许重新加载
     * Reset loading status flag to allow reloading
     * 2. 清除ModCompatibilityManager中的所有绕过配置
     * Clear all bypass configurations in ModCompatibilityManager
     * 3. 调用loadModCompatibilityConfigs()重新加载配置
     * Call loadModCompatibilityConfigs() to reload configurations
     * <p>
     * 使用场景：配置文件修改后，需要重新加载模组兼容性配置时调用此方法
     * Usage scenario: Call this method when configuration file is modified and mod compatibility configurations need to be reloaded
     */
    public static void reloadModCompatibilityConfigs() {
        ModCompatibilityConfigLoader.loaded = false;
        ModCompatibilityManager.clearBypassConfigurations();
        ModCompatibilityConfigLoader.loadModCompatibilityConfigs();
    }
}

