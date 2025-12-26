package com.github.chromabreak.blocks;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static net.neoforged.neoforge.common.data.SoundDefinition.Sound.sound;

/**
 * 模组方块注册类
 * <p>
 * Mod Blocks Registration Class
 * <p>
 * 负责注册模组中的所有方块
 * Responsible for registering all block in the mod
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
     * Crystal bud block (7 color variants)
     * 这些方块在创造模式标签栏不可见，类似紫水晶芽
     * These block are not visible in creative mode tab, similar to amethyst buds
     */
    public static final Supplier<Block> SMALL_CRYSTALS_BLACK_BUD = ModBlocks.BLOCKS.register("small_crystals_black_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLACK));

    public static final Supplier<Block> SMALL_CRYSTALS_WHITE_BUD = ModBlocks.BLOCKS.register("small_crystals_white_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.WHITE));

    public static final Supplier<Block> SMALL_CRYSTALS_YELLOW_BUD = ModBlocks.BLOCKS.register("small_crystals_yellow_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.YELLOW));

    public static final Supplier<Block> SMALL_CRYSTALS_GREEN_BUD = ModBlocks.BLOCKS.register("small_crystals_green_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.GREEN));

    public static final Supplier<Block> SMALL_CRYSTALS_BLUE_BUD = ModBlocks.BLOCKS.register("small_crystals_blue_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLUE));

    public static final Supplier<Block> SMALL_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("small_crystals_orange_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.ORANGE));

    public static final Supplier<Block> SMALL_CRYSTALS_RED_BUD = ModBlocks.BLOCKS.register("small_crystals_red_bud",
            () -> new CustomSmallBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMALL_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.RED));

    public static final Supplier<Block> MEDIUM_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("medium_crystals_orange_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.ORANGE));

    public static final Supplier<Block> MEDIUM_CRYSTALS_BLACK_BUD = ModBlocks.BLOCKS.register("medium_crystals_black_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLACK));

    public static final Supplier<Block> MEDIUM_CRYSTALS_WHITE_BUD = ModBlocks.BLOCKS.register("medium_crystals_white_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.WHITE));

    public static final Supplier<Block> MEDIUM_CRYSTALS_YELLOW_BUD = ModBlocks.BLOCKS.register("medium_crystals_yellow_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.YELLOW));

    public static final Supplier<Block> MEDIUM_CRYSTALS_GREEN_BUD = ModBlocks.BLOCKS.register("medium_crystals_green_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.GREEN));

    public static final Supplier<Block> MEDIUM_CRYSTALS_BLUE_BUD = ModBlocks.BLOCKS.register("medium_crystals_blue_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.BLUE));

    public static final Supplier<Block> MEDIUM_CRYSTALS_RED_BUD = ModBlocks.BLOCKS.register("medium_crystals_red_bud",
            () -> new CustomMediumBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MEDIUM_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.RED));

    public static final Supplier<Block> LARGE_CRYSTALS_ORANGE_BUD = ModBlocks.BLOCKS.register("large_crystals_orange_bud",
            () -> new CustomLargeBudBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_AMETHYST_BUD), BuddingCrystalsBlock.CrystalColor.ORANGE));

    public static final Supplier<Block> BUDDING_ORANGE_CRYSTALS = ModBlocks.BLOCKS.register("budding_orange_crystals",
            () -> new BuddingCrystalsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BUDDING_AMETHYST),
                    BuddingCrystalsBlock.CrystalColor.ORANGE));

    public static final Supplier<Block> CRYSTALS_ORANGE_CLUSTER = ModBlocks.BLOCKS.register("crystals_orange_cluster",
            () -> new CustomClusterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)
                    .requiresCorrectToolForDrops(), // 需要正确的工具才能掉落
                    BuddingCrystalsBlock.CrystalColor.ORANGE));


    public static final Supplier<Block> CRYSTALS_ORANGE_BLOCK = ModBlocks.BLOCKS.register("crystals_orange_block",
            () -> new CustomCrystalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                    .requiresCorrectToolForDrops() // 需要正确的工具才能掉落
                    .strength(1.5F) // 设置适当的硬度
                    .explosionResistance(6.0F))); // 设置爆炸抗性
}