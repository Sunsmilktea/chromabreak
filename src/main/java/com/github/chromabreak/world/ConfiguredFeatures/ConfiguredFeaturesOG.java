package com.github.chromabreak.world.ConfiguredFeatures;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.slf4j.Logger;

import java.util.List;

/**
 * 配置特征枚举 - Configured Features Enum
 * <p>
 * 这个类定义了模组的所有配置特征，特别是橙水晶矿洞的配置
 * This class defines all configured features for the mod, especially the orange crystal geode configuration
 * <p>
 * 配置特征定义了特征的生成参数，但不涉及在世界中的具体放置位置
 * Configured features define generation parameters but not specific placement in the world
 */
public enum ConfiguredFeaturesOG {
    ;
    /**
     * 橙水晶矿洞的配置特征键
     * Configured feature key for orange crystal geode
     */
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_GEODE =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("chromabreak", "orange_geode"));
    private static final Logger LOGGER = ChromaBreak.LOGGER;

    /**
     * 初始化配置特征 - Initialize configured features
     * <p>
     * 这个方法在数据生成时被调用，用于注册所有配置特征
     * This method is called during data generation to register all configured features
     *
     * @param context 引导上下文，用于注册配置特征
     *                Bootstrap context for registering configured features
     */
    public static void bootstrap(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ConfiguredFeaturesOG.LOGGER.info("开始注册橙水晶矿洞配置特征 - Starting to register orange geode configured feature");
        context.register(ConfiguredFeaturesOG.ORANGE_GEODE, new ConfiguredFeature<>(Feature.GEODE, new GeodeConfiguration(
                new GeodeBlockSettings(
                        BlockStateProvider.simple(Blocks.AIR),      // 内部：空气
                        BlockStateProvider.simple(ModBlocks.CRYSTALS_ORANGE_BLOCK.get()),          // 外层：橙水晶块
                        BlockStateProvider.simple(ModBlocks.BUDDING_ORANGE_CRYSTALS.get()),        // 中间层（备用）
                        BlockStateProvider.simple(net.minecraft.world.level.block.Blocks.CALCITE), // 内层：方解石
                        BlockStateProvider.simple(Blocks.DEEPSLATE),          // 最外填充：橙水晶块（代替平滑玄武岩）
                        List.of(                                                                   // 晶簇列表
                                ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get().defaultBlockState(),     // 小晶芽
                                ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get().defaultBlockState(),    // 中晶芽
                                ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get().defaultBlockState(),     // 大晶芽
                                ModBlocks.CRYSTALS_ORANGE_CLUSTER.get().defaultBlockState()        // 晶簇
                        ),
                        BlockTags.FEATURES_CANNOT_REPLACE,  // 不能替换的方块标签
                        BlockTags.GEODE_INVALID_BLOCKS       // 无效的矿洞方块标签
                ),
                new GeodeLayerSettings(2.1, 2.1, 3.1, 4.1), // 矿洞层设置（外壳厚度等）
                new GeodeCrackSettings(0.95, 2.0, 2),       // 矿洞裂缝设置
                0.35,                                       // 矿洞生成概率
                0.083,                                      // 矿洞生成温度参数
                true,                                       // 是否使用备用方块
                UniformInt.of(4, 6),                        // 外层厚度随机范围
                UniformInt.of(3, 4),                        // 中间层厚度随机范围
                UniformInt.of(1, 2),                        // 内层厚度随机范围
                -16,                                        // 最小Y轴生成高度
                16,                                         // 最大Y轴生成高度
                0.05,                                       // 噪声偏移
                1                                           // 噪声乘数
        )));
        ConfiguredFeaturesOG.LOGGER.info("橙水晶矿洞配置特征注册完成 - Orange geode configured feature registration completed");
    }

}
