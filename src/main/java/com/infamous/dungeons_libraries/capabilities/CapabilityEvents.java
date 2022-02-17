package com.infamous.dungeons_libraries.capabilities;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider;
import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableProvider;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MasterProvider;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionProvider;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.isEnchantableEntity;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.isMinionEntity;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (isMinionEntity(event.getObject())) {
            //TODO 1.18: rename to minion
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summonable"), new MinionProvider());
        }
        if (event.getObject() instanceof LivingEntity) {
            //TODO 1.18: rename to master
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summoner"), new MasterProvider());
        }
        if (isEnchantableEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(MODID, "enchantable"), new EnchantableProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
//        if (canBeEnchanted(event.getObject())) {
            event.addCapability(new ResourceLocation(MODID, "built_in_enchantments"), new BuiltInEnchantmentsProvider(event.getObject()));
//        }
    }

    private static boolean canBeEnchanted(ItemStack itemStack){
        return Arrays.stream(EnchantmentType.values()).anyMatch(enchantmentType -> enchantmentType.canEnchant(itemStack.getItem()));
    }
}
