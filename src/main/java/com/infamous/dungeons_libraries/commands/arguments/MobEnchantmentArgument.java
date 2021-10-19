package com.infamous.dungeons_libraries.commands.arguments;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentsRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MobEnchantmentArgument implements ArgumentType<MobEnchantment> {
    private static final Collection<String> EXAMPLES = Arrays.asList("spooky", "effect");
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_EFFECT = new DynamicCommandExceptionType((p_208663_0_) -> {
        return new TranslationTextComponent("mobEnchantment.mobEnchantmentNotFound", p_208663_0_);
    });

    public static MobEnchantmentArgument mobEnchantment() {
        return new MobEnchantmentArgument();
    }

    public static MobEnchantment getMobEnchantment(CommandContext<CommandSource> source, String name) throws CommandSyntaxException {
        return source.getArgument(name, MobEnchantment.class);
    }

    public MobEnchantment parse(StringReader p_parse_1_) throws CommandSyntaxException {
        ResourceLocation resourcelocation = ResourceLocation.read(p_parse_1_);
        if (MobEnchantmentsRegistry.MOB_ENCHANTMENTS.containsKey(resourcelocation)) {
            return MobEnchantmentsRegistry.MOB_ENCHANTMENTS.getValue(resourcelocation);
        } else {
            throw ERROR_UNKNOWN_EFFECT.create(resourcelocation);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> p_listSuggestions_1_, SuggestionsBuilder p_listSuggestions_2_) {
        return ISuggestionProvider.suggestResource(MobEnchantmentsRegistry.MOB_ENCHANTMENTS.getKeys(), p_listSuggestions_2_);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
