package com.github.chromabreak.blocks;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.system.CrystalColorVariant;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组方块注册类
 * <p>
 * Mod Blocks Registration Class
 * <p>
 * 负责注册模组中的所有方块，使用纹理着色系统支持7种颜色的水晶方块
 * Responsible for registering all blocks in the mod, using texture tinting system for 7 colors of crystal blocks
 */
public class ModBlocks {
    /**
     * 方块注册器
     * <p>
     * Block registry
     */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ChromaBreak.MODID);

    /**
     * 颜色属性，用于方块状态
     * <p>
     * Color property for block states
     */
    public static final EnumProperty<CrystalColorVariant> COLOR = EnumProperty.create("color", CrystalColorVariant.class);

    /**
     * 水晶块（完整方块，支持7种颜色变体）
     * <p>
     * Crystal block (full block, supports 7 color variants)
     */
    public static final Supplier<Block> CRYSTAL_BLOCK = BLOCKS.register("crystal_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)) {
                // 方块状态将包含颜色属性
            });

    /**
     * 水晶簇（类似紫水晶簇，支持7种颜色变体）
     * <p>
     * Crystal cluster (similar to amethyst cluster, supports 7 color variants)
     */
    public static final Supplier<Block> CRYSTAL_CLUSTER = BLOCKS.register("crystal_cluster",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_CLUSTER)) {
                // 方块状态将包含颜色属性
            });
}
