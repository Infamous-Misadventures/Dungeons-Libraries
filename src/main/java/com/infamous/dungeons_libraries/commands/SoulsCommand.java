package com.infamous.dungeons_libraries.commands;

import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class SoulsCommand {
    private static final SimpleCommandExceptionType ERROR_ADD_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.souls.add.failed"));
    private static final SimpleCommandExceptionType ERROR_SET_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.souls.set.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> mobEnchantCommand
                = Commands.literal("souls")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes((p_198445_0_) ->
                            addSouls(p_198445_0_.getSource(), EntityArgument.getPlayers(p_198445_0_, "targets"), IntegerArgumentType.getInteger(p_198445_0_, "amount"))))))
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes((p_198445_0_) ->
                                setSouls(p_198445_0_.getSource(), EntityArgument.getPlayers(p_198445_0_, "targets"), IntegerArgumentType.getInteger(p_198445_0_, "amount"))))));

        dispatcher.register(mobEnchantCommand);
    }

    private static int addSouls(CommandSource source, Collection<ServerPlayerEntity> targets, int amount) throws CommandSyntaxException {
        targets.forEach(serverPlayerEntity -> {
            SoulCasterHelper.addSouls(serverPlayerEntity, amount-1);
        });
        if(targets.isEmpty()){
            throw ERROR_ADD_FAILED.create();
        }else{
            source.sendSuccess(new TranslationTextComponent("commands.souls.add.success"), true);
        }
        return targets.size();
    }

    private static int setSouls(CommandSource source, Collection<ServerPlayerEntity> targets, int amount) throws CommandSyntaxException {
        targets.forEach(serverPlayerEntity -> {
            SoulCasterHelper.setSouls(serverPlayerEntity, amount);
        });
        if(targets.isEmpty()){
            throw ERROR_SET_FAILED.create();
        }else{
            source.sendSuccess(new TranslationTextComponent("commands.souls.set.success"), true);
        }
        return targets.size();
    }
}

