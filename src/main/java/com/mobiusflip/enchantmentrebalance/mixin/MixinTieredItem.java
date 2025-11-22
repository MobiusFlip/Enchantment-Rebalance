package com.mobiusflip.enchantmentrebalance.mixin;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TieredItem.class)
public interface MixinTieredItem {
    @Accessor("tier")
    Tier getTier();
}
