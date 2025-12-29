package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.util.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class ModBlockProvider extends BlockStateProvider {

    public ModBlockProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, ChromaBreak.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // 普通立方体方块（芽生方块和完整晶体块）
        this.simpleBlock(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        this.simpleBlock(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 橙色系列：小芽、中芽、大芽、大晶簇（6方向 variants）
        this.directionalCluster(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), "small_crystals_orange_bud");
        this.directionalCluster(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), "medium_crystals_orange_bud");
        this.directionalCluster(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), "large_crystals_orange_bud");
        this.directionalCluster(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), "crystals_orange_cluster");

        // 其他颜色小芽（需要时取消注释）
        // directionalCluster(ModBlocks.SMALL_CRYSTALS_BLACK_BUD.get(), "small_crystals_black_bud");
        // directionalCluster(ModBlocks.SMALL_CRYSTALS_WHITE_BUD.get(), "small_crystals_white_bud");
        // ...
    }

    /**
     * 生成只有 facing 的 6 方向 variants JSON，匹配你提供的格式
     * 同时生成 cross 模型和自动绑定物品模型
     */
    private void directionalCluster(final Block block, final String modelName) {
        // 生成 cross 模型
        final ModelFile model = this.models()
                .cross(modelName, this.modLoc("block/" + modelName))
                .renderType("cutout");

        // 生成 blockstate variants
        this.getVariantBuilder(block)
                .partialState().with(BlockStateProperties.FACING, Direction.DOWN)
                .modelForState().modelFile(model).rotationX(180).addModel()

                .partialState().with(BlockStateProperties.FACING, Direction.EAST)
                .modelForState().modelFile(model).rotationX(90).rotationY(90).addModel()

                .partialState().with(BlockStateProperties.FACING, Direction.NORTH)
                .modelForState().modelFile(model).rotationX(90).addModel()

                .partialState().with(BlockStateProperties.FACING, Direction.SOUTH)
                .modelForState().modelFile(model).rotationX(90).rotationY(180).addModel()

                .partialState().with(BlockStateProperties.FACING, Direction.UP)
                .modelForState().modelFile(model).addModel()

                .partialState().with(BlockStateProperties.FACING, Direction.WEST)
                .modelForState().modelFile(model).rotationX(90).rotationY(270).addModel();

        // 自动绑定物品模型（使用 cross 模型）
        this.simpleBlockItem(block, model);
    }
}