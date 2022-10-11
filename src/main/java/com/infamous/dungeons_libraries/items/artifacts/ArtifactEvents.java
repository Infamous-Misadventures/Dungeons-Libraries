package com.infamous.dungeons_libraries.items.artifacts;

import com.infamous.dungeons_gear.DungeonsGear;
import com.infamous.dungeons_gear.capabilities.artifact.ArtifactUsage;
import com.infamous.dungeons_gear.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_gear.capabilities.combo.Combo;
import com.infamous.dungeons_gear.capabilities.combo.ComboHelper;
import com.infamous.dungeons_gear.effects.CustomEffects;
import com.infamous.dungeons_gear.integration.curios.CuriosIntegration;
import com.infamous.dungeons_gear.utilties.AreaOfEffectHelper;
import com.infamous.dungeons_gear.utilties.SoundHelper;
import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsage;
import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Minion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = MODID)
public class ArtifactEvents {
    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        if(!event.getIdentifier().equals(CuriosIntegration.ARTIFACT_IDENTIFIER)) return;
        ItemStack itemstack = event.getTo();
        if(itemstack.getItem() instanceof ArtifactItem) {
            if (!itemstack.isEmpty()) {
                event.getEntityLiving().getAttributes().addTransientAttributeModifiers(((ArtifactItem) itemstack.getItem()).getDefaultAttributeModifiers(event.getSlotIndex()));
            }
        }

        ItemStack itemstack1 = event.getFrom();
        if(itemstack1.getItem() instanceof ArtifactItem) {
            if (!itemstack1.isEmpty()) {
                event.getEntityLiving().getAttributes().removeAttributeModifiers(((ArtifactItem) itemstack1.getItem()).getDefaultAttributeModifiers(event.getSlotIndex()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        ArtifactUsage cap = ArtifactUsageHelper.getArtifactUsageCapability(event.player);
        if(cap != null && cap.isUsingArtifact() && cap.getUsingArtifact().getItem() instanceof ArtifactItem){
            cap.getUsingArtifact().getItem().onUseTick(event.player.level, event.player, cap.getUsingArtifact(), cap.getUsingArtifactRemaining());
            cap.setUsingArtifactRemaining(cap.getUsingArtifactRemaining() - 1);
        }
    }
}
