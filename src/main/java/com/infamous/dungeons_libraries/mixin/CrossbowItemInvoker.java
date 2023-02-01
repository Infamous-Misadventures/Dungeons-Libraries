package com.infamous.dungeons_libraries.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(CrossbowItem.class)
public interface CrossbowItemInvoker {

    @Accessor
    boolean getStartSoundPlayed();

    @Accessor
    boolean getMidLoadSoundPlayed();

    @Accessor
    void setStartSoundPlayed(boolean b);

    @Accessor
    void setMidLoadSoundPlayed(boolean b);

    @Invoker
    static void callAddChargedProjectile(ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker
    static List<ItemStack> callGetChargedProjectiles(ItemStack pCrossbowStack) {
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker
    static boolean callTryLoadProjectiles(LivingEntity livingEntity, ItemStack stack){
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker
    static void callShootProjectile(Level p_40895_, LivingEntity p_40896_, InteractionHand p_40897_, ItemStack p_40898_, ItemStack p_40899_, float p_40900_, boolean p_40901_, float p_40902_, float p_40903_, float p_40904_){
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker
    SoundEvent callGetStartSound(int quickChargeLevel);

    @Invoker
    static float[] callGetShotPitches(RandomSource pRandomSource){
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker
    static void callOnCrossbowShot(Level pLevel, LivingEntity pShooter, ItemStack pCrossbowStack) {
        throw new RuntimeException("Invoker failed to mixin");
    }
}
