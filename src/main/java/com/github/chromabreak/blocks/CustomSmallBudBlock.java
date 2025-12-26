package com.github.chromabreak.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义小型水晶芽方块
 * <p>
 * Custom Small Crystal Bud Block
 * <p>
 * 继承自紫水晶簇方块，实现小型水晶芽的功能和生长逻辑
 * Extends AmethystClusterBlock to implement small crystal bud functionality and growth logic
 */
public class CustomSmallBudBlock extends AmethystClusterBlock {
    private final BuddingCrystalsBlock.CrystalColor color;

    public CustomSmallBudBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(3.0F, 3.0F, properties);  // 小芽模型大小（3像素）
        this.color = color;
    }

    @Override
    public void randomTick(final @NotNull BlockState state, final @NotNull ServerLevel level, final @NotNull BlockPos pos, final RandomSource random) {
        if (0 == random.nextInt(10)) { // 10% chance to grow
            // 小芽生长为中芽
            // Small bud grows to medium bud
            final Direction facing = state.getValue(AmethystClusterBlock.FACING);
            final BlockPos attachedPos = pos.relative(facing.getOpposite());

            // 检查附着位置是否是相同颜色的母岩
            // Check if attached position is budding block of same color
            final BlockState attachedState = level.getBlockState(attachedPos);
            if (attachedState.getBlock() instanceof final BuddingCrystalsBlock buddingBlock &&
                buddingBlock.getColor() == this.color) {

                // 生长为中芽
                // Grow to medium bud
                final BlockState mediumBudState = this.getMediumBudForColor(this.color)
                        .setValue(AmethystClusterBlock.FACING, facing)
                        .setValue(AmethystClusterBlock.WATERLOGGED, state.getValue(AmethystClusterBlock.WATERLOGGED));
                level.setBlockAndUpdate(pos, mediumBudState);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(final @NotNull BlockState state) {
        return true;
    }

    private BlockState getMediumBudForColor(final BuddingCrystalsBlock.CrystalColor color) {
        return switch (color) {
            case BLACK -> ModBlocks.MEDIUM_CRYSTALS_BLACK_BUD.get().defaultBlockState();
            case WHITE -> ModBlocks.MEDIUM_CRYSTALS_WHITE_BUD.get().defaultBlockState();
            case YELLOW -> ModBlocks.MEDIUM_CRYSTALS_YELLOW_BUD.get().defaultBlockState();
            case GREEN -> ModBlocks.MEDIUM_CRYSTALS_GREEN_BUD.get().defaultBlockState();
            case BLUE -> ModBlocks.MEDIUM_CRYSTALS_BLUE_BUD.get().defaultBlockState();
            case RED -> ModBlocks.MEDIUM_CRYSTALS_RED_BUD.get().defaultBlockState();
            default -> ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get().defaultBlockState();
        };
    }

    public BuddingCrystalsBlock.CrystalColor getColor() {
        return this.color;
    }
}
