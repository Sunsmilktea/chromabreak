package com.github.chromabreak.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CustomCrystalBlock extends Block {
    public CustomCrystalBlock(Properties properties) {
        super(properties);
    }
    // 使用数据驱动的掉落物表，不重写getDrops方法
}
