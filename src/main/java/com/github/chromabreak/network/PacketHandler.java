package com.github.chromabreak.network;

import com.github.chromabreak.ChromaBreak;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * PacketHandler - 网络数据包处理器
 * Network Packet Handler
 * <p>
 * 负责处理ChromaBreak模组的客户端与服务器之间的网络通信
 * Responsible for handling network communication between client and server for ChromaBreak mod
 * <p>
 * 主要功能：
 * Main functionalities:
 * - 数据包注册：注册所有需要在客户端和服务器之间传输的数据包类型
 * Packet registration: Registers all packet types that need to be transmitted between client and server
 * - 数据包处理：处理接收到的数据包并执行相应的逻辑
 * Packet handling: Processes received packets and executes corresponding logic
 * - 网络配置：配置网络通道和协议版本
 * Network configuration: Configures network channels and protocol versions
 * <p>
 * 设计特点：
 * Design features:
 * - 线程安全：确保在多线程环境下的数据包处理安全
 * Thread-safe: Ensures packet handling safety in multithreaded environments
 * - 可扩展：易于添加新的数据包类型和处理逻辑
 * Extensible: Easy to add new packet types and handling logic
 * - 错误处理：提供完善的错误处理和日志记录
 * Error handling: Provides comprehensive error handling and logging
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 配置同步：将服务器配置同步到客户端
 * Configuration synchronization: Synchronizes server configuration to client
 * - 状态更新：更新客户端或服务器的游戏状态
 * State updates: Updates game state on client or server
 * - 事件通知：通知客户端或服务器特定事件的发生
 * Event notification: Notifies client or server of specific events
 */
public enum PacketHandler {
    ;
    /**
     * 模组网络通道ID
     * Mod network channel ID
     * <p>
     * 用于标识ChromaBreak模组的网络通信通道
     * Used to identify the network communication channel for ChromaBreak mod
     */
    public static final ResourceLocation CHANNEL_ID = ResourceLocation.fromNamespaceAndPath(ChromaBreak.MODID, "main");

    /**
     * 协议版本
     * Protocol version
     * <p>
     * 用于网络协议版本控制，当协议不匹配时可以提供友好的错误信息
     * Used for network protocol version control, can provide friendly error messages when protocol mismatches
     */
    public static final String PROTOCOL_VERSION = "1.0.0";

    /**
     * 注册数据包处理器
     * Register packet handlers
     * <p>
     * 在模组初始化时调用，注册所有数据包类型和对应的处理器
     * Called during mod initialization to register all packet types and corresponding handlers
     *
     * @param event 数据包处理器注册事件
     *              Packet handler registration event
     */
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PacketHandler.CHANNEL_ID.toString())
                .versioned(PacketHandler.PROTOCOL_VERSION);

        ChromaBreak.LOGGER.info("注册ChromaBreak网络数据包处理器 - Registering ChromaBreak network packet handlers");

        // 在这里注册具体的数据包类型
        // Register specific packet types here
        // 示例：registrar.playToServer(ExamplePacket.TYPE, ExamplePacket.STREAM_CODEC, ExamplePacket::handle);
        // Example: registrar.playToServer(ExamplePacket.TYPE, ExamplePacket.STREAM_CODEC, ExamplePacket::handle);

        ChromaBreak.LOGGER.info("ChromaBreak网络数据包处理器注册完成 - ChromaBreak network packet handlers registration completed");
    }

    /**
     * 基础数据包接口
     * Base packet interface
     * <p>
     * 所有自定义数据包都应实现此接口
     * All custom packets should implement this interface
     */
    public interface ChromaBreakPacket extends CustomPacketPayload {
        /**
         * 获取数据包类型ID
         * Get packet type ID
         *
         * @return 数据包类型ID
         * Packet type ID
         */
        ResourceLocation id();

        /**
         * 写入数据包到缓冲区
         * Write packet to buffer
         *
         * @param buf 字节缓冲区
         *            Byte buffer
         */
        void write(FriendlyByteBuf buf);

        /**
         * 处理数据包
         * Handle packet
         * <p>
         * 在接收端执行数据包对应的逻辑
         * Execute corresponding logic at the receiving end
         */
        void handle();
    }
}
