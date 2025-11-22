package com.mobiusflip.enchantmentrebalance;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentGuillotine extends Enchantment {
    public EnchantmentGuillotine() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 25 + (pEnchantmentLevel - 1) * 12;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return super.getMinCost(pEnchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }
}
