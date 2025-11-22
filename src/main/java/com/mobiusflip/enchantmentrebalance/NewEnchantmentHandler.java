package com.mobiusflip.enchantmentrebalance;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = MainEnchantmentRebalance.MODID)
public class NewEnchantmentHandler {
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
    public static void getPrimaryTarget(AttackEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getTarget() instanceof LivingEntity) {
            PRIMARY_TARGET.put(event.getEntity(), (LivingEntity) event.getTarget());
        }
    }

    @SubscribeEvent
    public static void damageEnchantments(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        float damageBonus = 0;

        if(weapon.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS) > 0 && isInvertebrate(target.getType()) && (target.getMobType() != MobType.ARTHROPOD)) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(Enchantments.BANE_OF_ARTHROPODS);
        }

        if(weapon.getEnchantmentLevel(MainEnchantmentRebalance.SENTINEL.get()) > 0 && isHumanoid(target.getType())) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(MainEnchantmentRebalance.SENTINEL.get());
        }

        if(weapon.getEnchantmentLevel(MainEnchantmentRebalance.HUNTER.get()) > 0 && isAnimal(target.getType())) {
            damageBonus += 2.5 * weapon.getEnchantmentLevel(MainEnchantmentRebalance.HUNTER.get());
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

    @SubscribeEvent
    public static void collectorBreak(BlockEvent.BreakEvent event) {
        ServerPlayer player = (ServerPlayer) event.getPlayer();
        if(player == null) return;

        ItemStack tool = player.getMainHandItem();
        if(tool.isEmpty()) return;
        if(tool.getEnchantmentLevel(MainEnchantmentRebalance.COLLECTOR.get()) <= 0) return;

        event.setCanceled(true);
        collectorGather(event, player, tool);
    }

    private static void collectorGather(BlockEvent.BreakEvent event, ServerPlayer player, ItemStack tool) {
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        LootParams.Builder builder = (new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool).withParameter(LootContextParams.BLOCK_STATE, state).withOptionalParameter(LootContextParams.THIS_ENTITY, player));
        List<ItemStack> drops = state.getDrops(builder);
        for(ItemStack drop : drops) {
            player.getInventory().placeItemBackInInventory(drop.copy());
        }
        level.removeBlock(pos, false);
        tool.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
    }

    @SubscribeEvent
    public static void nocturnalNightVision(TickEvent.PlayerTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        Player player = event.player;

        if(player.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(MainEnchantmentRebalance.NOCTURNAL.get()) > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, true, false));
        }
    }

    @SubscribeEvent
    public static void nocturnalPhantoms(LivingChangeTargetEvent event) {
        if(!(event.getEntity() instanceof Phantom phantom)) return;

        LivingEntity target = event.getNewTarget();
        if(!(target instanceof Player player)) return;

        if(player.getItemBySlot(EquipmentSlot.HEAD).getEnchantmentLevel(MainEnchantmentRebalance.NOCTURNAL.get()) <= 0) return;

        LivingEntity lastHurtBy = phantom.getLastHurtByMob();
        if(lastHurtBy == player) return;

        event.setNewTarget(null);
    }

    @SubscribeEvent
    public static void mattockMiningSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        ItemStack tool = event.getEntity().getMainHandItem();
        if(!(tool.getItem() instanceof ShovelItem)) return;

        ItemStack woodenPick = new ItemStack(Items.WOODEN_PICKAXE);
        ItemStack woodenAxe = new ItemStack(Items.WOODEN_AXE);
        if(!woodenPick.isCorrectToolForDrops(event.getState()) && !woodenAxe.isCorrectToolForDrops(event.getState())) return;

        float speed = 4.0f;
        int eff = tool.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
        speed += eff * eff + 1;
        if (player.hasEffect(MobEffects.DIG_SPEED)) {
            speed *= 1.0F + (player.getEffect(MobEffects.DIG_SPEED).getAmplifier() + 1) * 0.2F;
        }
        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float f = switch (player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            speed *= f;
        }
        if (player.isUnderWater() && !EnchantmentHelper.hasAquaAffinity(player)) {
            speed /= 5.0F;
        }
        if (!player.onGround()) {
            speed /= 5.0F;
        }
        event.setNewSpeed(speed);
    }

    @SubscribeEvent
    public static void guillotineDrops(LivingDropsEvent event) {
        LivingEntity mob = event.getEntity();
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Player player)) return;

        ItemStack weapon = player.getMainHandItem();
        if (weapon.isEmpty()) return;

        int pLevel = weapon.getEnchantmentLevel(MainEnchantmentRebalance.GUILLOTINE.get());
        if (pLevel <= 0) return;
        float headChance = 0.025f * pLevel;

        ResourceLocation mobId = ForgeRegistries.ENTITY_TYPES.getKey(mob.getType());
        if(mobId == null) return;
        if(player.level().random.nextFloat() > headChance) return;
        ItemStack drop = ItemStack.EMPTY;

        if(mob instanceof Player killedPlayer) {
            drop = new ItemStack(Items.PLAYER_HEAD);
            CompoundTag tag = drop.getOrCreateTag();
            CompoundTag owner = new CompoundTag();
            owner.putUUID("Id", killedPlayer.getUUID());
            owner.putString("Name", killedPlayer.getName().getString());
            tag.put("SkullOwner", owner);
        }
        else if(Config.guillotine.containsKey(mobId)) {
            ResourceLocation headId = Config.guillotine.get(mobId);
            Item headItem = ForgeRegistries.ITEMS.getValue(headId);
            if(headItem != null) {
                drop = new ItemStack(headItem);
            }
        }

        if(!drop.isEmpty()) {
            event.getDrops().add(new ItemEntity(player.level(), mob.getX(), mob.getY(), mob.getZ(), drop));
        }
    }

    @SubscribeEvent
    public static void earthEater(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        ItemStack tool = player.getMainHandItem();
        if(tool.isEmpty()) return;
        if(!tool.getItem().isCorrectToolForDrops(state)) return;

        if(tool.getEnchantmentLevel(MainEnchantmentRebalance.EARTH_EATER.get()) > 0) {
            if(Config.earth_eater.contains(state.getBlock())) {
                event.setCanceled(true);
                level.removeBlock(pos, false);
            }
        }
    }

    @SubscribeEvent
    public static void verdant(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        ItemStack tool = event.getItemStack();
        if(tool.isEmpty()) return;
        if(!(tool.getItem() instanceof HoeItem)) return;

        if(tool.getEnchantmentLevel(MainEnchantmentRebalance.VERDANT.get()) > 0) {
            if(state.getBlock() instanceof BonemealableBlock crop && !(state.getBlock() instanceof GrassBlock)) {
                if(!level.isClientSide) {
                    if(crop.isValidBonemealTarget(level, pos, state, level.isClientSide)) {
                        crop.performBonemeal((ServerLevel) level, level.random, pos, state);
                        tool.hurtAndBreak(3, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    }
                } else {
                    BoneMealItem.addGrowthParticles(level, pos, 0);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}