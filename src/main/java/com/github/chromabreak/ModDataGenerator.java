package com.github.chromabreak;

import com.github.chromabreak.datagen.ModBlockProvider;
import com.github.chromabreak.datagen.ModItemProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/**
 * ModDataGenerator - 模组数据生成器主类
 * Mod Data Generator Main Class
 * <p>
 * 负责注册和管理ChromaBreak模组的数据生成器，在数据生成事件中创建方块和物品的数据提供者
 * Responsible for registering and managing data generators for ChromaBreak mod,
 * creates block and item data providers during data generation events
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 监听数据生成事件 (GatherDataEvent)
 * Listen to data generation events (GatherDataEvent)
 * - 注册方块数据生成器 (ModBlockProvider)
 * Register block data generator (ModBlockProvider)
 * - 注册物品数据生成器 (ModItemProvider)
 * Register item data generator (ModItemProvider)
 * - 管理数据生成流程和依赖
 * Manage data generation flow and dependencies
 * <p>
 * 工作流程：
 * Workflow:
 * 1. 当NeoForge触发数据生成事件时，此类的gatherData方法被调用
 * When NeoForge triggers data generation event, gatherData method of this class is called
 * 2. 获取数据生成器、输出路径和文件助手等必要组件
 * Get necessary components like data generator, output path, and file helper
 * 3. 注册方块和物品数据提供者到数据生成器
 * Register block and item data providers to the data generator
 * 4. 数据生成器自动生成方块状态、物品模型等JSON文件
 * Data generator automatically generates JSON files like block states, item models, etc.
 * <p>
 * 生成的数据类型：
 * Generated data types:
 * - 方块状态JSON文件 (blockstates/)
 * Block state JSON files (blockstates/)
 * - 方块模型JSON文件 (models/block/)
 * Block model JSON files (models/block/)
 * - 物品模型JSON文件 (models/item/)
 * Item model JSON files (models/item/)
 * <p>
 * 使用@EventBusSubscriber注解自动注册到NeoForge事件总线
 * Uses @EventBusSubscriber annotation to automatically register to NeoForge event bus
 */
@EventBusSubscriber(modid = ChromaBreak.MODID)
public enum ModDataGenerator {
    ;

    /**
     * 数据生成事件处理方法
     * Data generation event handler method
     * <p>
     * 当NeoForge触发数据生成事件时自动调用，负责注册所有数据提供者
     * Automatically called when NeoForge triggers data generation event,
     * responsible for registering all data providers
     *
     * @param event 数据生成事件，包含数据生成器、输出路径、文件助手等组件
     *              Data generation event containing components like data generator, output path, file helper
     *              <p>
     *              处理步骤：
     *              Processing steps:
     *              1. 从事件中获取数据生成器、输出路径、文件助手和查找提供者
     *              Get data generator, output path, file helper, and lookup provider from event
     *              2. 注册方块数据提供者，生成方块状态和模型文件
     *              Register block data provider to generate block state and model files
     *              3. 注册物品数据提供者，生成物品模型文件
     *              Register item data provider to generate item model files
     *              <p>
     *              注意：此方法只处理客户端数据生成（event.includeClient()）
     *              Note: This method only handles client-side data generation (event.includeClient())
     */
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        // 获取数据生成器组件
        // Get data generator components
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final ExistingFileHelper helper = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 注册方块数据提供者
        // Register block data provider
        generator.addProvider(event.includeClient(), new ModBlockProvider(output, helper));

        // 注册物品数据提供者
        // Register item data provider
        generator.addProvider(event.includeClient(), new ModItemProvider(output, helper));
    }
}
