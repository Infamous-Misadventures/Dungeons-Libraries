 package com.infamous.dungeons_libraries.integration.curios.client;

 import com.infamous.dungeons_libraries.integration.curios.client.message.CuriosArtifactStartMessage;
 import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
 import com.infamous.dungeons_libraries.items.artifacts.ArtifactUseContext;
 import com.infamous.dungeons_libraries.network.NetworkHandler;
 import com.mojang.math.Vector3d;
 import net.minecraft.client.KeyMapping;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.player.AbstractClientPlayer;
 import net.minecraft.client.player.LocalPlayer;
 import net.minecraft.core.Direction;
 import net.minecraft.world.entity.LivingEntity;
 import net.minecraft.world.entity.player.Player;
 import net.minecraft.world.entity.projectile.ProjectileUtil;
 import net.minecraft.world.item.ItemStack;
 import net.minecraft.world.phys.*;
 import net.minecraftforge.api.distmarker.Dist;
 import net.minecraftforge.client.ClientRegistry;
 import net.minecraftforge.client.event.InputEvent;
 import net.minecraftforge.client.settings.KeyConflictContext;
 import net.minecraftforge.event.entity.player.PlayerEvent;
 import net.minecraftforge.eventbus.api.SubscribeEvent;
 import net.minecraftforge.fml.common.Mod;
 import org.lwjgl.glfw.GLFW;
 import top.theillusivec4.curios.api.CuriosApi;
 import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

 import java.util.Optional;

 import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

 @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class CuriosKeyBindings {

    private static final double RAYTRACE_DISTANCE = 30;

    public static final KeyMapping activateArtifact1 = new KeyMapping("key.dungeons_libraries.curiosintegration.description_slot1", GLFW.GLFW_KEY_V, "key.dungeons_libraries.curiosintegration.category");
    public static final KeyMapping activateArtifact2 = new KeyMapping("key.dungeons_libraries.curiosintegration.description_slot2", GLFW.GLFW_KEY_B, "key.dungeons_libraries.curiosintegration.category");
    public static final KeyMapping activateArtifact3 = new KeyMapping("key.dungeons_libraries.curiosintegration.description_slot3", GLFW.GLFW_KEY_N, "key.dungeons_libraries.curiosintegration.category");

    public static void setupCuriosKeybindings() {
        activateArtifact1.setKeyConflictContext(KeyConflictContext.IN_GAME);
        ClientRegistry.registerKeyBinding(activateArtifact1);
        activateArtifact2.setKeyConflictContext(KeyConflictContext.IN_GAME);
        ClientRegistry.registerKeyBinding(activateArtifact2);
        activateArtifact3.setKeyConflictContext(KeyConflictContext.IN_GAME);
        ClientRegistry.registerKeyBinding(activateArtifact3);
    }

    @SubscribeEvent
    public static void onClientTick(InputEvent.KeyInputEvent event) {
        if (activateArtifact1.consumeClick()) {
            sendCuriosStartMessageToServer(0);
        }
        if (activateArtifact2.consumeClick()) {
            sendCuriosStartMessageToServer(1);
        }
        if (activateArtifact3.consumeClick()) {
            sendCuriosStartMessageToServer(2);
        }
    }

    private static void sendCuriosStartMessageToServer(int slot) {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null) {
            if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
                BlockHitResult blockHitResult = new BlockHitResult(player.position(), Direction.UP, player.blockPosition(), false);
                curiosStartMessage(slot, blockHitResult, player);
            } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                curiosStartMessage(slot, (BlockHitResult) hitResult, player);
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                BlockHitResult blockHitResult = new BlockHitResult(entityHitResult.getEntity().position(), Direction.UP, entityHitResult.getEntity().blockPosition(), false);
                curiosStartMessage(slot, blockHitResult, player);
            }
        }
    }

    private static void curiosStartMessage(int slot, BlockHitResult blockHitResult, LocalPlayer player) {
        NetworkHandler.INSTANCE.sendToServer(new CuriosArtifactStartMessage(slot, blockHitResult));
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(iCuriosItemHandler -> {
            Optional<ICurioStacksHandler> artifactStackHandler = iCuriosItemHandler.getStacksHandler("artifact");
            if (artifactStackHandler.isPresent()) {
                ItemStack artifact = artifactStackHandler.get().getStacks().getStackInSlot(slot);
                if (!artifact.isEmpty() && artifact.getItem() instanceof ArtifactItem) {
                    ArtifactUseContext iuc = new ArtifactUseContext(player.level, player, artifact, blockHitResult);
                    ((ArtifactItem) artifact.getItem()).activateArtifact(iuc);
                }
            }
        });
    }

     private static BlockHitResult getBlockHitResult(AbstractClientPlayer player) {
         Vec3 eyeVector = player.getEyePosition(1.0F);
         Vec3 lookVector = player.getViewVector(1.0F);
         Vec3 rayTraceVector = eyeVector.add(lookVector.x * RAYTRACE_DISTANCE, lookVector.y * RAYTRACE_DISTANCE, lookVector.z * RAYTRACE_DISTANCE);
         AABB rayTraceBoundingBox = player.getBoundingBox().expandTowards(lookVector.scale(RAYTRACE_DISTANCE)).inflate(1.0D, 1.0D, 1.0D);
         EntityHitResult entityRTR = ProjectileUtil.getEntityHitResult(player.level, player, eyeVector, rayTraceVector, rayTraceBoundingBox, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
         if(entityRTR != null) {
             return new BlockHitResult(entityRTR.getEntity().position(), Direction.UP, entityRTR.getEntity().blockPosition(), false);
         }else{
             BlockHitResult blockRTR = (BlockHitResult) player.pick(RAYTRACE_DISTANCE, 1.0f, false);
             return blockRTR;
         }
     }

 }