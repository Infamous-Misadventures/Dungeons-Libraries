package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.items.interfaces.IRangedWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import com.infamous.dungeons_libraries.items.interfaces.IUniqueGear;
import com.infamous.dungeons_libraries.mixin.ItemAccessor;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.UUID;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.RANGED_DAMAGE_MULTIPLIER;
import static java.util.UUID.randomUUID;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class BowGear extends BowItem implements IRangedWeapon, IReloadableGear, IUniqueGear {

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
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity livingEntity, int timeLeft) {
        if (livingEntity instanceof Player) {
            Player playerentity = (Player)livingEntity;
            boolean useInfiniteAmmo = playerentity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
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

                float arrowVelocity = RangedAttackHelper.getArrowVelocity(playerentity, stack, charge);
                this.fireArrows(stack, world, playerentity, itemstack, arrowVelocity);
            }
        }
    }

    public void fireArrows(ItemStack stack, Level world, Player playerentity, ItemStack itemstack, float arrowVelocity) {
        int multishotLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        int arrowsToFire = 1;
        if(multishotLevel > 0) arrowsToFire += 2;
        if(this.hasMultishotWhenCharged(stack) && arrowVelocity == 1.0F) arrowsToFire += 2;

        for(int arrowNumber = 0; arrowNumber < arrowsToFire; arrowNumber++){
            if ((double)arrowVelocity >= 0.1D) {
                boolean hasInfiniteAmmo = playerentity.getAbilities().instabuild || itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, playerentity);
                boolean isAdditionalShot = arrowNumber > 0;
                if (!world.isClientSide) {
                    this.createBowArrow(stack, world, playerentity, itemstack, arrowVelocity, arrowNumber, hasInfiniteAmmo, isAdditionalShot);
                }

                world.playSound((Player)null, playerentity.getX(), playerentity.getY(), playerentity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (playerentity.getRandom().nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);
                if (!hasInfiniteAmmo && !playerentity.getAbilities().instabuild && !isAdditionalShot) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        playerentity.getInventory().removeItem(itemstack);
                    }
                }

                playerentity.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public AbstractArrow createBowArrow(ItemStack stack, Level world, Player playerentity, ItemStack itemstack, float arrowVelocity, int i, boolean hasInfiniteAmmo, boolean isAdditionalShot) {
        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
        AbstractArrow abstractArrowEntity = arrowitem.createArrow(world, itemstack, playerentity);
        abstractArrowEntity = this.customArrow(abstractArrowEntity);
        AttributeInstance attribute = playerentity.getAttribute(RANGED_DAMAGE_MULTIPLIER.get());
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
        if (hasInfiniteAmmo || playerentity.getAbilities().instabuild
                && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
            abstractArrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        if(isAdditionalShot){
            abstractArrowEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        world.addFreshEntity(abstractArrowEntity);
        return abstractArrowEntity;
    }

    public void setArrowTrajectory(Player playerentity, float arrowVelocity, int i, AbstractArrow abstractarrowentity) {
        if(i == 0) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 1) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot() + 10.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 2) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot() - 10.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 3) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot() + 20.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 4) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot() - 20.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 5) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot() + 30.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
        if(i == 6) abstractarrowentity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot() - 30.0F, 0.0F, arrowVelocity * 3.0F, 1.0F);
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
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addFullDescription(list, stack);
    }
}
