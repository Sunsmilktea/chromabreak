package com.github.chromabreak.util;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * ModBlocks - 模组方块注册类
 * Mod Blocks Registration Class
 * <p>
 * 负责注册模组中的所有方块，使用枚举模式确保单例
 * Responsible for registering all blocks in the mod, using enum pattern to ensure singleton
 * <p>
 * 这个类使用NeoForge的DeferredRegister系统来延迟注册方块
 * This class uses NeoForge's DeferredRegister system for deferred block registration
 * <p>
 * 包含以下类型的方块：
 * Includes the following types of blocks:
 * - 小型水晶芽（7种颜色变体）
 * Small crystal buds (7 color variants)
 * - 中型水晶芽（7种颜色变体）
 * Medium crystal buds (7 color variants)
 * - 大型水晶芽（橙色）
 * Large crystal buds (orange)
 * - 橙色水晶芽生方块
 * Orange budding crystals block
 * - 橙色水晶晶簇
 * Orange crystal cluster
 * - 橙色水晶方块
 * Orange crystal block
 */
public enum ModBlocks {
    ;

    /**
     * 方块注册器 - Block registry
     * <p>
     * 使用DeferredRegister延迟注册系统，确保方块在正确的时机注册
     * Uses DeferredRegister deferred registration system to ensure blocks are registered at the correct time
     * <p>
     * 这个注册器会在模组初始化时自动处理方块的注册过程
     * This registry will automatically handle the block registration process during mod initialization
     */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChromaBreak.MODID);

    // ==================== 小型水晶芽方块（7种颜色变体） ====================
    // ==================== Small Crystal Bud Blocks (7 color variants) ====================

    /**
     * 黑色小型水晶芽方块
     * Black small crystal bud block
     * <p>
     * 这些方块在创造模式标签栏不可见，类似紫水晶芽
     * These blocks are not visible in creative mode tab, similar to amethyst buds
     */
    public static final Supplier<Block> SMALL_CRYSTALS_BLACK_BUD = ModBlocks.BLOCKS.register("small_crystals_black_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLACK));

    /**
     * 白色小型水晶芽方块
     * White small crystal bud block
     */
    public static final Supplier<Block> SMALL_CRYSTALS_WHITE_BUD = ModBlocks.BLOCKS.register("small_crystals_white_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.WHITE));

    /**
     * 黄色小型水晶芽方块
     * Yellow small crystal bud block
     */
    public static final Supplier<Block> SMALL_CRYSTALS_YELLOW_BUD = ModBlocks.BLOCKS.register("small_crystals_yellow_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.YELLOW));

    /**
     * 绿色小型水晶芽方块
     * Green small crystal bud block
     */
    public static final Supplier<Block> SMALL_CRYSTALS_GREEN_BUD = ModBlocks.BLOCKS.register("small_crystals_green_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.GREEN));

    /**
     * 蓝色小型水晶芽方块
     * Blue small crystal bud block
     */
    public static final Supplier<Block> SMALL_CRYSTALS_BLUE_BUD = ModBlocks.BLOCKS.register("small_crystals_blue_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLUE));

    /**
     * 橙色小型水晶芽方块
     * Orange small crystal bud block
     */
    public static final Supplier<Block> SMALL_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("small_crystals_orange_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.ORANGE));

    /**
     * 红色小型水晶芽方块
     * Red small crystal bud block
     */
    public static final Supplier<Block> SMALL_CRYSTALS_RED_BUD = ModBlocks.BLOCKS.register("small_crystals_red_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.RED));

    // ==================== 中型水晶芽方块（7种颜色变体） ====================
    // ==================== Medium Crystal Bud Blocks (7 color variants) ====================

    /**
     * 橙色中型水晶芽方块
     * Orange medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("medium_crystals_orange_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.ORANGE));

    /**
     * 黑色中型水晶芽方块
     * Black medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_BLACK_BUD = ModBlocks.BLOCKS.register("medium_crystals_black_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLACK));

    /**
     * 白色中型水晶芽方块
     * White medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_WHITE_BUD = ModBlocks.BLOCKS.register("medium_crystals_white_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.WHITE));

    /**
     * 黄色中型水晶芽方块
     * Yellow medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_YELLOW_BUD = ModBlocks.BLOCKS.register("medium_crystals_yellow_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.YELLOW));

    /**
     * 绿色中型水晶芽方块
     * Green medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_GREEN_BUD = ModBlocks.BLOCKS.register("medium_crystals_green_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.GREEN));

    /**
     * 蓝色中型水晶芽方块
     * Blue medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_BLUE_BUD = ModBlocks.BLOCKS.register("medium_crystals_blue_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLUE));

    /**
     * 红色中型水晶芽方块
     * Red medium crystal bud block
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_RED_BUD = ModBlocks.BLOCKS.register("medium_crystals_red_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.RED));

    // ==================== 大型水晶芽方块 ====================
    // ==================== Large Crystal Bud Blocks ====================

    /**
     * 橙色大型水晶芽方块
     * Orange large crystal bud block
     */
    public static final Supplier<Block> LARGE_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("large_crystals_orange_bud",
            () -> new CustomLargeBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.ORANGE));

    // ==================== 水晶芽生方块 ====================
    // ==================== Budding Crystal Blocks ====================

    /**
     * 橙色水晶芽生方块
     * Orange budding crystals block
     * <p>
     * 类似紫水晶芽生方块，可以生长各种大小的水晶芽
     * Similar to amethyst budding block, can grow crystal buds of various sizes
     */
    public static final Supplier<Block> BUDDING_ORANGE_CRYSTALS = ModBlocks.BLOCKS.register("budding_orange_crystals",
            () -> new BuddingCrystalsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST),
                    BuddingCrystalsBlock.CrystalColor.ORANGE));

    // ==================== 水晶晶簇方块 ====================
    // ==================== Crystal Cluster Blocks ====================

    /**
     * 橙色水晶晶簇方块
     * Orange crystal cluster block
     * <p>
     * 需要正确的工具才能掉落，类似紫水晶晶簇
     * Requires correct tool for drops, similar to amethyst cluster
     */
    public static final Supplier<Block> CRYSTALS_ORANGE_CLUSTER = ModBlocks.BLOCKS.register("crystals_orange_cluster",
            () -> new CustomClusterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)
                    .requiresCorrectToolForDrops(), // 需要正确的工具才能掉落 - Requires correct tool for drops
                    BuddingCrystalsBlock.CrystalColor.ORANGE));

    // ==================== 水晶方块 ====================
    // ==================== Crystal Blocks ====================

    /**
     * 橙色水晶方块
     * Orange crystal block
     * <p>
     * 完整的水晶方块，具有适当的硬度和爆炸抗性
     * Full crystal block with appropriate hardness and explosion resistance
     * <p>
     * 需要正确的工具才能掉落，设置适当的硬度和爆炸抗性
     * Requires correct tool for drops, sets appropriate hardness and explosion resistance
     */
    public static final Supplier<Block> CRYSTALS_ORANGE_BLOCK = ModBlocks.BLOCKS.register("crystals_orange_block",
            () -> new CustomCrystalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                    .requiresCorrectToolForDrops() // 需要正确的工具才能掉落 - Requires correct tool for drops
                    .strength(1.5F) // 设置适当的硬度 - Sets appropriate hardness
                    .explosionResistance(6.0F))); // 设置爆炸抗性 - Sets explosion resistance
}