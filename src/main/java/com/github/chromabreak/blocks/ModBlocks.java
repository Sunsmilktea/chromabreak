package com.github.chromabreak.blocks;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组方块注册类
 * <p>
 * Mod Blocks Registration Class
 * <p>
 * 负责注册模组中的所有方块
 * Responsible for registering all blocks in the mod
 */
public enum ModBlocks {
    ;

    /**
     * 方块注册器
     * <p>
     * Block registry
     */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChromaBreak.MODID);

    /**
     * 水晶芽方块（7种颜色变体）
     * <p>
     * Crystal bud blocks (7 color variants)
     * 这些方块在创造模式标签栏不可见，类似紫水晶芽
     * These blocks are not visible in creative mode tab, similar to amethyst buds
     */
    public static final Supplier<Block> SMALL_CRYSTALS_BLACK_BUD = ModBlocks.BLOCKS.register("small_crystals_black_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final Supplier<Block> SMALL_CRYSTALS_WHITE_BUD = ModBlocks.BLOCKS.register("small_crystals_white_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final Supplier<Block> SMALL_CRYSTALS_YELLOW_BUD = ModBlocks.BLOCKS.register("small_crystals_yellow_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final Supplier<Block> SMALL_CRYSTALS_GREEN_BUD = ModBlocks.BLOCKS.register("small_crystals_green_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final Supplier<Block> SMALL_CRYSTALS_BLUE_BUD = ModBlocks.BLOCKS.register("small_crystals_blue_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final Supplier<Block> SMALL_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("small_crystals_orange_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    public static final Supplier<Block> SMALL_CRYSTALS_RED_BUD = ModBlocks.BLOCKS.register("small_crystals_red_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD)));

    /**
     * 橙色水晶生长状态方块
     * <p>
     * Orange crystal growth state blocks
     * 这些方块在创造模式标签栏不可见
     * These blocks are not visible in creative mode tab
     */
    public static final Supplier<Block> MEDIUM_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("medium_crystals_orange_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD)));

    public static final Supplier<Block> LARGE_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("large_crystals_orange_bud",
            () -> new CustomLargeBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD)));

    public static final Supplier<Block> BUDDING_ORANGE_CRYSTALS = ModBlocks.BLOCKS.register("budding_orange_crystals",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST)));

    /**
     * 橙色水晶装饰方块
     * <p>
     * Orange crystal decorative blocks
     * 这些方块将放在装饰方块标签中
     * These blocks will be placed in the decorative blocks tab
     */
    public static final Supplier<Block> CRYSTALS_ORANGE_CLUSTER = ModBlocks.BLOCKS.register("crystals_orange_cluster",
            () -> new CustomClusterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)));

    public static final Supplier<Block> CRYSTALS_ORANGE_BLOCK = ModBlocks.BLOCKS.register("crystals_orange_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));
}
