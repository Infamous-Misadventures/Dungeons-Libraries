package com.infamous.dungeons_libraries.items.artifacts;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.capabilities.artifact.IArtifactUsage;
import com.infamous.dungeons_libraries.integration.curios.CuriosIntegration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

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
        IArtifactUsage cap = ArtifactUsageHelper.getArtifactUsageCapability(event.player);
        if(cap != null && cap.isUsingArtifact() && cap.getUsingArtifact().getItem() instanceof ArtifactItem){
            cap.getUsingArtifact().getItem().onUseTick(event.player.level, event.player, cap.getUsingArtifact(), cap.getUsingArtifactRemaining());
            cap.setUsingArtifactRemaining(cap.getUsingArtifactRemaining() - 1);
        }
    }
}
