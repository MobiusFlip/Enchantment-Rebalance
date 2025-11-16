package com.mobiusflip.enchantmentrebalance.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void shieldProtectionAnvil(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchant = (Enchantment)(Object) this;
        if(enchant instanceof ProtectionEnchantment && stack.getItem() instanceof ShieldItem) {
            if(((ProtectionEnchantment) enchant).type != ProtectionEnchantment.Type.FALL) { cir.setReturnValue(true); }
        }
    }
}
