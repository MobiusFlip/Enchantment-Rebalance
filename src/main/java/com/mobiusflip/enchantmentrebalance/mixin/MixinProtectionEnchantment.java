package com.mobiusflip.enchantmentrebalance.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public abstract class MixinProtectionEnchantment extends Enchantment {

    protected MixinProtectionEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(5);
    }

    @Inject(method = "getDamageProtection", at = @At("HEAD"), cancellable = true)
    private void newDamageProtection(int pLevel, DamageSource pSource, CallbackInfoReturnable<Integer> cir) {
        ProtectionEnchantment enchant = (ProtectionEnchantment)(Object)this;
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            cir.setReturnValue(0);
        } else if (enchant.type == ProtectionEnchantment.Type.ALL && (pSource.is(DamageTypes.FLY_INTO_WALL) || pSource.is(DamageTypes.MOB_ATTACK) || pSource.is(DamageTypes.MOB_ATTACK_NO_AGGRO) || pSource.is(DamageTypes.PLAYER_ATTACK) || pSource.is(DamageTypes.STING))) {
            cir.setReturnValue(pLevel * 2);
        } else if (enchant.type == ProtectionEnchantment.Type.FIRE && (pSource.is(DamageTypeTags.IS_FIRE) || pSource.is(DamageTypes.CACTUS) || pSource.is(DamageTypes.FREEZE) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.SWEET_BERRY_BUSH))) {
            cir.setReturnValue(pLevel * 2);
        } else if (enchant.type == ProtectionEnchantment.Type.FALL && pSource.is(DamageTypeTags.IS_FALL)) {
            cir.setReturnValue(pLevel * 4);
        } else if (enchant.type == ProtectionEnchantment.Type.EXPLOSION && (pSource.is(DamageTypeTags.IS_EXPLOSION) || pSource.is(DamageTypeTags.WITCH_RESISTANT_TO) || pSource.is(DamageTypes.WITHER))) {
            cir.setReturnValue(pLevel * 2);
        } else {
            cir.setReturnValue(enchant.type == ProtectionEnchantment.Type.PROJECTILE && (pSource.is(DamageTypeTags.IS_PROJECTILE) || pSource.is(DamageTypeTags.DAMAGES_HELMET)) ? pLevel * 2 : 0);
        }
    }

    @Inject(method = "checkCompatibility", at = @At("HEAD"), cancellable = true)
    private void exclusiveFeatherFall(Enchantment pEnch, CallbackInfoReturnable<Boolean> cir) {
        ProtectionEnchantment enchant = (ProtectionEnchantment)(Object)this;
        if (pEnch instanceof ProtectionEnchantment protectionenchantment) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(super.checkCompatibility(pEnch));
        }
    }
}