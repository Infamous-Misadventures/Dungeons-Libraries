package com.infamous.dungeons_libraries.items.gearconfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class GearConfigAttributeModifier {

    public static final Codec<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION_CODEC = Codec.INT.flatComapMap(AttributeModifier.Operation::fromValue, d -> DataResult.success(d.toValue()));

    public static final Codec<GearConfigAttributeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("attribute").forGetter(GearConfigAttributeModifier::getAttributeResourceLocation),
            Codec.DOUBLE.fieldOf("amount").forGetter(GearConfigAttributeModifier::getAmount),
            ATTRIBUTE_MODIFIER_OPERATION_CODEC.fieldOf("operation").forGetter(GearConfigAttributeModifier::getOperation)
    ).apply(instance, GearConfigAttributeModifier::new));

    private ResourceLocation attributeResourceLocation;
    private double amount;
    private AttributeModifier.Operation operation;

    public GearConfigAttributeModifier(ResourceLocation attributeResourceLocation, double amount, AttributeModifier.Operation operation) {
        this.attributeResourceLocation = attributeResourceLocation;
        this.amount = amount;
        this.operation = operation;
    }

    public ResourceLocation getAttributeResourceLocation() {
        return attributeResourceLocation;
    }

    public double getAmount() {
        return amount;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }
}
