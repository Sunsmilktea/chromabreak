package com.github.chromabreak.datagen;

import com.github.chromabreak.ChromaBreak;
import com.github.chromabreak.blocks.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockProvider extends BlockStateProvider {

    public ModBlockProvider(final PackOutput output, final ExistingFileHelper exFileHelper) {
        super(output, ChromaBreak.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // 普通立方体方块（母岩 + 晶体块）
        this.simpleBlock(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        this.simpleBlock(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 橙色系列：中芽、大芽、大晶簇（使用自定义方向十字模型）
        this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), "small_crystals_orange_bud");
        this.directionalColoredCluster(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), "medium_crystals_orange_bud");
        this.directionalColoredCluster(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), "large_crystals_orange_bud");
        this.directionalColoredCluster(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), "crystals_orange_cluster");

        // 所有颜色的小芽（7种）
        //this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_BLACK_BUD.get(), "small_crystals_black_bud");
        //this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_WHITE_BUD.get(), "small_crystals_white_bud");
        //this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_YELLOW_BUD.get(), "small_crystals_yellow_bud");
        //this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_GREEN_BUD.get(), "small_crystals_green_bud");
        //this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_BLUE_BUD.get(), "small_crystals_blue_bud");

        //this.directionalColoredCluster(ModBlocks.SMALL_CRYSTALS_RED_BUD.get(), "small_crystals_red_bud");

        // 如果你还加了其他颜色的中芽（如黑、白等），这里继续加：
        //this.directionalColoredCluster(ModBlocks.MEDIUM_CRYSTALS_BLACK_BUD.get(), "medium_crystals_black_bud");
        //this.directionalColoredCluster(ModBlocks.MEDIUM_CRYSTALS_WHITE_BUD.get(), "medium_crystals_white_bud");
        // ... 其他颜色同理
    }

    /**
     * 为带方向的彩色水晶芽/簇生成模型和状态
     * 纹理名称必须和注册名一致：block/<modelName>.png
     */
    private void directionalColoredCluster(final net.minecraft.world.level.block.Block block, final String modelName) {
        final ModelFile model = this.models().cross(modelName, this.modLoc("block/" + modelName));

        this.getVariantBuilder(block)
                .forAllStates(state -> {  // 不加 Except，直接处理所有状态，但只用 facing
                    final Direction dir = state.getValue(BlockStateProperties.FACING);

                    final int xRot = switch (dir) {
                        case DOWN -> 180;
                        case UP -> 0;
                        default -> 90;
                    };

                    final int yRot = switch (dir) {
                        case UP, DOWN -> 0;
                        case NORTH -> 0;
                        case SOUTH -> 180;
                        case WEST -> 270;
                        case EAST -> 90;
                        default -> 0;
                    };

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(xRot)
                            .rotationY(yRot)
                            .build();
                });

        this.simpleBlockItem(block, model);
    }
}