package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.ModBlocks;
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
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            
        }

    }
}