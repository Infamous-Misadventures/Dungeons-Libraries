package com.infamous.dungeons_libraries.commands;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CommandEvents {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        MobEnchantmentCommand.register(commandDispatcher);
        SoulsCommand.register(commandDispatcher);
    }
}
