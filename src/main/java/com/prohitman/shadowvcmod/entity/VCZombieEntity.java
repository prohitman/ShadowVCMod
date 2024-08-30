package com.prohitman.shadowvcmod.entity;

import com.prohitman.shadowvcmod.entity.goals.VCNearestAttackableTargetGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class VCZombieEntity extends Monster {
    public VCZombieEntity(EntityType<VCZombieEntity> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1f, true));

        this.targetSelector.addGoal(3, new VCNearestAttackableTargetGoal<>(this, Player.class, false, 8));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 0.0001D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public void tick() {
        super.tick();
        /*if(!level.isClientSide){
            if(this.getTarget() != null){
                if(this.getTarget() instanceof Player player){
                    System.out.println("Target Detected: " + this.distanceTo(player));
                }
            }
        }*/
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }
}
