package com.infamous.dungeons_libraries.client.gui.elementconfig;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

//@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuiElementConfigRegistry {
    public static final ResourceLocation ELEMENT_CONFIG_BUILTIN_RESOURCELOCATION = new ResourceLocation(MODID, "gui/element");

    public static final CodecJsonDataManager<GuiElementConfig> GUI_ELEMENT_CONFIGS = new CodecJsonDataManager<>("gui/element", GuiElementConfig.CODEC, DungeonsLibraries.LOGGER);


    public static GuiElementConfig getConfig(ResourceLocation resourceLocation) {
        return GUI_ELEMENT_CONFIGS.data.getOrDefault(resourceLocation, GuiElementConfig.DEFAULT);
    }

    public static boolean guiElementConfigExists(ResourceLocation resourceLocation) {
        return GUI_ELEMENT_CONFIGS.data.containsKey(resourceLocation);
    }

//    public static GuiElementConfigSyncPacket toPacket(Map<ResourceLocation, GuiElementConfig> map) {
//        return new GuiElementConfigSyncPacket(map);
//    }

//    @SubscribeEvent
//    public static void onAddReloadListeners(AddReloadListenerEvent event) {
//        event.addListener(GUI_ELEMENT_CONFIGS);
//    }

    public static void initGuiElementConfigs() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if(resourceManager instanceof IReloadableResourceManager){
            IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) resourceManager;
            reloadableResourceManager.registerReloadListener(GUI_ELEMENT_CONFIGS);
        }
    }
}