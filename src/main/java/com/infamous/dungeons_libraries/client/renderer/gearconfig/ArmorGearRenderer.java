package com.infamous.dungeons_libraries.client.renderer.gearconfig;

import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterialBaseType;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class ArmorGearRenderer<T extends ArmorGear>  extends GeoArmorRenderer<T> {
    public ArmorGearRenderer() {
        super(new ArmorGearModel<>());
    }

    public ArmorGearRenderer(ArmorGearModel<T> model) {
        super(model);
    }

    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight,
                                   int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        this.prepMatrixForBone(poseStack, bone);
        renderCubesOfBone(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        renderChildBones(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    public void prepMatrixForBone(PoseStack stack, GeoBone bone) {
        RenderUtils.translateMatrixToBone(stack, bone);
        RenderUtils.translateToPivotPoint(stack, bone);
        EntityRenderer<? super LivingEntity> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entityLiving);
        if(!(entityRenderer instanceof GeoEntityRenderer) || !bone.getName().contains("armor")) {
            RenderUtils.rotateMatrixAroundBone(stack, bone);
        }
        RenderUtils.scaleMatrixForBone(stack, bone);
        ArmorMaterial material = this.currentArmorItem.getMaterial();
        if(bone.getName().contains("Body") && material instanceof DungeonsArmorMaterial && ((DungeonsArmorMaterial) material).getBaseType() == ArmorMaterialBaseType.CLOTH){
            stack.scale(1.0F, 1.0F, 0.93F);
        }
        RenderUtils.translateAwayFromPivotPoint(stack, bone);
    }

    @Override
    public void renderCubesOfBone(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isHidden())
            return;

        for (GeoCube cube : bone.childCubes) {
            if (!bone.cubesAreHidden()) {
                poseStack.pushPose();
                if(entityLiving instanceof SpawnArmoredMob && ((SpawnArmoredMob) entityLiving).getArmorSet().getRegistryName() == this.currentArmorItem.getArmorSet()){
                renderCube(cube, poseStack, buffer, packedLight, LivingEntityRenderer.getOverlayCoords(entityLiving, 0.0F), red, green, blue, alpha);
                }else {
                    renderCube(cube, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
                }
                poseStack.popPose();
            }
        }
    }
}