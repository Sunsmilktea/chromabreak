package com.github.chromabreak.blocks;

import net.minecraft.world.level.block.Block;

/**
 * CustomCrystalBlock - 自定义水晶方块类
 * Custom Crystal Block Class
 * <p>
 * 继承自原版Block类，实现多种颜色水晶方块的基本功能
 * Extends vanilla Block class to implement basic functionality for multi-color crystal blocks
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 基础方块功能：继承原版Block的所有基础功能
 * Basic block functionality: Inherits all basic functionality of vanilla Block
 * - 多颜色支持：支持7种不同颜色的水晶方块
 * Multi-color support: Supports 7 different colors of crystal blocks
 * - 数据驱动：使用数据驱动的掉落物表，不重写getDrops方法
 * Data-driven: Uses data-driven loot tables, doesn't override getDrops method
 * <p>
 * 设计特点：
 * Design features:
 * - 简单继承：通过继承Block类复用原版方块的所有功能
 * Simple inheritance: Reuses all functionality of vanilla block by extending Block class
 * - 数据驱动：遵循Minecraft的数据驱动原则，使用JSON文件配置掉落物
 * Data-driven: Follows Minecraft's data-driven principle, uses JSON files to configure drops
 * - 模块化：作为水晶方块的基础类，支持多种颜色变体
 * Modular: Serves as base class for crystal blocks, supports multiple color variants
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 水晶方块：作为完整的水晶方块，用于建筑和装饰
 * Crystal blocks: As complete crystal blocks for building and decoration
 * - 资源存储：玩家可以存储和展示水晶碎片
 * Resource storage: Players can store and display crystal shards
 * - 合成材料：作为合成其他物品的材料
 * Crafting material: As material for crafting other items
 * <p>
 * 注意：掉落物通过数据驱动的掉落物表配置，不在此类中重写getDrops方法
 * Note: Drops are configured through data-driven loot tables, getDrops method is not overridden in this class
 */
public class CustomCrystalBlock extends Block {

    /**
     * 构造函数
     * Constructor
     *
     * @param properties 方块属性，包括硬度、抗爆性、声音、光照等
     *                   Block properties including hardness, blast resistance, sounds, light emission, etc.
     *                   <p>
     *                   示例：创建橙色水晶方块
     *                   Example: Create orange crystal block
     *                   new CustomCrystalBlock(Properties.of().strength(1.5f))
     *                   <p>
     *                   设计原则：使用数据驱动的掉落物表，不在代码中硬编码掉落物逻辑
     *                   Design principle: Uses data-driven loot tables, doesn't hardcode drop logic in code
     */
    public CustomCrystalBlock(final Properties properties) {
        super(properties);
    }

    // 使用数据驱动的掉落物表，不重写getDrops方法
    // Uses data-driven loot tables, doesn't override getDrops method
}
