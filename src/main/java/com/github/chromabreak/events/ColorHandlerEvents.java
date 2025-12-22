package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.ModBlocks;
import com.github.chromabreak.items.ModItems;
import com.github.chromabreak.system.CrystalColorVariant;
import com.github.chromabreak.system.ModDataComponents;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * 颜色处理器事件类
 * <p>
 * Color Handler Events Class
 * <p>
 * 负责注册物品和方块的纹理着色处理器
 * Responsible for registering texture tinting handlers for items and blocks
 */
@EventBusSubscriber(modid = ChromaBreak.MODID)
public class ColorHandlerEvents {

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColor blockColorHandler = (state, level, pos, tintIndex) -> {
            // 根据方块状态的颜色属性返回对应的颜色值
            if (state.hasProperty(ModBlocks.COLOR)) {
                CrystalColorVariant colorVariant = state.getValue(ModBlocks.COLOR);
                return colorVariant.getColor();
            }
            return 0xFFFFFFFF; // 默认白色（不改变颜色）
        };

        // 注册水晶块和水晶簇颜色处理器
        event.register(blockColorHandler, ModBlocks.CRYSTAL_BLOCK.get(), ModBlocks.CRYSTAL_CLUSTER.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // 方块物品颜色处理器（水晶块和水晶簇共用，返回默认白色）
        ItemColor blockItemColorHandler = (stack, tintIndex) -> 0xFFFFFFFF; // 默认白色

        // 注册水晶块和水晶簇物品颜色处理器（手持状态）
        event.register(blockItemColorHandler, ModBlocks.CRYSTAL_BLOCK.get().asItem(), ModBlocks.CRYSTAL_CLUSTER.get().asItem());

        // 水晶碎片物品颜色处理器（根据Data Components动态着色）
        ItemColor shardColorHandler = (stack, tintIndex) -> {
            CrystalColorVariant variant = stack.get(ModDataComponents.CRYSTAL_COLOR.get());
            return variant != null ? variant.getColor() : 0xFFFFFFFF; // 默认白色
        };

        // 注册水晶碎片物品颜色处理器
        event.register(shardColorHandler, ModItems.CRYSTAL_SHARD.get());
    }
}
