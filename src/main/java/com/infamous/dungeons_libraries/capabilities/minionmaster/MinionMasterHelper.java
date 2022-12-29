package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtByTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MinionFollowOwnerGoal;
import com.infamous.dungeons_libraries.entities.ai.goal.MeleeAttackGoal;
import com.infamous.dungeons_libraries.mixin.MobInvoker;
import com.infamous.dungeons_libraries.summon.SummonConfig;
import com.infamous.dungeons_libraries.summon.SummonConfigRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.MASTER_CAPABILITY;
import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.MINION_CAPABILITY;
import static com.infamous.dungeons_libraries.utils.PetHelper.canPetAttackEntity;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;

public class MinionMasterHelper {

    @Nullable
    public static LivingEntity getOwnerForHorse(AbstractHorse horseEntity){
        try {
            if(horseEntity.getOwnerUUID() != null){
                UUID ownerUniqueId = horseEntity.getOwnerUUID();
                return ownerUniqueId == null ? null : horseEntity.level.getPlayerByUUID(ownerUniqueId);
            }
            else return null;
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static boolean isMinionEntity(Entity target) {
        Minion targetSummonableCap = getMinionCapability(target);
        return targetSummonableCap.getMaster() != null;
    }

    public static boolean isMinionOf(LivingEntity target, LivingEntity owner) {
        Minion targetSummonableCap = getMinionCapability(target);
        return targetSummonableCap.getMaster() != null
                && targetSummonableCap.getMaster() == owner;
    }

    @Nullable
    public static LivingEntity getMaster(LivingEntity minionMob) {
        Minion minion = getMinionCapability(minionMob);
        return minion.getMaster();
    }

    public static Master getMasterCapability(Entity entity)
    {
        return entity.getCapability(MASTER_CAPABILITY).orElse(new Master());
    }

    public static Minion getMinionCapability(Entity entity)
    {
        return entity.getCapability(MINION_CAPABILITY).orElse(new Minion());
    }

    public static void addMinionGoals(Mob mobEntity) {
        Minion minionCap = getMinionCapability(mobEntity);
        if(minionCap.isGoalsAdded()) return;
        if(minionCap.isMinion()){
            mobEntity.goalSelector.addGoal(2, new MinionFollowOwnerGoal(mobEntity, 1.5D, 24.0F, 3.0F, false));
            clearGoals(mobEntity.targetSelector);
            mobEntity.targetSelector.addGoal(1, new MasterHurtByTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(2, new MasterHurtTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class, 5, false, false,
                    (entityIterator) -> entityIterator.getClassification(false).equals(MobCategory.MONSTER) &&  canPetAttackEntity(mobEntity, entityIterator)));

            minionCap.getMaster().getCapability(ModCapabilities.MASTER_CAPABILITY).ifPresent(master -> {
                master.addMinion(mobEntity);
            });
            minionCap.setGoalsAdded(true);
            if(minionCap.isSummon()){
                SummonConfig config = SummonConfigRegistry.getConfig(mobEntity.getType().getRegistryName());
                if(config.shouldAddAttackGoal()){
                    addSummonAttackGoal(mobEntity);
                }
            }
        }
    }

    private static void addSummonAttackGoal(Mob mobEntity) {
        AttributeInstance attribute = mobEntity.getAttribute(ATTACK_DAMAGE);
        if(attribute == null) return;
        if(attribute.getValue() == 0){
            attribute.addTransientModifier(new AttributeModifier("Summon Attack Damage", 1, AttributeModifier.Operation.ADDITION));
        }
        mobEntity.goalSelector.addGoal(1, new MeleeAttackGoal(mobEntity, 1.0D, true));
    }

    static void removeMinion(LivingEntity entityLiving, Minion cap) {
        LivingEntity master = cap.getMaster();
        Master masterCapability = getMasterCapability(master);
        masterCapability.removeMinion(entityLiving);
        cap.setMaster(null);
        if(entityLiving instanceof Mob){
            Mob mobEntity = (Mob) entityLiving;
            clearGoals(mobEntity.goalSelector);
            clearGoals(mobEntity.targetSelector);
            cap.setGoalsAdded(false);
            ((MobInvoker) entityLiving).invokeRegisterGoals();
        }
    }

    private static void clearGoals(GoalSelector goalSelector) {
        ArrayList<WrappedGoal> wrappedGoals = new ArrayList<>(goalSelector.getAvailableGoals());
        wrappedGoals.forEach(prioritizedGoal -> goalSelector.removeGoal(prioritizedGoal.getGoal()));
    }
}
