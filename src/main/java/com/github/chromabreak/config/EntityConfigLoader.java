package com.github.chromabreak.config;

import com.github.chromabreak.Config;
import com.github.chromabreak.system.EntityHealthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 实体配置加载器
 * 从配置文件加载实体配置并应用到EntityHealthManager
 * <p>
 * Entity Configuration Loader
 * Load entity configurations from config file and apply to EntityHealthManager
 */
public enum EntityConfigLoader {
    ;
    private static final Logger LOGGER = LoggerFactory.getLogger("EntityConfigLoader");
    private static boolean loaded;

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
     */
    public static void reloadEntityConfigs() {
        EntityConfigLoader.loaded = false;
        EntityHealthManager.clearAllCustomSettings();
        EntityConfigLoader.loadEntityConfigs();
    }
}

