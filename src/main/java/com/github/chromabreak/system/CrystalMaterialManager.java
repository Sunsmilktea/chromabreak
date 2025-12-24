package com.github.chromabreak.system;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.resources.ResourceLocation;

/**
 * 水晶材质管理器
 * <p>
 * Crystal Material Manager
 * <p>
 * 负责管理7种颜色水晶的材质资源路径
 * Responsible for managing texture resource paths for 7 colors of crystals
 */
public enum CrystalMaterialManager {
    ;

    /**
     * 获取水晶碎片物品的材质路径
     * <p>
     * Get crystal shard item texture path
     *
     * @param color 颜色名称
     * @return 材质资源路径
     */
    public static ResourceLocation getCrystalShardTexture(final String color) {
        return ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "item/" + color + "_crystal_shard");
    }

    /**
     * 获取水晶块方块的材质路径
     * <p>
     * Get crystal block texture path
     *
     * @param color 颜色名称
     * @return 材质资源路径
     */
    public static ResourceLocation getCrystalBlockTexture(final String color) {
        return ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "block/" + color + "_crystal_block");
    }

    /**
     * 获取水晶簇方块的材质路径
     * <p>
     * Get crystal cluster texture path
     *
     * @param color 颜色名称
     * @return 材质资源路径
     */
    public static ResourceLocation getCrystalClusterTexture(final String color) {
        return ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "block/" + color + "_crystal_cluster");
    }

    /**
     * 获取7种颜色的数组
     * <p>
     * Get array of 7 colors
     *
     * @return 颜色名称数组
     */
    public static String[] getCrystalColors() {
        return new String[]{"red", "blue", "green", "yellow", "white", "black", "orange"};
    }

    /**
     * 检查颜色是否有效
     * <p>
     * Check if color is valid
     *
     * @param color 颜色名称
     * @return 是否有效
     */
    public static boolean isValidColor(final String color) {
        for (final String validColor : CrystalMaterialManager.getCrystalColors()) {
            if (validColor.equals(color)) {
                return true;
            }
        }
        return false;
    }
}
