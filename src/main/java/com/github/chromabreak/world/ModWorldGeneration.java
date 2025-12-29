package com.github.chromabreak.world;

import com.github.chromabreak.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * ModWorldGeneration - 模组世界生成注册类
 * Mod World Generation Registration Class
 * <p>
 * 负责ChromaBreak模组的世界生成配置和管理，包括橙色水晶晶洞的生成概率调整
 * Responsible for ChromaBreak mod's world generation configuration and management,
 * including orange crystal geode generation probability adjustment
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 世界生成初始化：在模组启动时初始化世界生成相关配置
 * World generation initialization: Initialize world generation configurations when mod starts
 * - 动态配置调整：根据配置文件动态调整水晶晶洞的生成概率
 * Dynamic configuration adjustment: Dynamically adjust crystal geode generation probability based on config
 * - JSON文件管理：读取和修改世界生成JSON配置文件
 * JSON file management: Read and modify world generation JSON configuration files
 * - 调试信息输出：提供详细的世界生成调试信息
 * Debug information output: Provide detailed world generation debug information
 * <p>
 * 世界生成配置方式：
 * World generation configuration method:
 * - NeoForge 1.21+使用JSON文件配置世界生成
 * NeoForge 1.21+ uses JSON files to configure world generation
 * - 主要配置文件包括：
 * Main configuration files include:
 * - Configured Feature: 定义特征的具体配置
 * Configured Feature: Defines specific configuration of features
 * - Placed Feature: 定义特征的放置规则和概率
 * Placed Feature: Defines placement rules and probabilities of features
 * - Biome Modifier: 定义特征在生物群系中的生成
 * Biome Modifier: Defines feature generation in biomes
 * <p>
 * 橙色水晶晶洞生成机制：
 * Orange crystal geode generation mechanism:
 * - 基于原版紫水晶晶洞的生成机制
 * Based on vanilla amethyst geode generation mechanism
 * - 生成概率可配置，相对于紫水晶晶洞的概率倍数
 * Generation probability is configurable, relative to amethyst geode probability
 * - 使用稀有度过滤器控制生成频率
 * Uses rarity filter to control generation frequency
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 */
public enum ModWorldGeneration {
    ;

    /**
     * 日志记录器
     * Logger instance
     * <p>
     * 用于记录世界生成相关的信息和错误
     * Used to log world generation related information and errors
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * JSON序列化器
     * JSON serializer
     * <p>
     * 用于读取和写入JSON配置文件，设置美化打印格式
     * Used to read and write JSON configuration files, set pretty printing format
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 放置特征文件路径
     * Placed feature file path
     * <p>
     * 橙色水晶晶洞的放置特征配置文件路径
     * Path to orange crystal geode placed feature configuration file
     */
    private static final String PLACED_FEATURE_PATH = "data/chromabreak/worldgen/placed_feature/orange_crystal_geode.json";

    /**
     * 初始化世界生成
     * Initialize world generation
     * <p>
     * 模组初始化时调用的主方法，负责设置所有世界生成相关配置
     * Main method called during mod initialization, responsible for setting up all world generation related configurations
     * <p>
     * 处理逻辑：
     * Processing logic:
     * 1. 动态调整橙色水晶晶洞生成概率
     * Dynamically adjust orange crystal geode generation probability
     * 2. 检查调试模式，输出详细的世界生成信息
     * Check debug mode, output detailed world generation information
     * <p>
     * 调用时机：在模组主类的构造方法中调用
     * Call timing: Called in the constructor of the mod's main class
     * <p>
     * 示例：
     * Example:
     * public ChromaBreak() {
     * ModWorldGeneration.initialize();
     * }
     */
    public static void initialize() {
        // 动态调整橙色水晶晶洞生成概率
        ModWorldGeneration.adjustOrangeCrystalGeodeProbability();

        // 检查调试模式
        // Check debug mode
        if (Config.WORLD_GEN_DEBUG.get()) {
            ModWorldGeneration.LOGGER.info("===== 世界生成调试信息 =====");
            ModWorldGeneration.LOGGER.info("===== World Generation Debug Info =====");
            ModWorldGeneration.LOGGER.info("橙色水晶晶洞配置已加载");
            ModWorldGeneration.LOGGER.info("Orange crystal geode configuration loaded");
            ModWorldGeneration.LOGGER.info("生成概率: {}", Config.ORANGE_CRYSTAL_GEODE_PROBABILITY.get());
            ModWorldGeneration.LOGGER.info("Generation probability: {}", Config.ORANGE_CRYSTAL_GEODE_PROBABILITY.get());
            ModWorldGeneration.LOGGER.info("配置文件位置:");
            ModWorldGeneration.LOGGER.info("Configuration files location:");
            ModWorldGeneration.LOGGER.info("- Configured Feature: data/chromabreak/worldgen/configured_feature/orange_crystal_geode.json");
            ModWorldGeneration.LOGGER.info("- Placed Feature: data/chromabreak/worldgen/placed_feature/orange_crystal_geode.json");
            ModWorldGeneration.LOGGER.info("- Biome Modifier: data/chromabreak/neoforge/biome_modifier/orange_crystal_geode.json");
            ModWorldGeneration.LOGGER.info("================================");
        } else {
            ModWorldGeneration.LOGGER.info("World generation features configured via JSON files");
        }
    }

