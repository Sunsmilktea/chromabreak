package com.github.chromabreak.config;

import com.github.chromabreak.Config;
import com.github.chromabreak.system.EntityHealthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * EntityConfigLoader - 实体配置加载器
 * Entity Configuration Loader
 * <p>
 * 负责从ChromaBreak模组的配置文件加载实体配置，并将其应用到EntityHealthManager中
 * Responsible for loading entity configurations from ChromaBreak mod's config file
 * and applying them to EntityHealthManager
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 配置文件解析：从Config.ENTITY_CONFIGS配置项读取实体配置列表
 * Config file parsing: Read entity configuration list from Config.ENTITY_CONFIGS config item
 * - 配置处理：将配置字符串传递给EntityConfigProcessor进行处理
 * Configuration processing: Pass configuration strings to EntityConfigProcessor for processing
 * - 状态管理：管理配置加载状态，避免重复加载
 * State management: Manage configuration loading state to avoid duplicate loading
 * - 热重载支持：支持配置文件的动态重载
 * Hot reload support: Support dynamic reloading of configuration files
 * - 统计报告：记录配置加载的成功和失败数量
 * Statistics reporting: Record success and failure counts of configuration loading
 * <p>
 * 工作流程：
 * Workflow:
 * 1. 检查是否已加载配置（避免重复加载）
 * Check if configurations are already loaded (avoid duplicate loading)
 * 2. 从Config.ENTITY_CONFIGS获取配置字符串列表
 * Get configuration string list from Config.ENTITY_CONFIGS
 * 3. 遍历配置字符串，使用EntityConfigProcessor处理每个配置
 * Iterate through configuration strings, process each with EntityConfigProcessor
 * 4. 统计处理结果并记录日志
 * Count processing results and log statistics
 * 5. 标记配置为已加载状态
 * Mark configurations as loaded
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 */
public enum EntityConfigLoader {
    ;

    /**
     * 日志记录器
     * Logger instance
     * <p>
     * 用于记录配置加载过程中的信息和错误
     * Used to log information and errors during configuration loading process
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("EntityConfigLoader");

    /**
     * 配置加载状态标志
     * Configuration loading status flag
     * <p>
     * 用于避免重复加载配置，确保配置只加载一次
     * Used to avoid duplicate configuration loading, ensures configurations are loaded only once
     */
    private static boolean loaded;

    /**
     * 从配置文件加载实体配置
     * Load entity configurations from config file
     * <p>
     * 主配置加载方法，负责读取配置文件并处理所有实体配置
     * Main configuration loading method, responsible for reading config file and processing all entity configurations
     * <p>
     * 处理逻辑：
     * Processing logic:
     * 1. 检查配置是否已加载，避免重复处理
     * Check if configurations are already loaded to avoid duplicate processing
     * 2. 从Config.ENTITY_CONFIGS获取配置列表
     * Get configuration list from Config.ENTITY_CONFIGS
     * 3. 如果配置列表为空，记录信息并返回
     * If configuration list is empty, log info and return
     * 4. 遍历配置列表，使用EntityConfigProcessor处理每个配置
     * Iterate through configuration list, process each with EntityConfigProcessor
     * 5. 统计成功和失败的数量
     * Count success and failure numbers
     * 6. 记录加载结果并标记为已加载
     * Log loading results and mark as loaded
     * <p>
     * 异常处理：如果发生异常，记录错误并标记为已加载（避免重复尝试）
     * Exception handling: If exception occurs, log error and mark as loaded (avoid repeated attempts)
     */
    public static void loadEntityConfigs() {
        if (EntityConfigLoader.loaded) {
            return;
        }

        EntityConfigLoader.LOGGER.info("Loading entity configurations from config file...");
        EntityConfigLoader.LOGGER.info("从配置文件加载实体配置...");

        try {
            @SuppressWarnings("unchecked") final List<String> configList = (List<String>) Config.ENTITY_CONFIGS.get();
            if (null == configList || configList.isEmpty()) {
                EntityConfigLoader.LOGGER.info("No entity configurations found in config file");
                EntityConfigLoader.LOGGER.info("配置文件中没有找到实体配置");
                EntityConfigLoader.loaded = true;
                return;
            }

            int successCount = 0;
            int failCount = 0;

            for (final String configStr : configList) {
                if (EntityConfigProcessor.processEntityConfig(configStr)) {
                    successCount++;
                } else {
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
     * <p>
     * 支持配置文件的动态重载，通常在配置修改后调用
     * Supports dynamic reloading of configuration files, typically called after config modifications
     * <p>
     * 处理步骤：
     * Processing steps:
     * 1. 重置加载状态标志，允许重新加载
     * Reset loading status flag to allow reloading
     * 2. 清除EntityHealthManager中的所有自定义设置
     * Clear all custom settings in EntityHealthManager
     * 3. 调用loadEntityConfigs()重新加载配置
     * Call loadEntityConfigs() to reload configurations
     * <p>
     * 使用场景：配置文件修改后，需要重新加载配置时调用此方法
     * Usage scenario: Call this method when configuration file is modified and needs to be reloaded
     */
    public static void reloadEntityConfigs() {
        EntityConfigLoader.loaded = false;
        EntityHealthManager.clearAllCustomSettings();
        EntityConfigLoader.loadEntityConfigs();
    }
}

