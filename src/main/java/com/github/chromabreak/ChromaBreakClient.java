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
 * ChromaBreak模组客户端类
 * <p>
 * ChromaBreak Mod Client Class
 * <p>
 * 这个类专门处理客户端相关的功能，不会在专用服务器上加载。
 * 从这里访问客户端代码是安全的。
 * <p>
 * This class handles client-side specific functionality and will not load on dedicated servers.
 * Accessing client side code from here is safe.
 */
@Mod(value = ChromaBreak.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ChromaBreak.MODID, value = Dist.CLIENT)
public class ChromaBreakClient {
    /**
     * 客户端构造器
     * <p>
     * Client constructor
     *
     * @param container 模组容器，包含模组的元数据和配置信息
     *                  Mod container containing mod metadata and configuration information
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
     * 在客户端初始化时执行一些客户端特定的设置代码
     * <p>
     * Client setup event handler
     * Executes some client-specific setup code during client initialization
     *
     * @param event 客户端设置事件
     *              Client setup event
     */
    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        ChromaBreak.LOGGER.info("HELLO FROM CLIENT SETUP");
        ChromaBreak.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
