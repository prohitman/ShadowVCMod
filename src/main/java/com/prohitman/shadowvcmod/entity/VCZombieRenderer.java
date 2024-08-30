package com.prohitman.shadowvcmod.entity;

import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class VCZombieRenderer extends HumanoidMobRenderer<VCZombieEntity, VCZombieModel> {
    private static final ResourceLocation ZOMBIE_LOCATION = new ResourceLocation("textures/entity/zombie/zombie.png");

    public VCZombieRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new VCZombieModel(pContext.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(VCZombieEntity pEntity) {
        return ZOMBIE_LOCATION;
    }
}
