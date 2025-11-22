package com.mobiusflip.enchantmentrebalance;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentCollector extends Enchantment {
    public EnchantmentCollector() {
        super(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 15;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof net.minecraft.world.item.ShearsItem || super.canEnchant(pStack);
    }

    public boolean checkCompatibility(Enchantment pOther) {
        return pOther != MainEnchantmentRebalance.OUTSTRETCH.get() && super.checkCompatibility(pOther);
    }
}
