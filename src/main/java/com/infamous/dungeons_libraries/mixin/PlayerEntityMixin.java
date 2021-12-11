package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.compat.pehkui.PehkuiCompat;
import com.infamous.dungeons_libraries.entities.EntityAttributeUtil;
import com.infamous.dungeons_libraries.entities.EntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

	/**
	 * @author Elenterius
	 */
	@Inject(method = "createAttributes", at = @At(value = "RETURN"))
	private static void dungeonsLibraries_onCreateAttributes(CallbackInfoReturnable<AttributeModifierMap.MutableAttribute> cir) {
		DungeonsLibraries.LOGGER.debug(MarkerManager.getMarker("ATTRIBUTE INJECTION"), "adding attack reach attribute to player");
		cir.getReturnValue().add(EntityAttributes.ATTACK_REACH.get());
	}

	/**
	 * allow sweep attacks with larger attack reach
	 *
	 * @author Elenterius
	 */
	//TODO: check if this is broken in 1.17/1.18 --> Reason: forge added custom sweep hit boxes in 1.17 (#7981)
	@Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;distanceToSqr(Lnet/minecraft/entity/Entity;)D"))
	private double dungeonsLibraries_DistanceToSqProxy(PlayerEntity player, Entity targetEntity) {
		float scale = Math.max(PehkuiCompat.getPlayerReachScale(player), 1f);
		double reachDist = EntityAttributeUtil.getAttackReachDist(player);
		double maxReachDistSq = scale * scale * reachDist * reachDist;

		double distSq = player.distanceToSqr(targetEntity);
		return distSq < maxReachDistSq ? Double.MIN_VALUE : Double.MAX_VALUE;
	}

}
