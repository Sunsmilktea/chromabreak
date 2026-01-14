package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * ModBlockTagsProvider - 方块标签数据生成器
 * ModBlockTagsProvider - Block Tags Data Generator
 * <p>
 * 这个类负责为ChromaBreak模组生成方块标签数据
 * This class is responsible for generating block tag data for the ChromaBreak mod
 * <p>
 * 继承自NeoForge的BlockTagsProvider，用于在数据生成阶段自动创建方块标签定义
 * Extends NeoForge's BlockTagsProvider to automatically create block tag definitions during data generation phase
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 为方块添加可挖掘标签（例如：需要镐子挖掘）
 * Add mineable tags to blocks (e.g., requires pickaxe to mine)
 * - 为方块添加工具等级标签（例如：需要石质工具）
 * Add tool tier tags to blocks (e.g., requires stone tool)
 * <p>
 * 生成的数据类型：
 * Generated data types:
 * - 方块标签JSON文件 (data/minecraft/tags/block/)
 * Block tag JSON files (data/minecraft/tags/block/)
 */
public class ModBlockTagsProvider extends BlockTagsProvider {
    /**
     * 构造函数 - 初始化方块标签提供器
     * Constructor - Initializes the block tags provider
     *
     * @param output             PackOutput实例，用于输出生成的数据文件
     *                           PackOutput instance for outputting generated data files
     * @param lookupProvider     HolderLookup.Provider实例，用于查找注册表
     *                           HolderLookup.Provider instance for registry lookups
     * @param existingFileHelper ExistingFileHelper实例，用于验证现有文件
     *                           ExistingFileHelper instance for validating existing files
     */
    public ModBlockTagsProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChromaBreak.MODID, existingFileHelper);
    }

    /**
     * 添加方块标签 - 定义哪些方块属于哪些标签
     * Add block tags - define which blocks belong to which tags
     * <p>
     * 这个方法在数据生成时被调用，用于注册模组中方块的标签
     * This method is called during data generation to register tags for blocks in the mod
     *
     * @param provider HolderLookup.Provider实例，提供注册表查找功能
     *                 HolderLookup.Provider instance providing registry lookup functionality
     *                 <p>
     *                 当前实现的标签：
     *                 Currently implemented tags:
     *                 - mineable/pickaxe: 标识方块可用镐子挖掘
     *                 Identifies blocks that can be mined with a pickaxe
     *                 - needs_stone_tool: 标识方块需要石质或更好的工具挖掘
     *                 Identifies blocks that require stone or better tools to mine
     */
    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        // 添加可挖掘标签 - 标识这些方块可用镐子挖掘
        // Add mineable tag - identifies these blocks can be mined with a pickaxe
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.CRYSTALS_ORANGE_BLOCK.get())
                .add(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get());

        // 添加工具等级标签 - 标识这些方块需要石质工具挖掘
        // Add tool tier tag - identifies these blocks require stone tools to mine
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.CRYSTALS_ORANGE_BLOCK.get())
                .add(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get());
    }
}

