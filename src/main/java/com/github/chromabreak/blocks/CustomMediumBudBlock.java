package com.github.chromabreak.blocks;

import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * CustomMediumBudBlock - 自定义中型水晶芽方块类
 * Custom Medium Crystal Bud Block Class
 * <p>
 * 继承自原版紫水晶簇方块，实现中型水晶芽的功能和生长逻辑
 * Extends vanilla AmethystClusterBlock to implement medium crystal bud functionality and growth logic
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 继承紫水晶簇特性：继承原版紫水晶簇的所有特性，包括模型、碰撞箱、水浸等
 * Inherit amethyst cluster features: Inherit all features of vanilla amethyst cluster including model, collision box, waterlogging, etc.
 * - 多颜色支持：支持7种不同颜色的中型水晶芽
 * Multi-color support: Supports 7 different colors of medium crystal buds
 * - 生长逻辑：实现中芽生长为大芽的随机tick机制
 * Growth logic: Implements random tick mechanism for medium bud growing to large bud
 * - 附着检查：检查是否附着在相同颜色的母岩上才能生长
 * Attachment check: Checks if attached to budding block of same color to allow growth
 * <p>
 * 生长机制：
 * Growth mechanism:
 * - 每个游戏刻有10%的几率尝试生长
 * Each game tick has 10% chance to attempt growth
 * - 检查附着位置是否是相同颜色的母岩
 * Checks if attached position is budding block of same color
 * - 如果条件满足，中芽生长为大芽
 * If conditions are met, medium bud grows to large bud
 * - 保持原有的朝向和水浸状态
 * Maintains original facing direction and waterlogged state
 * <p>
 * 设计特点：
 * Design features:
 * - 继承设计：通过继承AmethystClusterBlock复用原版紫水晶簇的所有功能
 * Inheritance design: Reuses all functionality of vanilla amethyst cluster by extending AmethystClusterBlock
 * - 模块化颜色支持：通过CrystalColor枚举支持多种颜色
 * Modular color support: Supports multiple colors through CrystalColor enum
 * - 生长条件检查：确保只有在正确条件下才能生长
 * Growth condition check: Ensures growth only occurs under correct conditions
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 水晶生长系统：作为水晶生长系统的第二阶段（小芽→中芽→大芽→晶簇）
 * Crystal growth system: As the second stage of crystal growth system (small bud → medium bud → large bud → cluster)
 * - 装饰用途：中型水晶芽用于建筑装饰
 * Decorative use: Medium crystal buds for building decoration
 * - 资源获取：玩家可以采集中型水晶芽获得水晶碎片
 * Resource acquisition: Players can harvest medium crystal buds to obtain crystal shards
 * <p>
 * 水晶生长阶段：
 * Crystal growth stages:
 * - 小芽 (Small Bud)：3像素大小，生长为中芽
 * Small Bud: 3 pixels size, grows to medium bud
 * - 中芽 (Medium Bud)：4像素大小，生长为大芽
 * Medium Bud: 4 pixels size, grows to large bud
 * - 大芽 (Large Bud)：5像素大小，生长为晶簇
 * Large Bud: 5 pixels size, grows to cluster
 * - 晶簇 (Cluster)：7像素大小，最终形态
 * Cluster: 7 pixels size, final form
 */
public class CustomMediumBudBlock extends AmethystClusterBlock {
    private final BuddingCrystalsBlock.CrystalColor color;

    public CustomMediumBudBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(4.0F, 3.0F, properties);  // 中芽模型大小（4像素）
        this.color = color;
    }

    @Override
    public void randomTick(final @NotNull BlockState state, final @NotNull ServerLevel level, final @NotNull BlockPos pos, final RandomSource random) {
        if (0 == random.nextInt(10)) { // 10% chance to grow
            // 中芽生长为大芽
            // Medium bud grows to large bud
            final Direction facing = state.getValue(AmethystClusterBlock.FACING);
            final BlockPos attachedPos = pos.relative(facing.getOpposite());

            // 检查附着位置是否是相同颜色的母岩
            // Check if attached position is budding block of same color
            final BlockState attachedState = level.getBlockState(attachedPos);
            if (attachedState.getBlock() instanceof final BuddingCrystalsBlock buddingBlock &&
                    buddingBlock.getColor() == this.color) {

                // 生长为大芽
                // Grow to large bud
                final BlockState largeBudState = this.getLargeBudForColor(this.color)
                        .setValue(AmethystClusterBlock.FACING, facing)
                        .setValue(AmethystClusterBlock.WATERLOGGED, state.getValue(AmethystClusterBlock.WATERLOGGED));
                level.setBlockAndUpdate(pos, largeBudState);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(final @NotNull BlockState state) {
        return true;
    }

    private BlockState getLargeBudForColor(final BuddingCrystalsBlock.CrystalColor color) {
        return ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get().defaultBlockState();
    }

    public BuddingCrystalsBlock.CrystalColor getColor() {
        return this.color;
    }
}
