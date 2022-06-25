package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.event.CrossbowEvent;
import com.infamous.dungeons_libraries.items.interfaces.IRangedWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import com.infamous.dungeons_libraries.items.interfaces.IUniqueGear;
import com.infamous.dungeons_libraries.mixin.CrossbowItemInvoker;
import com.infamous.dungeons_libraries.mixin.ItemAccessor;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.RANGED_DAMAGE_MULTIPLIER;
import static java.util.UUID.randomUUID;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class CrossbowGear extends CrossbowItem  implements IRangedWeapon, IReloadableGear, IUniqueGear {
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private BowGearConfig crossbowGearConfig;

    public boolean isLoadingStart = false;
    public boolean isLoadingMiddle = false;

    public CrossbowGear(Properties builder) {
        super(builder.durability(384));
        reload();
    }

    @Override
    public void reload(){
        crossbowGearConfig = CrossbowGearConfigRegistry.getConfig(this.getRegistryName());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        crossbowGearConfig.getAttributes().forEach(attributeModifier -> {
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
        ((ItemAccessor) this).setMaxDamage(crossbowGearConfig.getDurability());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    public static float getArrowVelocity(LivingEntity livingEntity, ItemStack stack) {
        float baseVelocity = 3.15F;
        if (containsChargedProjectile(stack, Items.FIREWORK_ROCKET)) {
            baseVelocity = 1.6F;
        }
        CrossbowEvent.Velocity event = new CrossbowEvent.Velocity(livingEntity, stack, baseVelocity);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return event.getVelocity();
    }

    public static float[] getRandomSoundPitches(Random rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomSoundPitch(flag), getRandomSoundPitch(!flag)};
    }

    private static float getRandomSoundPitch(boolean flagIn) {
        float f = flagIn ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static boolean canAddChargedProjectile(LivingEntity livingEntity, ItemStack stack, ItemStack stack1, boolean b, boolean b1) {
        if (stack1.isEmpty()) {
            return false;
        } else {
            boolean flag = b1 && stack1.getItem() instanceof ArrowItem;
            ItemStack itemstack;
            if (!flag && !b1 && !b) {
                itemstack = stack1.split(1);
                if (stack1.isEmpty() && livingEntity instanceof PlayerEntity) {
                    ((PlayerEntity) livingEntity).inventory.removeItem(stack1);
                }
            } else {
                itemstack = stack1.copy();
            }

            CrossbowItemInvoker.addChargedProjectile(stack, itemstack);
            return true;
        }
    }


    public float getDefaultChargeTime(){
        return this.crossbowGearConfig.getDefaultChargeTime();
    }

    @Override
    public void onUseTick(World world, LivingEntity livingEntity, ItemStack stack, int timeLeft) {
        if (!world.isClientSide) {
            int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);

            SoundEvent quickChargeSoundEvent = this.getCrossbowSoundEvent(quickChargeLevel);
            SoundEvent loadingMiddleSoundEvent = quickChargeLevel == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float chargeTime = (float) (stack.getUseDuration() - timeLeft) / (float) this.getCrossbowChargeTime(livingEntity, stack);
            if (chargeTime < 0.2F) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
            }

            if (chargeTime >= 0.2F && !this.isLoadingStart && chargeTime < 1.0F) {
                this.isLoadingStart = true;
                world.playSound((PlayerEntity) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), quickChargeSoundEvent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (chargeTime >= 0.5F && loadingMiddleSoundEvent != null && !this.isLoadingMiddle && chargeTime < 1.0F) {
                this.isLoadingMiddle = true;
                world.playSound((PlayerEntity) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), loadingMiddleSoundEvent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity livingEntity, int timeLeft) {
        float i = getCrossbowChargeTime(livingEntity, stack) + 3 - timeLeft;
        float f = this.getCrossbowCharge(livingEntity, i, stack);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(livingEntity, stack)) {
            setCharged(stack, true);
            SoundCategory soundcategory = livingEntity instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            worldIn.playSound((PlayerEntity) null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    public float getCrossbowCharge(LivingEntity livingEntity, float useTime, ItemStack stack) {
        float crossbowChargeTime = this.getCrossbowChargeTime(livingEntity, stack);
        float f = useTime / crossbowChargeTime;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public float getCrossbowChargeTime(LivingEntity livingEntity, ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        CrossbowEvent.ChargeTime event = new CrossbowEvent.ChargeTime(livingEntity, stack, this.getDefaultChargeTime());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
    }

    public SoundEvent getCrossbowSoundEvent(int i) {
        switch (i) {
            case 1:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    public void fireCrossbowProjectiles(World world, LivingEntity livingEntity, Hand hand, ItemStack stack, float velocityIn, float inaccuracyIn) {
        List<ItemStack> list = CrossbowItemInvoker.getChargedProjectiles(stack);
        float[] randomSoundPitches = CrossbowGear.getRandomSoundPitches(livingEntity.getRandom());

        for (int i = 0; i < list.size(); ++i) {
            ItemStack currentProjectile = list.get(i);
            boolean playerInCreativeMode = livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).abilities.instabuild;
            if (!currentProjectile.isEmpty()) {
                if (i == 0) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i], playerInCreativeMode, velocityIn, inaccuracyIn, 0.0F);
                } else if (i == 1) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i], playerInCreativeMode, velocityIn, inaccuracyIn, -10.0F);
                } else if (i == 2) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i], playerInCreativeMode, velocityIn, inaccuracyIn, 10.0F);
                } else if (i == 3) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i - 2], playerInCreativeMode, velocityIn, inaccuracyIn, -20.0F);
                } else if (i == 4) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i - 2], playerInCreativeMode, velocityIn, inaccuracyIn, 20.0F);
                } else if (i == 5) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i - 4], playerInCreativeMode, velocityIn, inaccuracyIn, -30.0F);
                } else if (i == 6) {
                    fireProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[i - 4], playerInCreativeMode, velocityIn, inaccuracyIn, 30.0F);
                }
            }
        }

        CrossbowItemInvoker.onCrossbowShot(world, livingEntity, stack);
    }

    private AbstractArrowEntity createCrossbowArrow(World world, LivingEntity livingEntity, ItemStack stack, ItemStack stack1) {
        ArrowItem arrowItem = (ArrowItem) ((ArrowItem) (stack1.getItem() instanceof ArrowItem ? stack1.getItem() : Items.ARROW));
        AbstractArrowEntity abstractArrowEntity = arrowItem.createArrow(world, stack1, livingEntity);
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(RANGED_DAMAGE_MULTIPLIER.get());
        if(attribute != null) {
            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() * (attribute.getValue() + 1));
        }
        if (livingEntity instanceof PlayerEntity) {
            abstractArrowEntity.setCritArrow(true);
        }

        abstractArrowEntity.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        abstractArrowEntity.setShotFromCrossbow(true);
        int piercingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack);
        if (piercingLevel > 0) {
            abstractArrowEntity.setPierceLevel((byte) piercingLevel);
        }

        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        if (this.shootsHeavyArrows(stack)) powerLevel++;
        if (powerLevel > 0) {
            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
        }

        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
        if (this.shootsHeavyArrows(stack)) punchLevel++;
        if (punchLevel > 0) {
            abstractArrowEntity.setKnockback(punchLevel);
        }

        return abstractArrowEntity;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return (int) getCrossbowChargeTime(null, stack) + 3;
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return getGearConfig().getRarity();
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

    // FORMER CROSSBOWITEM STATIC METHODS MADE NON-STATIC
    public void fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!worldIn.isClientSide) {
            boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
            ProjectileEntity projectileentity;
            if (flag) {
                projectileentity = new FireworkRocketEntity(worldIn, projectile, shooter, shooter.getX(), shooter.getEyeY() - (double) 0.15F, shooter.getZ(), true);
            } else {
                projectileentity = createCrossbowArrow(worldIn, shooter, crossbow, projectile);
                if (isCreativeMode || projectileAngle != 0.0F) {
                    ((AbstractArrowEntity) projectileentity).pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                }
            }

            if (shooter instanceof ICrossbowUser) {
                ICrossbowUser icrossbowuser = (ICrossbowUser) shooter;
                icrossbowuser.shootCrossbowProjectile(Objects.requireNonNull(icrossbowuser.getTarget()), crossbow, projectileentity, projectileAngle);
            } else {
                Vector3d vector3d1 = shooter.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
                Vector3d vector3d = shooter.getViewVector(1.0F);
                Vector3f vector3f = new Vector3f(vector3d);
                vector3f.transform(quaternion);
                projectileentity.shoot((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), velocity, inaccuracy);
            }

            damageItem(flag?3:1, crossbow, shooter, handIn);
            worldIn.addFreshEntity(projectileentity);
            worldIn.playSound((PlayerEntity) null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    protected void damageItem(int amount, ItemStack crossbow, LivingEntity shooter, Hand handIn){
        crossbow.hurtAndBreak(amount, shooter, (p_220017_1_) -> {
            p_220017_1_.broadcastBreakEvent(handIn);
        });
    }

    protected static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
        int multishotLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        CrossbowGear adci= ((CrossbowGear) stack.getItem());
        if (adci.hasExtraMultishot(stack)) multishotLevel++;
        int arrowsToFire = 1 + multishotLevel * 2;
        boolean flag = entityIn instanceof PlayerEntity && ((PlayerEntity) entityIn).abilities.instabuild;
        ItemStack itemstack = entityIn.getProjectile(stack);
        ItemStack itemstack1 = itemstack.copy();

        for (int i = 0; i < arrowsToFire; ++i) {
            if (i > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!canAddChargedProjectile(entityIn, stack, itemstack, i > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isUnique() {
        return this.crossbowGearConfig.isUnique();
    }

    public BowGearConfig getGearConfig() {
        return crossbowGearConfig;
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addFullDescription(list, stack);
    }
}
