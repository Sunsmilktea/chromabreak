package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import com.github.chromabreak.util.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * ModItemProvider - 物品模型数据生成器
 * Item Model Provider - Data generator for item models
 * <p>
 * 这个类负责为 ChromaBreak 模组生成所有物品的模型文件
 * This class is responsible for generating model files for all items in the ChromaBreak mod
 * <p>
 * 继承自 NeoForge 的 ItemModelProvider，提供物品模型生成功能
 * Extends NeoForge's ItemModelProvider to provide item model generation functionality
 * <p>
 * 生成的模型文件将保存在 src/generated/resources 目录中
 * Generated model files will be saved in the src/generated/resources directory
 */
public class ModItemProvider extends ItemModelProvider {

    /**
     * 构造函数 - Constructor
     *
     * @param output             PackOutput 实例，用于输出生成的文件
     *                           PackOutput instance for outputting generated files
     * @param existingFileHelper ExistingFileHelper 实例，用于检查现有文件
     *                           ExistingFileHelper instance for checking existing files
     */
    public ModItemProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, ChromaBreak.MODID, existingFileHelper);
    }

    /**
     * 注册所有物品模型 - Register all item models
     * <p>
     * 这个方法在数据生成时被调用，用于注册模组中的所有物品模型
     * This method is called during data generation to register all item models in the mod
     * <p>
     * 包括：
     * Includes:
     * - 橙色水晶碎片（2D手持图标）
     * Orange crystal shard (2D handheld icon)
     * - 普通立方体方块物品（3D预览）
     * Regular cube block items (3D preview)
     * - 橙色系列芽和晶簇（原版紫水晶风格）
     * Orange series buds and clusters (vanilla amethyst style)
     */
    @Override
    protected void registerModels() {
        // 橙色水晶碎片（2D手持图标）
        // Orange crystal shard (2D handheld icon)
        this.basicItem(ModItems.ORANGE_CRYSTAL_SHARD.get());

        // 普通立方体方块物品（3D预览）
        // Regular cube block items (3D preview)
        this.simpleBlockItem(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        this.simpleBlockItem(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 橙色系列芽和晶簇（原版物品模型风格）
        // Orange series buds and clusters (vanilla item model style)
        this.amethystStyleItem(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), "small_crystals_orange_bud");
        this.amethystStyleItem(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), "medium_crystals_orange_bud");
        this.amethystStyleItem(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), "large_crystals_orange_bud");
        this.amethystStyleItem(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), "crystals_orange_cluster");
    }

    /**
     * 使用原版 amethyst_bud / amethyst_cluster 的物品模型风格
     * Use vanilla amethyst_bud / amethyst_cluster item model style
     * <p>
     * 这个方法为水晶芽和晶簇创建类似原版紫水晶的物品模型
     * This method creates item models for crystal buds and clusters similar to vanilla amethyst
     *
     * @param block       要创建模型的方块
     *                    The block to create model for
     * @param textureName 纹理名称（不包含路径）
     *                    Texture name (without path)
     */
    private void amethystStyleItem(final net.minecraft.world.level.block.Block block, final String textureName) {
        // 获取方块的注册名称（路径部分）
        // Get the block's registry name (path part)
        final String itemName = net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getPath();

        // 根据纹理名称判断是晶簇还是芽，选择对应的父模型
        // Determine if it's a cluster or bud based on texture name, select corresponding parent model
        final String parent = textureName.contains("cluster") ? "item/amethyst_cluster" : "item/amethyst_bud";

        // 创建物品模型，使用原版紫水晶模型作为父模型，替换纹理
        // Create item model using vanilla amethyst model as parent, replacing the texture
        this.withExistingParent(itemName, this.mcLoc(parent))
                .texture("layer0", this.modLoc("block/" + textureName));
    }
}