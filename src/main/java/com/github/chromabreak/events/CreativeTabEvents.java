package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * CreativeTabEvents - 创造标签事件类
 * Creative Tab Events Class
 * <p>
 * 负责处理Minecraft创造模式标签内容构建事件，将ChromaBreak模组的物品添加到相应的创造模式标签中
 * Handles Minecraft creative mode tab content building events, adding ChromaBreak mod items to appropriate creative mode tabs
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 材料标签管理：将水晶碎片添加到材料标签中
 * Ingredients tab management: Adds crystal shards to ingredients tab
 * - 自然方块标签管理：将水晶方块和生长状态方块添加到自然方块标签中
 * Natural blocks tab management: Adds crystal blocks and growth state blocks to natural blocks tab
 * - 事件订阅：订阅创造模式标签内容构建事件
 * Event subscription: Subscribes to creative mode tab content building events
 * - 物品分类：根据物品类型将其分类到合适的创造模式标签
 * Item categorization: Categorizes items into appropriate creative mode tabs based on item type
 * <p>
 * 标签分类说明：
 * Tab categorization explanation:
 * - 材料标签 (INGREDIENTS)：包含所有7种颜色的水晶碎片，作为合成材料
 * Ingredients tab (INGREDIENTS): Contains all 7 colors of crystal shards, used as crafting materials
 * - 自然方块标签 (NATURAL_BLOCKS)：包含水晶方块、母岩和各种生长状态的水晶芽
 * Natural blocks tab (NATURAL_BLOCKS): Contains crystal blocks, budding blocks, and various growth state crystal buds
 * <p>
 * 设计特点：
 * Design features:
 * - 枚举单例模式：使用枚举确保单例，所有方法都是静态方法
 * Enum singleton pattern: Uses enum to ensure singleton, all methods are static methods
 * - 事件驱动：基于NeoForge事件系统，在标签构建时自动调用
 * Event-driven: Based on NeoForge event system, automatically called when tabs are built
 * - 模块化设计：支持轻松添加新的物品到创造模式标签
 * Modular design: Supports easy addition of new items to creative mode tabs
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 创造模式：玩家在创造模式中可以通过标签快速找到模组物品
 * Creative mode: Players can quickly find mod items through tabs in creative mode
 * - 物品分类：确保模组物品被正确分类到合适的创造模式标签
 * Item categorization: Ensures mod items are correctly categorized into appropriate creative mode tabs
 * - 模组集成：与其他模组和谐共存，不干扰其他模组的创造模式标签
 * Mod integration: Harmoniously coexists with other mods, doesn't interfere with other mods' creative mode tabs
 */
@EventBusSubscriber(modid = ChromaBreak.MODID)
public enum CreativeTabEvents {
    ;

    /**
     * 构建创造模式标签内容事件处理方法
     * Build creative mode tab contents event handler
     * <p>
     * 在Minecraft构建创造模式标签内容时被调用，负责将ChromaBreak模组的物品添加到相应的标签中
     * Called when Minecraft builds creative mode tab contents, responsible for adding ChromaBreak mod items to appropriate tabs
     *
     * @param event 创造模式标签内容构建事件，包含标签信息和添加物品的方法
     *              Creative mode tab content building event, containing tab information and item adding methods
     *              <p>
     *              处理逻辑：
     *              Processing logic:
     *              1. 检查当前构建的标签类型
     *              Check the type of tab currently being built
     *              2. 如果是材料标签，添加所有7种颜色的水晶碎片
     *              If it's ingredients tab, add all 7 colors of crystal shards
     *              3. 如果是自然方块标签，添加水晶方块、母岩和各种生长状态的水晶芽
     *              If it's natural blocks tab, add crystal blocks, budding blocks, and various growth state crystal buds
     *              <p>
     *              物品列表：
     *              Item list:
     *              - 材料标签：黑色、白色、黄色、绿色、蓝色、橙色、红色水晶碎片
     *              Ingredients tab: Black, white, yellow, green, blue, orange, red crystal shards
     *              - 自然方块标签：橙色水晶方块、橙色母岩、小芽、中芽、大芽、晶簇
     *              Natural blocks tab: Orange crystal block, orange budding block, small bud, medium bud, large bud, cluster
     *              <p>
     *              注意：目前只实现了橙色水晶系列，未来可以扩展支持其他颜色
     *              Note: Currently only orange crystal series is implemented, can be extended to support other colors in the future
     */
    @SubscribeEvent
    public static void buildCreativeModeTabContents(final BuildCreativeModeTabContentsEvent event) {
        // 材料标签：所有水晶碎片
        // Ingredients tab: All crystal shards
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BLACK_CRYSTAL_SHARD.get());
            event.accept(ModItems.WHITE_CRYSTAL_SHARD.get());
            event.accept(ModItems.YELLOW_CRYSTAL_SHARD.get());
            event.accept(ModItems.GREEN_CRYSTAL_SHARD.get());
            event.accept(ModItems.BLUE_CRYSTAL_SHARD.get());
            event.accept(ModItems.ORANGE_CRYSTAL_SHARD.get());
            event.accept(ModItems.RED_CRYSTAL_SHARD.get());
        }

        // 自然方块标签：水晶方块和生长状态方块
        // Natural blocks tab: Crystal blocks and growth state blocks
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModItems.CRYSTALS_ORANGE_BLOCK_ITEM.get());
            event.accept(ModItems.BUDDING_ORANGE_CRYSTALS_ITEM.get());
            // 添加水晶生长状态方块到自然方块标签栏
            // Add crystal growth state blocks to natural blocks tab
            event.accept(ModItems.SMALL_CRYSTALS_ORANGE_BUD_ITEM.get());
            event.accept(ModItems.MEDIUM_CRYSTALS_ORANGE_BUD_ITEM.get());
            event.accept(ModItems.LARGE_CRYSTALS_ORANGE_BUD_ITEM.get());
            event.accept(ModItems.CRYSTALS_ORANGE_CLUSTER_ITEM.get());
        }
    }
}