package com.github.chromabreak.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * 通用水晶母岩方块
 * Generic Budding Crystals Block
 * <p>
 * 实现多种颜色水晶在母岩上生长的功能
 * Implements crystal growth functionality for multiple colors on budding block
 */
public class BuddingCrystalsBlock extends Block {

    private final CrystalColor color;

    /**
     * 构造函数
     * Constructor
     *
     * @param properties 方块属性 Block properties
     * @param color      水晶颜色 Crystal color
     */
    public BuddingCrystalsBlock(final Properties properties, final CrystalColor color) {
        super(properties);
        this.color = color;
    }

    /**
     * 随机Tick方法，用于实现生长逻辑
     * Random tick method for implementing growth logic
     *
     * @param state  当前方块状态 Current block state
     * @param level  服务器级别 Server level
     * @param pos    方块位置 Block position
     * @param random 随机源 Random source
     */
    @Override
    public void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random) {
        // 模拟原版紫水晶母岩生长机制：每个表面有20%几率尝试生长
        // Simulate vanilla budding amethyst growth: each face has 20% chance to attempt growth
        for (final Direction direction : Direction.values()) {
            if (random.nextInt(10) == 0) { // 10% chance per face
                final BlockPos growthPos = pos.relative(direction);
                final BlockState growthBlockState = level.getBlockState(growthPos);
                
                // 检查生长位置是否适合生长（空气或水）
                // Check if growth position is suitable (air or water)
                if (this.canGrowthOccur(growthBlockState)) {
                    // 根据当前位置的芽状态决定生长阶段
                    // Determine growth stage based on current bud state at this position
                    this.growCrystal(level, growthPos, direction, growthBlockState);
                }
            }
        }
    }

    /**
     * 检查是否可以生长
     * Check if growth can occur
     *
     * @param blockState 目标位置方块状态 Target position block state
     * @return 是否可以生长 Whether growth can occur
     */
    private boolean canGrowthOccur(final BlockState blockState) {
        // 只能在空气或水中生长
        // Can only grow in air or water
        return blockState.isAir() || blockState.getFluidState().is(Fluids.WATER);
    }

    /**
     * 生长水晶
     * Grow crystal
     *
     * @param level        服务器级别 Server level
     * @param pos          生长位置 Growth position
     * @param direction    生长方向 Growth direction
     * @param currentState 当前方块状态 Current block state
     */
    private void growCrystal(final ServerLevel level, final BlockPos pos, final Direction direction, final BlockState currentState) {
        // 根据生长位置周围的方块决定生长阶段
        // Determine growth stage based on surrounding block
        final BlockState newState = this.determineGrowthStage(level, pos, direction, currentState);

        if (null != newState) {
            // 放置新的水晶方块
            // Place new crystal block
            level.setBlockAndUpdate(pos, newState);
        }
    }

    /**
     * 确定生长阶段
     * Determine growth stage
     *
     * @param level     服务器级别 Server level
     * @param pos       生长位置 Growth position
     * @param direction 生长方向 Growth direction
     * @param currentState 当前位置的方块状态 Current block state at the position
     * @return 新的方块状态 New block state
     */
    private BlockState determineGrowthStage(final ServerLevel level, final BlockPos pos, final Direction direction, final BlockState currentState) {
        // 基于当前位置的方块状态决定生长阶段（模拟原版紫水晶母岩行为）
        // Determine growth stage based on current block state (simulate vanilla budding amethyst behavior)
        BlockState newState = null;
        final boolean isWaterlogged = currentState.getFluidState().is(Fluids.WATER);
        
        // 检查当前位置的方块类型
        final Block currentBlock = currentState.getBlock();

        switch (this.color) {
            case ORANGE:
                if (currentState.isAir() || currentState.getFluidState().is(Fluids.WATER)) {
                    // 空气或水 → 生长小芽
                    // Air or water → grow small bud
                    newState = ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get().defaultBlockState();
                } else if (currentBlock == ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get()) {
                    // 小芽 → 生长中芽
                    // Small bud → grow medium bud
                    newState = ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get().defaultBlockState();
                } else if (currentBlock == ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get()) {
                    // 中芽 → 生长大芽
                    // Medium bud → grow large bud
                    newState = ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get().defaultBlockState();
                } else if (currentBlock == ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get()) {
                    // 大芽 → 生长簇
                    // Large bud → grow cluster
                    newState = ModBlocks.CRYSTALS_ORANGE_CLUSTER.get().defaultBlockState();
                }
                break;

            // 其他颜色目前只有小芽
            // Other colors currently only have small buds
            case BLACK:
            case WHITE:
            case YELLOW:
            case GREEN:
            case BLUE:
            case RED:
                if (currentState.isAir() || currentState.getFluidState().is(Fluids.WATER)) {
                    // 空气或水 → 生长小芽
                    // Air or water → grow small bud
                    newState = this.getSmallBudForColor();
                }
                break;
        }

        if (null != newState) {
            return newState
                    .setValue(AmethystClusterBlock.FACING, direction)
                    .setValue(AmethystClusterBlock.WATERLOGGED, isWaterlogged);
        }

        return null;
    }

    /**
     * 根据颜色获取小芽方块
     * Get small bud block for color
     *
     * @return 小芽方块状态 Small bud block state
     */
    private BlockState getSmallBudForColor() {
        switch (this.color) {
            case BLACK:
                return ModBlocks.SMALL_CRYSTALS_BLACK_BUD.get().defaultBlockState();
            case WHITE:
                return ModBlocks.SMALL_CRYSTALS_WHITE_BUD.get().defaultBlockState();
            case YELLOW:
                return ModBlocks.SMALL_CRYSTALS_YELLOW_BUD.get().defaultBlockState();
            case GREEN:
                return ModBlocks.SMALL_CRYSTALS_GREEN_BUD.get().defaultBlockState();
            case BLUE:
                return ModBlocks.SMALL_CRYSTALS_BLUE_BUD.get().defaultBlockState();
            case RED:
                return ModBlocks.SMALL_CRYSTALS_RED_BUD.get().defaultBlockState();
            default:
                return ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get().defaultBlockState();
        }
    }

    /**
     * 检查是否可以进行随机tick
     * Check if random tick can occur
     *
     * @param state 方块状态 Block state
     * @return 是否可以进行随机tick Whether random tick can occur
     */
    @Override
    public boolean isRandomlyTicking(final BlockState state) {
        return true;
    }

    /**
     * 获取水晶颜色
     * Get crystal color
     *
     * @return 水晶颜色 Crystal color
     */
    public CrystalColor getColor() {
        return this.color;
    }

    /**
     * 水晶颜色枚举
     * Crystal Color Enum
     */
    public enum CrystalColor {
        ORANGE,
        BLACK,
        WHITE,
        YELLOW,
        GREEN,
        BLUE,
        RED
    }
}
