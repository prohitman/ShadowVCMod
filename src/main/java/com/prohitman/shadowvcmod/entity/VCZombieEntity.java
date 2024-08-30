package com.prohitman.shadowvcmod.entity;

import com.prohitman.shadowvcmod.ShadowVCMod;
import com.prohitman.shadowvcmod.entity.goals.VCNearestAttackableTargetGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class VCZombieEntity extends Monster {
    private static final EntityDataAccessor<BlockPos> SUSPICION_POS = SynchedEntityData.defineId(VCZombieEntity.class, EntityDataSerializers.BLOCK_POS);

    public BlockPos getSuspicionPos(){
        return entityData.get(SUSPICION_POS);
    }
    public void setSuspicionPos(BlockPos targetPos){
        entityData.set(SUSPICION_POS, targetPos);
    }
    public VCZombieEntity(EntityType<VCZombieEntity> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1f, true));
        //this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        //this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        //this.goalSelector.addGoal(7, new GoToSusPos(this));

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
        if(!level.isClientSide){
            if(this.getTarget() != null){
                if(this.getTarget() instanceof Player player){
                    System.out.println("Target Detected: " + this.distanceTo(player));
                }
            }
/*            if(this.getSuspicionPos() != BlockPos.ZERO){
                //this.getNavigation().moveTo(this.getSuspicionPos(), 0, 0);
            }
            if(this.getSuspicionPos() == this.getOnPos().above()){
                System.out.println("Resetting pos");
                this.setSuspicionPos(BlockPos.ZERO);
            }*/
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUSPICION_POS, BlockPos.ZERO);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        int x = pCompound.getInt("suspicionPosX");
        int y = pCompound.getInt("suspicionPosY");
        int z = pCompound.getInt("suspicionPosZ");

        this.setSuspicionPos(new BlockPos(x,y,z));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("suspicionPosX", this.getSuspicionPos().getX());
        pCompound.putInt("suspicionPosY", this.getSuspicionPos().getY());
        pCompound.putInt("suspicionPosZ", this.getSuspicionPos().getZ());
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
