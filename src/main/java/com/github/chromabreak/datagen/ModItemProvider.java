package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.ModBlocks;
import com.github.chromabreak.items.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemProvider extends ItemModelProvider {

    public ModItemProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChromaBreak.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // 1. 橙色水晶碎片（普通2D手持物品）
        // 注册名 "crystals_orange_shard" → 纹理 crystal_orange_shard.png（请确认文件名一致）
        basicItem(ModItems.ORANGE_CRYSTAL_SHARD.get());

        // 2. 普通立方体方块物品（3D预览）
        simpleBlockItem(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        simpleBlockItem(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 3. 十字模型的芽和晶簇（使用原版 amethyst_bud 父模型，和原版显示效果100%一致）
        amethystBudStyleItem(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), "small_crystals_orange_bud");
        amethystBudStyleItem(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), "medium_crystals_orange_bud");
        amethystBudStyleItem(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), "large_crystals_orange_bud");
        amethystBudStyleItem(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), "crystals_orange_cluster");

        // 其他颜色的小芽（需要时取消注释）
        // amethystBudStyleItem(ModBlocks.SMALL_CRYSTALS_BLACK_BUD.get(), "small_crystals_black_bud");
        // amethystBudStyleItem(ModBlocks.SMALL_CRYSTALS_WHITE_BUD.get(), "small_crystals_white_bud");
        // ...
    }

    /**
     * 使用原版 amethyst_bud / cluster 的物品模型风格
     * 生成的 JSON 完全匹配你提供的格式
     */
    private void amethystBudStyleItem(net.minecraft.world.level.block.Block block, String textureName) {
        String name = block.builtInRegistryHolder().key().location().getPath();

        // 小/中/大芽使用 "item/amethyst_bud" 父模型
        // 大晶簇使用 "item/amethyst_cluster" 父模型（原版就是这样区分的）
        String parent = textureName.contains("cluster") ? "item/amethyst_cluster" : "item/amethyst_bud";

        withExistingParent(name, mcLoc(parent))
                .texture("layer0", modLoc("block/" + textureName));
    }
}