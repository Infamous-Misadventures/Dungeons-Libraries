package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.SwitchHandMessage;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    private static boolean SHOULD_SWITCH_HAND = false;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerController;attack(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"),
            method = "Lnet/minecraft/client/Minecraft;startAttack()V")
    private void dungeons_libraries_startAttack_onAttackEntity(CallbackInfo ci) {
        SHOULD_SWITCH_HAND = true;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onEmptyLeftClick(Lnet/minecraft/entity/player/PlayerEntity;)V"),
            method = "Lnet/minecraft/client/Minecraft;startAttack()V")
    private void dungeons_libraries_startAttack_onMiss(CallbackInfo ci) {
        SHOULD_SWITCH_HAND = true;
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/event/InputEvent$ClickInputEvent;shouldSwingHand()Z"),
            method = "Lnet/minecraft/client/Minecraft;startAttack()V")
    private void dungeons_libraries_startAttack_onSwing(CallbackInfo ci) {
        if(SHOULD_SWITCH_HAND){
            NetworkHandler.INSTANCE.sendToServer(new SwitchHandMessage());
            SHOULD_SWITCH_HAND = false;
        }
    }



}
