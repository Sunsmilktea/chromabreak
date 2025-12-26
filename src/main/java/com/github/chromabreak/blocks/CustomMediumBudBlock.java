package com.github.chromabreak.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义中型水晶芽方块
 * <p>
 * Custom Medium Crystal Bud Block
 * <p>
 * 继承自紫水晶簇方块，实现中型水晶芽的功能和生长逻辑
 * Extends AmethystClusterBlock to implement medium crystal bud functionality and growth logic
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
