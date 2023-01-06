package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.infamous.dungeons_libraries.entities.elite.EliteMobConfig.EMPTY_TEXTURE;

@Mixin(LivingEntityRenderer.class)
public class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @ModifyVariable(method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;", at = @At("STORE"), ordinal = 0)
    private ResourceLocation dungeonslibraries_getRenderType(ResourceLocation value, T p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(p_115322_);
        if (cap.isElite() && !cap.getTexture().equals(EMPTY_TEXTURE)) {
            return cap.getTexture();
        }
        return value;
    }
}
