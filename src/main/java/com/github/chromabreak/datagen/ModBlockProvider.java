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

/**
 * ModBlockProvider - 方块状态和模型数据生成器
 * ModBlockProvider - Block state and model data generator
 * <p>
 * 这个类负责为ChromaBreak模组生成方块状态JSON文件和对应的模型文件
 * This class is responsible for generating block state JSON files and corresponding model files for the ChromaBreak mod
 * <p>
 * 继承自NeoForge的BlockStateProvider，用于在数据生成阶段自动创建方块状态定义
 * Extends NeoForge's BlockStateProvider to automatically create block state definitions during data generation phase
 */
public class ModBlockProvider extends BlockStateProvider {

    /**
     * 构造函数 - 初始化方块状态提供器
     * Constructor - Initializes the block state provider
     *
     * @param output             PackOutput实例，用于输出生成的数据文件
     *                           PackOutput instance for outputting generated data files
     * @param existingFileHelper ExistingFileHelper实例，用于验证现有文件
     *                           ExistingFileHelper instance for validating existing files
     */
    public ModBlockProvider(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, ChromaBreak.MODID, existingFileHelper);
    }

    /**
     * 注册所有方块的状态和模型
     * Register states and models for all blocks
     * <p>
     * 这是数据生成的核心方法，为每个方块定义其状态变体和对应的模型
     * This is the core method for data generation, defining state variants and corresponding models for each block
     * <p>
     * 方法会自动生成：
     * The method automatically generates:
     * 1. 方块状态JSON文件 (blockstates/*.json)
     * Block state JSON files (blockstates/*.json)
     * 2. 方块模型JSON文件 (models/block/*.json)
     * Block model JSON files (models/block/*.json)
     * 3. 物品模型JSON文件 (models/item/*.json)
     * Item model JSON files (models/item/*.json)
     */
    @Override
    protected void registerStatesAndModels() {
        // 普通立方体方块
        // Regular cube blocks
        this.simpleBlock(ModBlocks.BUDDING_ORANGE_CRYSTALS.get());
        this.simpleBlock(ModBlocks.CRYSTALS_ORANGE_BLOCK.get());

        // 橙色系列：小芽、中芽、大芽、大晶簇（6个方向的variants）
        // Orange series: small bud, medium bud, large bud, large cluster (6-direction variants)
        this.directionalCluster(ModBlocks.SMALL_CRYSTALS_ORANGE_BUD.get(), "small_crystals_orange_bud");
        this.directionalCluster(ModBlocks.MEDIUM_CRYSTALS_ORANGE_BUD.get(), "medium_crystals_orange_bud");
        this.directionalCluster(ModBlocks.LARGE_CRYSTALS_ORANGE_BUD.get(), "large_crystals_orange_bud");
        this.directionalCluster(ModBlocks.CRYSTALS_ORANGE_CLUSTER.get(), "crystals_orange_cluster");

        // 其他颜色小芽（需要时取消注释）
        // Other color small buds (uncomment when needed)
        // directionalCluster(ModBlocks.SMALL_CRYSTALS_BLACK_BUD.get(), "small_crystals_black_bud");
        // directionalCluster(ModBlocks.SMALL_CRYSTALS_WHITE_BUD.get(), "small_crystals_white_bud");
        // ...
    }

    /**
     * 生成具有6个方向变体的方块状态和模型
     * Generate block states and models with 6-direction variants
     * <p>
     * 这个方法专门处理需要根据朝向(facing)显示不同旋转的方块，如晶体芽和晶簇
     * This method specifically handles blocks that need different rotations based on facing direction, such as crystal buds and clusters
     *
     * @param block     要处理的方块实例
     *                  The block instance to process
     * @param modelName 模型名称，对应纹理文件的名称（不含路径和扩展名）
     *                  Model name, corresponding to the texture file name (without path and extension)
     *                  <p>
     *                  实现逻辑：
     *                  Implementation logic:
     *                  1. 创建cross类型的模型，使用"cutout"渲染类型实现透明效果
     *                  Create a cross-type model with "cutout" render type for transparency effects
     *                  2. 为6个方向（上、下、北、南、东、西）生成不同的旋转角度
     *                  Generate different rotation angles for 6 directions (up, down, north, south, east, west)
     *                  3. 自动绑定物品模型到方块模型
     *                  Automatically bind item model to block model
     */
    private void directionalCluster(final Block block, final String modelName) {
        // 生成 cross 模型 - 使用cross模型类型，适合十字交叉形状的方块
        // Generate cross model - using cross model type, suitable for cross-shaped blocks
        final ModelFile model = this.models()
                .cross(modelName, this.modLoc("block/" + modelName))
                .renderType("cutout");  // 设置渲染类型为cutout，实现透明效果
        // Set render type to cutout for transparency effects

        // 生成 blockstate variants - 为每个朝向创建不同的旋转
        // Generate blockstate variants - create different rotations for each facing direction
        this.getVariantBuilder(block)
                // 向下朝向：旋转180度
                // Down facing: rotate 180 degrees
                .partialState().with(BlockStateProperties.FACING, Direction.DOWN)
                .modelForState().modelFile(model).rotationX(180).addModel()

                // 向东朝向：旋转X轴90度，Y轴90度
                // East facing: rotate X-axis 90 degrees, Y-axis 90 degrees
                .partialState().with(BlockStateProperties.FACING, Direction.EAST)
                .modelForState().modelFile(model).rotationX(90).rotationY(90).addModel()

                // 向北朝向：旋转X轴90度
                // North facing: rotate X-axis 90 degrees
                .partialState().with(BlockStateProperties.FACING, Direction.NORTH)
                .modelForState().modelFile(model).rotationX(90).addModel()

                // 向南朝向：旋转X轴90度，Y轴180度
                // South facing: rotate X-axis 90 degrees, Y-axis 180 degrees
                .partialState().with(BlockStateProperties.FACING, Direction.SOUTH)
                .modelForState().modelFile(model).rotationX(90).rotationY(180).addModel()

                // 向上朝向：无旋转（默认方向）
                // Up facing: no rotation (default direction)
                .partialState().with(BlockStateProperties.FACING, Direction.UP)
                .modelForState().modelFile(model).addModel()

                // 向西朝向：旋转X轴90度，Y轴270度
                // West facing: rotate X-axis 90 degrees, Y-axis 270 degrees
                .partialState().with(BlockStateProperties.FACING, Direction.WEST)
                .modelForState().modelFile(model).rotationX(90).rotationY(270).addModel();

        // 自动绑定物品模型（使用 cross 模型）
        // Automatically bind item model (using cross model)
        this.simpleBlockItem(block, model);
    }
}