package com.github.chromabreak.network;

import com.github.chromabreak.ChromaBreak;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * NetworkEvents - 网络事件处理器
 * Network Events Handler
 * <p>
 * 负责注册和处理ChromaBreak模组的网络相关事件
 * Responsible for registering and handling network-related events for ChromaBreak mod
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 注册数据包处理器：在模组初始化时注册所有数据包类型和处理器
 * Register packet handlers: Registers all packet types and handlers during mod initialization
 * - 处理网络事件：响应网络相关的事件
 * Handle network events: Responds to network-related events
 * <p>
 * 设计特点：
 * Design features:
 * - 事件驱动：基于NeoForge事件总线系统
 * Event-driven: Based on NeoForge event bus system
 * - 模块化：将网络相关逻辑集中管理
 * Modular: Centralizes network-related logic
 * - 线程安全：确保在多线程环境下的安全操作
 * Thread-safe: Ensures safe operations in multi-threaded environments
 */
@net.neoforged.fml.common.EventBusSubscriber(modid = ChromaBreak.MODID)
public enum NetworkEvents {
    ;

    /**
     * 注册数据包处理器
     * Register packet handlers
     * <p>
     * 在模组初始化时调用，注册所有数据包类型和处理器
     * Called during mod initialization to register all packet types and handlers
     *
     * @param event 数据包处理器注册事件
     *              Packet handler registration event
     */
    @SubscribeEvent
    public static void onRegisterPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        PacketHandler.register(event);
    }
}
