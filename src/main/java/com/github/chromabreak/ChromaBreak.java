package com.github.chromabreak;

import com.github.chromabreak.config.ConfigExampleGenerator;
import com.github.chromabreak.config.EntityConfigLoader;
import com.github.chromabreak.config.ModCompatibilityConfigLoader;
import com.github.chromabreak.tool.WorldGenCommands;
import com.github.chromabreak.util.ModBlocks;
import com.github.chromabreak.util.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

/**
 * ChromaBreak模组主类 - ChromaBreak Mod Main Class
 * <p>
 * 这是模组的主入口点，负责模组的初始化和核心配置。
 * 具体的功能实现已经分离到专门的包和类中：
 * - events包：处理各种事件监听
 * - config包：处理配置相关功能
 * - blocks包：处理方块相关功能
 * - items包：处理物品相关功能
 * - system包：处理核心系统功能
 * - world包：处理世界生成功能
 * <p>
 * This is the main entry point of the mod, responsible for mod initialization and core configuration.
 * Specific functionality implementations have been separated into dedicated packages and classes:
 * - events package: handles various event listeners
 * - config package: handles configuration-related functionality
 * - blocks package: handles block-related functionality
 * - items package: handles item-related functionality
 * - system package: handles core system functionality
 * - world package: handles world generation functionality
 *
 * @Mod 注解标识这是一个NeoForge模组主类
 * @Mod annotation identifies this as a NeoForge mod main class
 */
@Mod(ChromaBreak.MODID)
public class ChromaBreak {
    /**
     * 模组ID，在公共位置定义以供所有地方引用
     * Define mod id in a common place for everything to reference
     * <p>
     * 这个常量在整个模组中被广泛使用，包括：
     * This constant is widely used throughout the mod, including:
     * - 资源定位（纹理、模型、语言文件等）
     * Resource location (textures, models, language files, etc.)
     * - 配置文件和注册表
     * Configuration files and registries
     * - 网络通信和数据存储
     * Network communication and data storage
     */
    public static final String MODID = "chromabreak";

    /**
     * 直接引用slf4j日志记录器
     * Directly reference a slf4j logger
     * <p>
     * 用于在整个模组中记录日志信息
     * Used for logging information throughout the mod
     * <p>
     * 使用LogUtils.getLogger()自动获取正确的日志记录器
     * Uses LogUtils.getLogger() to automatically get the correct logger
     */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * 模组构造器 - Mod constructor
     * <p>
     * 当模组加载时，这是第一个被运行的代码
     * FML会自动识别IEventBus和ModContainer等参数类型并传入
     * <p>
     * This is the first code that runs when your mod is loaded
     * FML automatically recognizes parameter types like IEventBus or ModContainer and passes them in
     *
     * @param modEventBus  模组事件总线，用于注册事件监听器
     *                     Mod event bus for registering event listeners
     * @param modContainer 模组容器，包含模组的元数据和配置信息
     *                     Mod container containing mod metadata and configuration information
     */
    public ChromaBreak(final IEventBus modEventBus, final ModContainer modContainer) {
        // 注册模组的配置规范，以便FML可以为我们创建和加载配置文件
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // 注册物品、方块到模组事件总线
        // Register items and blocks to mod event bus
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        // 注册模组事件总线监听器 - 通用设置事件
        // Register mod event bus listener - common setup event
        modEventBus.addListener(this::onCommonSetup);

        // 注册Forge事件总线监听器 - 命令注册事件
        // Register Forge event bus listener - command registration event
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);

        // 记录模组初始化完成日志
        // Log mod initialization completion
        ChromaBreak.LOGGER.info("ChromaBreak模组初始化完成 - ChromaBreak mod initialization completed");
    }

    /**
     * 注册命令 - Register commands
     * <p>
     * 这个方法在命令注册事件中被调用，用于注册模组的自定义命令
     * This method is called during the command registration event to register custom mod commands
     *
     * @param event 命令注册事件，包含命令分发器
     *              Command registration event containing the command dispatcher
     */
    private void onRegisterCommands(final RegisterCommandsEvent event) {
        // 注册结构查找器命令
        // Register structure finder command
        WorldGenCommands.register(event.getDispatcher());

        // 记录命令注册成功日志
        // Log successful command registration
        ChromaBreak.LOGGER.info("WorldGenCommands命令已注册 - WorldGenCommands command registered");
    }

    /**
     * 通用设置事件处理器 - Common setup event handler
     * <p>
     * 在模组初始化时加载实体配置和其他初始化任务
     * Load entity configurations and other initialization tasks during mod initialization
     * <p>
     * 使用event.enqueueWork()确保任务在正确的线程中执行
     * Uses event.enqueueWork() to ensure tasks are executed in the correct thread
     *
     * @param event 通用设置事件
     *              Common setup event
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

            // 初始化网络系统
            // Initialize network system
            ChromaBreak.LOGGER.info("ChromaBreak网络系统初始化 - ChromaBreak network system initialization");

            // 记录通用设置完成日志
            // Log common setup completion
            ChromaBreak.LOGGER.info("ChromaBreak通用设置完成 - ChromaBreak common setup completed");
        });
    }
}