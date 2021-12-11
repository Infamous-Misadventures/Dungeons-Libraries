package com.infamous.dungeons_libraries.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Optional;

public final class RayTraceUtil {

	private RayTraceUtil() {}

	public static RayTraceResult pickBlock(Entity entity, float partialTicks, double rayDist, RayTraceContext.BlockMode blockMode, boolean traceFluids) {
		return pickBlock(entity, partialTicks, rayDist, blockMode, traceFluids ? RayTraceContext.FluidMode.ANY : RayTraceContext.FluidMode.NONE);
	}

	public static RayTraceResult pickBlock(Entity entity, float partialTicks, double rayDist, RayTraceContext.BlockMode blockMode, RayTraceContext.FluidMode fluidMode) {
		Vector3d startPos = entity.getEyePosition(partialTicks);
		Vector3d lookVec = entity.getViewVector(partialTicks);
		Vector3d endPos = startPos.add(lookVec.x * rayDist, lookVec.y * rayDist, lookVec.z * rayDist);
		return entity.level.clip(new RayTraceContext(startPos, endPos, blockMode, fluidMode, entity));
	}

	/**
	 * instead of getting the distance between two positions, tries to get the distance between the position of the source and the inflated bounding box of the target
	 */
	public static double distanceSqToInflatedBoundingBox(Entity source, Entity target, double maxDist) {
		//make it easier to hit small targets
		return distanceSqToBoundingBox(source, target, 0.3f, maxDist);
	}

	/**
	 * instead of getting the distance between two points, tries to get the distance between the position of the source and the bounding box of the target
	 *
	 * @param inflate inflates the target aabb if the value is not zero, a possible input value could be {@link Entity#getPickRadius()}
	 */
	public static double distanceSqToBoundingBox(Entity source, Entity target, float inflate, double maxDist) {
		//create "ray"
		Vector3d startPos = source.getEyePosition(1f);
		Vector3d direction = target.position().subtract(startPos).normalize();
		Vector3d endPos = startPos.add(direction.scale(maxDist));

		AxisAlignedBB aabb = inflate != 0f ? target.getBoundingBox().inflate(inflate) : target.getBoundingBox();

		//tries to get the "intersection point" of the aabb with the ray
		Optional<Vector3d> optional = aabb.clip(startPos, endPos);
		if (aabb.contains(startPos)) {
			return startPos.distanceToSqr(optional.orElse(startPos));
		}
		else if (optional.isPresent()) {
			return startPos.distanceToSqr(optional.get());
		}

		return source.distanceToSqr(target); //fallback
	}

}
