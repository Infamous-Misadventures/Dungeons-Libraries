package com.infamous.dungeons_libraries.integration.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.integration.curios.client.CuriosClientIntegration.CURIOS_ICON_TEXTURE;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CuriosIntegration {

	public static final String ARTIFACT_IDENTIFIER = "artifact";

	@SubscribeEvent
	public static void enqueue(InterModEnqueueEvent event) {
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder(ARTIFACT_IDENTIFIER).icon(CURIOS_ICON_TEXTURE).priority(10).size(3).build());
	}

	public static List<ItemStack> getArtifacts(LivingEntity livingEntity){
		LazyOptional<ICuriosItemHandler> curiosHandler = CuriosApi.getCuriosHelper().getCuriosHandler(livingEntity);
		if(curiosHandler.isPresent()){
			Optional<ICurioStacksHandler> artifactStackHandler = curiosHandler.resolve().get().getStacksHandler(ARTIFACT_IDENTIFIER);
			if (artifactStackHandler.isPresent()) {
				IDynamicStackHandler stacks = artifactStackHandler.get().getStacks();
				List<ItemStack> artifacts = new ArrayList<>();
				for (int i = 0; i < stacks.getSlots(); i++) {
					artifacts.add(stacks.getStackInSlot(i));
				}
				return artifacts;
			}
		}
		return Collections.emptyList();
	}
	
}