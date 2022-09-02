package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.infamous.dungeons_libraries.entities.elite.EliteMobConfig.EMPTY_TEXTURE;

@Mixin(LivingRenderer.class)
public class LivingRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @ModifyVariable(method = "Lnet/minecraft/client/renderer/entity/LivingRenderer;getRenderType(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;", at = @At("STORE"), ordinal = 0)
    private ResourceLocation dungeonslibraries_getRenderTypeMixin(ResourceLocation value, T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(p_230496_1_);
        if(cap != null && cap.isElite() && !cap.getTexture().equals(EMPTY_TEXTURE)) {
            return cap.getTexture();
        }
        return value;
    }
}
