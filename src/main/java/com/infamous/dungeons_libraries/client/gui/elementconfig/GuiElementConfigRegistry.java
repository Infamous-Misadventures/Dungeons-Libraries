package com.infamous.dungeons_libraries.client.gui.elementconfig;

import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

//@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiElementConfigRegistry {
    public static final ResourceLocation ELEMENT_CONFIG_BUILTIN_RESOURCELOCATION = new ResourceLocation(MODID, "gui/element");

    public static final CodecJsonDataManager<GuiElementConfig> GUI_ELEMENT_CONFIGS = new CodecJsonDataManager<>("gui/element", GuiElementConfig.CODEC);


    public static GuiElementConfig getConfig(ResourceLocation resourceLocation) {
        return GUI_ELEMENT_CONFIGS.getData().getOrDefault(resourceLocation, GuiElementConfig.DEFAULT);
    }

    public static boolean guiElementConfigExists(ResourceLocation resourceLocation) {
        return GUI_ELEMENT_CONFIGS.getData().containsKey(resourceLocation);
    }

//    public static GuiElementConfigSyncPacket toPacket(Map<ResourceLocation, GuiElementConfig> map) {
//        return new GuiElementConfigSyncPacket(map);
//    }

//    @SubscribeEvent
//    public static void onAddReloadListeners(AddReloadListenerEvent event) {
//        event.addListener(GUI_ELEMENT_CONFIGS);
//    }

    public static void initGuiElementConfigs() {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null) {
            ResourceManager resourceManager = mc.getResourceManager();
            if (resourceManager instanceof ReloadableResourceManager reloadableResourceManager) {
                reloadableResourceManager.registerReloadListener(GUI_ELEMENT_CONFIGS);
            }
        }
    }
}