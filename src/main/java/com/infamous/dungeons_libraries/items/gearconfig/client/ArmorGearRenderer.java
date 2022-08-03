package com.infamous.dungeons_libraries.items.gearconfig.client;

import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ArmorGearRenderer  extends GeoArmorRenderer<ArmorGear> {
    public ArmorGearRenderer() {
        super(new ArmorGearModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}