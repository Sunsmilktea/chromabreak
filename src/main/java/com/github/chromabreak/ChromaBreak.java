package com.github.chromabreak;

import com.github.chromabreak.blocks.ModBlocks;
import com.github.chromabreak.config.ConfigExampleGenerator;
import com.github.chromabreak.config.EntityConfigLoader;
import com.github.chromabreak.config.ModCompatibilityConfigLoader;
import com.github.chromabreak.items.ModItems;
import com.github.chromabreak.tool.StructureFinderCommand;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file

/**
 * ChromaBreak模组主类
 * <p>
 * ChromaBreak Mod Main Class
 * <p>
 * 这是模组的主入口点，负责模组的初始化和核心配置。
 * 具体的功能实现已经分离到专门的包和类中：
 * - events包：处理各种事件监听
 * - config包：处理配置相关功能
 * - blocks包：处理方块相关功能
 * <p>
 * This is the main entry point of the mod, responsible for mod initialization and core configuration.
 * Specific functionality implementations have been separated into dedicated packages and classes:
 * - events package: handles various event listeners
 * - config package: handles configuration-related functionality
 * - block package: handles block-related functionality
 */
@Mod(ChromaBreak.MODID)
public class ChromaBreak {
    /**
     * 模组ID，在公共位置定义以供所有地方引用
     * <p>
     * Define mod id in a common place for everything to reference
     */
    public static final String MODID = "chromabreak";

    /**
     * 直接引用slf4j日志记录器
     * <p>
     * Directly reference a slf4j logger
     */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * 模组构造器
     * 当模组加载时，这是第一个被运行的代码
     * FML会自动识别IEventBus和ModContainer等参数类型并传入
     * <p>
     * Mod constructor
     * This is the first code that runs when your mod is loaded
     * FML automatically recognizes parameter types like IEventBus or ModContainer and passes them in
     *
     * @param modEventBus  模组事件总线，用于注册事件监听器
     *                     Mod event bus for registering event listeners
     * @param modContainer 模组容器，包含模组的元数据和配置信息
     *                     Mod container containing mod metadata and configuration information
     */
    public ChromaBreak(final IEventBus modEventBus, final ModContainer modContainer) {
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        // 注册模组的配置规范，以便FML可以为我们创建和加载配置文件
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // 注册物品、方块和数据组件到模组事件总线
        // Register items, block and data components to mod event bus
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        // 注册模组事件总线监听器
        // Register mod event bus listener
        modEventBus.addListener(this::onCommonSetup);

        // 注册Forge事件总线监听器（命令注册）
        // Register Forge event bus listener (command registration)
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    /**
     * 注册命令
     * Register commands
     *
     * @param event 命令注册事件
     */
    private void onRegisterCommands(final RegisterCommandsEvent event) {
        StructureFinderCommand.register(event.getDispatcher());
        LOGGER.info("StructureFinder命令已注册");
        LOGGER.info("StructureFinder command registered");
    }

    /**
     * 在模组初始化时加载实体配置
     * Load entity configurations during mod initialization
     */
    private void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 生成配置文件示例（如果不存在）
            // Generate configuration example file (if not exists)
            ConfigExampleGenerator.generateExampleFile();

            // 加载模组兼容性配置
            // Load mod compatibility configurations
            ModCompatibilityConfigLoader.loadModCompatibilityConfigs();

            // 加载实体配置
            // Load entity configurations
            EntityConfigLoader.loadEntityConfigs();

            // 初始化世界生成
            // Initialize world generation
            com.github.chromabreak.world.ModWorldGeneration.initialize();
        });
    }
}