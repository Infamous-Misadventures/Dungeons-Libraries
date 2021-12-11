package com.infamous.dungeons_libraries.compat.pehkui;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;

public final class PehkuiCompat {

	private static IScaleUtil scaleUtil = new IScaleUtil.NullObject();

	public static void init() {
		Optional<? extends ModContainer> optional = ModList.get().getModContainerById("pehkui");
		if (optional.isPresent()) {
			IModInfo info = optional.get().getModInfo();
			DungeonsLibraries.LOGGER.debug(MarkerManager.getMarker("Compat"), "setting up compatibility for mod {} ({})", info.getDisplayName(), info.getVersion());
			scaleUtil = new PehkuiScaleUtil();
		}
	}

	private PehkuiCompat() {}

	public static float getPlayerReachScale(Entity player) {
		return scaleUtil.getPlayerReachScale(player);
	}

	public static float getPlayerReachScale(Entity player, float partialTicks) {
		return scaleUtil.getPlayerReachScale(player, partialTicks);
	}

	interface IScaleUtil {

		float getPlayerReachScale(Entity player);

		float getPlayerReachScale(Entity player, float partialTicks);

		class NullObject implements IScaleUtil {

			@Override
			public float getPlayerReachScale(Entity player) {
				return 1f;
			}

			@Override
			public float getPlayerReachScale(Entity player, float partialTicks) {
				return 1f;
			}

		}

	}

}
