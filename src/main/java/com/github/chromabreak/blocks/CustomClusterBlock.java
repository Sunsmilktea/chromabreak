package com.github.chromabreak.blocks;

import com.github.chromabreak.items.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 自定义水晶簇方块
 * <p>
 * Custom Crystal Cluster Block
 * <p>
 * 继承自紫水晶簇方块，实现水晶簇的功能和掉落物逻辑
 * Extends AmethystClusterBlock to implement crystal cluster functionality and drop logic
 */
public class CustomClusterBlock extends AmethystClusterBlock {
    private final BuddingCrystalsBlock.CrystalColor color;

    public CustomClusterBlock(final Properties properties, final BuddingCrystalsBlock.CrystalColor color) {
        super(7.0F, 3.0F, properties);  // 完整簇模型大小（7像素）
        this.color = color;
    }

    // 使用数据驱动的掉落物表，不重写getDrops方法

    /**
     * 根据颜色获取对应的水晶碎片
     */
    private ItemStack getCrystalShardForColor() {
        return switch (this.color) {
            case BLACK -> new ItemStack(ModItems.BLACK_CRYSTAL_SHARD.get());
            case WHITE -> new ItemStack(ModItems.WHITE_CRYSTAL_SHARD.get());
            case YELLOW -> new ItemStack(ModItems.YELLOW_CRYSTAL_SHARD.get());
            case GREEN -> new ItemStack(ModItems.GREEN_CRYSTAL_SHARD.get());
            case BLUE -> new ItemStack(ModItems.BLUE_CRYSTAL_SHARD.get());
            case ORANGE -> new ItemStack(ModItems.ORANGE_CRYSTAL_SHARD.get());
            case RED -> new ItemStack(ModItems.RED_CRYSTAL_SHARD.get());
            default -> new ItemStack(ModItems.ORANGE_CRYSTAL_SHARD.get());
        };
    }

    public BuddingCrystalsBlock.CrystalColor getColor() {
        return this.color;
    }
}
