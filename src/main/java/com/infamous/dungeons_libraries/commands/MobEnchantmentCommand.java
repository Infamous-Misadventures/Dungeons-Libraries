package com.infamous.dungeons_libraries.commands;

import com.google.common.collect.ImmutableList;
import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper;
import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.commands.arguments.MobEnchantmentArgument;
import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import com.infamous.dungeons_libraries.network.MobEnchantmentMessage;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Collection;

public class MobEnchantmentCommand {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.mobEnchantment.give.failed"));
    private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.mobEnchantment.clear.everything.failed"));
    private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.mobEnchantment.clear.specific.failed"));

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> mobEnchantCommand
                = Commands.literal("mobenchantment")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.literal("clear").executes((p_198352_0_) -> clearMobEnchantments(p_198352_0_.getSource(), ImmutableList.of(p_198352_0_.getSource().getEntityOrException())))
                        .then(Commands.argument("targets", EntityArgument.entities()).executes((p_198356_0_) -> clearMobEnchantments(p_198356_0_.getSource(), EntityArgument.getEntities(p_198356_0_, "targets")))
                                .then(Commands.argument("MobEnchantment", MobEnchantmentArgument.mobEnchantment()).executes((p_198351_0_) -> removeEnchantment(p_198351_0_.getSource(), EntityArgument.getEntities(p_198351_0_, "targets"), MobEnchantmentArgument.getMobEnchantment(p_198351_0_, "MobEnchantment"))))))
                .then(Commands.literal("give").then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("MobEnchantment", MobEnchantmentArgument.mobEnchantment()).executes((p_198357_0_) -> addMobEnchantment(p_198357_0_.getSource(), EntityArgument.getEntities(p_198357_0_, "targets"), MobEnchantmentArgument.getMobEnchantment(p_198357_0_, "MobEnchantment"))))));

        dispatcher.register(mobEnchantCommand);
    }

    private static int addMobEnchantment(CommandSource source, Collection<? extends Entity> targets, MobEnchantment mobEnchantment) throws CommandSyntaxException {

        int i = 0;
        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                IEnchantable cap = EnchantableHelper.getEnchantableCapability(entity);
                if (cap != null) {
                    cap.addEnchantment(mobEnchantment);
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
                    ++i;
                }
            }
        }
        if (i == 0) {
            throw ERROR_GIVE_FAILED.create();
        } else {
            if (targets.size() == 1) {
                source.sendSuccess(new TranslationTextComponent("commands.MobEnchantment.give.success.single", mobEnchantment.getDisplayName(), targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslationTextComponent("commands.MobEnchantment.give.success.multiple", mobEnchantment.getDisplayName(), targets.size()), true);
            }

            return i;
        }
    }

    private static int clearMobEnchantments(CommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                IEnchantable cap = EnchantableHelper.getEnchantableCapability(entity);
                if (cap != null) {
                    cap.clearAllEnchantments();
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
                    ++i;
                }
            }
        }

        if (i == 0) {
            throw ERROR_CLEAR_EVERYTHING_FAILED.create();
        } else {
            if (targets.size() == 1) {
                source.sendSuccess(new TranslationTextComponent("commands.MobEnchantment.clear.everything.success.single", targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslationTextComponent("commands.MobEnchantment.clear.everything.success.multiple", targets.size()), true);
            }

            return i;
        }
    }

    private static int removeEnchantment(CommandSource source, Collection<? extends Entity> targets, MobEnchantment mobEnchantment) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity) {
                IEnchantable cap = EnchantableHelper.getEnchantableCapability(entity);
                if (cap != null) {
                    cap.removeEnchantment(mobEnchantment);
                    NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MobEnchantmentMessage(entity.getId(), cap.getEnchantments()));
                    ++i;
                }
            }
        }

        if (i == 0) {
            throw ERROR_CLEAR_SPECIFIC_FAILED.create();
        } else {
            if (targets.size() == 1) {
                source.sendSuccess(new TranslationTextComponent("commands.MobEnchantment.clear.specific.success.single", mobEnchantment.getDisplayName(), targets.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslationTextComponent("commands.MobEnchantment.clear.specific.success.multiple", mobEnchantment.getDisplayName(), targets.size()), true);
            }
            return i;
        }
    }
}

