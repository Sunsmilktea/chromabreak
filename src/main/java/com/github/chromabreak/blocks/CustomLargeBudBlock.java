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
 * CustomLargeBudBlock - 自定义大型水晶芽方块类
 * Custom Large Crystal Bud Block Class
 * <p>
 * 继承自原版紫水晶簇方块，实现大型水晶芽的功能和生长逻辑
 * Extends vanilla AmethystClusterBlock to implement large crystal bud functionality and growth logic
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 继承紫水晶簇特性：继承原版紫水晶簇的所有特性，包括模型、碰撞箱、水浸等
 * Inherit amethyst cluster features: Inherit all features of vanilla amethyst cluster including model, collision box, waterlogging, etc.
 * - 多颜色支持：支持7种不同颜色的大型水晶
 * Multi-color support: Supports 7 different colors of large crystal buds
 * - 生长逻辑：实现大水晶生长为晶簇的随机tick机制
 * Growth logic: Implements random tick mechanism for large bud growing to cluster
 * - 附着检查：检查是否附着在相同颜色的母岩上才能生长
 * Attachment check: Checks if attached to budding block of same color to allow growth
 * <p>
 * 生长机制：
 * Growth mechanism:
 * - 每个游戏刻有10%的几率尝试生长
 * Each game tick has 10% chance to attempt growth
 * - 检查附着位置是否是相同颜色的母岩
 * Checks if attached position is budding block of same color
 * - 如果条件满足，大水晶生长为晶簇
 * If conditions are met, large bud grows to cluster
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
 * - 水晶生长系统：作为水晶生长系统的第三阶段（小水晶→中水晶→大水晶→晶簇）
 * Crystal growth system: As the third stage of crystal growth system (small bud → medium bud → large bud → cluster)
 * - 装饰用途：大型水晶用于建筑装饰
 * Decorative use: Large crystal buds for building decoration
 * - 资源获取：玩家可以采集大型水晶获得水晶碎片
 * Resource acquisition: Players can harvest large crystal buds to obtain crystal shards
 * <p>
 * 水晶生长阶段：
 * Crystal growth stages:
 * - 小水晶 (Small Bud)：3像素大小，生长为中水晶
 * Small Bud: 3 pixels size, grows to medium bud
 * - 中水晶 (Medium Bud)：4像素大小，生长为大水晶
 * Medium Bud: 4 pixels size, grows to large bud
 * - 大水晶 (Large Bud)：5像素大小，生长为晶簇
 * Large Bud: 5 pixels size, grows to cluster
 * - 晶簇 (Cluster)：7像素大小，最终形态
 * Cluster: 7 pixels size, final form
 * <p>
 * 最终形态：晶簇是水晶生长系统的最终阶段，可以产出水晶碎片
 * Final form: Cluster is the final stage of crystal growth system, can produce crystal shards
 */
public class CustomLargeBudBlock extends AmethystClusterBlock {
    private final BuddingCrystalsBlock.CrystalColor color;

    public CustomLargeBudBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(5.0F, 3.0F, properties);  // 大水晶模型大小（5像素）
        this.color = color;
    }

    @Override
    public void randomTick(final @NotNull BlockState state, final @NotNull ServerLevel level, final @NotNull BlockPos pos, final RandomSource random) {
        if (0 == random.nextInt(10)) { // 10% chance to grow
            // 大水晶生长为簇
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
