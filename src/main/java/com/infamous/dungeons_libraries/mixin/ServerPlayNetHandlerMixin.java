package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.compat.pehkui.PehkuiCompat;
import com.infamous.dungeons_libraries.entities.EntityAttributeUtil;
import com.infamous.dungeons_libraries.utils.RayTraceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CUseEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetHandler.class)
public abstract class ServerPlayNetHandlerMixin {

	@Shadow
	public ServerPlayerEntity player;

	/**
	 * @author Elenterius
	 */
	@Unique
	private double getMaxReachDist(ServerPlayerEntity player, CUseEntityPacket.Action action) {
		float scale = Math.max(PehkuiCompat.getPlayerReachScale(player), 1f);
		if (action == CUseEntityPacket.Action.ATTACK) {
			return scale * EntityAttributeUtil.getAttackReachDist(player, player.isCreative());
		}
		else {
			return scale * (EntityAttributeUtil.getBlockReachDist(player, true) + 1d);
		}
	}

	/**
	 * Fixes the issue where for players with a low maxReachDist the target's position can be outside the maxReachDist while the bounding box of the target is still within range.
	 *
	 * @author Elenterius
	 */
	@Redirect(method = "handleInteract", require = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ServerPlayerEntity;distanceToSqr(Lnet/minecraft/entity/Entity;)D"))
	private double dungeonsLibraries_distanceToSqProxy(ServerPlayerEntity player, Entity targetOfClient, CUseEntityPacket packet) {

		final double maxReachDist = getMaxReachDist(player, packet.getAction());
		final double distSqToBBox = RayTraceUtil.distanceSqToInflatedBoundingBox(player, targetOfClient, maxReachDist);
		final double maxReachDistSq = maxReachDist * maxReachDist;

		//TODO: don't trust the client? And perform a block raytrace to prevent attacks through walls?

		//For testing spawn a large slime and attack in survival mode or with reduced attack reach (e.g. /summon minecraft:slime ~ ~ ~ {Size: 10})
		DungeonsLibraries.LOGGER.debug(() -> {
			double distSqToPosition = player.distanceToSqr(targetOfClient);
			return String.format(
					"\nmaxReach[dist: %f, distSq: %f]\n" +
							"|-> old >> distSqToPosition[%f] is within maxReachDistSq: %s\n" +
							"|-> new >>     distSqToBBox[%f] is within maxReachDistSq: %s",
					maxReachDist, maxReachDistSq, distSqToPosition, distSqToPosition < maxReachDistSq, distSqToBBox, distSqToBBox < maxReachDistSq
			);
		});

		return distSqToBBox < maxReachDistSq ? Double.MIN_VALUE : Double.MAX_VALUE;
	}

}
