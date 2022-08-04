package com.infamous.dungeons_libraries.entities.armored;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.armored.ArmoredMob;
import com.infamous.dungeons_libraries.capabilities.armored.ArmoredMobHelper;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class ArmoredMobEvents {
    public static final float SIZE_ADJUSTMENT = 1.1F;

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity && DungeonsLibrariesConfig.ENABLE_ARMORED_MOBS.get()) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            ArmoredMob cap = ArmoredMobHelper.getArmoredMobCapability(entity);
            if(cap == null) return;
            ArmoredMobConfig config = ArmoredMobConfigRegistry.getRandomConfig(entity.getType().getRegistryName(), entity.getRandom());
            if (!cap.hasSpawned() && entity.getRandom().nextFloat() < DungeonsLibrariesConfig.ARMORED_MOBS_BASE_CHANCE.get() * entity.level.getCurrentDifficultyAt(entity.blockPosition()).getSpecialMultiplier() && config != null) {
                setItemSlot(entity, EquipmentSlotType.HEAD, config.getHeadItem());
                setItemSlot(entity, EquipmentSlotType.CHEST, config.getChestItem());
                setItemSlot(entity, EquipmentSlotType.LEGS, config.getLegsItem());
                setItemSlot(entity, EquipmentSlotType.FEET, config.getFeetItem());
                setItemSlot(entity, EquipmentSlotType.MAINHAND, config.getHandItem());
                setItemSlot(entity, EquipmentSlotType.OFFHAND, config.getOffhandItem());
                cap.setArmored(true);
            }
            cap.setHasSpawned(true);
        }
    }

    private static void setItemSlot(LivingEntity entity, EquipmentSlotType slotType, ItemStack item) {
        if(!item.equals(ItemStack.EMPTY)) {
            entity.setItemSlot(slotType, item);
        }
    }

    @SubscribeEvent
    public static void onEntityEventSize(EntityEvent.Size event) {
        Entity entity = event.getEntity();

        ArmoredMob cap = ArmoredMobHelper.getArmoredMobCapability(entity);
        if(cap == null) return;
        if (cap.isArmored()) {
            float totalWidth = event.getNewSize().width * SIZE_ADJUSTMENT;
            float totalHeight = event.getNewSize().height * SIZE_ADJUSTMENT;
            event.setNewEyeHeight(event.getNewEyeHeight() * SIZE_ADJUSTMENT);
            event.setNewSize(EntitySize.fixed(totalWidth, totalHeight));
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        ArmoredMob cap = ArmoredMobHelper.getArmoredMobCapability(entity);
        if(cap == null) return;
        if (cap.isArmored()) {
            event.getMatrixStack().pushPose();
            event.getMatrixStack().scale(SIZE_ADJUSTMENT, SIZE_ADJUSTMENT, SIZE_ADJUSTMENT);

        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        ArmoredMob cap = ArmoredMobHelper.getArmoredMobCapability(entity);
        if(cap == null) return;
        if (cap.isArmored()) {
            event.getMatrixStack().popPose();
        }
    }
}