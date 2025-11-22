package com.mobiusflip.enchantmentrebalance;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class EnchantmentHunter extends Enchantment {
    public EnchantmentHunter() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 8 * (pEnchantmentLevel - 1);
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 20;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean checkCompatibility(Enchantment pOther) {
        return pOther != Enchantments.SHARPNESS && pOther != Enchantments.SMITE && pOther != Enchantments.BANE_OF_ARTHROPODS && pOther != MainEnchantmentRebalance.SENTINEL.get() && super.checkCompatibility(pOther);
    }

    public boolean canEnchant(ItemStack pStack) {
        return Enchantments.SHARPNESS.canEnchant(pStack) || super.canEnchant(pStack);
    }
}
