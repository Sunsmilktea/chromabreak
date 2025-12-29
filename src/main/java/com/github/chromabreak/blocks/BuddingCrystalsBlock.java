package com.github.chromabreak.blocks;

import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

/**
 * BuddingCrystalsBlock - 通用水晶母岩方块类
 * Generic Budding Crystals Block Class
 * <p>
 * 实现多种颜色水晶在母岩上生长的功能，模拟原版紫水晶母岩的生长机制
 * Implements crystal growth functionality for multiple colors on budding block,
 * simulating vanilla budding amethyst growth mechanism
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 随机生长机制：每个游戏刻有几率在母岩的各个方向尝试生长水晶
 * Random growth mechanism: Each game tick has a chance to attempt crystal growth in all directions of the budding block
 * - 多阶段生长：支持小芽、中芽、大芽、晶簇四个生长阶段
 * Multi-stage growth: Supports four growth stages: small bud, medium bud, large bud, cluster
 * - 多颜色支持：支持7种不同颜色的水晶生长
 * Multi-color support: Supports crystal growth in 7 different colors
 * - 水浸处理：支持在水中生长水晶（水浸方块）
 * Waterlogging handling: Supports crystal growth in water (waterlogged blocks)
 * - 生长方向控制：水晶根据生长方向正确放置
 * Growth direction control: Crystals are correctly placed based on growth direction
 * <p>
 * 生长机制：
 * Growth mechanism:
 * - 每个游戏刻，母岩的6个方向（上下左右前后）都有10%的几率尝试生长
 * Each game tick, all 6 directions of the budding block have 10% chance to attempt growth
 * - 生长位置必须是空气或水
 * Growth position must be air or water
 * - 生长阶段根据当前位置的方块状态决定
 * Growth stage is determined by the current block state at the position
 * - 橙色水晶支持完整生长阶段（小芽→中芽→大芽→晶簇）
 * Orange crystals support full growth stages (small bud → medium bud → large bud → cluster)
 * - 其他颜色目前只支持小芽阶段
 * Other colors currently only support small bud stage
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 水晶地质生成：在世界生成时放置水晶母岩
 * Crystal geode generation: Place budding crystals during world generation
 * - 水晶农场：玩家可以建造水晶农场自动获取水晶碎片
 * Crystal farms: Players can build crystal farms to automatically obtain crystal shards
 * - 装饰用途：多彩水晶用于建筑装饰
 * Decorative use: Multi-color crystals for building decoration
 * <p>
 * 设计特点：
 * Design features:
 * - 模拟原版紫水晶母岩行为，确保玩家体验一致
 * Simulates vanilla budding amethyst behavior to ensure consistent player experience
 * - 支持多种颜色，扩展了原版紫水晶的功能
 * Supports multiple colors, extending vanilla amethyst functionality
 * - 模块化设计，易于添加新的水晶颜色
 * Modular design, easy to add new crystal colors
 */
public class BuddingCrystalsBlock extends Block {

    /**
     * 水晶颜色
     * Crystal color
     * <p>
     * 决定母岩生长的水晶颜色和生长阶段
     * Determines the crystal color and growth stages for the budding block
     */
    private final CrystalColor color;

    /**
     * 构造函数
     * Constructor
     *
     * @param properties 方块属性，包括硬度、抗爆性、声音等
     *                   Block properties including hardness, blast resistance, sounds, etc.
     * @param color      水晶颜色，决定生长的水晶类型和颜色
     *                   Crystal color, determines the type and color of crystals to grow
     *                   <p>
     *                   示例：创建橙色水晶母岩
     *                   Example: Create orange crystal budding block
     *                   new BuddingCrystalsBlock(Properties.of().strength(1.5f), CrystalColor.ORANGE)
     */
    public BuddingCrystalsBlock(final Properties properties, final CrystalColor color) {
        super(properties);
        this.color = color;
    }

    /**
     * 随机Tick方法，用于实现生长逻辑
     * Random tick method for implementing growth logic
     * <p>
     * 每个游戏刻调用，检查母岩的各个方向是否有生长机会
     * Called each game tick, checks each direction of the budding block for growth opportunities
     *
     * @param state  当前方块状态
     *               Current block state
     * @param level  服务器级别，用于修改方块状态
     *               Server level, used to modify block states
     * @param pos    母岩位置
     *               Budding block position
     * @param random 随机数生成器，用于决定生长几率
     *               Random number generator, used to determine growth probability
     *               <p>
     *               处理逻辑：
     *               Processing logic:
     *               1. 遍历6个方向（上下左右前后）
     *               Iterate through 6 directions (up, down, north, south, east, west)
     *               2. 每个方向有10%的几率尝试生长
     *               Each direction has 10% chance to attempt growth
     *               3. 检查生长位置是否适合生长（空气或水）
     *               Check if growth position is suitable (air or water)
     *               4. 如果适合生长，调用生长逻辑
     *               If suitable for growth, call growth logic
     *               <p>
     *               生长几率：每个方向10%，每个游戏刻平均有60%的几率至少有一个方向尝试生长
     *               Growth probability: 10% per direction, average 60% chance per game tick for at least one direction to attempt growth
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
            if (0 == random.nextInt(10)) { // 10% chance per face
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
     * @param level        服务器级别 Server level
     * @param pos          生长位置 Growth position
     * @param direction    生长方向 Growth direction
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
