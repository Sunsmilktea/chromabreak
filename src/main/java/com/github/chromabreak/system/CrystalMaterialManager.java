package com.github.chromabreak.system;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.resources.ResourceLocation;

/**
 * CrystalMaterialManager - 水晶材质管理器
 * Crystal Material Manager
 * <p>
 * 负责管理ChromaBreak模组中7种颜色水晶的材质资源路径
 * Responsible for managing texture resource paths for 7 colors of crystals in ChromaBreak mod
 * <p>
 * 支持的颜色包括（与Minecraft染料颜色一一对应）：
 * Supported colors include (one-to-one with Minecraft dye colors):
 * - red（红色）
 * - blue（蓝色）
 * - green（绿色）
 * - yellow（黄色）
 * - white（白色）
 * - black（黑色）
 * - orange（橙色）
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 水晶碎片物品材质路径管理
 * Crystal shard item texture path management
 * - 水晶块方块材质路径管理
 * Crystal block texture path management
 * - 水晶簇方块材质路径管理
 * Crystal cluster texture path management
 * - 颜色验证和颜色数组管理
 * Color validation and color array management
 * <p>
 * 使用枚举模式确保单例，所有方法都是静态方法
 * Uses enum pattern to ensure singleton, all methods are static methods
 * <p>
 * 材质路径格式：modid:type/color_crystal_type
 * Texture path format: modid:type/color_crystal_type
 */
public enum CrystalMaterialManager {
    ;

    /**
     * 获取水晶碎片物品的材质路径
     * Get crystal shard item texture path
     *
     * @param color 颜色名称（支持的颜色：red, blue, green, yellow, white, black, orange）
     *              Color name (supported colors: red, blue, green, yellow, white, black, orange)
     * @return 材质资源路径，格式为"modid:item/color_crystal_shard"
     * Texture resource location in format "modid:item/color_crystal_shard"
     * <p>
     * 示例：对于红色水晶碎片，返回"chromabreak:item/red_crystal_shard"
     * Example: For red crystal shard, returns "chromabreak:item/red_crystal_shard"
     */
    public static ResourceLocation getCrystalShardTexture(final String color) {
        return ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "item/" + color + "_crystal_shard");
    }

    /**
     * 获取水晶块方块的材质路径
     * Get crystal block texture path
     *
     * @param color 颜色名称（支持的颜色：red, blue, green, yellow, white, black, orange）
     *              Color name (supported colors: red, blue, green, yellow, white, black, orange)
     * @return 材质资源路径，格式为"modid:block/color_crystal_block"
     * Texture resource location in format "modid:block/color_crystal_block"
     * <p>
     * 示例：对于蓝色水晶块，返回"chromabreak:block/blue_crystal_block"
     * Example: For blue crystal block, returns "chromabreak:block/blue_crystal_block"
     */
    public static ResourceLocation getCrystalBlockTexture(final String color) {
        return ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "block/" + color + "_crystal_block");
    }

    /**
     * 获取水晶簇方块的材质路径
     * Get crystal cluster texture path
     *
     * @param color 颜色名称（支持的颜色：red, blue, green, yellow, white, black, orange）
     *              Color name (supported colors: red, blue, green, yellow, white, black, orange)
     * @return 材质资源路径，格式为"modid:block/color_crystal_cluster"
     * Texture resource location in format "modid:block/color_crystal_cluster"
     * <p>
     * 示例：对于绿色水晶簇，返回"chromabreak:block/green_crystal_cluster"
     * Example: For green crystal cluster, returns "chromabreak:block/green_crystal_cluster"
     */
    public static ResourceLocation getCrystalClusterTexture(final String color) {
        return ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "block/" + color + "_crystal_cluster");
    }

    /**
     * 获取7种颜色的数组
     * Get array of 7 colors
     *
     * @return 颜色名称数组，包含所有支持的颜色：red, blue, green, yellow, white, black, orange
     * Array of color names containing all supported colors: red, blue, green, yellow, white, black, orange
     * <p>
     * 用于遍历所有颜色或验证颜色有效性
     * Used for iterating through all colors or validating color validity
     */
    public static String[] getCrystalColors() {
        return new String[]{"red", "blue", "green", "yellow", "white", "black", "orange"};
    }

    /**
     * 检查颜色是否有效
     * Check if color is valid
     *
     * @param color 要检查的颜色名称
     *              Color name to check
     * @return 如果颜色在支持的颜色列表中则返回true，否则返回false
     * Returns true if color is in the supported color list, false otherwise
     * <p>
     * 用于验证用户输入的颜色参数是否有效
     * Used to validate if user-provided color parameter is valid
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
