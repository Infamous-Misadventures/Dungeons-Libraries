package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.event.BowEvent;
import com.infamous.dungeons_libraries.items.interfaces.IRangedWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import com.infamous.dungeons_libraries.mixin.ItemAccessor;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.UUID;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.RANGED_DAMAGE_MULTIPLIER;
import static java.util.UUID.randomUUID;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class BowGear extends BowItem implements IRangedWeapon, IReloadableGear {

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private BowGearConfig bowGearConfig;

    public BowGear(Properties builder) {
        super(builder.durability(384));
        reload();
    }

    @Override
    public void reload(){
        bowGearConfig = BowGearConfigRegistry.getConfig(this.getRegistryName());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        bowGearConfig.getAttributes().forEach(attributeModifier -> {
            Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
            if(attribute != null) {
                UUID uuid = randomUUID();
                if (ATTACK_DAMAGE.equals(attribute)) {
                    uuid = BASE_ATTACK_DAMAGE_UUID;
                } else if (ATTACK_SPEED.equals(attribute)) {
                    uuid = BASE_ATTACK_SPEED_UUID;
                }
                builder.put(attribute, new AttributeModifier(uuid, "Weapon modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
            }
        });
        this.defaultModifiers = builder.build();
        ((ItemAccessor) this).setMaxDamage(bowGearConfig.getDurability());
    }

    public float getDefaultChargeTime(){
        return this.bowGearConfig.getDefaultChargeTime();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)livingEntity;
            boolean useInfiniteAmmo = playerentity.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
            ItemStack itemstack = playerentity.getProjectile(stack);
            int charge = this.getUseDuration(stack) - timeLeft;
            charge = ForgeEventFactory.onArrowLoose(stack, world, playerentity, charge, !itemstack.isEmpty() || useInfiniteAmmo);
            if (charge < 0) {
                return;
            }

            if (!itemstack.isEmpty() || useInfiniteAmmo) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float arrowVelocity = this.getBowArrowVelocity(playerentity, stack, charge);
                this.fireArrows(stack, world, playerentity, itemstack, arrowVelocity);
            }
        }
    }

    public void fireArrows(ItemStack stack, World world, PlayerEntity playerentity, ItemStack itemstack, float arrowVelocity) {
        int multishotLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        int arrowsToFire = 1;
        if(multishotLevel > 0) arrowsToFire += 2;
        if(this.hasMultishotWhenCharged(stack) && arrowVelocity == 1.0F) arrowsToFire += 2;

        for(int arrowNumber = 0; arrowNumber < arrowsToFire; arrowNumber++){
            if ((double)arrowVelocity >= 0.1D) {
                boolean hasInfiniteAmmo = playerentity.abilities.instabuild || itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity);
                boolean isAdditionalShot = arrowNumber > 0;
                if (!world.isClientSide) {
                    this.createBowArrow(stack, world, playerentity, itemstack, arrowVelocity, arrowNumber, hasInfiniteAmmo, isAdditionalShot);
                }

                world.playSound((PlayerEntity)null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);
                if (!hasInfiniteAmmo && !playerentity.abilities.instabuild && !isAdditionalShot) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        playerentity.inventory.removeItem(itemstack);
                    }
                }

                playerentity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public AbstractArrowEntity createBowArrow(ItemStack stack, World world, PlayerEntity playerentity, ItemStack itemstack, float arrowVelocity, int i, boolean hasInfiniteAmmo, boolean isAdditionalShot) {
        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
        AbstractArrowEntity abstractArrowEntity = arrowitem.createArrow(world, itemstack, playerentity);
        abstractArrowEntity = this.customArrow(abstractArrowEntity);
        ModifiableAttributeInstance attribute = playerentity.getAttribute(RANGED_DAMAGE_MULTIPLIER.get());
        if(attribute != null) {
            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() * (attribute.getValue() + 1));
        }
        this.setArrowTrajectory(playerentity, arrowVelocity, i, abstractArrowEntity);
        if (arrowVelocity == 1.0F) {
            abstractArrowEntity.setCritArrow(true);
        }
        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);

        // Damage Boosters
        if(this.shootsHeavyArrows(stack)) powerLevel++;

        if (powerLevel > 0) {
            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() + (double)powerLevel * 0.5D + 0.5D);
        }
        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
        if (punchLevel > 0) {
            abstractArrowEntity.setKnockback(punchLevel);
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
            abstractArrowEntity.setSecondsOnFire(100);
        }
        stack.hurtAndBreak(1, playerentity, (p_lambda$onPlayerStoppedUsing$0_1_) -> {
            p_lambda$onPlayerStoppedUsing$0_1_.broadcastBreakEvent(playerentity.getUsedItemHand());
        });
        if (hasInfiniteAmmo || playerentity.abilities.instabuild
                && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
            abstractArrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
        }
        if(isAdditionalShot){
            abstractArrowEntity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
        }
        world.addFreshEntity(abstractArrowEntity);
        return abstractArrowEntity;
    }

    public void setArrowTrajectory(PlayerEntity playerentity, float arrowVelocity, int i, AbstractArrowEntity abstractarrowentity) {
        if(i == 0) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 1) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot + 10.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 2) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot - 10.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 3) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot + 20.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 4) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot - 20.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 5) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot + 30.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 6) abstractarrowentity.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot - 30.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
    }

    public float getBowArrowVelocity(LivingEntity livingEntity, ItemStack stack, int charge) {
        float bowChargeTime = getBowChargeTime(livingEntity, stack);
        float arrowVelocity = (float)charge / bowChargeTime;
        arrowVelocity = (arrowVelocity * arrowVelocity + arrowVelocity * 2.0F) / 3.0F;
        float velocityLimit = 1.0F;
        int overchargeLevel = 0; //EnchantmentHelper.getItemEnchantmentLevel(RangedEnchantmentList.OVERCHARGE, stack);
        if(overchargeLevel > 0){
            velocityLimit += overchargeLevel;
        }
        if (arrowVelocity > velocityLimit) {
            arrowVelocity = velocityLimit;
        }

        return arrowVelocity;
    }

    public float getBowChargeTime(LivingEntity livingEntity, ItemStack stack){
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        BowEvent.ChargeTime event = new BowEvent.ChargeTime(livingEntity, stack, this.getDefaultChargeTime());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return getGearConfig().getRarity();
    }

    @Override
    public boolean isUnique() {
        return bowGearConfig.isUnique();
    }

    public BowGearConfig getGearConfig() {
        return bowGearConfig;
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addFullDescription(list, stack);
    }
}
