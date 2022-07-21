package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.items.gearconfig.ArmorGearConfigRegistry.ARMOR_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.gearconfig.BowGearConfigRegistry.BOW_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.gearconfig.CrossbowGearConfigRegistry.CROSSBOW_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry.MELEE_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterials.ARMOR_MATERIALS;
import static com.infamous.dungeons_libraries.items.materials.weapon.WeaponMaterials.WEAPON_MATERIALS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GearConfigReloadListener implements IResourceManagerReloadListener {

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(WEAPON_MATERIALS);
        event.addListener(ARMOR_MATERIALS);
        event.addListener(MELEE_GEAR_CONFIGS);
        event.addListener(ARMOR_GEAR_CONFIGS);
        event.addListener(BOW_GEAR_CONFIGS);
        event.addListener(CROSSBOW_GEAR_CONFIGS);
        event.addListener(new GearConfigReloadListener());
    }
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        reloadAllItems();
    }

    public static void reloadAllItems() {
        ITEMS.getEntries().stream().filter(registryKeyItemEntry -> registryKeyItemEntry.getValue() instanceof IReloadableGear).map(registryKeyItemEntry -> (IReloadableGear) registryKeyItemEntry.getValue()).forEach(IReloadableGear::reload);
    }
}
