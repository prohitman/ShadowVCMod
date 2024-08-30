package com.prohitman.shadowvcmod;

import com.mojang.logging.LogUtils;
import com.prohitman.shadowvcmod.config.ServerConfig;
import com.prohitman.shadowvcmod.entity.VCZombieEntity;
import com.prohitman.shadowvcmod.entity.VCZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(ShadowVCMod.MODID)
public class ShadowVCMod
{
    public static final String MODID = "shadowvcmod";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final RegistryObject<EntityType<VCZombieEntity>> VC_ZOMBIE = ENTITIES.register("vc_zombie", () ->
            EntityType.Builder.<VCZombieEntity>of(VCZombieEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build("vc_zombie"));
    public static final RegistryObject<Item> VC_ZOMBIE_SPAWN_EGG = ITEMS.register("vc_zombie_spawn_egg", () -> new ForgeSpawnEggItem(VC_ZOMBIE, 0x232A27, 0x4B8152, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public ShadowVCMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ServerConfig.COMMON_CONFIG);

        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(VC_ZOMBIE.get(), VCZombieRenderer::new);
        }
    }
}
