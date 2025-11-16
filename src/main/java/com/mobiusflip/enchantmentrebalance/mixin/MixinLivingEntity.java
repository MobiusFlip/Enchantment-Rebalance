package com.mobiusflip.enchantmentrebalance.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @ModifyArg(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageProtection(Ljava/lang/Iterable;Lnet/minecraft/world/damagesource/DamageSource;)I"))
    private Iterable<ItemStack> shieldProtection(Iterable<ItemStack> armorSlots, DamageSource source) {
        LivingEntity entity = (LivingEntity)(Object) this;
        List<ItemStack> armorShield = new ArrayList<>();
        armorSlots.forEach(armorShield::add);
        if(entity.getOffhandItem().getItem() instanceof ShieldItem) {
            armorShield.add(entity.getOffhandItem());
        }
        return armorShield;
    }
}
