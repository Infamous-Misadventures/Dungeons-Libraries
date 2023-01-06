package com.infamous.dungeons_libraries.items.artifacts;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsage;
import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.integration.curios.CuriosIntegration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ArtifactEvents {
    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        if (!event.getIdentifier().equals(CuriosIntegration.ARTIFACT_IDENTIFIER)) return;
        ItemStack itemstack = event.getTo();
        if (itemstack.getItem() instanceof ArtifactItem) {
            if (!itemstack.isEmpty()) {
                event.getEntity().getAttributes().addTransientAttributeModifiers(((ArtifactItem) itemstack.getItem()).getDefaultAttributeModifiers(event.getSlotIndex()));
            }
        }

        ItemStack itemstack1 = event.getFrom();
        if (itemstack1.getItem() instanceof ArtifactItem) {
            if (!itemstack1.isEmpty()) {
                event.getEntity().getAttributes().removeAttributeModifiers(((ArtifactItem) itemstack1.getItem()).getDefaultAttributeModifiers(event.getSlotIndex()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        ArtifactUsage cap = ArtifactUsageHelper.getArtifactUsageCapability(event.player);
        if (cap.isUsingArtifact() && cap.getUsingArtifact().getItem() instanceof ArtifactItem) {
            cap.getUsingArtifact().getItem().onUseTick(event.player.level, event.player, cap.getUsingArtifact(), cap.getUsingArtifactRemaining());
            cap.setUsingArtifactRemaining(cap.getUsingArtifactRemaining() - 1);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        stopUsingAllArtifacts(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        stopUsingAllArtifacts(event.getEntity());

    }

    @SubscribeEvent
    public static void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        stopUsingAllArtifacts(event.getEntity());
    }

    private static void stopUsingAllArtifacts(Player player) {
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(iCuriosItemHandler -> {
            Optional<ICurioStacksHandler> artifactStackHandler = iCuriosItemHandler.getStacksHandler("artifact");
            if (artifactStackHandler.isPresent()) {
                int slots = artifactStackHandler.get().getStacks().getSlots();
                for (int slot = 0; slot < slots; slot++) {
                    ItemStack artifact = artifactStackHandler.get().getStacks().getStackInSlot(slot);
                    if (!artifact.isEmpty() && artifact.getItem() instanceof ArtifactItem) {
                        ((ArtifactItem) artifact.getItem()).stopUsingArtifact(player);
                    }
                }
            }
        });
    }
}