    /**
     * 动态调整橙色水晶晶洞生成概率
     * Dynamically adjust orange crystal geode generation probability
     * <p>
     * 根据配置文件中的概率设置，动态修改世界生成JSON文件中的稀有度值
     * Dynamically modify rarity values in world generation JSON files based on probability settings in config
     * <p>
     * 处理逻辑：
     * Processing logic:
     * 1. 获取配置文件中设置的生成概率倍数
     * Get generation probability multiplier from config file
     * 2. 计算相对于紫水晶晶洞的实际生成概率
     * Calculate actual generation probability relative to amethyst geode
     * 3. 计算新的稀有度值（取整并限制在合理范围内）
     * Calculate new rarity value (rounded and limited to reasonable range)
     * 4. 读取放置特征JSON文件
     * Read placed feature JSON file
     * 5. 修改稀有度过滤器中的chance值
     * Modify chance value in rarity filter
     * 6. 将修改后的内容写回文件
     * Write modified content back to file
     * 7. 如果启用调试模式，输出详细信息
     * If debug mode is enabled, output detailed information
     * <p>
     * 异常处理：如果发生异常，记录错误日志但不中断程序
     * Exception handling: If exception occurs, log error but don't interrupt program
     */
    private static void adjustOrangeCrystalGeodeProbability() {
        try {
            // 获取配置值
            final double probability = Config.ORANGE_CRYSTAL_GEODE_PROBABILITY.get();

            // 紫水晶洞的基础概率是1/53 ≈ 0.01887
            final double amethystProbability = 1.0 / 53.0;

            // 计算橙色水晶洞的概率
            final double orangeProbability = amethystProbability * probability;

            // 计算新的稀有度值（取整）
            int newChance = (int) Math.round(1.0 / orangeProbability);

            // 确保稀有度值在合理范围内
            newChance = Math.max(10, Math.min(1000, newChance));

            // 读取并修改放置特征文件
            final Path placedFeaturePath = Paths.get("src/main/resources", ModWorldGeneration.PLACED_FEATURE_PATH);
            if (Files.exists(placedFeaturePath)) {
                final String content = Files.readString(placedFeaturePath);
                final JsonObject json = JsonParser.parseString(content).getAsJsonObject();

                // 修改稀有度过滤器中的chance值
                if (json.has("placement") && json.get("placement").isJsonArray()) {
                    final var placementArray = json.getAsJsonArray("placement");
                    for (final var element : placementArray) {
                        if (element.isJsonObject()) {
                            final JsonObject placement = element.getAsJsonObject();
                            if (placement.has("type") &&
                                    "minecraft:rarity_filter".equals(placement.get("type").getAsString()) &&
                                    placement.has("chance")) {
                                placement.addProperty("chance", newChance);
                                break;
                            }
                        }
                    }
                }

                // 写回文件
                final String updatedContent = ModWorldGeneration.GSON.toJson(json);
                Files.writeString(placedFeaturePath, updatedContent, StandardOpenOption.TRUNCATE_EXISTING);

                if (Config.WORLD_GEN_DEBUG.get()) {
                    ModWorldGeneration.LOGGER.info("已更新橙色水晶晶洞生成概率");
                    ModWorldGeneration.LOGGER.info("Updated orange crystal geode generation probability");
                    ModWorldGeneration.LOGGER.info("配置概率: {}", probability);
                    ModWorldGeneration.LOGGER.info("Config probability: {}", probability);
                    ModWorldGeneration.LOGGER.info("新稀有度值: {}", newChance);
                    ModWorldGeneration.LOGGER.info("New rarity value: {}", newChance);
                    ModWorldGeneration.LOGGER.info("实际生成概率: {}", (1.0 / newChance));
                    ModWorldGeneration.LOGGER.info("Actual generation probability: {}", (1.0 / newChance));
                }
            }
        } catch (final Exception e) {
            ModWorldGeneration.LOGGER.error("调整橙色水晶晶洞生成概率时出错:", e);
            ModWorldGeneration.LOGGER.error("Error adjusting orange crystal geode generation probability:", e);
        }
    }
}
