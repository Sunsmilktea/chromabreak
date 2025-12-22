package com.github.chromabreak.mixin;

import com.github.chromabreak.system.ToughnessSystem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * LivingEntity Mixin
 * 修改伤害计算，实现韧性条系统的伤害减免机制
 * <p>
 * LivingEntity Mixin
 * Modifies damage calculation to implement toughness-based damage reduction
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     * 在伤害计算前注入，实现韧性条系统的伤害减免
     * Inject before damage calculation to implement toughness-based damage reduction
     *
     * @param source 伤害来源
     * @param amount 原始伤害值
     * @param cir    回调信息
     */
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir) {
        final LivingEntity entity = (LivingEntity) (Object) this;

        // Skip if entity is a player or dead
        // 如果是玩家或死亡的实体则跳过
        if (entity instanceof net.minecraft.world.entity.player.Player || !entity.isAlive()) {
            return;
        }

        // Check if entity has toughness and it's not broken
        // 检查实体是否有韧性且韧性未被破坏
        if (!ToughnessSystem.isToughnessBroken(entity)) {
            final float toughnessPercentage = ToughnessSystem.getToughnessPercentage(entity);

            // If toughness is present (percentage > 0), apply damage reduction
            // 如果韧性存在（百分比 > 0），应用伤害减免
            if (0 < toughnessPercentage) {
                // Check if damage can bypass toughness (void damage, guns, avaritia, or configured bypass types)
                // 检查伤害是否可以绕过韧性（虚空伤害、枪械、无尽贪婪或配置的绕过类型）
                if (com.github.chromabreak.system.ModCompatibilityManager.canBypassToughness(source)) {
                    // Damage can bypass toughness, deal full damage
                    // 伤害可以绕过韧性，造成全额伤害
                    return;
                }

                // Check if this is a direct health damage source (guns, avaritia weapons, etc.)
                // 检查这是否是直接血量伤害源（枪械、无尽贪婪武器等）
                if (com.github.chromabreak.system.ModCompatibilityManager.isDirectHealthDamage(source)) {
                    // Direct health damage sources bypass toughness (except void damage which is already handled above)
                    // 直接血量伤害源绕过韧性（除了虚空伤害，已在上面处理）
                    return;
                }

                // Get base damage (remove critical hit, strength, and other buff bonuses)
                // 获取基础伤害（移除暴击、力量等buff加成）
                final float baseDamage = this.getBaseDamage(source, amount);

                // Check if the attacker has a weapon with break toughness component
                // 检查攻击者是否有带有破韧属性的武器
                final boolean canBreakToughness = this.canBreakToughness(source);

                // Check if weapon can break toughness colors
                // 检查武器是否可以破坏韧性颜色
                final com.github.chromabreak.system.ToughnessColorDistribution colorDistribution =
                        com.github.chromabreak.system.ToughnessSystem.getColorDistribution(entity);
                final boolean canBreakColors = com.github.chromabreak.system.WeaponColorHelper.canBreakDistribution(
                        this.getAttackerWeapon(source), colorDistribution);

                // 武器必须同时有破韧属性和匹配的颜色才能破韧
                // Weapon must have both break toughness attribute and matching colors to break toughness
                if (!canBreakToughness || !canBreakColors) {
                    // Weapon doesn't have break toughness component, apply full damage reduction
                    // 武器没有破韧属性，应用全额伤害减免
                    // Don't reduce toughness if weapon can't break it
                    // 如果武器无法破韧，则不减少韧性
                    // Control damage to 0.02-0.05 range (fixed range, not percentage)
                    // 无论原始伤害多高（包括来自其他模组的极高伤害），都限制在0.02-0.05范围内
                    // No matter how high the original damage is (including extremely high damage from other mods),
                    // limit it to 0.02-0.05 range
                    final float randomFactor = 0.02f + (float) Math.random() * 0.03f; // 0.02 to 0.05
                    final float reducedDamage = randomFactor;

                    // Apply the reduced damage directly to health (avoid recursion)
                    // 直接将减免后的伤害应用到生命值（避免递归）
                    if (0 < reducedDamage) {
                        final float newHealth = Math.max(0.0f, entity.getHealth() - reducedDamage);
                        entity.setHealth(newHealth);
                        cir.setReturnValue(true);
                        return;
                    } else {
                        // Damage is completely absorbed by toughness
                        // 伤害被韧性完全吸收
                        cir.setReturnValue(false);
                        return;
                    }
                } else {
                    // Weapon has break toughness component and matching colors, can reduce toughness and deal more damage
                    // 武器有破韧属性和匹配的颜色，可以减少韧性并造成更多伤害
                    // Reduce toughness based on the base damage amount (not modified damage)
                    // 根据基础伤害量减少韧性（不是修改后的伤害）
                    final float oldToughness = ToughnessSystem.getToughness(entity);
                    ToughnessSystem.reduceToughness(entity, baseDamage);

                    // Check if toughness was broken by this attack
                    // 检查韧性是否被此次攻击破坏
                    if (ToughnessSystem.isToughnessBroken(entity) && 0 < oldToughness) {
                        // Toughness broken, allow normal damage processing (don't cancel)
                        // 韧性已破，允许正常伤害处理（不取消）
                        return;
                    } else {
                        // Still has toughness, but weapon can break it, so deal more damage (50% of base)
                        // 仍有韧性，但武器可以破韧，所以造成更多伤害（基础伤害的50%）
                        final float reducedDamage = baseDamage * 0.5f;
                        if (0 < reducedDamage) {
                            final float newHealth = Math.max(0.0f, entity.getHealth() - reducedDamage);
                            entity.setHealth(newHealth);
                            cir.setReturnValue(true);
                            return;
                        } else {
                            cir.setReturnValue(false);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取基础伤害（移除暴击、力量等buff加成）
     * Get base damage (remove critical hit, strength, and other buff bonuses)
     *
     * @param source         伤害来源
     * @param modifiedAmount 修改后的伤害值（包含所有加成）
     * @return 基础伤害值
     */
    private float getBaseDamage(final DamageSource source, final float modifiedAmount) {
        // Get attacker entity
        // 获取攻击者实体
        if (null == source.getEntity() || !(source.getEntity() instanceof final net.minecraft.world.entity.LivingEntity attacker)) {
            return modifiedAmount; // Can't determine base damage, return as-is
        }

        // Get main hand item
        // 获取主手物品
        final net.minecraft.world.item.ItemStack mainHandItem = attacker.getMainHandItem();
        if (mainHandItem.isEmpty()) {
            return modifiedAmount; // No weapon, return as-is
        }

        // Get base weapon damage
        // 获取基础武器伤害
        float baseDamage = 1.0f; // Default base damage

        // Try to get weapon attack damage
        // 尝试获取武器攻击伤害
        try {
            // Check for attack damage attribute
            // 检查攻击伤害属性
            final var attackDamageComponent = net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS;
            final var attributeModifiers = mainHandItem.get(attackDamageComponent);
            if (null != attributeModifiers) {
                // Try to find attack damage modifier
                // 尝试查找攻击伤害修饰符
                final var attackDamageAttribute = net.minecraft.core.registries.BuiltInRegistries.ATTRIBUTE.get(net.minecraft.resources.ResourceLocation.parse("minecraft:generic.attack_damage"));
                if (null != attackDamageAttribute) {
                    for (final var modifier : attributeModifiers.modifiers()) {
                        if (modifier.attribute().equals(attackDamageAttribute)) {
                            // Found attack damage modifier
                            // 找到攻击伤害修饰符
                            // Use reflection to get amount value
                            // 使用反射获取amount值
                            try {
                                final java.lang.reflect.Method amountMethod = modifier.getClass().getMethod("amount");
                                final double value = (Double) amountMethod.invoke(modifier);
                                baseDamage = (float) value;
                                break;
                            } catch (final Exception e) {
                                // Try alternative method names
                                // 尝试替代方法名
                                try {
                                    final java.lang.reflect.Method getAmountMethod = modifier.getClass().getMethod("getAmount");
                                    final double value = (Double) getAmountMethod.invoke(modifier);
                                    baseDamage = (float) value;
                                    break;
                                } catch (final Exception e2) {
                                    // Try value() method
                                    // 尝试 value() 方法
                                    try {
                                        final java.lang.reflect.Method valueMethod = modifier.getClass().getMethod("value");
                                        final double value = (Double) valueMethod.invoke(modifier);
                                        baseDamage = (float) value;
                                        break;
                                    } catch (final Exception e3) {
                                        // Ignore, use default
                                        // 忽略，使用默认值
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions, use default
            // 忽略异常，使用默认值
        }

        // If we couldn't get weapon damage, estimate from item type
        // 如果无法获取武器伤害，从物品类型估算
        if (1.0f >= baseDamage) {
            final net.minecraft.world.item.Item item = mainHandItem.getItem();
            // Try to get attack damage from item properties
            // 尝试从物品属性获取攻击伤害
            try {
                // Use reflection to get attack damage if available
                // 如果可用，使用反射获取攻击伤害
                final java.lang.reflect.Method getAttackDamageMethod = item.getClass().getMethod("getAttackDamage");
                final double attackDamage = (Double) getAttackDamageMethod.invoke(item);
                if (0 < attackDamage) {
                    baseDamage = (float) attackDamage;
                }
            } catch (final Exception e) {
                // Ignore exceptions
                // 忽略异常
            }
        }

        // Remove critical hit bonus (critical hits deal 1.5x damage)
        // 移除暴击加成（暴击造成1.5倍伤害）
        float estimatedDamage = baseDamage;
        if (this.isCriticalHit(source, attacker)) {
            // If this is a critical hit, divide by 1.5 to get base damage
            // 如果是暴击，除以1.5得到基础伤害
            estimatedDamage = modifiedAmount / 1.5f;
        } else {
            estimatedDamage = modifiedAmount;
        }

        // Remove strength effect bonus (each level adds 3 damage)
        // 移除力量效果加成（每级增加3点伤害）
        if (attacker.hasEffect(net.minecraft.world.effect.MobEffects.DAMAGE_BOOST)) {
            final var strengthEffect = attacker.getEffect(net.minecraft.world.effect.MobEffects.DAMAGE_BOOST);
            if (null != strengthEffect) {
                final int amplifier = strengthEffect.getAmplifier();
                // Each level of strength adds 3 damage
                // 每级力量增加3点伤害
                estimatedDamage = Math.max(0.0f, estimatedDamage - (amplifier + 1) * 3.0f);
            }
        }

        // Use the smaller value between base weapon damage and estimated damage
        // 使用基础武器伤害和估算伤害中的较小值
        return Math.min(baseDamage, estimatedDamage);
    }

    /**
     * 检查是否为暴击
     * Check if this is a critical hit
     *
     * @param source   伤害来源
     * @param attacker 攻击者
     * @return 是否为暴击
     */
    private boolean isCriticalHit(final DamageSource source, final net.minecraft.world.entity.LivingEntity attacker) {
        // Check if attacker is a player and is performing a critical hit
        // 检查攻击者是否是玩家且正在执行暴击
        if (attacker instanceof final net.minecraft.world.entity.player.Player player) {
            // Check if player is falling (jump attack = critical hit)
            // 检查玩家是否在下落（跳劈 = 暴击）
            if (player.fallDistance > 0.0f && !player.onGround() && !player.isInWater() && !player.isPassenger()) {
                return true;
            }
        }

        // Check damage source for critical hit indicators
        // 检查伤害来源的暴击指示器
        final String msgId = source.getMsgId().toLowerCase();
        return msgId.contains("critical") || msgId.contains("crit");
    }

    /**
     * 检查伤害来源的武器是否有破韧属性
     * Check if the weapon from damage source has break toughness component
     *
     * @param source 伤害来源
     * @return 是否有破韧属性
     */
    private boolean canBreakToughness(final DamageSource source) {
        // Get the attacker entity
        // 获取攻击者实体
        if (null == source.getEntity() || !(source.getEntity() instanceof final net.minecraft.world.entity.LivingEntity attacker)) {
            return false;
        }

        // Get the main hand item
        // 获取主手物品
        final net.minecraft.world.item.ItemStack mainHandItem = attacker.getMainHandItem();

        // Check if item has break toughness component
        // 检查物品是否有破韧组件
        // In Minecraft 1.21.1, we use DataComponents to check for custom attributes
        // 在Minecraft 1.21.1中，我们使用DataComponents来检查自定义属性
        if (mainHandItem.isEmpty()) {
            return false;
        }

        // Check for break toughness component using DataComponents (Minecraft 1.21.1+)
        // 使用DataComponents检查破韧组件（Minecraft 1.21.1+）
        try {
            final net.minecraft.core.component.DataComponentMap components = mainHandItem.getComponents();
            if (null != components) {
                // Check for custom data component
                // 检查自定义数据组件
                // In 1.21.1, custom data is stored in CustomData component
                // 在1.21.1中，自定义数据存储在CustomData组件中
                final var customDataType = net.minecraft.core.component.DataComponents.CUSTOM_DATA;
                final var customData = components.get(customDataType);
                if (null != customData) {
                    // Try to get the tag using available methods
                    // 尝试使用可用方法获取标签
                    // Use reflection to access the tag safely
                    // 使用反射安全地访问标签
                    try {
                        // Try copyTag() method first
                        // 首先尝试 copyTag() 方法
                        final java.lang.reflect.Method copyTagMethod = customData.getClass().getMethod("copyTag");
                        final net.minecraft.nbt.CompoundTag tag = (net.minecraft.nbt.CompoundTag) copyTagMethod.invoke(customData);
                        if (null != tag && tag.getBoolean("chromabreak:break_toughness")) {
                            return true;
                        }
                    } catch (final NoSuchMethodException e) {
                        // Try getUnsafe() method
                        // 尝试 getUnsafe() 方法
                        try {
                            final java.lang.reflect.Method getUnsafeMethod = customData.getClass().getMethod("getUnsafe");
                            final net.minecraft.nbt.CompoundTag tag = (net.minecraft.nbt.CompoundTag) getUnsafeMethod.invoke(customData);
                            if (null != tag && tag.getBoolean("chromabreak:break_toughness")) {
                                return true;
                            }
                        } catch (final Exception e2) {
                            // Try toString() and parse (last resort)
                            // 尝试 toString() 并解析（最后手段）
                            final String dataStr = customData.toString();
                            if (dataStr.contains("chromabreak:break_toughness") && dataStr.contains("true")) {
                                return true;
                            }
                        }
                    } catch (final Exception e) {
                        // Ignore other exceptions
                        // 忽略其他异常
                    }
                }
            }
        } catch (final Exception e) {
            // Ignore exceptions, fall through to other checks
            // 忽略异常，继续其他检查
        }

        // Method 3: Check item ID or type for special items
        // 方法3：检查特殊物品的ID或类型
        final net.minecraft.resources.ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(mainHandItem.getItem());
        if (null != itemId) {
            final String itemIdString = itemId.toString().toLowerCase();
            // Check if item name contains break toughness indicator
            // 检查物品名称是否包含破韧指示器
            return itemIdString.contains("break_toughness") || itemIdString.contains("breaktoughness");
        }

        return false;
    }

    /**
     * 获取攻击者的武器
     * Get attacker's weapon
     *
     * @param source 伤害来源
     * @return 武器物品堆栈，如果不存在则返回空堆栈
     */
    private net.minecraft.world.item.ItemStack getAttackerWeapon(final DamageSource source) {
        if (null == source.getEntity() || !(source.getEntity() instanceof final net.minecraft.world.entity.LivingEntity attacker)) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        return attacker.getMainHandItem();
    }
}