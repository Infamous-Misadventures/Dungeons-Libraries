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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class CrossbowGear extends CrossbowItem implements IRangedWeapon, IReloadableGear, IUniqueGear {
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private BowGearConfig crossbowGearConfig;

    public CrossbowGear(Properties builder) {
        super(builder.durability(384));
        reload();
    }

    @Override
    public void reload() {
        crossbowGearConfig = CrossbowGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(this));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        crossbowGearConfig.getAttributes().forEach(attributeModifier -> {
            Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
            if (attribute != null) {
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
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }


    public float getDefaultChargeTime() {
        return this.crossbowGearConfig.getDefaultChargeTime();
    }

    @Override
    public void onUseTick(Level world, LivingEntity livingEntity, ItemStack stack, int timeLeft) {
        if (!world.isClientSide) {
            int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);

            CrossbowItemInvoker crossbowItemInvoker = (CrossbowItemInvoker) this;
            SoundEvent quickChargeSoundEvent = crossbowItemInvoker.callGetStartSound(quickChargeLevel);
            SoundEvent loadingMiddleSoundEvent = quickChargeLevel == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float chargeTime = (float) (stack.getUseDuration() - timeLeft) / this.getCrossbowChargeTime(livingEntity, stack);
            if (chargeTime < 0.2F) {
                crossbowItemInvoker.setStartSoundPlayed(false);
                crossbowItemInvoker.setMidLoadSoundPlayed(false);
            }

            if (chargeTime >= 0.2F && !crossbowItemInvoker.getStartSoundPlayed() && chargeTime < 1.0F) {
                crossbowItemInvoker.setStartSoundPlayed(true);
                world.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), quickChargeSoundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (chargeTime >= 0.5F && loadingMiddleSoundEvent != null && !crossbowItemInvoker.getMidLoadSoundPlayed() && chargeTime < 1.0F) {
                crossbowItemInvoker.setMidLoadSoundPlayed(true);
                world.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), loadingMiddleSoundEvent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity livingEntity, int timeLeft) {
        float chargeTime = getCrossbowChargeTime(livingEntity, stack) + 3 - timeLeft;
        float getCharge = this.getCrossbowCharge(livingEntity, chargeTime, stack);
        // Call to CrossbowItem.tryLoadProjectiles must be in-line as it modifies NBT without the previous checks
        // Do not refactor as a variable preceding this if statement
        if (getCharge >= 1.0F && !isCharged(stack) && CrossbowItemInvoker.callTryLoadProjectiles(livingEntity, stack)) {
            setCharged(stack, true);
            SoundSource soundSource = livingEntity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            worldIn.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundSource, 1.0F, 1.0F / (livingEntity.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    public float getCrossbowCharge(LivingEntity livingEntity, float useTime, ItemStack stack) {
        float crossbowChargeTime = this.getCrossbowChargeTime(livingEntity, stack);
        float charge = useTime / crossbowChargeTime;
        if (charge > 1.0F) {
            charge = 1.0F;
        }

        return charge;
    }

    public float getCrossbowChargeTime(@Nullable LivingEntity livingEntity, ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        CrossbowEvent.ChargeTime event = new CrossbowEvent.ChargeTime(livingEntity, stack, this.getDefaultChargeTime());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
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

    @Override
    public boolean isUnique() {
        return this.crossbowGearConfig.isUnique();
    }

    public BowGearConfig getGearConfig() {
        return crossbowGearConfig;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addFullDescription(list, stack);
    }
}
