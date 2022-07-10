package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.LIFE_STEAL;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.MAGIC_DAMAGE_MULTIPLIER;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class ItemEvents {

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
                float victimMaxHealth = event.getEntityLiving().getMaxHealth();
                if (attacker.getHealth() < attacker.getMaxHealth()) {
                    attacker.heal(victimMaxHealth * (float) lifeStealAmount);
                }
            }
        }
    }
}
