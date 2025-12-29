package com.github.chromabreak.util;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * ModItems - 模组物品注册类
 * Mod Items Registration Class
 * <p>
 * 负责注册模组中的所有物品，使用枚举模式确保单例
 * Responsible for registering all items in the mod, using enum pattern to ensure singleton
 * <p>
 * 这个类使用NeoForge的DeferredRegister系统来延迟注册物品
 * This class uses NeoForge's DeferredRegister system for deferred item registration
 * <p>
 * 包含以下类型的物品：
 * Includes the following types of items:
 * - 水晶碎片（7种颜色变体）
 * Crystal shards (7 color variants)
 * - 方块物品（将方块转换为可放置的物品形式）
 * Block items (converting blocks to placeable item forms)
 */
public enum ModItems {
    ;

    /**
     * 物品注册器 - Item registry
     * <p>
     * 使用DeferredRegister延迟注册系统，确保物品在正确的时机注册
     * Uses DeferredRegister deferred registration system to ensure items are registered at the correct time
     * <p>
     * 这个注册器会在模组初始化时自动处理物品的注册过程
     * This registry will automatically handle the item registration process during mod initialization
     */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChromaBreak.MODID);

    // ==================== 水晶碎片物品（7种颜色变体） ====================
    // ==================== Crystal Shard Items (7 color variants) ====================

    /**
     * 黑色水晶碎片物品
     * Black crystal shard item
     * <p>
     * 基础物品，最大堆叠数为64
     * Basic item with maximum stack size of 64
     */
    public static final Supplier<Item> BLACK_CRYSTAL_SHARD = ModItems.ITEMS.register("black_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 白色水晶碎片物品
     * White crystal shard item
     */
    public static final Supplier<Item> WHITE_CRYSTAL_SHARD = ModItems.ITEMS.register("white_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 黄色水晶碎片物品
     * Yellow crystal shard item
     */
    public static final Supplier<Item> YELLOW_CRYSTAL_SHARD = ModItems.ITEMS.register("yellow_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 绿色水晶碎片物品
     * Green crystal shard item
     */
    public static final Supplier<Item> GREEN_CRYSTAL_SHARD = ModItems.ITEMS.register("green_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 蓝色水晶碎片物品
     * Blue crystal shard item
     */
    public static final Supplier<Item> BLUE_CRYSTAL_SHARD = ModItems.ITEMS.register("blue_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 橙色水晶碎片物品
     * Orange crystal shard item
     */
    public static final Supplier<Item> ORANGE_CRYSTAL_SHARD = ModItems.ITEMS.register("orange_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 红色水晶碎片物品
     * Red crystal shard item
     */
    public static final Supplier<Item> RED_CRYSTAL_SHARD = ModItems.ITEMS.register("red_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    // ==================== 方块物品（将方块转换为可放置的物品形式） ====================
    // ==================== Block Items (converting blocks to placeable item forms) ====================

    /**
     * 橙色水晶方块物品
     * Orange crystal block item
     * <p>
     * 将橙色水晶方块转换为可放置的物品形式
     * Converts orange crystal block to placeable item form
     * <p>
     * 关键修复：确保方块有对应的物品形式才能在游戏中放置
     * Critical fix: Ensures blocks have corresponding item forms to be placeable in-game
     */
    public static final Supplier<Item> CRYSTALS_ORANGE_BLOCK_ITEM = ModItems.ITEMS.register("crystals_orange_block",
            () -> new BlockItem(ModBlocks.CRYSTALS_ORANGE_BLOCK.get(), new Item.Properties()));

    /**
     * 橙色水晶芽生方块物品
     * Orange budding crystals block item
     */
    public static final Supplier<Item> BUDDING_ORANGE_CRYSTALS_ITEM = ModItems.ITEMS.register("budding_orange_crystals",
            () -> new BlockItem(ModBlocks.BUDDING_ORANGE_CRYSTALS.get(), new Item.Properties()));

    // ==================== 水晶生长状态方块物品（四种状态） ====================
    // ==================== Crystal Growth State Block Items (four states) ====================

    /**
     * 小型橙色水晶芽方块物品
     * Small orange crystal bud block item
     * <p>
     * 水晶生长的第一阶段状态
     * First stage of crystal growth
     */
    public static final Supplier<Item> SMALL_CRYSTALS_ORANGE_BUD_ITEM = ModItems.ITEMS.register("small_crystals_orange_bud",
            () -> new BlockItem(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), new Item.Properties()));

    /**
     * 中型橙色水晶芽方块物品
     * Medium orange crystal bud block item
     * <p>
     * 水晶生长的第二阶段状态
     * Second stage of crystal growth
     */
    public static final Supplier<Item> MEDIUM_CRYSTALS_ORANGE_BUD_ITEM = ModItems.ITEMS.register("medium_crystals_orange_bud",
            () -> new BlockItem(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), new Item.Properties()));

    /**
     * 大型橙色水晶芽方块物品
     * Large orange crystal bud block item
     * <p>
     * 水晶生长的第三阶段状态
     * Third stage of crystal growth
     */
    public static final Supplier<Item> LARGE_CRYSTALS_ORANGE_BUD_ITEM = ModItems.ITEMS.register("large_crystals_orange_bud",
            () -> new BlockItem(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), new Item.Properties()));

    /**
     * 橙色水晶晶簇方块物品
     * Orange crystal cluster block item
     * <p>
     * 水晶生长的最终阶段状态
     * Final stage of crystal growth
     */
    public static final Supplier<Item> CRYSTALS_ORANGE_CLUSTER_ITEM = ModItems.ITEMS.register("crystals_orange_cluster",
            () -> new BlockItem(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), new Item.Properties()));
}