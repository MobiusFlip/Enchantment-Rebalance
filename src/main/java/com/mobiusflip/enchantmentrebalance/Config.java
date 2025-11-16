package com.mobiusflip.enchantmentrebalance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = EnchantmentRebalance.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> INVERTEBANE = BUILDER
            .comment("Mobs affected by the Invertebane enchantment. Intended to include all arthropods, slimes, creepers, and shelled things. Do not include spiders, cave spiders, silverfish, bees, endermites, or any other mob already vulnerable to Bane of Arthropods by default.")
            .defineList("invertebane", List.of("minecraft:creeper", "minecraft:guardian", "minecraft:elder_guardian", "minecraft:slime", "minecraft:magma_cube", "minecraft:shulker"), o -> o instanceof String);

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SENTINEL = BUILDER
            .comment("Mobs affected by the Sentinel enchantment. Intended to include \"humanoid\" monsters like illagers, piglins, and endermen.")
            .defineList("sentinel", List.of("minecraft:pillager", "minecraft:vindicator", "minecraft:evoker", "minecraft:vex", "minecraft:illusioner", "minecraft:ravager", "minecraft:witch", "minecraft:ravager", "minecraft:piglin", "minecraft:piglin_brute", "minecraft:enderman"), o -> o instanceof String);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> HUNTER = BUILDER
            .comment("Mobs affected by the Hunter enchantment. Intended to include all non-monstrous animals.")
            .defineList("hunter", List.of("minecraft:hoglin", "minecraft:dolphin", "minecraft:goat", "minecraft:llama", "minecraft:panda", "minecraft:polar_bear", "minecraft:wolf", "minecraft:axolotl", "minecraft:bat", "minecraft:camel", "minecraft:cat", "minecraft:chicken", "minecraft:cod", "minecraft:cow", "minecraft:donkey", "minecraft:fox", "minecraft:frog", "minecraft:glow_squid", "minecraft:horse", "minecraft:mooshroom", "minecraft:mule", "minecraft:ocelot", "minecraft:parrot", "minecraft:pig", "minecraft:pufferfish", "minecraft:rabbit", "minecraft:salmon", "minecraft:sheep", "minecraft:sniffer", "minecraft:strider", "minecraft:squid", "minecraft:tadpole", "minecraft:tropical_fish", "minecraft:turtle"), o -> o instanceof String);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Set<Item> items;
    public static Set<EntityType<?>> invertebane;
    public static Set<EntityType<?>> sentinel;
    public static Set<EntityType<?>> hunter;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        invertebane = INVERTEBANE.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toSet());

        sentinel = SENTINEL.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toSet());

        hunter = HUNTER.get().stream()
                .map(mobName -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobName)))
                .collect(Collectors.toSet());
    }
}
