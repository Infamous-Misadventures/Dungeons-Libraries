package com.infamous.dungeons_libraries.entities.elite;

import com.google.common.collect.ImmutableMultimap;
import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.message.EliteMobMessage;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static java.util.UUID.randomUUID;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class EliteMobEvents {
    public static final float SIZE_ADJUSTMENT = 1.1F;

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide() && event.getEntity() instanceof LivingEntity && DungeonsLibrariesConfig.ENABLE_ELITE_MOBS.get()) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
            if(cap == null) return;
            EliteMobConfig config = EliteMobConfigRegistry.getRandomConfig(entity.getType().getRegistryName(), entity.getRandom());
            World level = event.getWorld();
            if (!cap.hasSpawned() && config != null) {
                Chunk chunk = level.getChunkSource().getChunkNow(entity.blockPosition().getX() >> 4, entity.blockPosition().getZ() >> 4);
                if (chunk != null && chunk.getStatus().isOrAfter(ChunkStatus.FULL)
                        && entity.getRandom().nextFloat() < DungeonsLibrariesConfig.ELITE_MOBS_BASE_CHANCE.get() * level.getCurrentDifficultyAt(entity.blockPosition()).getSpecialMultiplier()) {
                    setItemSlot(entity, EquipmentSlotType.HEAD, config.getHeadItem());
                    setItemSlot(entity, EquipmentSlotType.CHEST, config.getChestItem());
                    setItemSlot(entity, EquipmentSlotType.LEGS, config.getLegsItem());
                    setItemSlot(entity, EquipmentSlotType.FEET, config.getFeetItem());
                    setItemSlot(entity, EquipmentSlotType.MAINHAND, config.getHandItem());
                    setItemSlot(entity, EquipmentSlotType.OFFHAND, config.getOffhandItem());
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                    config.getAttributes().forEach(attributeModifier -> {
                        Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
                        if (attribute != null) {
                            builder.put(attribute, new AttributeModifier(randomUUID(), "Armor modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
                        }
                    });
                    entity.getAttributes().addTransientAttributeModifiers(builder.build());
                    cap.setElite(true);
                    cap.setTexture(config.getTexture());
                }
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

        EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
        if(cap == null) return;
        if (cap.isElite()) {
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
        EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
        if(cap == null) return;
        if (cap.isElite()) {
            event.getMatrixStack().pushPose();
            event.getMatrixStack().scale(SIZE_ADJUSTMENT, SIZE_ADJUSTMENT, SIZE_ADJUSTMENT);

        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
        if(cap == null) return;
        if (cap.isElite()) {
            event.getMatrixStack().popPose();
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayerEntity && target instanceof LivingEntity) {
            EliteMob cap = EliteMobHelper.getEliteMobCapability(event.getTarget());
            if(cap == null) return;
            if (cap.isElite()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new EliteMobMessage(target.getId(), cap.isElite(), cap.getTexture()));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingConvert(LivingConversionEvent.Post event)
    {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(event.getEntity());
        EliteMob outcomeCap = EliteMobHelper.getEliteMobCapability(event.getOutcome());
        if(cap == null || outcomeCap == null) return;
        outcomeCap.setHasSpawned(true);
        if(cap.isElite()) {
            outcomeCap.setElite(true);
        }
    }
}