package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.items.interfaces.IComboWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IMeleeWeapon;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class SwordGearConfig extends SwordItem implements IMeleeWeapon, IComboWeapon {

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private GearConfig gearConfig;

    public SwordGearConfig(IItemTier tier, Item.Properties properties) {
        super(tier,
                1, -2.4F,
                properties);
        reload();
    }

    public void reload(){
        gearConfig = GearConfigRegistry.getConfig(this.getRegistryName());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        gearConfig.getAttributes().forEach(attributeModifier -> {
            Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
            UUID uuid = randomUUID();
            if(ATTACK_DAMAGE.equals(attribute)){
                uuid = BASE_ATTACK_DAMAGE_UUID;
            }else if(ATTACK_SPEED.equals(attribute)){
                uuid = BASE_ATTACK_SPEED_UUID;
            }
            builder.put(attribute, new AttributeModifier(uuid, "Weapon modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
        });
        this.defaultModifiers = builder.build();
    }

    public GearConfig getGearConfig() {
        return gearConfig;
    }

    @Override
    public int getComboLength(ItemStack stack, LivingEntity attacker) {
        return 0;
    }

    @Override
    public boolean isUnique() {
        return gearConfig.isUnique();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addFullDescription(list, stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker.getAttributeBaseValue(ATTACK_SPEED) >= -1.0) {
            target.invulnerableTime = 0;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

}
