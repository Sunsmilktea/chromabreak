package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * ModItemTagsProvider - 物品标签数据生成器
 * ModItemTagsProvider - Item Tags Data Generator
 * <p>
 * 这个类负责为ChromaBreak模组生成物品标签数据
 * This class is responsible for generating item tag data for the ChromaBreak mod
 * <p>
 * 继承自NeoForge的ItemTagsProvider，用于在数据生成阶段自动创建物品标签定义
 * Extends NeoForge's ItemTagsProvider to automatically create item tag definitions during data generation phase
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 为物品添加标签（例如：食物、工具、材料等）
 * Add tags to items (e.g., food, tools, materials, etc.)
 * - 继承方块标签关系，自动为方块物品添加相应的物品标签
 * Inherit block tag relationships, automatically add corresponding item tags for block items
 * <p>
 * 生成的数据类型：
 * Generated data types:
 * - 物品标签JSON文件 (data/minecraft/tags/item/)
 * Item tag JSON files (data/minecraft/tags/item/)
 * <p>
 * 注意：当前版本尚未实现具体的物品标签，但保留了扩展接口
 * Note: Current version does not implement specific item tags yet, but keeps the extension interface
 */
public class ModItemTagsProvider extends ItemTagsProvider {

    /**
     * 构造函数 - 初始化物品标签提供器
     * Constructor - Initializes the item tags provider
     *
     * @param output             PackOutput实例，用于输出生成的数据文件
     *                           PackOutput instance for outputting generated data files
     * @param lookupProvider     HolderLookup.Provider实例，用于查找注册表
     *                           HolderLookup.Provider instance for registry lookups
     * @param blockTags          方块标签查找器，用于继承方块标签关系
     *                           Block tag lookup for inheriting block tag relationships
     * @param existingFileHelper ExistingFileHelper实例，用于验证现有文件
     *                           ExistingFileHelper instance for validating existing files
     */
    public ModItemTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final CompletableFuture<TagLookup<Block>> blockTags, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ChromaBreak.MODID, existingFileHelper);
    }

    /**
     * 添加物品标签 - 定义哪些物品属于哪些标签
     * Add item tags - define which items belong to which tags
     * <p>
     * 这个方法在数据生成时被调用，用于注册模组中物品的标签
     * This method is called during data generation to register tags for items in the mod
     * <p>
     * 当前实现：预留方法，未来可以添加具体的物品标签
     * Current implementation: Reserved method, can add specific item tags in the future
     *
     * @param provider HolderLookup.Provider实例，提供注册表查找功能
     *                 HolderLookup.Provider instance providing registry lookup functionality
     */
    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        // 预留方法 - 未来可以在这里添加物品标签
        // Reserved method - can add item tags here in the future
        // 例如：this.tag(ItemTags.FOODS).add(ModItems.ORANGE_CRYSTAL_SHARD.get());
        // Example: this.tag(ItemTags.FOODS).add(ModItems.ORANGE_CRYSTAL_SHARD.get());
    }
}
