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
 * CustomSmallBudBlock - 自定义小型水晶芽方块类
 * Custom Small Crystal Bud Block Class
 * <p>
 * 继承自原版紫水晶簇方块，实现小型水晶芽的功能和生长逻辑
 * Extends vanilla AmethystClusterBlock to implement small crystal bud functionality and growth logic
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 继承紫水晶簇特性：继承原版紫水晶簇的所有特性，包括模型、碰撞箱、水浸等
 * Inherit amethyst cluster features: Inherit all features of vanilla amethyst cluster including model, collision box, waterlogging, etc.
 * - 多颜色支持：支持7种不同颜色的小型水晶芽
 * Multi-color support: Supports 7 different colors of small crystal buds
 * - 生长逻辑：实现小芽生长为中芽的随机tick机制
 * Growth logic: Implements random tick mechanism for small bud growing to medium bud
 * - 附着检查：检查是否附着在相同颜色的母岩上才能生长
 * Attachment check: Checks if attached to budding block of same color to allow growth
 * <p>
 * 生长机制：
 * Growth mechanism:
 * - 每个游戏刻有10%的几率尝试生长
 * Each game tick has 10% chance to attempt growth
 * - 检查附着位置是否是相同颜色的母岩
 * Checks if attached position is budding block of same color
 * - 如果条件满足，小芽生长为中芽
 * If conditions are met, small bud grows to medium bud
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
 * - 水晶生长系统：作为水晶生长系统的第一阶段（小芽→中芽→大芽→晶簇）
 * Crystal growth system: As the first stage of crystal growth system (small bud → medium bud → large bud → cluster)
 * - 装饰用途：小型水晶芽用于建筑装饰
 * Decorative use: Small crystal buds for building decoration
 * - 资源获取：玩家可以采集小型水晶芽获得水晶碎片
 * Resource acquisition: Players can harvest small crystal buds to obtain crystal shards
 */
public class CustomSmallBudBlock extends AmethystClusterBlock {

    /**
     * 水晶颜色
     * Crystal color
     * <p>
     * 决定水晶芽的颜色和生长逻辑
     * Determines the color of the crystal bud and growth logic
     */
    private final BuddingCrystalsBlock.CrystalColor color;

    /**
     * 构造函数
     * Constructor
     *
     * @param properties 方块属性，包括硬度、抗爆性、声音、光照等
     *                   Block properties including hardness, blast resistance, sounds, light emission, etc.
     * @param color      水晶颜色，决定水晶芽的外观和生长行为
     *                   Crystal color, determines the appearance and growth behavior of the crystal bud
     *                   <p>
     *                   示例：创建橙色小型水晶芽
     *                   Example: Create orange small crystal bud
     *                   new CustomSmallBudBlock(Properties.of().strength(1.5f), CrystalColor.ORANGE)
     *                   <p>
     *                   模型大小：使用小芽模型大小（3像素），与紫水晶小芽保持一致
     *                   Model size: Uses small bud model size (3 pixels), consistent with amethyst small bud
     */
    public CustomSmallBudBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(3.0F, 3.0F, properties);  // 小芽模型大小（3像素）
        this.color = color;
    }

    /**
     * 随机Tick方法，用于实现生长逻辑
     * Random tick method for implementing growth logic
     * <p>
     * 每个游戏刻调用，检查小芽是否有生长为中芽的机会
     * Called each game tick, checks if small bud has opportunity to grow to medium bud
     *
     * @param state  当前方块状态
     *               Current block state
     * @param level  服务器级别，用于修改方块状态
     *               Server level, used to modify block states
     * @param pos    小芽位置
     *               Small bud position
     * @param random 随机数生成器，用于决定生长几率
     *               Random number generator, used to determine growth probability
     *               <p>
     *               处理逻辑：
     *               Processing logic:
     *               1. 检查是否有10%的生长几率
     *               Check if there's 10% growth chance
     *               2. 获取小芽的朝向和附着位置
     *               Get small bud's facing direction and attached position
     *               3. 检查附着位置是否是相同颜色的母岩
     *               Check if attached position is budding block of same color
     *               4. 如果条件满足，将小芽替换为中芽
     *               If conditions are met, replace small bud with medium bud
     *               <p>
     *               生长条件：必须附着在相同颜色的母岩上才能生长
     *               Growth condition: Must be attached to budding block of same color to grow
     */
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

    /**
     * 检查是否可以进行随机tick
     * Check if random tick can occur
     * <p>
     * 返回此方块是否支持随机tick，用于生长逻辑
     * Returns whether this block supports random tick for growth logic
     *
     * @param state 方块状态
     *              Block state
     * @return 总是返回true，表示此方块支持随机tick
     * Always returns true, indicating this block supports random tick
     * <p>
     * 使用场景：Minecraft引擎调用此方法决定是否为此方块调用randomTick方法
     * Usage scenario: Minecraft engine calls this method to decide whether to call randomTick method for this block
     */
    @Override
    public boolean isRandomlyTicking(final @NotNull BlockState state) {
        return true;
    }

    /**
     * 根据颜色获取中芽方块状态
     * Get medium bud block state for color
     * <p>
     * 根据水晶颜色返回对应的中芽方块状态
     * Returns corresponding medium bud block state based on crystal color
     *
     * @param color 水晶颜色
     *              Crystal color
     * @return 对应颜色的中芽方块状态
     * Medium bud block state of corresponding color
     * <p>
     * 颜色映射：
     * Color mapping:
     * - BLACK → 黑色中芽
     * BLACK → Black medium bud
     * - WHITE → 白色中芽
     * WHITE → White medium bud
     * - YELLOW → 黄色中芽
     * YELLOW → Yellow medium bud
     * - GREEN → 绿色中芽
     * GREEN → Green medium bud
     * - BLUE → 蓝色中芽
     * BLUE → Blue medium bud
     * - RED → 红色中芽
     * RED → Red medium bud
     * - ORANGE → 橙色中芽
     * ORANGE → Orange medium bud
     * <p>
     * 默认值：如果颜色未定义，返回橙色中芽
     * Default: If color is undefined, returns orange medium bud
     */
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

    /**
     * 获取水晶颜色
     * Get crystal color
     * <p>
     * 返回此水晶芽的颜色
     * Returns the color of this crystal bud
     *
     * @return 水晶颜色枚举值
     * Crystal color enum value
     * <p>
     * 使用场景：用于获取水晶芽的颜色信息，例如在渲染或逻辑判断时
     * Usage scenario: Used to get color information of crystal bud, e.g., during rendering or logic judgment
     */
    public BuddingCrystalsBlock.CrystalColor getColor() {
        return this.color;
    }
}
