package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_libraries.client.event.RenderGeoEntityEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import software.bernie.example.entity.GeoExampleEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.entities.elite.EliteMobConfig.EMPTY_TEXTURE;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends LivingEntity & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T>  {

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

    @Inject(method = "Lsoftware/bernie/geckolib3/renderers/geo/GeoEntityRenderer;getTextureLocation(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/ResourceLocation;",
            at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true, remap = false)
    private void dungeonsLibraries_bindEliteTexture(T instance, CallbackInfoReturnable<ResourceLocation> cir) {
        EliteMob cap = EliteMobHelper.getEliteMobCapability(instance);
        if(cap != null && cap.isElite() && !cap.getTexture().equals(EMPTY_TEXTURE)) {
            cir.setReturnValue(cap.getTexture());
        }
    }
}
