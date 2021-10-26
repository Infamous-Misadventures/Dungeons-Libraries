package com.infamous.dungeons_libraries.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public abstract class RenderGeoEntityEvent<T extends LivingEntity & IAnimatable> extends Event
{

    private final LivingEntity entity;
    private final GeoEntityRenderer<T> renderer;
    private final float partialRenderTick;
    private final MatrixStack matrixStack;
    private final IRenderTypeBuffer buffers;
    private final int light;

    public RenderGeoEntityEvent(LivingEntity entity, GeoEntityRenderer<T> renderer, float partialRenderTick, MatrixStack matrixStack,
                                IRenderTypeBuffer buffers, int light)
    {
        this.entity = entity;
        this.renderer = renderer;
        this.partialRenderTick = partialRenderTick;
        this.matrixStack = matrixStack;
        this.buffers = buffers;
        this.light = light;
    }

    public LivingEntity getEntity() { return entity; }
    public GeoEntityRenderer<T> getRenderer() { return renderer; }
    public float getPartialRenderTick() { return partialRenderTick; }
    public MatrixStack getMatrixStack() { return matrixStack; }
    public IRenderTypeBuffer getBuffers() { return buffers; }
    public int getLight() { return light; }

    @Cancelable
    public static class Pre<T extends LivingEntity & IAnimatable> extends RenderGeoEntityEvent<T>
    {
        public Pre(LivingEntity entity, GeoEntityRenderer<T> renderer, float partialRenderTick, MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
            super(entity, renderer, partialRenderTick, matrixStack, buffers, light);
        }
    }

    public static class Post<T extends LivingEntity & IAnimatable> extends RenderGeoEntityEvent<T>
    {
        public Post(LivingEntity entity, GeoEntityRenderer<T> renderer, float partialRenderTick, MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
            super(entity, renderer, partialRenderTick, matrixStack, buffers, light);
        }
    }
}
