package com.github.chromabreak;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

/**
 * ChromaBreakServer - ChromaBreak模组服务器端类
 * ChromaBreak Mod Server Class
 * <p>
 * 专门处理ChromaBreak模组的服务器端相关功能，不会在客户端上加载
 * Specifically handles server-side functionality for ChromaBreak mod, will not load on client
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 服务器端模组注册：在服务器环境中注册模组实例
 * Server mod registration: Registers mod instance in server environment
 * - 服务器端事件处理：处理服务器生命周期事件
 * Server event handling: Handles server lifecycle events
 * - 服务器端特定初始化：执行服务器特有的初始化代码
 * Server-specific initialization: Executes server-specific initialization code
 * <p>
 * 设计特点：
 * Design features:
 * - 服务器端专用：使用@Mod(dist = Dist.DEDICATED_SERVER)注解确保只在服务器加载
 * Server-only: Uses @Mod(dist = Dist.DEDICATED_SERVER) annotation to ensure loading only on server
 * - 事件总线订阅：使用@EventBusSubscriber订阅服务器端事件
 * Event bus subscription: Uses @EventBusSubscriber to subscribe to server events
 * <p>
 * 重要说明：此类不会在客户端上加载，只会在服务器环境中运行
 * Important note: This class will not load on client, only runs in server environment
 */
@Mod(value = ChromaBreak.MODID, dist = Dist.DEDICATED_SERVER)
@EventBusSubscriber(modid = ChromaBreak.MODID, value = Dist.DEDICATED_SERVER)
public class ChromaBreakServer {
    /**
     * 服务器端构造器
     * Server constructor
     * <p>
     * 在服务器环境中创建模组实例时调用
     * Called when creating mod instance in server environment
     *
     * @param container 模组容器，包含模组的元数据和配置信息
     *                  Mod container containing mod metadata and configuration information
     */
    public ChromaBreakServer(final ModContainer container) {
        ChromaBreak.LOGGER.info("ChromaBreak服务器端初始化 - ChromaBreak server initialization");
    }

    /**
     * 服务器端设置事件处理方法
     * Server setup event handler
     * <p>
     * 在服务器初始化时执行一些服务器端特定的设置代码
     * Executes some server-specific setup code during server initialization
     *
     * @param event 服务器端设置事件，包含服务器初始化相关信息
     *              Server setup event, containing server initialization related information
     */
    @SubscribeEvent
    static void onServerSetup(final FMLDedicatedServerSetupEvent event) {
        ChromaBreak.LOGGER.info("ChromaBreak服务器端设置完成 - ChromaBreak server setup completed");
    }
}
