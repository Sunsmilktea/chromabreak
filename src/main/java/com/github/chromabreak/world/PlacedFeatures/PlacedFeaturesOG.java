package com.github.chromabreak.world.PlacedFeatures;

import com.github.chromabreak.world.ConfiguredFeatures.ConfiguredFeaturesOG;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class PlacedFeaturesOG {

    public class ModPlacedFeatures {
        public static final ResourceKey<PlacedFeature> ORANGE_GEODE_PLACED =
                ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("chromabreak", "orange_geode_placed"));

        public static void bootstrap(final BootstrapContext<PlacedFeature> context) {
            final var configured = context.lookup(Registries.CONFIGURED_FEATURE);

            context.register(ModPlacedFeatures.ORANGE_GEODE_PLACED, new PlacedFeature(
                    configured.getOrThrow(ConfiguredFeaturesOG.ORANGE_GEODE),
                    List.of(
                            RarityFilter.onAverageOnceEvery(40),  // 稀有度，可调（原版紫水晶是24）
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), // Y=-58 ~ 30（深板岩层）
                            EnvironmentScanPlacement.scanningFor(
                                    net.minecraft.core.Direction.DOWN,
                                    net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesTag(net.minecraft.tags.BlockTags.BASE_STONE_OVERWORLD),
                                    32
                            ),
                            RandomOffsetPlacement.vertical(ConstantInt.of(0))
                    )
            ));
        }
    }
}
