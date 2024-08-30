package com.prohitman.shadowvcmod.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VCZombieModel extends HumanoidModel<VCZombieEntity> {
    public VCZombieModel(ModelPart pRoot) {
        super(pRoot);
    }
}
