package com.mobiusflip.enchantmentrebalance;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentSwiftStride extends Enchantment {
    public EnchantmentSwiftStride() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS});
    }

    public int getMinCost(int pEnchantmentLevel) {
        return pEnchantmentLevel * 20;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 40;
    }

    public int getMaxLevel() {
        return 4;
    }

    public boolean checkCompatibility(Enchantment pOther) {
        return pOther != MainEnchantmentRebalance.HIGH_CLIMB.get() && super.checkCompatibility(pOther);
    }
}
