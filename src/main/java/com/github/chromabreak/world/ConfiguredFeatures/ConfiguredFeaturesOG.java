package com.github.chromabreak.world.ConfiguredFeatures;

import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class ConfiguredFeaturesOG {
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_GEODE =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("chromabreak", "orange_geode"));

    public static void bootstrap(final BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(ORANGE_GEODE, new ConfiguredFeature<>(Feature.GEODE, new GeodeConfiguration(
                new GeodeBlockSettings(
                        BlockStateProvider.simple(net.minecraft.world.level.block.Blocks.AIR),
                        BlockStateProvider.simple(ModBlocks.CRYSTALS_ORANGE_BLOCK.get()),           // 外层：橙水晶块
                        BlockStateProvider.simple(ModBlocks.BUDDING_ORANGE_CRYSTALS.get()),        // 中间层（备用）
                        BlockStateProvider.simple(net.minecraft.world.level.block.Blocks.CALCITE), // 内层：方解石
                        BlockStateProvider.simple(ModBlocks.CRYSTALS_ORANGE_BLOCK.get()),           // 最外填充：橙水晶块（代替平滑玄武岩）
                        List.of(
                                ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get().defaultBlockState(),
                                ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get().defaultBlockState(),
                                ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get().defaultBlockState(),
                                ModBlocks.CRYSTALS_ORANGE_CLUSTER.get().defaultBlockState()
                        ),
                        BlockTags.FEATURES_CANNOT_REPLACE,
                        BlockTags.GEODE_INVALID_BLOCKS
                ),
                new GeodeLayerSettings(2.1, 2.1, 3.1, 4.1),
                new GeodeCrackSettings(0.95, 2.0, 2),
                0.35,
                0.083,
                true,
                UniformInt.of(4, 6),
                UniformInt.of(3, 4),
                UniformInt.of(1, 2),
                -16,
                16,
                0.05,
                1
        )));
    }

}
