package com.infamous.dungeons_libraries.test.mod;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.entities.EntityAttributeUtil;
import com.infamous.dungeons_libraries.items.IRayTraceModeProvider;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.math.RayTraceContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID) //forces loading of class
public final class AttackReachStuff {

	public static final DeferredRegister<Item> ITEMS = TestMod.ITEMS;

	private AttackReachStuff() {}

//	public static RegistryObject<Enchantment>  CORRUPTED_ATTACK_REACH = ENCHANTMENTS.register("corrupted_attack_reach", () -> new Enchantment());

	private static Item.Properties getDefaultProperties() {
		return new Item.Properties().tab(TestMod.ITEM_GROUP);
	}

	public static RegistryObject<Item> VERY_SHORT_SWORD = ITEMS.register("very_short_sword", () -> new TestSwordItem(ItemTier.DIAMOND, -1.5f, 3, -1.5f, getDefaultProperties()));
	public static RegistryObject<Item> LONG_SWORD = ITEMS.register("long_sword", () -> new TestSwordItem(ItemTier.DIAMOND, 2f, 3, -2.8f, getDefaultProperties()));

	public static RegistryObject<Item> SHORT_SWORD = ITEMS.register("short_sword", () -> new TestSwordItem(ItemTier.DIAMOND, -1f, 3, -2f, getDefaultProperties()) {
		@Override
		protected void addAdditionalAttributeModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
			//test item with increased block reach and reduced attack reach
			UUID uuid = UUID.fromString("d77d85ea-0112-4aad-9c18-1bc2a0b70f9e");
			builder.put(EntityAttributeUtil.getBlockReach(), new AttributeModifier(uuid, "Weapon modifier", 3, AttributeModifier.Operation.ADDITION));
		}
	});

	public static RegistryObject<Item> VERY_LONG_SWORD = ITEMS.register("very_long_sword", () -> new TestSwordItem(ItemTier.DIAMOND, 4f, 3, -3f, getDefaultProperties()) {
		@Override
		public RayTraceContext.BlockMode getRayTraceBlockModeForPickBlock(ItemStack stack, PlayerEntity player) {
			return RayTraceContext.BlockMode.COLLIDER;
		}

		@Override
		public RayTraceContext.BlockMode getRayTraceBlockModeForAttack(ItemStack stack, PlayerEntity player) {
			return RayTraceContext.BlockMode.COLLIDER;
		}
	});

	//sword that sets block reach to zero (vanilla attack reach)
	public static RegistryObject<Item> ETHEREAL_SWORD = ITEMS.register("ethereal_sword", () -> new TestSwordItem(ItemTier.DIAMOND, 0f, 3, -2.4f, getDefaultProperties()) {
		@Override
		protected void addAdditionalAttributeModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
			UUID uuid = UUID.fromString("43483e9b-a2f2-4673-bb6b-1157653584f9");
			builder.put(EntityAttributeUtil.getBlockReach(), new AttributeModifier(uuid, "Weapon modifier", -1f, AttributeModifier.Operation.MULTIPLY_TOTAL));
		}

		@Override
		public RayTraceContext.BlockMode getRayTraceBlockModeForAttack(ItemStack stack, PlayerEntity player) {
			return RayTraceContext.BlockMode.VISUAL;
		}
	});

	//pickaxe that sets attack reach to zero
	public static RegistryObject<Item> ETHEREAL_PICKAXE = ITEMS.register("ethereal_pickaxe", () -> new EtherealPickaxeItem(ItemTier.DIAMOND, 1, -2.8f, getDefaultProperties()));


	public static final UUID ATTACK_REACH_UUID = UUID.fromString("9bc1ee60-2d06-40d2-aeb5-1292cc416f72");

	static class TestSwordItem extends SwordItem implements IRayTraceModeProvider {

		final Lazy<Multimap<Attribute, AttributeModifier>> lazyAttributeModifiers;

		public TestSwordItem(IItemTier tier, float attackReachModifier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
			super(tier, attackDamageModifier, attackSpeedModifier, properties);
			lazyAttributeModifiers = Lazy.of(() -> {
				ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
				builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlotType.MAINHAND));
				builder.put(EntityAttributeUtil.getAttackReach(), new AttributeModifier(ATTACK_REACH_UUID, "Weapon modifier", attackReachModifier, AttributeModifier.Operation.ADDITION));
				addAdditionalAttributeModifiers(builder);
				return builder.build();
			});
		}

		protected void addAdditionalAttributeModifiers(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {}

		@Override
		public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlotType) {
			return equipmentSlotType == EquipmentSlotType.MAINHAND ? lazyAttributeModifiers.get() : super.getDefaultAttributeModifiers(equipmentSlotType);
		}

	}

	//pickaxe that can't attack entities
	static class EtherealPickaxeItem extends PickaxeItem {

		final Lazy<Multimap<Attribute, AttributeModifier>> lazyAttributeModifiers;

		public EtherealPickaxeItem(IItemTier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
			super(tier, attackDamageModifier, attackSpeedModifier, properties);
			lazyAttributeModifiers = Lazy.of(() -> {
				ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
				builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlotType.MAINHAND));
				builder.put(EntityAttributeUtil.getAttackReach(), new AttributeModifier(ATTACK_REACH_UUID, "Tool modifier", -1f, AttributeModifier.Operation.MULTIPLY_TOTAL)); // sets attack reach to zero
				builder.put(EntityAttributeUtil.getBlockReach(), new AttributeModifier(UUID.fromString("7d67c367-3a17-461a-b362-e7b75b2709fb"), "Tool modifier", 2, AttributeModifier.Operation.ADDITION));
				return builder.build();
			});
		}

		@Override
		public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlotType) {
			return equipmentSlotType == EquipmentSlotType.MAINHAND ? lazyAttributeModifiers.get() : super.getDefaultAttributeModifiers(equipmentSlotType);
		}

	}

}
