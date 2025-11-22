package com.mobiusflip.enchantmentrebalance.mixin;

import com.mobiusflip.enchantmentrebalance.MainEnchantmentRebalance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @ModifyVariable(method = "attack", at = @At(value = "STORE"))
    private boolean noSweep(boolean flag3) {
        Player player = (Player) (Object) this;
        ItemStack weapon = player.getMainHandItem();
        if(weapon.getEnchantmentLevel(MainEnchantmentRebalance.PRECISE_POINT.get()) > 0) {
            return false;
        }
        return flag3;
    }

    @ModifyArg(method = "causeFoodExhaustion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"), index = 0)
    private float modifyEndurance(float original) {
        Player player = (Player)(Object) this;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        int pLevel = chest.getEnchantmentLevel(MainEnchantmentRebalance.ENDURANCE.get());
        if(pLevel <= 0) return original;
        return original / (1f + pLevel);
    }

}
