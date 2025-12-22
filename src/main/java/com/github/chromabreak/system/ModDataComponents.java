package com.github.chromabreak.system;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {

    // 使用原版注册表（DataComponentType 是原版）
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, ChromaBreak.MODID);

    // 直接 register 一个 Supplier<DataComponentType<?>>
    public static final Supplier<DataComponentType<CrystalColorVariant>> CRYSTAL_COLOR =
            COMPONENTS.register("color", () -> DataComponentType.<CrystalColorVariant>builder()
                    .persistent(CrystalColorVariant.CODEC)
                    .networkSynchronized(CrystalColorVariant.STREAM_CODEC)
                    .build());
}