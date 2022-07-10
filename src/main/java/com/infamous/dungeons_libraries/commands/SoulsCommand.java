package com.infamous.dungeons_libraries.commands;

import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class SoulsCommand {
    private static final SimpleCommandExceptionType ERROR_ADD_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.souls.add.failed"));
    private static final SimpleCommandExceptionType ERROR_SET_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.souls.set.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> mobEnchantCommand
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

    private static int addSouls(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) throws CommandSyntaxException {
        targets.forEach(serverPlayer -> {
            SoulCasterHelper.addSouls(serverPlayer, amount-1);
        });
        if(targets.isEmpty()){
            throw ERROR_ADD_FAILED.create();
        }else{
            source.sendSuccess(new TranslatableComponent("commands.souls.add.success"), true);
        }
        return targets.size();
    }

    private static int setSouls(CommandSourceStack source, Collection<ServerPlayer> targets, int amount) throws CommandSyntaxException {
        targets.forEach(serverPlayer -> {
            SoulCasterHelper.setSouls(serverPlayer, amount);
        });
        if(targets.isEmpty()){
            throw ERROR_SET_FAILED.create();
        }else{
            source.sendSuccess(new TranslatableComponent("commands.souls.set.success"), true);
        }
        return targets.size();
    }
}

