package com.github.chromabreak.world;

import com.github.chromabreak.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 模组世界生成注册类
 * Mod World Generation Registration Class
 * <p>
 * 负责模组的世界生成配置
 * Responsible for mod's world generation configuration
 * <p>
 * 在NeoForge 1.21+中，世界生成通过JSON文件配置
 * 这个类主要用于组织相关代码和提供初始化方法
 * <p>
 * In NeoForge 1.21+, world generation is configured via JSON files
 * This class is mainly used to organize related code and provide initialization methods
 */
public enum ModWorldGeneration {
    ;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String PLACED_FEATURE_PATH = "data/chromabreak/worldgen/placed_feature/orange_crystal_geode.json";

    /**
     * 初始化世界生成
     * Initialize world generation
     * <p>
     * 在模组初始化时调用此方法来设置世界生成
     * Call this method during mod initialization to set up world generation
     */
    public static void initialize() {
        // 动态调整橙色水晶晶洞生成概率
        ModWorldGeneration.adjustOrangeCrystalGeodeProbability();
        
        // 检查调试模式
        // Check debug mode
        if (Config.WORLD_GEN_DEBUG.get()) {
            LOGGER.info("===== 世界生成调试信息 =====");
            LOGGER.info("===== World Generation Debug Info =====");
            LOGGER.info("橙色水晶晶洞配置已加载");
            LOGGER.info("Orange crystal geode configuration loaded");
            LOGGER.info("生成概率: {}", Config.ORANGE_CRYSTAL_GEODE_PROBABILITY.get());
            LOGGER.info("Generation probability: {}", Config.ORANGE_CRYSTAL_GEODE_PROBABILITY.get());
            LOGGER.info("配置文件位置:");
            LOGGER.info("Configuration files location:");
            LOGGER.info("- Configured Feature: data/chromabreak/worldgen/configured_feature/orange_crystal_geode.json");
            LOGGER.info("- Placed Feature: data/chromabreak/worldgen/placed_feature/orange_crystal_geode.json");
            LOGGER.info("- Biome Modifier: data/chromabreak/neoforge/biome_modifier/orange_crystal_geode.json");
            LOGGER.info("================================");
        } else {
            LOGGER.info("World generation features configured via JSON files");
        }
    }

    /**
     * 动态调整橙色水晶晶洞生成概率
     * Dynamically adjust orange crystal geode generation probability
     */
    private static void adjustOrangeCrystalGeodeProbability() {
        try {
            // 获取配置值
            double probability = Config.ORANGE_CRYSTAL_GEODE_PROBABILITY.get();
            
            // 紫水晶洞的基础概率是1/53 ≈ 0.01887
            double amethystProbability = 1.0 / 53.0;
            
            // 计算橙色水晶洞的概率
            double orangeProbability = amethystProbability * probability;
            
            // 计算新的稀有度值（取整）
            int newChance = (int) Math.round(1.0 / orangeProbability);
            
            // 确保稀有度值在合理范围内
            newChance = Math.max(10, Math.min(1000, newChance));
            
            // 读取并修改放置特征文件
            Path placedFeaturePath = Paths.get("src/main/resources", PLACED_FEATURE_PATH);
            if (Files.exists(placedFeaturePath)) {
                String content = Files.readString(placedFeaturePath);
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();
                
                // 修改稀有度过滤器中的chance值
                if (json.has("placement") && json.get("placement").isJsonArray()) {
                    var placementArray = json.getAsJsonArray("placement");
                    for (var element : placementArray) {
                        if (element.isJsonObject()) {
                            JsonObject placement = element.getAsJsonObject();
                            if (placement.has("type") && 
                                placement.get("type").getAsString().equals("minecraft:rarity_filter") &&
                                placement.has("chance")) {
                                placement.addProperty("chance", newChance);
                                break;
                            }
                        }
                    }
                }
                
                // 写回文件
                String updatedContent = GSON.toJson(json);
                Files.writeString(placedFeaturePath, updatedContent, StandardOpenOption.TRUNCATE_EXISTING);
                
                if (Config.WORLD_GEN_DEBUG.get()) {
                    LOGGER.info("已更新橙色水晶晶洞生成概率");
                    LOGGER.info("Updated orange crystal geode generation probability");
                    LOGGER.info("配置概率: {}", probability);
                    LOGGER.info("Config probability: {}", probability);
                    LOGGER.info("新稀有度值: {}", newChance);
                    LOGGER.info("New rarity value: {}", newChance);
                    LOGGER.info("实际生成概率: {}", (1.0 / newChance));
                    LOGGER.info("Actual generation probability: {}", (1.0 / newChance));
                }
            }
        } catch (Exception e) {
            LOGGER.error("调整橙色水晶晶洞生成概率时出错:", e);
            LOGGER.error("Error adjusting orange crystal geode generation probability:", e);
        }
    }
}
