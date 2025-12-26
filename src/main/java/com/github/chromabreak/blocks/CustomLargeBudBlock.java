package com.github.chromabreak.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义大型水晶芽方块
 * <p>
 * Custom Large Crystal Bud Block
 * <p>
 * 继承自紫水晶簇方块，实现大型水晶芽的功能和生长逻辑
 * Extends AmethystClusterBlock to implement large crystal bud functionality and growth logic
 */
public class CustomLargeBudBlock extends AmethystClusterBlock {
    private final BuddingCrystalsBlock.CrystalColor color;

    public CustomLargeBudBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(5.0F, 3.0F, properties);  // 大芽模型大小（5像素）
        this.color = color;
    }

    @Override
    public void randomTick(final @NotNull BlockState state, final @NotNull ServerLevel level, final @NotNull BlockPos pos, final RandomSource random) {
        if (0 == random.nextInt(10)) { // 10% chance to grow
            // 大芽生长为簇
            // Large bud grows to cluster
            final Direction facing = state.getValue(AmethystClusterBlock.FACING);
            final BlockPos attachedPos = pos.relative(facing.getOpposite());

            // 检查附着位置是否是相同颜色的母岩
            // Check if attached position is budding block of same color
            final BlockState attachedState = level.getBlockState(attachedPos);
            if (attachedState.getBlock() instanceof final BuddingCrystalsBlock buddingBlock &&
                buddingBlock.getColor() == this.color) {

                // 生长为簇
                // Grow to cluster
                final BlockState clusterState = this.getClusterForColor(this.color)
                        .setValue(AmethystClusterBlock.FACING, facing)
                        .setValue(AmethystClusterBlock.WATERLOGGED, state.getValue(AmethystClusterBlock.WATERLOGGED));
                level.setBlockAndUpdate(pos, clusterState);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(final @NotNull BlockState state) {
        return true;
    }

    private BlockState getClusterForColor(final BuddingCrystalsBlock.CrystalColor color) {
        return ModBlocks.CRYSTALS_ORANGE_CLUSTER.get().defaultBlockState();
    }

    public BuddingCrystalsBlock.CrystalColor getColor() {
        return this.color;
    }
}
