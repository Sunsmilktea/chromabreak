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
    public static final Supplier<Item> BLACK_CRYSTAL_SHARD = ITEMS.register("black_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> WHITE_CRYSTAL_SHARD = ITEMS.register("white_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> YELLOW_CRYSTAL_SHARD = ITEMS.register("yellow_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> GREEN_CRYSTAL_SHARD = ITEMS.register("green_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> BLUE_CRYSTAL_SHARD = ITEMS.register("blue_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> ORANGE_CRYSTAL_SHARD = ITEMS.register("orange_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final Supplier<Item> RED_CRYSTAL_SHARD = ITEMS.register("red_crystal_shard",
            () -> new Item(new Item.Properties().stacksTo(64)));

    /**
     * 橙色水晶方块的物品形式（关键修复）
     */
    public static final Supplier<Item> CRYSTALS_ORANGE_BLOCK_ITEM = ITEMS.register("crystals_orange_block",
            () -> new BlockItem(ModBlocks.CRYSTALS_ORANGE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<Item> BUDDING_ORANGE_CRYSTALS_ITEM = ITEMS.register("budding_orange_crystals",
            () -> new BlockItem(ModBlocks.BUDDING_ORANGE_CRYSTALS.get(), new Item.Properties()));
}