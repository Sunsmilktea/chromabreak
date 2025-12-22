package com.github.chromabreak.config;

import com.github.chromabreak.Config;
import com.github.chromabreak.system.ModCompatibilityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 模组兼容性配置加载器
 * 从配置文件加载模组兼容性设置并应用到ModCompatibilityManager
 * <p>
 * Mod Compatibility Configuration Loader
 * Load mod compatibility settings from config file and apply to ModCompatibilityManager
 */
public class ModCompatibilityConfigLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("ModCompatibilityConfigLoader");
    private static boolean loaded = false;

    /**
     * 从配置文件加载模组兼容性配置
     * Load mod compatibility configurations from config file
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
            @SuppressWarnings("unchecked") final List<String> bypassModIds = (List<String>) (List<?>) Config.BYPASS_MOD_IDS.get();
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
            @SuppressWarnings("unchecked") final List<String> bypassItemPatterns = (List<String>) (List<?>) Config.BYPASS_ITEM_PATTERNS.get();
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
     */
    public static void reloadModCompatibilityConfigs() {
        ModCompatibilityConfigLoader.loaded = false;
        ModCompatibilityManager.clearBypassConfigurations();
        ModCompatibilityConfigLoader.loadModCompatibilityConfigs();
    }
}

