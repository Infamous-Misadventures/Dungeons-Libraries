package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.items.interfaces.IComboWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IMeleeWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import com.infamous.dungeons_libraries.mixin.ItemAccessor;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import com.infamous.dungeons_libraries.utils.MojankHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class MeleeGear extends TieredItem implements IMeleeWeapon, IComboWeapon, IVanishable, IReloadableGear {

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private MeleeGearConfig meleeGearConfig;
    private float attackDamage;

    public MeleeGear(Item.Properties properties) {
        super(ItemTier.WOOD, properties);
        reload();
    }

    @Override
    public void reload(){
        meleeGearConfig = MeleeGearConfigRegistry.getConfig(this.getRegistryName());
        IItemTier material = meleeGearConfig.getWeaponMaterial();
        ((ItemAccessor)this).setMaxDamage(material.getUses());
        Set<ToolType> toolTypes = ((ItemAccessor) this).getToolClasses().keySet();
        toolTypes.forEach(toolType -> ((ItemAccessor) this).getToolClasses().put(toolType, material.getLevel()));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        meleeGearConfig.getAttributes().forEach(attributeModifier -> {
            Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
            if(attribute != null) {
                UUID uuid = randomUUID();
                if (ATTACK_DAMAGE.equals(attribute)) {
                    uuid = BASE_ATTACK_DAMAGE_UUID;
                    this.attackDamage = (float) attributeModifier.getAmount() + this.getTier().getAttackDamageBonus();
                } else if (ATTACK_SPEED.equals(attribute)) {
                    uuid = BASE_ATTACK_SPEED_UUID;
                }
                builder.put(attribute, new AttributeModifier(uuid, "Weapon modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
            }
        });
        this.defaultModifiers = builder.build();
    }

    public MeleeGearConfig getGearConfig() {
        return meleeGearConfig;
    }

    @Override
    public int getComboLength(ItemStack stack, LivingEntity attacker) {
        return this.getGearConfig().getComboLength();
    }

    @Override
    public boolean isUnique() {
        return meleeGearConfig.isUnique();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addFullDescription(list, stack);
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker)
    {
        return this.getGearConfig().isDisablesShield();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, MojankHelper::hurtEnemyBroadcastBreakEvent);
        if(attacker.getAttributeBaseValue(ATTACK_SPEED) >= -1.0) {
            target.invulnerableTime = 0;
        }
        return true;
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public boolean canAttackBlock(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer) {
        return !pPlayer.isCreative();
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, World level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (blockState.getDestroySpeed(level, blockPos) != 0.0F) {
            itemStack.hurtAndBreak(1, livingEntity, MojankHelper::hurtEnemyBroadcastBreakEvent);
        }

        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState p_150897_1_) {
        return p_150897_1_.is(Blocks.COBWEB) || p_150897_1_.is(BlockTags.LEAVES);
    }

    @Override
    public float getDestroySpeed(ItemStack p_150893_1_, BlockState p_150893_2_) {
        if (p_150893_2_.is(Blocks.COBWEB) || p_150893_2_.is(BlockTags.LEAVES)) {
            return 15.0F;
        } else {
            Material lvt_3_1_ = p_150893_2_.getMaterial();
            return lvt_3_1_ != Material.PLANT && lvt_3_1_ != Material.REPLACEABLE_PLANT && lvt_3_1_ != Material.CORAL && !p_150893_2_.is(BlockTags.LEAVES) && lvt_3_1_ != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category.canEnchant(Items.IRON_SWORD);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return getGearConfig().getRarity();
    }
}
