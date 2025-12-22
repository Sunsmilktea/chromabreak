package com.github.chromabreak.system;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 模组数据组件注册类
 * <p>
 * Mod Data Components Registration Class
 * <p>
 * 负责注册模组中的所有DataComponentType
 * Responsible for registering all DataComponentType in the mod
 */
public class ModDataComponents {

    /**
     * 数据组件注册器（使用NeoForge 1.21.1+的新API）
     * <p>
     * Data components registry (using NeoForge 1.21.1+ new API)
     */
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.createDataComponents(ChromaBreak.MODID);

    /**
     * 水晶颜色数据组件
     * <p>
     * Crystal color data component
     */
    public static final Supplier<DataComponentType<CrystalColorVariant>> CRYSTAL_COLOR =
            COMPONENTS.registerComponentType(
                    "color",  // 组件名
                    () -> DataComponentType.<CrystalColorVariant>builder()
                            .persistent(CrystalColorVariant.CODEC)
                            .networkSynchronized(CrystalColorVariant.STREAM_CODEC)
                            .build()
            );
}
