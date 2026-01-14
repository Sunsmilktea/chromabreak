// 文件位置：com/github/chromabreak/world/ModWorldGenerationProvider.java
package com.github.chromabreak.datagen;

import com.github.chromabreak.world.BiomeModifiers.BiomeModifiersOG;
import com.github.chromabreak.world.ConfiguredFeatures.ConfiguredFeaturesOG;
import com.github.chromabreak.world.PlacedFeatures.PlacedFeaturesOG;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * ModWorldGenerationProvider - 世界生成数据提供器
 * ModWorldGenerationProvider - World Generation Data Provider
 * <p>
 * 这个类负责为ChromaBreak模组生成世界生成数据
 * This class is responsible for generating world generation data for the ChromaBreak mod
 * <p>
 * 继承自NeoForge的DatapackBuiltinEntriesProvider，用于在数据生成阶段自动创建世界生成配置
 * Extends NeoForge's DatapackBuiltinEntriesProvider to automatically create world generation configurations during data generation phase
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 注册配置特征（ConfiguredFeature）：定义晶洞的生成规则和属性
 * Register configured features: define generation rules and properties for geodes
 * - 注册放置特征（PlacedFeature）：定义晶洞在世界中的放置规则
 * Register placed features: define placement rules for geodes in the world
 * - 注册生物群系修改器（BiomeModifier）：定义晶洞在哪些生物群系中生成
 * Register biome modifiers: define which biomes geodes generate in
 * <p>
 * 生成的数据类型：
 * Generated data types:
 * - 配置特征JSON文件 (data/chromabreak/worldgen/configured_feature/)
 * Configured feature JSON files (data/chromabreak/worldgen/configured_feature/)
 * - 放置特征JSON文件 (data/chromabreak/worldgen/placed_feature/)
 * Placed feature JSON files (data/chromabreak/worldgen/placed_feature/)
 * - 生物群系修改器JSON文件 (data/chromabreak/neoforge/biome_modifier/)
 * Biome modifier JSON files (data/chromabreak/neoforge/biome_modifier/)
 */
public class ModWorldGenerationProvider extends DatapackBuiltinEntriesProvider {

    // 1. 创建一个RegistrySetBuilder
    //    This Builder tells the game what we want to register
    //    这个Builder会告诉游戏我们要注册什么
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            // 第一步：注册ConfiguredFeature（晶洞的配置）
            // Step 1: Register ConfiguredFeature (geode configuration)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeaturesOG::bootstrap)
            // 第二步：注册PlacedFeature（晶洞的放置规则）
            // Step 2: Register PlacedFeature (geode placement rules)
            .add(Registries.PLACED_FEATURE, PlacedFeaturesOG::bootstrap)
            // 第三步：注册生物群系修改器（告诉游戏在哪里生成）
            // Step 3: Register biome modifiers (tell the game where to generate)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifiersOG::bootstrap);

    /**
     * 构造函数 - 初始化世界生成数据提供器
     * Constructor - Initializes the world generation data provider
     *
     * @param output     PackOutput实例，用于输出生成的数据文件
     *                   PackOutput instance for outputting generated data files
     * @param registries HolderLookup.Provider实例，用于查找注册表
     *                   HolderLookup.Provider instance for registry lookups
     */
    public ModWorldGenerationProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
        // 使用我们创建的BUILDER，而不是空的new RegistrySetBuilder()
        // Use our created BUILDER instead of empty new RegistrySetBuilder()
        super(output, registries, ModWorldGenerationProvider.BUILDER, Set.of("chromabreak"));
    }
}