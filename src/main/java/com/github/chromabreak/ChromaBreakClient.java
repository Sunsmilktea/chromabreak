package com.github.chromabreak;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.

/**
 * ChromaBreakClient - ChromaBreak模组客户端类
 * ChromaBreak Mod Client Class
 * <p>
 * 专门处理ChromaBreak模组的客户端相关功能，不会在专用服务器上加载
 * Specifically handles client-side functionality for ChromaBreak mod, will not load on dedicated servers
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 客户端模组注册：在客户端环境中注册模组实例
 * Client mod registration: Registers mod instance in client environment
 * - 配置屏幕注册：为模组配置注册图形界面
 * Configuration screen registration: Registers GUI for mod configuration
 * - 客户端事件处理：处理客户端生命周期事件
 * Client event handling: Handles client lifecycle events
 * - 客户端特定初始化：执行客户端特有的初始化代码
 * Client-specific initialization: Executes client-specific initialization code
 * <p>
 * 设计特点：
 * Design features:
 * - 客户端专用：使用@Mod(dist = Dist.CLIENT)注解确保只在客户端加载
 * Client-only: Uses @Mod(dist = Dist.CLIENT) annotation to ensure loading only on client
 * - 事件总线订阅：使用@EventBusSubscriber订阅客户端事件
 * Event bus subscription: Uses @EventBusSubscriber to subscribe to client events
 * - 安全访问：从此类访问客户端代码是安全的
 * Safe access: Accessing client-side code from this class is safe
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 客户端模组初始化：在客户端启动时初始化模组
 * Client mod initialization: Initializes mod when client starts
 * - 配置界面：为玩家提供图形化的配置界面
 * Configuration interface: Provides graphical configuration interface for players
 * - 客户端渲染：处理客户端特有的渲染逻辑
 * Client rendering: Handles client-specific rendering logic
 * - 用户界面：管理模组的用户界面元素
 * User interface: Manages mod's user interface elements
 * <p>
 * 重要说明：此类不会在专用服务器上加载，只会在客户端环境中运行
 * Important note: This class will not load on dedicated servers, only runs in client environment
 */
@Mod(value = ChromaBreak.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ChromaBreak.MODID, value = Dist.CLIENT)
public class ChromaBreakClient {
    /**
     * 客户端构造器
     * Client constructor
     * <p>
     * 在客户端环境中创建模组实例时调用
     * Called when creating mod instance in client environment
     *
     * @param container 模组容器，包含模组的元数据和配置信息
     *                  Mod container containing mod metadata and configuration information
     *                  <p>
     *                  主要功能：
     *                  Main functionalities:
     *                  - 注册配置屏幕扩展点，允许NeoForge为模组创建配置界面
     *                  Registers configuration screen extension point, allowing NeoForge to create configuration interface for mod
     *                  - 设置模组的客户端特定配置
     *                  Sets up client-specific configuration for mod
     *                  <p>
     *                  配置屏幕访问方式：
     *                  Configuration screen access method:
     *                  1. 打开Minecraft主菜单
     *                  Open Minecraft main menu
     *                  2. 进入"模组"屏幕
     *                  Go to "Mods" screen
     *                  3. 点击"ChromaBreak"模组
     *                  Click on "ChromaBreak" mod
     *                  4. 点击"配置"按钮
     *                  Click on "Config" button
     *                  <p>
     *                  注意：需要为配置选项在en_us.json文件中添加翻译
     *                  Note: Need to add translations for configuration options in en_us.json file
     */
    public ChromaBreakClient(final ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        /*
          允许NeoForge为这个模组的配置创建配置屏幕
          配置屏幕可以通过以下方式访问：模组屏幕 > 点击你的模组 > 点击配置
          不要忘记为你的配置选项在en_us.json文件中添加翻译
          <p>
          Allows NeoForge to create a config screen for this mod's configs.
          The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
          Do not forget to add translations for your config options to the en_us.json file.
         */
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    /**
     * 客户端设置事件处理方法
     * Client setup event handler
     * <p>
     * 在客户端初始化时执行一些客户端特定的设置代码
     * Executes some client-specific setup code during client initialization
     *
     * @param event 客户端设置事件，包含客户端初始化相关信息
     *              Client setup event, containing client initialization related information
     *              <p>
     *              调用时机：在客户端完全初始化后调用
     *              Call timing: Called after client is fully initialized
     *              <p>
     *              主要功能：
     *              Main functionalities:
     *              - 记录客户端设置日志信息
     *              Log client setup information
     *              - 获取并记录当前玩家名称
     *              Get and log current player name
     *              - 执行其他客户端特定的初始化代码
     *              Execute other client-specific initialization code
     *              <p>
     *              使用场景：用于初始化客户端特有的功能，如渲染器、用户界面等
     *              Usage scenario: Used to initialize client-specific features like renderers, user interfaces, etc.
     */
    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        ChromaBreak.LOGGER.info("HELLO FROM CLIENT SETUP");
        ChromaBreak.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
