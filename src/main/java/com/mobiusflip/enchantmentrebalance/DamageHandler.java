package com.mobiusflip.enchantmentrebalance;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = EnchantmentRebalance.MODID)
public class DamageHandler {
    private static final Map<Player, LivingEntity> PRIMARY_TARGET = new WeakHashMap<>();

    public static boolean isInvertebrate(EntityType<?> type) {
        return Config.invertebane.contains(type);
    }
    public static boolean isHumanoid(EntityType<?> type) {
        return Config.sentinel.contains(type);
    }
    public static boolean isAnimal(EntityType<?> type) {
        return Config.hunter.contains(type);
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getTarget() instanceof LivingEntity) {
            PRIMARY_TARGET.put(event.getEntity(), (LivingEntity) event.getTarget());
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        float damageBonus = 0;

        if(weapon.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS) > 0 && isInvertebrate(target.getType()) && (target.getMobType() != MobType.ARTHROPOD)) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS);
        }

        if(weapon.getEnchantmentLevel(EnchantmentRebalance.SENTINEL.get()) > 0 && isHumanoid(target.getType())) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(EnchantmentRebalance.SENTINEL.get());
        }

        if(weapon.getEnchantmentLevel(EnchantmentRebalance.HUNTER.get()) > 0 && isAnimal(target.getType())) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(EnchantmentRebalance.HUNTER.get());
        }

        if(weapon.getEnchantmentLevel(Enchantments.IMPALING) > 0 && target.isInWaterOrRain() && target.getMobType() != MobType.WATER) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(Enchantments.IMPALING);
        }

        float sweepMultiplier = 1f;
        if(PRIMARY_TARGET.getOrDefault(attacker, null) != target) {
            if(weapon.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) > 0) {
                sweepMultiplier = (float) weapon.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) /(weapon.getEnchantmentLevel(Enchantments.SWEEPING_EDGE)+1);
            }
            else sweepMultiplier = 0f;
        } else if (weapon.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS) > 0 && isInvertebrate(target.getType())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (20 + attacker.getRandom().nextInt(10) * weapon.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS)), 3));
        }

        if(damageBonus > 0) event.setAmount(event.getAmount() + sweepMultiplier * damageBonus);
        PRIMARY_TARGET.remove(attacker);
    }
}