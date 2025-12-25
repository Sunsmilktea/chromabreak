package com.github.chromabreak.items;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组物品注册类
 */
public enum ModItems {
    ;

    /**
     * 物品注册器
     */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChromaBreak.MODID);

    /**
     * 水晶碎片（7种颜色）
     */
    public static final Supplier<Item> BLACK_CRYSTAL_SHARD = ModItems.ITEMS.register("black_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> WHITE_CRYSTAL_SHARD = ModItems.ITEMS.register("white_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> YELLOW_CRYSTAL_SHARD = ModItems.ITEMS.register("yellow_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> GREEN_CRYSTAL_SHARD = ModItems.ITEMS.register("green_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> BLUE_CRYSTAL_SHARD = ModItems.ITEMS.register("blue_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> ORANGE_CRYSTAL_SHARD = ModItems.ITEMS.register("orange_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> RED_CRYSTAL_SHARD = ModItems.ITEMS.register("red_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 橙色水晶方块的物品形式（关键修复）
     */
    public static final Supplier<Item> CRYSTALS_ORANGE_BLOCK_ITEM = ModItems.ITEMS.register("crystals_orange_block",
            () -> new BlockItem(ModBlocks.CRYSTALS_ORANGE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<Item> BUDDING_ORANGE_CRYSTALS_ITEM = ModItems.ITEMS.register("budding_orange_crystals",
            () -> new BlockItem(ModBlocks.BUDDING_ORANGE_CRYSTALS.get(), new Item.Properties()));

    /**
     * 水晶生长状态方块的物品形式（四种状态）
     */
    public static final Supplier<Item> SMALL_CRYSTALS_ORANGE_BUD_ITEM = ModItems.ITEMS.register("small_crystals_orange_bud",
            () -> new BlockItem(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), new Item.Properties()));

    public static final Supplier<Item> MEDIUM_CRYSTALS_ORANGE_BUD_ITEM = ModItems.ITEMS.register("medium_crystals_orange_bud",
            () -> new BlockItem(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), new Item.Properties()));

    public static final Supplier<Item> LARGE_CRYSTALS_ORANGE_BUD_ITEM = ModItems.ITEMS.register("large_crystals_orange_bud",
            () -> new BlockItem(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), new Item.Properties()));

    public static final Supplier<Item> CRYSTALS_ORANGE_CLUSTER_ITEM = ModItems.ITEMS.register("crystals_orange_cluster",
            () -> new BlockItem(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), new Item.Properties()));
}