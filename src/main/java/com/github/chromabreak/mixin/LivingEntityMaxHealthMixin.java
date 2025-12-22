package com.github.chromabreak.mixin;

import com.github.chromabreak.system.EntityHealthManager;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * LivingEntity最大生命值Mixin
 * 允许KubeJS通过EntityHealthManager修改生物的最大生命值
 * <p>
 * LivingEntity Max Health Mixin
 * Allows KubeJS to modify entity max health via EntityHealthManager
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


