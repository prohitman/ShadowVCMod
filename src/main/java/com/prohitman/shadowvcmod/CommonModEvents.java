package com.prohitman.shadowvcmod;

import com.prohitman.shadowvcmod.entity.VCZombieEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShadowVCMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ShadowVCMod.VC_ZOMBIE.get(), VCZombieEntity.createAttributes().build());
    }
}
