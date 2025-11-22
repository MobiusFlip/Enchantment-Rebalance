package com.mobiusflip.enchantmentrebalance;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentVitality extends Enchantment {
    public EnchantmentVitality() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    public int getMinCost(int pEnchantmentLevel) {
        return pEnchantmentLevel * 15;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 30;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment pOther) {
        return pOther != MainEnchantmentRebalance.ENDURANCE.get() && super.checkCompatibility(pOther);
    }
}
