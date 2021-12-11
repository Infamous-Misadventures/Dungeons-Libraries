package com.infamous.dungeons_libraries.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceContext;

public interface IRayTraceModeProvider {

	/**
	 * determines which block shape is hittable by the attack ray trace
	 * <br><br>
	 *
	 * @return <p>
	 * OUTLINE:  block shape (vanilla behavior, collides with all blocks)<br>
	 * COLLIDER: collision block shape (ignores plants/grass, etc.)<br>
	 * VISUAL:   visual block shape (some blocks are visually empty (Glass) or bigger (SoulSand) --> the ray will go through Glass)
	 * </p>
	 */
	default RayTraceContext.BlockMode getRayTraceBlockModeForAttack(ItemStack stack, PlayerEntity player) {
		return RayTraceContext.BlockMode.OUTLINE;
	}

	/**
	 * determines which fluid is hittable by the attack ray trace
	 */
	default RayTraceContext.FluidMode getRayTraceFluidModeForAttack(ItemStack stack, PlayerEntity player) {
		return RayTraceContext.FluidMode.NONE;
	}

	/**
	 * determines which block shape is hittable by the pick block ray trace
	 * <br><br>
	 *
	 * @return <p>
	 * OUTLINE:  block shape (vanilla behavior, collides with all blocks)<br>
	 * COLLIDER: collision block shape (ignores plants/grass, etc.)<br>
	 * VISUAL:   visual block shape (some blocks are visually empty (Glass) or bigger (SoulSand) --> the ray will go through Glass)
	 * </p>
	 */
	default RayTraceContext.BlockMode getRayTraceBlockModeForPickBlock(ItemStack stack, PlayerEntity player) {
		return RayTraceContext.BlockMode.OUTLINE;
	}

	/**
	 * determines which fluid is hittable by the pick block ray trace
	 */
	default RayTraceContext.FluidMode getRayTraceFluidModeForPickBlock(ItemStack stack, PlayerEntity player) {
		return RayTraceContext.FluidMode.NONE;
	}

}
