package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.items.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = ChromaBreak.MODID)
public enum CreativeTabEvents {
    ;

    @SubscribeEvent
    public static void buildCreativeModeTabContents(final BuildCreativeModeTabContentsEvent event) {
        // 材料标签：所有水晶碎片
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BLACK_CRYSTAL_SHARD.get());
            event.accept(ModItems.WHITE_CRYSTAL_SHARD.get());
            event.accept(ModItems.YELLOW_CRYSTAL_SHARD.get());
            event.accept(ModItems.GREEN_CRYSTAL_SHARD.get());
            event.accept(ModItems.BLUE_CRYSTAL_SHARD.get());
            event.accept(ModItems.ORANGE_CRYSTAL_SHARD.get());
            event.accept(ModItems.RED_CRYSTAL_SHARD.get());
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModItems.CRYSTALS_ORANGE_BLOCK_ITEM.get());
            event.accept(ModItems.BUDDING_ORANGE_CRYSTALS_ITEM.get());
            // 添加水晶生长状态方块到自然方块标签栏
            event.accept(ModItems.SMALL_CRYSTALS_ORANGE_BUD_ITEM.get());
            event.accept(ModItems.MEDIUM_CRYSTALS_ORANGE_BUD_ITEM.get());
            event.accept(ModItems.LARGE_CRYSTALS_ORANGE_BUD_ITEM.get());
            event.accept(ModItems.CRYSTALS_ORANGE_CLUSTER_ITEM.get());
        }

    }
}