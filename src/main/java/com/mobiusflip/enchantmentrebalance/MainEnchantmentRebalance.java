package com.mobiusflip.enchantmentrebalance;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MainEnchantmentRebalance.MODID)
public class MainEnchantmentRebalance
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "enchantmentrebalance";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);

    public static final RegistryObject<EnchantmentNocturnal> NOCTURNAL = ENCHANTMENTS.register("nocturnal", EnchantmentNocturnal::new);
    public static final RegistryObject<EnchantmentEndurance> ENDURANCE = ENCHANTMENTS.register("endurance", EnchantmentEndurance::new);
    public static final RegistryObject<EnchantmentVitality> VITALITY = ENCHANTMENTS.register("vitality", EnchantmentVitality::new);
    public static final RegistryObject<EnchantmentHighClimb> HIGH_CLIMB = ENCHANTMENTS.register("high_climb", EnchantmentHighClimb::new);
    public static final RegistryObject<EnchantmentSwiftStride> SWIFT_STRIDE = ENCHANTMENTS.register("swift_stride", EnchantmentSwiftStride::new);
    public static final RegistryObject<EnchantmentHunter> HUNTER = ENCHANTMENTS.register("hunter", EnchantmentHunter::new);
    public static final RegistryObject<EnchantmentSentinel> SENTINEL = ENCHANTMENTS.register("sentinel", EnchantmentSentinel::new);
    public static final RegistryObject<EnchantmentPrecisePoint> PRECISE_POINT = ENCHANTMENTS.register("precise_point", EnchantmentPrecisePoint::new);
    public static final RegistryObject<EnchantmentGuillotine> GUILLOTINE = ENCHANTMENTS.register("guillotine", EnchantmentGuillotine::new);
    public static final RegistryObject<EnchantmentEarthEater> EARTH_EATER = ENCHANTMENTS.register("earth_eater", EnchantmentEarthEater::new);
    public static final RegistryObject<EnchantmentMattock> MATTOCK = ENCHANTMENTS.register("mattock", EnchantmentMattock::new);
    public static final RegistryObject<EnchantmentVerdant> VERDANT = ENCHANTMENTS.register("verdant", EnchantmentVerdant::new);
    public static final RegistryObject<EnchantmentCollector> COLLECTOR = ENCHANTMENTS.register("collector", EnchantmentCollector::new);
    public static final RegistryObject<EnchantmentOutstretch> OUTSTRETCH = ENCHANTMENTS.register("outstretch", EnchantmentOutstretch::new);

    public MainEnchantmentRebalance(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // modEventBus.addListener(this::packEnable);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MainEnchantmentRebalance.ENCHANTMENTS.register(context.getModEventBus());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
