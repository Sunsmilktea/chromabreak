package com.github.chromabreak.world.PlacedFeatures;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.world.ConfiguredFeatures.ConfiguredFeaturesOG;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import org.slf4j.Logger;

import java.util.List;

/**
 * 放置特征枚举 - Placed Features Enum
 * <p>
 * 这个类定义了模组的所有放置特征，特别是橙水晶矿洞的放置规则
 * This class defines all placed features for the mod, especially the orange crystal geode placement rules
 * <p>
 * 放置特征定义了配置特征在世界中的具体放置位置和条件
 * Placed features define the specific placement locations and conditions for configured features in the world
 */
public enum PlacedFeaturesOG {
    ;
    /**
     * 橙水晶矿洞的放置特征键
     * Placed feature key for orange crystal geode
     */
    public static final ResourceKey<PlacedFeature> ORANGE_GEODE_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("chromabreak", "orange_geode_placed"));
    private static final Logger LOGGER = ChromaBreak.LOGGER;

    /**
     * 初始化放置特征 - Initialize placed features
     * <p>
     * 这个方法在数据生成时被调用，用于注册所有放置特征
     * This method is called during data generation to register all placed features
     *
     * @param context 引导上下文，用于注册放置特征
     *                Bootstrap context for registering placed features
     */
    public static void bootstrap(final BootstrapContext<PlacedFeature> context) {
        PlacedFeaturesOG.LOGGER.info("开始注册橙水晶矿洞放置特征 - Starting to register orange geode placed feature");

        final var configured = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(PlacedFeaturesOG.ORANGE_GEODE_PLACED, new PlacedFeature(
                configured.getOrThrow(ConfiguredFeaturesOG.ORANGE_GEODE),
                List.of(
                        RarityFilter.onAverageOnceEvery(60),  // 稀有度过滤器：平均每60个区块生成一次
                        InSquarePlacement.spread(),           // 在区块内均匀分布
                        HeightRangePlacement.uniform(         // 高度范围：-64到0（深板岩层）
                                VerticalAnchor.absolute(-64),  // 最低高度
                                VerticalAnchor.absolute(0)),   // 最高高度（深板岩层顶部）
                        EnvironmentScanPlacement.scanningFor( // 环境扫描：向下寻找基岩
                                net.minecraft.core.Direction.DOWN,
                                BlockPredicate.matchesTag(BlockTags.BASE_STONE_OVERWORLD), // 匹配基岩标签
                                16                                                         // 最大扫描距离
                        ),
                        RandomOffsetPlacement.horizontal(ConstantInt.of(7)), // 水平随机偏移：±7格
                        RandomOffsetPlacement.vertical(ConstantInt.of(1))    // 垂直随机偏移：±1格
                )
        ));

        PlacedFeaturesOG.LOGGER.info("橙水晶矿洞放置特征注册完成 - Orange geode placed feature registration completed");
    }
}
