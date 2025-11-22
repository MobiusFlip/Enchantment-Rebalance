package com.mobiusflip.enchantmentrebalance.mixin;

import com.mobiusflip.enchantmentrebalance.MainEnchantmentRebalance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(FoodData.class)
public abstract class MixinFoodData {
    private int getVitality(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        return chest.getEnchantmentLevel(MainEnchantmentRebalance.VITALITY.get());
    }

    @ModifyConstant(method = "tick", constant = @org.spongepowered.asm.mixin.injection.Constant(intValue = 18))
    private int modifyRegenReq(int original, Player player) {
        int pLevel = getVitality(player);
        if(pLevel <= 0) return original;

        return Math.max(6, original - pLevel * 2);
    }

    @ModifyConstant(method = "tick", constant = @org.spongepowered.asm.mixin.injection.Constant(intValue = 80))
    private int modifyRegenRate(int original, Player player) {
        int pLevel = getVitality(player);
        if(pLevel <= 0) return original;

        return Math.max(10, original - 15 - pLevel * 15);
    }
}
