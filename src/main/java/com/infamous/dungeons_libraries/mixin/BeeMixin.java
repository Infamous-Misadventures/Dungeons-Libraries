package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Minion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal {

    protected BeeMixin(EntityType<? extends Animal> p_i48568_1_, Level p_i48568_2_) {
        super(p_i48568_1_, p_i48568_2_);
    }

    @Shadow
    public abstract void setFlag(int pFlagId, boolean p_226404_2_);

    @Inject(method = "Lnet/minecraft/world/entity/animal/Bee;setHasStung(Z)V",
            at = @At(value = "TAIL"))
    private void dungeonslibraries_canReplaceCurrentItem(boolean p_226449_1_, CallbackInfo ci) {
        Minion minionCapability = getMinionCapability(this);
        if(minionCapability.isMinion()){
            this.setFlag(4, false);
        }
    }

}
