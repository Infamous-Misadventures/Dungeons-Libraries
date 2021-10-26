package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.client.event.RenderGeoEntityEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends LivingEntity & IAnimatable> extends EntityRenderer<T> {
    protected GeoEntityRendererMixin(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
            at = @At(value = "HEAD"), remap=false)
    private void dungeonsLibraries_throwPreEvent(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new RenderGeoEntityEvent.Pre<T>(entity, (GeoEntityRenderer<T>) (Object) this, partialTicks, stack, bufferIn, packedLightIn))) return;
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V",
            at = @At(value = "TAIL"), remap=false)
    private void dungeonsLibraries_throwPostEvent(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new RenderGeoEntityEvent.Post<T>(entity, (GeoEntityRenderer<T>) (Object) this, partialTicks, stack, bufferIn, packedLightIn));
    }
}
