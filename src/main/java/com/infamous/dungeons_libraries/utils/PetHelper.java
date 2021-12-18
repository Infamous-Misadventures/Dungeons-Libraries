package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.summoning.MinionMasterHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;

import java.util.List;

public class PetHelper {

    public static void makeNearbyPetsAttackTarget(LivingEntity target, LivingEntity owner) {
        if (isPetOfAttacker(target, owner) || isPetOfAttacker(owner, target))
            return;//don't kill your pets or master!
        List<LivingEntity> nearbyEntities = owner.getCommandSenderWorld().getLoadedEntitiesOfClass(LivingEntity.class, owner.getBoundingBox().inflate(12), nearbyEntity -> canPetAttackEntity(owner, nearbyEntity));
        for (LivingEntity nearbyEntity : nearbyEntities) {

            if (nearbyEntity instanceof TameableEntity) {
                TameableEntity tameableEntity = (TameableEntity) nearbyEntity;
                tameableEntity.setTarget(target);
            }
            if (nearbyEntity instanceof LlamaEntity) {
                LlamaEntity llamaEntity = (LlamaEntity) nearbyEntity;
                llamaEntity.setTarget(target);
            }
            if (nearbyEntity instanceof IronGolemEntity) {
                IronGolemEntity ironGolemEntity = (IronGolemEntity) nearbyEntity;
                ironGolemEntity.setTarget(target);
            }
        }
    }

    private static boolean canPetAttackEntity(LivingEntity owner, LivingEntity nearbyEntity) {
        return nearbyEntity != owner && isPetOfAttacker(owner, nearbyEntity) && nearbyEntity.isAlive();
    }

    public static boolean isPetOfAttacker(LivingEntity possibleOwner, LivingEntity possiblePet) {
        if (possiblePet instanceof TameableEntity) {
            TameableEntity pet = (TameableEntity) possiblePet;
            return pet.getOwner() == possibleOwner;
        } else if(possiblePet instanceof AbstractHorseEntity){
            AbstractHorseEntity horse = (AbstractHorseEntity) possiblePet;
            return horse.getOwnerUUID() == possibleOwner.getUUID();
        } else if(MinionMasterHelper.isMinionEntity(possiblePet)){
                return MinionMasterHelper.isMinionOf(possiblePet, possibleOwner.getUUID());
        }
        else{
            return false;
        }
    }

    public static boolean isPetOrColleagueRelation(LivingEntity potentialPet1, LivingEntity potentialPet2) {
        LivingEntity owner = null;
        if (potentialPet1 instanceof TameableEntity)
            owner = ((TameableEntity) potentialPet1).getOwner();
        else if (potentialPet1 instanceof AbstractHorseEntity)
            owner = MinionMasterHelper.getOwnerForHorse((AbstractHorseEntity) potentialPet1);
        else if (MinionMasterHelper.isMinionEntity(potentialPet1))
            owner = MinionMasterHelper.getMaster(potentialPet1);

        LivingEntity otherOwner = null;
        if (potentialPet2 instanceof TameableEntity)
            otherOwner = ((TameableEntity) potentialPet2).getOwner();
        else if (potentialPet2 instanceof AbstractHorseEntity)
            otherOwner = MinionMasterHelper.getOwnerForHorse((AbstractHorseEntity) potentialPet2);
        else if (MinionMasterHelper.isMinionEntity(potentialPet2))
            otherOwner = MinionMasterHelper.getMaster(potentialPet2);

        if (owner == null)
            return potentialPet1 == otherOwner;
        if (otherOwner == null)
            return potentialPet2 == owner;
        return owner == otherOwner;
    }

}
