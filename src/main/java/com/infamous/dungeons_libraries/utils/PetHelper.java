package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.List;

public class PetHelper {

    public static void makeNearbyPetsAttackTarget(LivingEntity target, LivingEntity owner) {
        if (isPetOf(target, owner) || isPetOf(owner, target))
            return;//don't kill your pets or master!
        IMaster masterCapability = MinionMasterHelper.getMasterCapability(owner);
        if(masterCapability == null) return;
        List<LivingEntity> nearbyEntities = owner.getCommandSenderWorld().getLoadedEntitiesOfClass(LivingEntity.class, owner.getBoundingBox().inflate(12), nearbyEntity -> isPetOf(owner, nearbyEntity));
        HashSet<LivingEntity> pets = new HashSet<>();
//        pets.addAll(masterCapability.getSummonedMobs()); // TODO: Rewrite SummonedMobs to be a list of entities, instead of a list of UUID.
        pets.addAll(nearbyEntities);
        for (LivingEntity pet : pets) {

            if (pet instanceof TameableEntity) {
                TameableEntity tameableEntity = (TameableEntity) pet;
                tameableEntity.setTarget(target);
            }
            if (pet instanceof LlamaEntity) {
                LlamaEntity llamaEntity = (LlamaEntity) pet;
                llamaEntity.setTarget(target);
            }
            if (pet instanceof IronGolemEntity) {
                IronGolemEntity ironGolemEntity = (IronGolemEntity) pet;
                ironGolemEntity.setTarget(target);
            }
        }
    }

    public static boolean canPetAttackEntity(LivingEntity master, LivingEntity pet, LivingEntity target) {
        return pet != target && pet.isAlive() && !isPetOrColleagueRelation(pet, target) &&
                ((master instanceof PlayerEntity && !isPetOfPlayer(target)) || (!(master instanceof PlayerEntity) && (target instanceof PlayerEntity || isPetOfPlayer(target))));
    }

    private static boolean isPetOfPlayer(LivingEntity target) {
        IMinion minionCapability = MinionMasterHelper.getMinionCapability(target);
        if(minionCapability == null) return false;
        if(minionCapability.getMaster() == null) return false;
        return minionCapability.getMaster() instanceof PlayerEntity;
    }

    public static boolean isPetOf(LivingEntity possibleOwner, LivingEntity possiblePet) {
        if (possiblePet instanceof TameableEntity) {
            TameableEntity pet = (TameableEntity) possiblePet;
            return pet.getOwner() == possibleOwner;
        } else if(possiblePet instanceof AbstractHorseEntity){
            AbstractHorseEntity horse = (AbstractHorseEntity) possiblePet;
            return horse.getOwnerUUID() == possibleOwner.getUUID();
        } else {
            return MinionMasterHelper.isMinionOf(possiblePet, possibleOwner);
        }
    }

    public static boolean isPetOrColleagueRelation(LivingEntity potentialPet1, LivingEntity potentialPet2) {
        LivingEntity owner = null;
        if (potentialPet1 instanceof TameableEntity)
            owner = ((TameableEntity) potentialPet1).getOwner();
        else if (potentialPet1 instanceof AbstractHorseEntity)
            owner = MinionMasterHelper.getOwnerForHorse((AbstractHorseEntity) potentialPet1);
        else owner = MinionMasterHelper.getMaster(potentialPet1);

        LivingEntity otherOwner = null;
        if (potentialPet2 instanceof TameableEntity)
            otherOwner = ((TameableEntity) potentialPet2).getOwner();
        else if (potentialPet2 instanceof AbstractHorseEntity)
            otherOwner = MinionMasterHelper.getOwnerForHorse((AbstractHorseEntity) potentialPet2);
        else otherOwner = MinionMasterHelper.getMaster(potentialPet2);

        if (owner == null)
            return potentialPet1 == otherOwner;
        if (otherOwner == null)
            return potentialPet2 == owner;
        return owner == otherOwner;
    }

}
