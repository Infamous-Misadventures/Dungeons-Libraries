package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.items.gearconfig.*;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.LIFE_STEAL;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.MAGIC_DAMAGE_MULTIPLIER;
import static com.infamous.dungeons_libraries.items.gearconfig.BowGearConfig.DEFAULT;
import static java.util.UUID.randomUUID;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class ItemEvents {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    protected static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @SubscribeEvent
    public static void onMagicDamage(LivingDamageEvent event) {
        if (event.getSource() instanceof IndirectEntityDamageSource && event.getSource().isMagic() &&
                event.getSource().getEntity() instanceof LivingEntity) {

            float originalDamage = event.getAmount();

            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            AttributeInstance magicDamageMultiplierAttribute = attacker.getAttribute(MAGIC_DAMAGE_MULTIPLIER.get());
            double attributeModifier = magicDamageMultiplierAttribute != null ? magicDamageMultiplierAttribute.getValue() : 1.0D;
            double additionalDamage = originalDamage * attributeModifier;

            if (additionalDamage > 0) event.setAmount(originalDamage + (float) additionalDamage);
        }
    }

    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            AttributeInstance attribute = attacker.getAttribute(LIFE_STEAL.get());
            if (attribute != null) {
                double lifeStealAmount = attribute.getValue() - 1.0D;
                float victimMaxHealth = event.getEntity().getMaxHealth();
                if (attacker.getHealth() < attacker.getMaxHealth()) {
                    attacker.heal(victimMaxHealth * (float) lifeStealAmount);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onItemAttributeModifierEvent(ItemAttributeModifierEvent event){
//        Item item = event.getItemStack().getItem();
//        if(item instanceof BowItem && (event.getSlotType() == EquipmentSlot.MAINHAND || event.getSlotType() == EquipmentSlot.OFFHAND)){
//            BowGearConfig config = BowGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(item));
//            if(config != BowGearConfig.DEFAULT){
//                event.clearModifiers();
//                UUID uuid = randomUUID();
//                config.getAttributes().forEach((gearAttributeModifier) -> {
//                        event.addModifier(gearAttributeModifier.getAttribute(), gearAttributeModifier.toAttributeModifier(uuid, "bow modifier"));
//                });
//            }
//        }else if(item instanceof CrossbowItem && (event.getSlotType() == EquipmentSlot.MAINHAND || event.getSlotType() == EquipmentSlot.OFFHAND)){
//            BowGearConfig config = CrossbowGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(item));
//            if(config != BowGearConfig.DEFAULT){
//                event.clearModifiers();
//                UUID uuid = randomUUID();
//                config.getAttributes().forEach((gearAttributeModifier) -> {
//                    event.addModifier(gearAttributeModifier.getAttribute(), gearAttributeModifier.toAttributeModifier(uuid, "bow modifier"));
//                });
//            }
//        }else if(item instanceof ArmorItem && event.getSlotType() == ((ArmorItem) item).getSlot()){
//            ArmorGearConfig config = ArmorGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(item));
//            if(config == ArmorGearConfig.DEFAULT) return;
//            event.clearModifiers();
//            ArmorMaterial material = config.getArmorMaterial();
//            UUID primaryUuid = ARMOR_MODIFIER_UUID_PER_SLOT[((ArmorItem) item).getSlot().getIndex()];
//            event.addModifier(Attributes.ARMOR, new AttributeModifier(primaryUuid, "Armor modifier", material.getDefenseForSlot(((ArmorItem) item).getSlot()), AttributeModifier.Operation.ADDITION));
//            event.addModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(primaryUuid, "Armor toughness", material.getToughness(), AttributeModifier.Operation.ADDITION));
//            if (material.getKnockbackResistance() > 0) {
//                event.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(primaryUuid, "Armor knockback resistance", material.getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
//            }
//            config.getAttributes().forEach(gearAttributeModifier -> {
//                UUID uuid = randomUUID();
//                Attribute attribute = gearAttributeModifier.getAttribute();
//                if(attribute != null){
//                    event.addModifier(attribute, gearAttributeModifier.toAttributeModifier(uuid, "Armor modifier"));
//                }
//            });
//        }else if(event.getSlotType() == EquipmentSlot.MAINHAND || event.getSlotType() == EquipmentSlot.OFFHAND) {
//            MeleeGearConfig meleeGearConfig = MeleeGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(item));
//            if (meleeGearConfig == MeleeGearConfig.DEFAULT) return;
//            event.clearModifiers();
//            meleeGearConfig.getAttributes().forEach(attributeModifier -> {
//                Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
//                if (attribute != null) {
//                    UUID uuid = randomUUID();
//                    if (ATTACK_DAMAGE.equals(attribute)) {
//                        uuid = BASE_ATTACK_DAMAGE_UUID;
//                    } else if (ATTACK_SPEED.equals(attribute)) {
//                        uuid = BASE_ATTACK_SPEED_UUID;
//                    }
//                    event.addModifier(attribute, new AttributeModifier(uuid, "Weapon modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
//                }
//            });
//        }
    }
}
