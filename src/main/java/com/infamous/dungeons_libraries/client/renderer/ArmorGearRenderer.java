package com.infamous.dungeons_libraries.client.renderer;

import com.infamous.dungeons_libraries.client.model.ArmorGearModel;
import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterialBaseType;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IArmorMaterial;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class ArmorGearRenderer<T extends ArmorGear> extends GeoArmorRenderer<T> {
    public ArmorGearRenderer() {
        super(new ArmorGearModel<>());
    }

    public ArmorGearRenderer(ArmorGearModel<T> model) {
        super(model);
    }

    @Override
    public void preparePositionRotationScale(GeoBone bone, MatrixStack stack) {
        RenderUtils.translate(bone, stack);
        RenderUtils.moveToPivot(bone, stack);
        EntityRenderer<? super LivingEntity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entityLiving);
        if(!(entityRenderer instanceof GeoEntityRenderer) || !bone.getName().contains("armor")) {
            RenderUtils.rotate(bone, stack);
        }
        RenderUtils.scale(bone, stack);
        IArmorMaterial material = this.currentArmorItem.getMaterial();
        if(bone.getName().contains("Body") && material instanceof DungeonsArmorMaterial && ((DungeonsArmorMaterial) material).getBaseType() == ArmorMaterialBaseType.CLOTH){
            stack.scale(1.0F, 1.0F, 0.93F);
        }
        RenderUtils.moveBackFromPivot(bone, stack);
    }

    @Override
    public void renderCubesOfBone(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (!bone.isHidden()) {
            for (GeoCube cube : bone.childCubes) {
                stack.pushPose();
                if (!bone.cubesAreHidden()) {
                    if(entityLiving instanceof SpawnArmoredMob && ((SpawnArmoredMob) entityLiving).getArmorSet().getRegistryName() == this.currentArmorItem.getArmorSet()){
                        renderCube(cube, stack, bufferIn, packedLightIn, LivingRenderer.getOverlayCoords(entityLiving, 0.0F), red, green, blue, alpha);
                    }else {
                        renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                    }
                }
                stack.popPose();
            }
        }
    }
}