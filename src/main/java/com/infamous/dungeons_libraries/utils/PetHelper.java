package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.List;

import static com.infamous.dungeons_libraries.utils.AbilityHelper.canApplyToEnemy;

public class PetHelper {

    public static void makeNearbyPetsAttackTarget(LivingEntity target, LivingEntity owner) {
        if (isPetOf(target, owner) || isPetOf(owner, target))
            return;//don't kill your pets or master!
        IMaster masterCapability = MinionMasterHelper.getMasterCapability(owner);
        if(masterCapability == null) return;
        List<LivingEntity> nearbyEntities = owner.getCommandSenderWorld().getLoadedEntitiesOfClass(LivingEntity.class, owner.getBoundingBox().inflate(12), nearbyEntity -> isPetOf(owner, nearbyEntity));
        HashSet<Entity> pets = new HashSet<>();
        pets.addAll(masterCapability.getAllMinions());
        pets.addAll(nearbyEntities);
        for (Entity pet : pets) {
            if(pet instanceof MobEntity){
                ((MobEntity) pet).setTarget(target);
            }
        }
    }

    public static boolean canPetAttackEntity(LivingEntity pet, LivingEntity target) {
        return pet.isAlive() && canApplyToEnemy(pet, target);
    }

    public static boolean isPetOf(LivingEntity possibleOwner, LivingEntity possiblePet) {
        return getOwner(possiblePet) == possibleOwner;
    }

    public static boolean isPetOrColleagueRelation(LivingEntity potentialPet1, LivingEntity potentialPet2) {
        LivingEntity owner = getOwner(potentialPet1);
        LivingEntity otherOwner = getOwner(potentialPet2);
        
        if (owner == null)
            return potentialPet1 == otherOwner;
        if (otherOwner == null)
            return potentialPet2 == owner;
        return owner == otherOwner;
    }

    public static LivingEntity getOwner(LivingEntity potentialPet) {
        LivingEntity owner = null;
        if (potentialPet instanceof TameableEntity)
            owner = ((TameableEntity) potentialPet).getOwner();
        if (potentialPet instanceof AbstractHorseEntity)
            owner = MinionMasterHelper.getOwnerForHorse((AbstractHorseEntity) potentialPet);
        if(MinionMasterHelper.getMaster(potentialPet) != null)
            owner = MinionMasterHelper.getMaster(potentialPet);
        return owner;
    }

}
