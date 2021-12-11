package com.infamous.dungeons_libraries.compat.pehkui;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

/**
 * works with Pehkui 2 & 3
 */
final class PehkuiScaleUtil implements PehkuiCompat.IScaleUtil {

	PehkuiScaleUtil() {
		ScaleUtils.getEntityReachScale(null); //checks if pehkui ScaleUtils is available, if not this will cause an error
	}

	@Override
	public float getPlayerReachScale(Entity player) {
		return ScaleUtils.getEntityReachScale(player);
	}

	@Override
	public float getPlayerReachScale(Entity player, float partialTicks) {
		return ScaleUtils.getEntityReachScale(player, partialTicks);
	}

}
