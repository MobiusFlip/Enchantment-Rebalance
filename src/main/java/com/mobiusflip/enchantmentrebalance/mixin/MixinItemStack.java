package com.mobiusflip.enchantmentrebalance.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mobiusflip.enchantmentrebalance.MainEnchantmentRebalance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void injectPrecisePoint(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if(!(stack.getItem() instanceof SwordItem) || slot != EquipmentSlot.MAINHAND) return;
        int pLevel = stack.getEnchantmentLevel(MainEnchantmentRebalance.PRECISE_POINT.get());
        if(pLevel <= 0) return;

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(cir.getReturnValue());
        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("e96fa348-738a-44d5-ad30-877944391c08"), "Precise Point", .1f * pLevel, AttributeModifier.Operation.ADDITION));
        cir.setReturnValue(modifiers);
    }

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void injectOutstretch(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if(!(stack.getItem() instanceof DiggerItem) || slot != EquipmentSlot.MAINHAND) return;
        int pLevel = stack.getEnchantmentLevel(MainEnchantmentRebalance.OUTSTRETCH.get());
        if(pLevel <= 0) return;

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(cir.getReturnValue());
        modifiers.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(UUID.fromString("9b4b618b-4719-4ec2-b6d9-da80292e3c5c"), "Outstretch", 1.0f * pLevel, AttributeModifier.Operation.ADDITION));
        cir.setReturnValue(modifiers);
    }

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void injectSwiftStride(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if(!(stack.getItem() instanceof ArmorItem) || slot != EquipmentSlot.LEGS) return;
        int pLevel = stack.getEnchantmentLevel(MainEnchantmentRebalance.SWIFT_STRIDE.get());
        if(pLevel <= 0) return;

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(cir.getReturnValue());
        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("b818f926-cac8-44aa-b147-fa0933410d11"), "Swift Stride", 0.05f * pLevel, AttributeModifier.Operation.MULTIPLY_BASE));
        cir.setReturnValue(modifiers);
    }

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void injectHighClimb(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if(!(stack.getItem() instanceof ArmorItem) || slot != EquipmentSlot.LEGS) return;
        int pLevel = stack.getEnchantmentLevel(MainEnchantmentRebalance.HIGH_CLIMB.get());
        if(pLevel <= 0) return;

        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create(cir.getReturnValue());
        modifiers.put(ForgeMod.STEP_HEIGHT_ADDITION.get(), new AttributeModifier(UUID.fromString("e105f83f-18d1-4ff7-a986-711d1c09f9e2"), "High Climb", 0.6f, AttributeModifier.Operation.ADDITION));
        cir.setReturnValue(modifiers);
    }

    @Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    private void injectMattock(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        ItemStack tool = (ItemStack) (Object) this;
        if(!(tool.getItem() instanceof ShovelItem)) return;
        int pLevel = tool.getEnchantmentLevel(MainEnchantmentRebalance.MATTOCK.get());
        if(pLevel <= 0) return;

        ItemStack woodenPick = new ItemStack(Items.WOODEN_PICKAXE);
        ItemStack woodenAxe = new ItemStack(Items.WOODEN_AXE);
        if(woodenPick.isCorrectToolForDrops(state) || woodenAxe.isCorrectToolForDrops(state)) {
            cir.setReturnValue(true);
        }
    }
}
