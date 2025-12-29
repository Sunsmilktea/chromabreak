package com.github.chromabreak.blocks;

import com.github.chromabreak.util.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AmethystClusterBlock;

/**
 * CustomClusterBlock - 自定义水晶簇方块类
 * Custom Crystal Cluster Block Class
 * <p>
 * 继承自原版紫水晶簇方块，实现多种颜色水晶簇的功能和掉落物逻辑
 * Extends vanilla AmethystClusterBlock to implement multi-color crystal cluster functionality and drop logic
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 继承紫水晶簇特性：继承原版紫水晶簇的所有特性，包括模型、碰撞箱、水浸等
 * Inherit amethyst cluster features: Inherit all features of vanilla amethyst cluster including model, collision box, waterlogging, etc.
 * - 多颜色支持：支持7种不同颜色的水晶簇
 * Multi-color support: Supports 7 different colors of crystal clusters
 * - 掉落物逻辑：根据水晶颜色掉落对应的水晶碎片
 * Drop logic: Drops corresponding crystal shards based on crystal color
 * - 数据驱动：使用数据驱动的掉落物表，不重写getDrops方法
 * Data-driven: Uses data-driven loot tables, doesn't override getDrops method
 * <p>
 * 设计特点：
 * Design features:
 * - 继承设计：通过继承AmethystClusterBlock复用原版紫水晶簇的所有功能
 * Inheritance design: Reuses all functionality of vanilla amethyst cluster by extending AmethystClusterBlock
 * - 模块化颜色支持：通过CrystalColor枚举支持多种颜色
 * Modular color support: Supports multiple colors through CrystalColor enum
 * - 数据驱动掉落：使用Minecraft的数据驱动系统管理掉落物
 * Data-driven drops: Uses Minecraft's data-driven system to manage drops
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 水晶生长系统：作为水晶生长系统的最终阶段（小芽→中芽→大芽→晶簇）
 * Crystal growth system: As the final stage of crystal growth system (small bud → medium bud → large bud → cluster)
 * - 装饰用途：多彩水晶簇用于建筑装饰
 * Decorative use: Multi-color crystal clusters for building decoration
 * - 资源获取：玩家可以采集水晶簇获得水晶碎片
 * Resource acquisition: Players can harvest crystal clusters to obtain crystal shards
 */
public class CustomClusterBlock extends AmethystClusterBlock {

    /**
     * 水晶颜色
     * Crystal color
     * <p>
     * 决定水晶簇的颜色和掉落的碎片类型
     * Determines the color of the crystal cluster and the type of shards to drop
     */
    private final BuddingCrystalsBlock.CrystalColor color;

    /**
     * 构造函数
     * Constructor
     *
     * @param properties 方块属性，包括硬度、抗爆性、声音、光照等
     *                   Block properties including hardness, blast resistance, sounds, light emission, etc.
     * @param color      水晶颜色，决定水晶簇的外观和掉落物
     *                   Crystal color, determines the appearance and drops of the crystal cluster
     *                   <p>
     *                   示例：创建橙色水晶簇
     *                   Example: Create orange crystal cluster
     *                   new CustomClusterBlock(Properties.of().strength(1.5f), CrystalColor.ORANGE)
     *                   <p>
     *                   模型大小：使用完整簇模型大小（7像素），与紫水晶簇保持一致
     *                   Model size: Uses full cluster model size (7 pixels), consistent with amethyst cluster
     */
    public CustomClusterBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(7.0F, 3.0F, properties);  // 完整簇模型大小（7像素）
        this.color = color;
    }

    // 使用数据驱动的掉落物表，不重写getDrops方法
    // Uses data-driven loot tables, doesn't override getDrops method

    /**
     * 根据颜色获取对应的水晶碎片
     * Get corresponding crystal shard for color
     * <p>
     * 根据水晶颜色返回对应的水晶碎片物品堆栈
     * Returns corresponding crystal shard item stack based on crystal color
     *
     * @return 对应颜色的水晶碎片物品堆栈
     * Crystal shard item stack of corresponding color
     * <p>
     * 颜色映射：
     * Color mapping:
     * - BLACK → 黑色水晶碎片
     * BLACK → Black crystal shard
     * - WHITE → 白色水晶碎片
     * WHITE → White crystal shard
     * - YELLOW → 黄色水晶碎片
     * YELLOW → Yellow crystal shard
     * - GREEN → 绿色水晶碎片
     * GREEN → Green crystal shard
     * - BLUE → 蓝色水晶碎片
     * BLUE → Blue crystal shard
     * - ORANGE → 橙色水晶碎片
     * ORANGE → Orange crystal shard
     * - RED → 红色水晶碎片
     * RED → Red crystal shard
     * <p>
     * 默认值：如果颜色未定义，返回橙色水晶碎片
     * Default: If color is undefined, returns orange crystal shard
     */
    private ItemStack getCrystalShardForColor() {
        return switch (this.color) {
            case BLACK -> new ItemStack(ModItems.BLACK_CRYSTAL_SHARD.get());
            case WHITE -> new ItemStack(ModItems.WHITE_CRYSTAL_SHARD.get());
            case YELLOW -> new ItemStack(ModItems.YELLOW_CRYSTAL_SHARD.get());
            case GREEN -> new ItemStack(ModItems.GREEN_CRYSTAL_SHARD.get());
            case BLUE -> new ItemStack(ModItems.BLUE_CRYSTAL_SHARD.get());
            case ORANGE -> new ItemStack(ModItems.ORANGE_CRYSTAL_SHARD.get());
            case RED -> new ItemStack(ModItems.RED_CRYSTAL_SHARD.get());
            default -> new ItemStack(ModItems.ORANGE_CRYSTAL_SHARD.get());
        };
    }

    /**
     * 获取水晶颜色
     * Get crystal color
     * <p>
     * 返回此水晶簇的颜色
     * Returns the color of this crystal cluster
     *
     * @return 水晶颜色枚举值
     * Crystal color enum value
     * <p>
     * 使用场景：用于获取水晶簇的颜色信息，例如在渲染或逻辑判断时
     * Usage scenario: Used to get color information of crystal cluster, e.g., during rendering or logic judgment
     */
    public BuddingCrystalsBlock.CrystalColor getColor() {
        return this.color;
    }
}
