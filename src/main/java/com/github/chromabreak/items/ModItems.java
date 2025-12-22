package com.github.chromabreak.items;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.system.CrystalColorVariant;
import com.github.chromabreak.system.ModDataComponents;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组物品注册类
 * <p>
 * Mod Items Registration Class
 * <p>
 * 负责注册模组中的所有物品，使用Data Components系统支持7种颜色的水晶碎片
 * Responsible for registering all items in the mod, using Data Components system for 7 colors of crystal shards
 */
public class ModItems {
    /**
     * 物品注册器
     * <p>
     * Item registry
     */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChromaBreak.MODID);

    /**
     * 水晶碎片物品（使用Data Components区分颜色变体）
     * <p>
     * Crystal shard item (uses Data Components to distinguish color variants)
     */
    public static final Supplier<Item> CRYSTAL_SHARD = ITEMS.register("crystal_shard",
            () -> new Item(new Item.Properties()
                    .stacksTo(64)
                    .component(ModDataComponents.CRYSTAL_COLOR.get(), CrystalColorVariant.WHITE)  // 默认白色
            ));
}
