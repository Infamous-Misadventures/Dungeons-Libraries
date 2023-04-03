package com.infamous.dungeons_libraries.capabilities.builtinenchants;


import com.google.common.collect.Lists;
import com.infamous.dungeons_libraries.items.gearconfig.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.BUILT_IN_ENCHANTMENTS_CAPABILITY;

public class BuiltInEnchantments implements INBTSerializable<CompoundTag> {
    private final Map<ResourceLocation, List<EnchantmentInstance>> enchantments = new HashMap<>();

    public BuiltInEnchantments() {
    }

    public BuiltInEnchantments(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MeleeGear item) {
            enchantments.put(MeleeGearConfigRegistry.GEAR_CONFIG_BUILTIN_RESOURCELOCATION, new ArrayList<>(item.getGearConfig().getBuiltInEnchantments()));
        }
        if (itemStack.getItem() instanceof BowGear item) {
            enchantments.put(MeleeGearConfigRegistry.GEAR_CONFIG_BUILTIN_RESOURCELOCATION, new ArrayList<>(item.getGearConfig().getBuiltInEnchantments()));
        }
        if (itemStack.getItem() instanceof CrossbowGear item) {
            enchantments.put(MeleeGearConfigRegistry.GEAR_CONFIG_BUILTIN_RESOURCELOCATION, new ArrayList<>(item.getGearConfig().getBuiltInEnchantments()));
        }
        if (itemStack.getItem() instanceof ArmorGear item) {
            List<EnchantmentInstance> builtInEnchantments = item.getGearConfig().getBuiltInEnchantments().stream()
                    .filter(enchantmentInstance -> enchantmentInstance.enchantment.canEnchant(itemStack))
                    .toList();
            enchantments.put(MeleeGearConfigRegistry.GEAR_CONFIG_BUILTIN_RESOURCELOCATION, builtInEnchantments);
        }
    }

    public boolean addBuiltInEnchantment(ResourceLocation source, EnchantmentInstance enchantmentInstance) {
        enchantments.computeIfAbsent(source, resourceLocation -> enchantments.put(resourceLocation, new ArrayList<>()));
        enchantments.get(source).add(enchantmentInstance);
        return true;
    }

    public boolean removeBuiltInEnchantment(ResourceLocation source, Enchantment enchantment) {
        if (!enchantments.containsKey(source)) {
            return false;
        }
        enchantments.put(source, enchantments.get(source).stream().filter(enchantmentInstance -> enchantmentInstance.enchantment != enchantment).collect(Collectors.toList()));
        return true;
    }

    public boolean setBuiltInEnchantments(ResourceLocation source, List<EnchantmentInstance> enchantmentInstance) {
        enchantments.put(source, new ArrayList<>(enchantmentInstance));
        return true;
    }

    public boolean clearAllBuiltInEnchantments(ResourceLocation source) {
        enchantments.remove(source);
        return true;
    }

    public List<EnchantmentInstance> getBuiltInEnchantments(ResourceLocation source) {
        List<EnchantmentInstance> enchantmentInstance = enchantments.get(source);
        if (enchantmentInstance == null) {
            return Lists.newArrayList();
        }
        return enchantmentInstance;
    }

    public List<EnchantmentInstance> getAllBuiltInEnchantmentInstances() {
        return enchantments.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Map<ResourceLocation, List<EnchantmentInstance>> getAllBuiltInEnchantmentInstancesPerSource() {
        return enchantments;
    }

    public boolean hasBuiltInEnchantment(ResourceLocation source) {
        return !getBuiltInEnchantments(source).isEmpty();
    }

    public boolean hasBuiltInEnchantment() {
        return !this.enchantments.isEmpty();
    }

    public boolean hasBuiltInEnchantment(Enchantment enchantment) {
        return getAllBuiltInEnchantmentInstances().stream().anyMatch(enchantmentInstance -> enchantmentInstance.enchantment.equals(enchantment));
    }

    public int getBuiltInItemEnchantmentLevel(Enchantment enchantment) {
        return getAllBuiltInEnchantmentInstances().stream().filter(enchantmentInstance -> enchantmentInstance.enchantment.equals(enchantment)).map(enchantmentInstance -> enchantmentInstance.level).max(Comparator.naturalOrder()).orElse(0);
    }

    public static final String ENCHANTS_KEY = "BuiltInEnchantments";
    public static final String SOURCE_KEY = "source";
    public static final String ENCHANTMENT_DATA_KEY = "data";

    @Override
    public CompoundTag serializeNBT() {
        if (BUILT_IN_ENCHANTMENTS_CAPABILITY == null) {
            return new CompoundTag();
        }
        CompoundTag tag = new CompoundTag();
        ListTag listnbt = new ListTag();
        this.getAllBuiltInEnchantmentInstancesPerSource().forEach((resourceLocation, enchantmentInstances) -> {
            CompoundTag compoundnbt = new CompoundTag();
            compoundnbt.putString(SOURCE_KEY, String.valueOf(resourceLocation));
            ListTag enchantmentListnbt = new ListTag();
            enchantmentInstances.forEach(enchantmentInstance -> {
                CompoundTag enchantmentInstanceNBT = new CompoundTag();
                enchantmentInstanceNBT.putString("id", String.valueOf(ForgeRegistries.ENCHANTMENTS.getKey(enchantmentInstance.enchantment)));
                enchantmentInstanceNBT.putShort("lvl", (short) enchantmentInstance.level);
                enchantmentListnbt.add(enchantmentInstanceNBT);
            });
            compoundnbt.put(ENCHANTMENT_DATA_KEY, enchantmentListnbt);
            listnbt.add(compoundnbt);
        });
        if(!this.enchantments.isEmpty()) {
            tag.put(ENCHANTS_KEY, listnbt);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if(tag.contains(ENCHANTS_KEY, 10)) {
            ListTag listNBT = tag.getList(ENCHANTS_KEY, 10);
            for (int i = 0; i < listNBT.size(); ++i) {
                CompoundTag compoundnbt = listNBT.getCompound(i);
                ResourceLocation resourcelocation = ResourceLocation.tryParse(compoundnbt.getString(SOURCE_KEY));
                ListTag enchantmentListnbt = new ListTag();
                Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.deserializeEnchantments(compoundnbt.getList(ENCHANTMENT_DATA_KEY, 10));
                List<EnchantmentInstance> enchantmentInstanceList = enchantmentIntegerMap.entrySet().stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getValue())).collect(Collectors.toList());
                this.setBuiltInEnchantments(resourcelocation, enchantmentInstanceList);
            }
        }
    }
}
