package com.github.chromabreak.mixin;

import com.github.chromabreak.system.EntityHealthManager;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * LivingEntityMaxHealthMixin - LivingEntity最大生命值混入类
 * LivingEntity Max Health Mixin Class
 * <p>
 * 通过Mixin技术拦截LivingEntity的getMaxHealth方法，实现自定义最大生命值功能
 * Intercepts LivingEntity's getMaxHealth method through Mixin technology to implement custom max health functionality
 * <p>
 * 主要功能包括：
 * Main functionalities include:
 * - 方法拦截：在getMaxHealth方法调用时进行拦截
 * Method interception: Intercepts getMaxHealth method calls
 * - 自定义生命值检查：检查EntityHealthManager中是否有自定义最大生命值
 * Custom health check: Checks if EntityHealthManager has custom max health values
 * - 返回值重写：如果有自定义值，则返回自定义最大生命值
 * Return value override: Returns custom max health value if available
 * - KubeJS集成：为KubeJS脚本提供修改实体最大生命值的能力
 * KubeJS integration: Provides ability for KubeJS scripts to modify entity max health
 * <p>
 * Mixin技术说明：
 * Mixin technology explanation:
 * - @Mixin(LivingEntity.class)：指定要混入的目标类为LivingEntity
 *
 * @Mixin(LivingEntity.class): Specifies target class to mix into as LivingEntity
 * - @Inject(method = "getMaxHealth", at = @At("HEAD"))：在getMaxHealth方法头部注入代码
 * @Inject(method = "getMaxHealth", at = @At("HEAD")): Injects code at the head of getMaxHealth method
 * - cancellable = true：允许取消原方法的执行
 * cancellable = true: Allows cancellation of original method execution
 * <p>
 * 工作流程：
 * Workflow:
 * 1. 当游戏调用LivingEntity的getMaxHealth方法时
 * When game calls LivingEntity's getMaxHealth method
 * 2. Mixin在方法开始处拦截调用
 * Mixin intercepts the call at method start
 * 3. 检查EntityHealthManager中是否有该实体的自定义最大生命值
 * Checks if EntityHealthManager has custom max health for this entity
 * 4. 如果有自定义值，则设置回调返回值并取消原方法执行
 * If custom value exists, sets callback return value and cancels original method execution
 * 5. 如果没有自定义值，则继续执行原方法
 * If no custom value, continues with original method execution
 * <p>
 * 设计特点：
 * Design features:
 * - 非侵入式修改：通过Mixin技术在不修改原代码的情况下扩展功能
 * Non-invasive modification: Extends functionality without modifying original code through Mixin
 * - 性能优化：只在需要时进行自定义值检查，避免不必要的计算
 * Performance optimization: Only performs custom value check when needed, avoids unnecessary calculations
 * - 兼容性：与Minecraft原版系统和其他模组兼容
 * Compatibility: Compatible with vanilla Minecraft system and other mods
 * <p>
 * 使用场景：
 * Usage scenarios:
 * - 游戏平衡调整：通过KubeJS脚本动态调整实体最大生命值
 * Game balance adjustment: Dynamically adjust entity max health through KubeJS scripts
 * - 自定义游戏模式：创建特殊游戏模式，如高难度模式或休闲模式
 * Custom game modes: Create special game modes like hard mode or casual mode
 * - 服务器管理：服务器管理员可以动态调整实体属性
 * Server management: Server administrators can dynamically adjust entity attributes
 * - 模组集成：与其他模组配合，提供更灵活的生命值管理
 * Mod integration: Works with other mods to provide more flexible health management
 */
@Mixin(LivingEntity.class)
public class LivingEntityMaxHealthMixin {

    /**
     * 拦截getMaxHealth方法，如果EntityHealthManager中有自定义最大生命值，则返回自定义值
     * Intercept getMaxHealth method, return custom value if EntityHealthManager has custom max health
     *
     * @param cir 回调信息
     */
    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    private void chromabreak$getMaxHealth(final CallbackInfoReturnable<Float> cir) {
        final LivingEntity entity = (LivingEntity) (Object) this;
        if (EntityHealthManager.hasCustomMaxHealth(entity)) {
            cir.setReturnValue(EntityHealthManager.getEntityMaxHealth(entity));
        }
    }
}


