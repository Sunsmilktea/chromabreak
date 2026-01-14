package com.github.chromabreak.world.BiomeModifiers;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.world.PlacedFeatures.PlacedFeaturesOG;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

/**
 * 生物群系修改器类 - Biome Modifiers Class
 * <p>
 * 这个类定义了模组的所有生物群系修改器，特别是将橙水晶矿洞添加到沙漠生物群系的修改器
 * This class defines all biome modifiers for the mod, especially the one that adds orange crystal geode to desert biomes
 * <p>
 * 生物群系修改器用于向特定生物群系添加或修改特征生成
 * Biome modifiers are used to add or modify feature generation in specific biomes
 */
public enum BiomeModifiersOG {
    ;
    /**
     * 为橙水晶矿洞创建一个生物群系修改器的Key
     * Create a biome modifier key for orange crystal geode
     * <p>
     * 这个键用于在注册表中唯一标识这个生物群系修改器
     * This key is used to uniquely identify this biome modifier in the registry
     */
    public static final ResourceKey<BiomeModifier> ADD_ORANGE_GEODE_TO_DESERTS =
            ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                    ResourceLocation.fromNamespaceAndPath("chromabreak", "orange_geode_biome_modifier"));
    private static final Logger LOGGER = ChromaBreak.LOGGER;

    /**
     * 初始化生物群系修改器 - Initialize biome modifiers
     * <p>
     * 这个方法在数据生成时被调用，用于注册所有生物群系修改器
     * This method is called during data generation to register all biome modifiers
     *
     * @param context 引导上下文，用于注册生物群系修改器
     *                Bootstrap context for registering biome modifiers
     */
    public static void bootstrap(final BootstrapContext<BiomeModifier> context) {
        BiomeModifiersOG.LOGGER.info("开始注册橙水晶矿洞生物群系修改器 - Starting to register orange geode biome modifier");

        // 1. 获取已注册的生物群系
        final var biomes = context.lookup(Registries.BIOME);

        // 2. 获取已注册的晶洞放置特征
        final var placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        // 3. 创建一个生物群系修改器
        //    功能：在沙漠生物群系中添加橙水晶矿洞
        context.register(BiomeModifiersOG.ADD_ORANGE_GEODE_TO_DESERTS,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        // 选择沙漠生物群系
                        HolderSet.direct(
                                biomes.getOrThrow(Biomes.DESERT)  // 使用沙漠生物群系
                        ),
                        // 选择要添加的特征
                        HolderSet.direct(
                                placedFeatures.getOrThrow(PlacedFeaturesOG.ORANGE_GEODE_PLACED)
                        ),
                        // 在哪个生成阶段添加（UNDERGROUND_DECORATION是地下装饰阶段）
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );

        BiomeModifiersOG.LOGGER.info("橙水晶矿洞生物群系修改器注册完成 - Orange geode biome modifier registration completed");
    }
}


