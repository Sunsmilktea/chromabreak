package com.github.chromabreak.datagen;

import com.github.chromabreak.util.ModBlocks;
import com.github.chromabreak.util.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


/**
 * Custom loot table provider for ChromaBreak mod.
 * 自定义战利品表提供者，用于ChromaBreak模组
 */
public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(final HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        this.dropSelf(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 水晶簇：统一调用复用方法
        this.addCrystalClusterDrops(
                ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(),
                ModItems.ORANGE_CRYSTAL_SHARD.get()
        );
    }

    /**
     * 通用方法：为所有水晶簇生成与原版紫水晶簇完全一致的掉落表
     *
     * @param clusterBlock 水晶簇方块本身（精准收集时掉落这个）
     * @param shardItem    掉落的碎片物品
     */
    private void addCrystalClusterDrops(final Block clusterBlock, final Item shardItem) {
        this.add(clusterBlock, block -> this.createSilkTouchDispatchTable(
                block,
                LootItem.lootTableItem(shardItem)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(
                                this.registries.lookupOrThrow(Registries.ENCHANTMENT)
                                        .getOrThrow(Enchantments.FORTUNE)
                        ))
                        .when(MatchTool.toolMatches(
                                ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)
                        ))
                        .otherwise(
                                this.applyExplosionDecay(
                                        block,
                                        LootItem.lootTableItem(shardItem)
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
