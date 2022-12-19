package com.infamous.dungeons_libraries.entities.elite;

import com.google.common.collect.ImmutableMultimap;
import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.network.EliteMobMessage;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

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
            EliteMobConfig config = EliteMobConfigRegistry.getRandomConfig(entity.getType().getRegistryName(), entity.getRandom());
            Level level = event.getWorld();
            if (!cap.hasSpawned() && config != null) {
                LevelChunk chunk = level.getChunkSource().getChunkNow(entity.blockPosition().getX() >> 4, entity.blockPosition().getZ() >> 4);
                if (chunk != null && chunk.getStatus().isOrAfter(ChunkStatus.FULL)
                        && entity.getRandom().nextFloat() < DungeonsLibrariesConfig.ELITE_MOBS_BASE_CHANCE.get() * level.getCurrentDifficultyAt(entity.blockPosition()).getSpecialMultiplier()){
                    setItemSlot(entity, EquipmentSlot.HEAD, config.getHeadItem());
                    setItemSlot(entity, EquipmentSlot.CHEST, config.getChestItem());
                    setItemSlot(entity, EquipmentSlot.LEGS, config.getLegsItem());
                    setItemSlot(entity, EquipmentSlot.FEET, config.getFeetItem());
                    setItemSlot(entity, EquipmentSlot.MAINHAND, config.getHandItem());
                    setItemSlot(entity, EquipmentSlot.OFFHAND, config.getOffhandItem());
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

    private static void setItemSlot(LivingEntity entity, EquipmentSlot slotType, ItemStack item) {
        if(!item.equals(ItemStack.EMPTY)) {
            entity.setItemSlot(slotType, item);
        }
    }

    @SubscribeEvent
    public static void onEntityEventSize(EntityEvent.Size event) {
        Entity entity = event.getEntity();

        EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
        if (cap.isElite()) {
            float totalWidth = event.getNewSize().width * SIZE_ADJUSTMENT;
            float totalHeight = event.getNewSize().height * SIZE_ADJUSTMENT;
            event.setNewEyeHeight(event.getNewEyeHeight() * SIZE_ADJUSTMENT);
            event.setNewSize(EntityDimensions.fixed(totalWidth, totalHeight));
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPre(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
        if (cap.isElite()) {
            event.getPoseStack().pushPose();
            event.getPoseStack().scale(SIZE_ADJUSTMENT, SIZE_ADJUSTMENT, SIZE_ADJUSTMENT);

        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderLivingEventPost(RenderLivingEvent.Post<LivingEntity, EntityModel<LivingEntity>> event) {
        final LivingEntity entity = event.getEntity();
        EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
        if (cap.isElite()) {
            event.getPoseStack().popPose();
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event){
        Player player = event.getPlayer();
        Entity target = event.getTarget();
        if (player instanceof ServerPlayer && target instanceof LivingEntity) {
            EliteMob cap = EliteMobHelper.getEliteMobCapability(event.getTarget());
            if (cap.isElite()) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new EliteMobMessage(target.getId(), cap.isElite(), cap.getTexture()));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingConvert(LivingConversionEvent.Post event)
    {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(event.getEntity());
        EliteMob outcomeCap = EliteMobHelper.getEliteMobCapability(event.getOutcome());
        outcomeCap.setHasSpawned(true);
        if(cap.isElite()) {
            outcomeCap.setElite(true);
        }
    }
}