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
 * ModBlockLootTableProvider - 方块战利品表数据生成器
 * ModBlockLootTableProvider - Block Loot Table Data Generator
 * <p>
 * 这个类负责为ChromaBreak模组生成方块战利品表数据
 * This class is responsible for generating block loot table data for the ChromaBreak mod
 * <p>
 * 继承自Minecraft的BlockLootSubProvider，用于在数据生成阶段自动创建方块战利品表
 * Extends Minecraft's BlockLootSubProvider to automatically create block loot tables during data generation phase
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 定义方块被破坏时的掉落物
 * Define items dropped when blocks are broken
 * - 处理精准采集和普通采集的不同掉落逻辑
 * Handle different drop logic for silk touch and normal mining
 * - 为水晶簇实现与原版紫水晶簇一致的掉落行为
 * Implement drop behavior for crystal clusters consistent with vanilla amethyst clusters
 * <p>
 * 生成的数据类型：
 * Generated data types:
 * - 方块战利品表JSON文件 (data/chromabreak/loot_table/blocks/)
 * Block loot table JSON files (data/chromabreak/loot_table/blocks/)
 * <p>
 * 实现细节：
 * Implementation details:
 * - 使用createSilkTouchDispatchTable处理精准采集逻辑
 * Use createSilkTouchDispatchTable to handle silk touch logic
 * - 使用ApplyBonusCount实现时运附魔效果
 * Use ApplyBonusCount to implement fortune enchantment effects
 * - 使用MatchTool限制特定工具才能获取完整掉落
 * Use MatchTool to restrict full drops to specific tools
 */
public class ModBlockLootTableProvider extends BlockLootSubProvider {
    /**
     * 构造函数 - 初始化方块战利品表提供器
     * Constructor - Initializes the block loot table provider
     *
     * @param registries HolderLookup.Provider实例，用于查找注册表
     *                   HolderLookup.Provider instance for registry lookups
     */
    public ModBlockLootTableProvider(final HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    /**
     * 生成战利品表 - 定义所有方块的掉落物
     * Generate loot tables - define drops for all blocks
     * <p>
     * 这个方法在数据生成时被调用，用于注册模组中方块的战利品表
     * This method is called during data generation to register loot tables for blocks in the mod
     * <p>
     * 当前实现：
     * Current implementation:
     * - 为普通方块添加直接掉落自身的战利品表
     * Add loot tables for normal blocks that drop themselves
     * - 为水晶簇添加特殊的战利品表（类似原版紫水晶簇）
     * Add special loot tables for crystal clusters (similar to vanilla amethyst clusters)
     */
    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        this.dropSelf(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 水晶簇：统一调用复用方法
        // Crystal clusters: uniformly call reusable method
        this.addCrystalClusterDrops(
                ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(),
                ModItems.ORANGE_CRYSTAL_SHARD.get()
        );
    }

    /**
     * 通用方法：为所有水晶簇生成与原版紫水晶簇完全一致的掉落表
     * Generic method: Generate loot tables for all crystal clusters identical to vanilla amethyst clusters
     * <p>
     * 这个方法为水晶簇创建与原版紫水晶簇完全一致的掉落行为：
     * This method creates drop behavior for crystal clusters identical to vanilla amethyst clusters:
     * - 精准采集时掉落完整方块
     * Drop complete block when mined with silk touch
     * - 普通采集时掉落2-4个碎片（受时运附魔影响）
     * Drop 2-4 shards when mined normally (affected by fortune enchantment)
     * - 必须使用特定工具才能获取完整掉落
     * Must use specific tools to get full drops
     *
     * @param clusterBlock 水晶簇方块本身（精准收集时掉落这个）
     *                     The crystal cluster block itself (dropped when mined with silk touch)
     * @param shardItem    掉落的碎片物品
     *                     The shard item to drop
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

    /**
     * 获取已知方块 - 返回模组中所有需要生成战利品表的方块
     * Get known blocks - return all blocks in the mod that need loot tables
     *
     * @return 模组中所有方块的迭代器
     * Iterator of all blocks in the mod
     */
    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
