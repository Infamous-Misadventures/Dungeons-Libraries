package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGear;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity {

    protected BeeEntityMixin(EntityType<? extends AnimalEntity> p_i48568_1_, World p_i48568_2_) {
        super(p_i48568_1_, p_i48568_2_);
    }

    @Shadow
    public abstract void setFlag(int pFlagId, boolean p_226404_2_);

    @Inject(method = "Lnet/minecraft/entity/passive/BeeEntity;setHasStung(Z)V",
            at = @At(value = "TAIL"))
    private void dungeonslibraries_canReplaceCurrentItem(boolean p_226449_1_, CallbackInfo ci) {
        IMinion minionCapability = getMinionCapability(this);
        if(minionCapability != null && minionCapability.isMinion()){
            this.setFlag(4, false);
        }
    }

}
