package com.github.chromabreak.system;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Locale;

/**
 * 水晶颜色变体枚举
 * <p>
 * Crystal Color Variant Enum
 * <p>
 * 用于纹理着色系统，支持7种颜色的水晶变体
 * Used for texture tinting system, supports 7 colors of crystal variants
 */
public enum CrystalColorVariant implements StringRepresentable {
    RED("red", 0xFFFF0000),      // 红色
    BLUE("blue", 0xFF0000FF),    // 蓝色
    GREEN("green", 0xFF00FF00),  // 绿色
    YELLOW("yellow", 0xFFFFFF00), // 黄色
    WHITE("white", 0xFFFFFFFF),  // 白色
    BLACK("black", 0xFF000000),  // 黑色
    ORANGE("orange", 0xFFFFA500); // 橙色

    private final String name;
    private final int colorValue;

    CrystalColorVariant(String name, int colorValue) {
        this.name = name;
        this.colorValue = colorValue;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    /**
     * 获取颜色值（ARGB格式）
     * <p>
     * Get color value (ARGB format)
     *
     * @return ARGB颜色值
     */
    public int getColor() {
        return this.colorValue;
    }

    /**
     * 根据名称获取颜色变体
     * <p>
     * Get color variant by name
     *
     * @param name 颜色名称
     * @return 颜色变体，如果未找到返回null
     */
    public static CrystalColorVariant byName(String name) {
        for (CrystalColorVariant variant : values()) {
            if (variant.name.equals(name)) {
                return variant;
            }
        }
        return null;
    }

    /**
     * 获取所有颜色变体的名称数组
     * <p>
     * Get array of all color variant names
     *
     * @return 颜色名称数组
     */
    public static String[] getColorNames() {
        CrystalColorVariant[] variants = values();
        String[] names = new String[variants.length];
        for (int i = 0; i < variants.length; i++) {
            names[i] = variants[i].name;
        }
        return names;
    }

    // Data Components序列化支持
    // Data Components serialization support
    public static final Codec<CrystalColorVariant> CODEC = StringRepresentable.fromEnum(CrystalColorVariant::values);

    // 用于网络同步（NeoForge专用，使用安全的ordinal方法）
    public static final StreamCodec<RegistryFriendlyByteBuf, CrystalColorVariant> STREAM_CODEC =
            StreamCodec.of(
                    (buf, variant) -> buf.writeVarInt(variant.ordinal()),
                    buf -> {
                        int ordinal = buf.readVarInt();
                        return ordinal >= 0 && ordinal < values().length ? values()[ordinal] : WHITE; // 安全默认
                    }
            );
}
