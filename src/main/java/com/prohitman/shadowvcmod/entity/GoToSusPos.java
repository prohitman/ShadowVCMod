package com.prohitman.shadowvcmod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.pathfinder.Path;

public class GoToSusPos extends Goal {
    public VCZombieEntity mob;
    public Path pathToSus;

    public GoToSusPos(VCZombieEntity mob){
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if(this.mob.getSuspicionPos() == BlockPos.ZERO){
            return false;
        }

        pathToSus = this.mob.getNavigation().createPath(this.mob.getSuspicionPos(), 0, 0);

        return pathToSus != null && pathToSus.canReach();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.pathToSus.isDone() || !this.pathToSus.canReach();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        if(this.mob.getSuspicionPos() == this.mob.getOnPos().above()){
            this.mob.setSuspicionPos(BlockPos.ZERO);
            System.out.println("Postion reset");
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.mob.getNavigation().moveTo(pathToSus, 0.8);
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }
}
