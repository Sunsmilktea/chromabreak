package com.github.chromabreak.events;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.ModBlocks;
import com.github.chromabreak.items.ModItems;
import com.github.chromabreak.system.CrystalColorVariant;
import com.github.chromabreak.system.ModDataComponents; // 假设你的 DataComponent 在这里定义
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * 创造模式标签事件处理器
 * <p>
 * Creative Mode Tab Events Handler
 * <p>
 * 负责将模组物品添加到正确的创造模式标签中
 * Responsible for adding mod items to correct creative mode tabs
 */
@EventBusSubscriber(modid = ChromaBreak.MODID)
public class CreativeTabEvents {

    /**
     * 构建创造模式标签内容
     * <p>
     * Build creative mode tab contents
     *
     * @param event 构建创造模式标签内容事件
     */
    @SubscribeEvent
    public static void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        // 将水晶碎片添加到材料标签（Ingredients）
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            for (CrystalColorVariant variant : CrystalColorVariant.values()) {
                ItemStack crystalShard = new ItemStack(ModItems.CRYSTAL_SHARD.get());
                // 最安全的方式：使用 Data Component 设置颜色变体
                crystalShard.set(ModDataComponents.CRYSTAL_COLOR.get(), variant);
                // 额外安全检查：确保栈非空且不是空气
                if (!crystalShard.isEmpty()) {
                    event.accept(crystalShard);
                }
            }
        }

        // 将水晶块和水晶簇添加到建筑方块标签（Building Blocks）
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // 添加水晶块（假设它是单一物品或也有变体逻辑）
            event.accept(ModBlocks.CRYSTAL_BLOCK.get());

            // 添加水晶簇
            event.accept(ModBlocks.CRYSTAL_CLUSTER.get());
        }

        // 如果你有其他自定义标签，也可以这样添加
        // if (event.getTab() == YourCustomTabs.YOUR_TAB.get()) { ... }
    }
}