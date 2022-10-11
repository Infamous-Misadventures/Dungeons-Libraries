package com.infamous.dungeons_libraries.items.artifacts;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.event.ArtifactEvent;
import com.infamous.dungeons_libraries.mixin.CooldownAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.ARTIFACT_COOLDOWN_MULTIPLIER;
import static com.infamous.dungeons_libraries.client.TooltipHelper.addFullDescription;
import static com.infamous.dungeons_libraries.tags.ItemTags.ARTIFACT_REPAIR_ITEMS;

public abstract class ArtifactItem extends Item implements ICurioItem {
    protected final UUID SLOT0_UUID = UUID.fromString("7037798e-ac2c-4711-aa72-ba73589f1411");
    protected final UUID SLOT1_UUID = UUID.fromString("1906bae9-9f26-4194-bb8a-ef95b8cad134");
    protected final UUID SLOT2_UUID = UUID.fromString("b99aa930-03d0-4b2d-aa69-7b5d943dd75c");
    protected boolean procOnItemUse = false;

    public ArtifactItem(Properties properties) {
        super(properties
                .durability(DungeonsLibrariesConfig.ARTIFACT_DURABILITY.get())
        );
    }

    public static void putArtifactOnCooldown(Player playerIn, Item item) {
        int cooldownInTicks = item instanceof ArtifactItem ?
                ((ArtifactItem)item).getCooldownInSeconds() * 20 : 0;

        AttributeInstance artifactCooldownMultiplierAttribute = playerIn.getAttribute(ARTIFACT_COOLDOWN_MULTIPLIER.get());
        double attributeModifier = artifactCooldownMultiplierAttribute != null ? artifactCooldownMultiplierAttribute.getValue() : 1.0D;
        playerIn.getCooldowns().addCooldown(item, Math.max(0, (int) (cooldownInTicks * attributeModifier)));
    }

    public static void triggerSynergy(Player player, ItemStack stack){
        ArtifactEvent.Activated event = new ArtifactEvent.Activated(player, stack);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
    }

    public static void reduceArtifactCooldowns(Player playerEntity, double reductionInSeconds){
        for(Item item : playerEntity.getCooldowns().cooldowns.keySet()){
            if(item instanceof ArtifactItem){
                int createTicks = ((CooldownAccessor)playerEntity.getCooldowns().cooldowns.get(item)).getStartTime();
                int expireTicks = ((CooldownAccessor)playerEntity.getCooldowns().cooldowns.get(item)).getEndTime();
                int duration = expireTicks - createTicks;
                playerEntity.getCooldowns().addCooldown(item, Math.max(0, duration - (int)(reductionInSeconds * 20)));
            }
        }
    }

    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.RARE;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return ForgeRegistries.ITEMS.tags().getTag(ARTIFACT_REPAIR_ITEMS).contains(repair.getItem()) || super.isValidRepairItem(toRepair, repair);
    }

    public InteractionResultHolder<ItemStack> activateArtifact(ArtifactUseContext artifactUseContext) {
        if(artifactUseContext.getPlayer() != null) {
            ItemStack itemStack = artifactUseContext.getItemStack();
            if (artifactUseContext.getPlayer().getCooldowns().isOnCooldown(itemStack.getItem())){
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
            }
        }
        InteractionResultHolder<ItemStack> procResult = procArtifact(artifactUseContext);
        if(procResult.getResult().consumesAction() && artifactUseContext.getPlayer() != null && !artifactUseContext.getLevel().isClientSide){
            triggerSynergy(artifactUseContext.getPlayer(), artifactUseContext.getItemStack());
        }
        return procResult;
    }

    public abstract InteractionResultHolder<ItemStack> procArtifact(ArtifactUseContext iuc);

    public abstract int getCooldownInSeconds();

    public abstract int getDurationInSeconds();

    public void stopUsingArtifact(LivingEntity livingEntity){}

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(int slotIndex) {
        return ImmutableMultimap.of();
    }

    protected UUID getUUIDForSlot(int slotIndex){
        switch(slotIndex){
            case 0: return SLOT0_UUID;
            case 1: return SLOT1_UUID;
            case 2: return SLOT2_UUID;
            default: return SLOT2_UUID;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        addFullDescription(list, stack);
    }
}
