package com.infamous.dungeons_libraries.client.artifactBar;

import com.infamous.dungeons_libraries.client.gui.elementconfig.GuiElementConfig;
import com.infamous.dungeons_libraries.client.gui.elementconfig.GuiElementConfigRegistry;
import com.infamous.dungeons_libraries.integration.curios.client.CuriosKeyBindings;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class ArtifactsBarRender {
    private static final ResourceLocation ARTIFACT_BAR_RESOURCE = new ResourceLocation(MODID, "textures/gui/artifact_bar.png");

    @SubscribeEvent
    public static void displayArtifactBar(RenderGuiOverlayEvent.Post event) {
        final Minecraft mc = Minecraft.getInstance();
//        if(mc != null && ForgeRegistries.ITEMS.tags().getTag(CURIOS_ARTIFACTS).isEmpty()) return;

        if (event.getOverlay().equals(VanillaGuiOverlay.HOTBAR.type()) && mc.getCameraEntity() instanceof Player renderPlayer) {
            if (renderPlayer == null) return;
            GuiElementConfig guiElementConfig = GuiElementConfigRegistry.getConfig(new ResourceLocation(MODID, "artifact_bar"));
            if (guiElementConfig.isHidden()) return;


            Window sr = event.getWindow();
            int scaledWidth = sr.getGuiScaledWidth();
            int scaledHeight = sr.getGuiScaledHeight();

            int x = guiElementConfig.getXPosition(scaledWidth);
            int y = guiElementConfig.getYPosition(scaledHeight);

            CuriosApi.getCuriosHelper().getCuriosHandler(renderPlayer).ifPresent(iCuriosItemHandler -> {
                renderBar(event.getPoseStack(), mc, renderPlayer, x, y, iCuriosItemHandler);
            });

            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
        }


    }

    private static void renderBar(PoseStack poseStack, Minecraft mc, Player renderPlayer, int x, int y, ICuriosItemHandler iCuriosItemHandler) {
        Optional<ICurioStacksHandler> artifactStackHandler = iCuriosItemHandler.getStacksHandler("artifact");
        if (artifactStackHandler.isPresent()) {
            IDynamicStackHandler stacks = artifactStackHandler.get().getStacks();
            if(noArtifactEquipped(stacks)) return;
            int slots = stacks.getSlots();
            renderSlotBg(poseStack, mc, x, y, slots);
            for (int slot = 0; slot < slots; slot++) {
                ItemStack artifact = stacks.getStackInSlot(slot);
                if (!artifact.isEmpty() && artifact.getItem() instanceof ArtifactItem) {
                    int xPos = x + slot * 20 + 3;
                    int yPos = y + 3;
                    renderSlot(poseStack, mc, xPos, yPos, renderPlayer, artifact);
                }
                renderSlotKeybind(poseStack, mc, x, y, slot);
            }
        }
    }

    private static boolean noArtifactEquipped(IDynamicStackHandler stacks) {
        int slots = stacks.getSlots();
        for (int slot = 0; slot < slots; slot++) {
            ItemStack artifact = stacks.getStackInSlot(slot);
            if (!artifact.isEmpty() && artifact.getItem() instanceof ArtifactItem) {
                return false;
            }
        }
        return true;
    }

    private static void renderSlot(PoseStack posestack, Minecraft mc, int xPos, int yPos, Player renderPlayer, ItemStack artifactStack) {
        if (!artifactStack.isEmpty()) {
            float f = (float) artifactStack.getPopTime() - 0;
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                posestack.pushPose();
                posestack.translate((float) (xPos + 8), (float) (yPos + 12), 0.0F);
                posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                posestack.translate((float) (-(xPos + 8)), (float) (-(yPos + 12)), 0.0F);
                RenderSystem.applyModelViewMatrix();
            }

            Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(renderPlayer, artifactStack, xPos, yPos, 1);
            if (f > 0.0F) {
                posestack.popPose();
            }

            Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, artifactStack, xPos, yPos);
        }
    }

    private static void renderSlotBg(PoseStack poseStack, Minecraft mc, int xPos, int yPos, int slots) {
        RenderSystem.setShaderTexture(0, ARTIFACT_BAR_RESOURCE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiComponent.blit(poseStack, xPos, yPos, 0, 0, 62, 22, 62, 22);
        RenderSystem.disableBlend();
    }

    private static void renderSlotKeybind(PoseStack poseStack, Minecraft mc, int x, int y, int slot) {
        String keybind = "";
        if (slot == 0) {
            KeyMapping keyMapping = CuriosKeyBindings.activateArtifact1;
            keybind = getString(keyMapping);
        } else if (slot == 1) {
            KeyMapping keyMapping = CuriosKeyBindings.activateArtifact2;
            keybind = getString(keyMapping);
        } else if (slot == 2) {
            KeyMapping keyMapping = CuriosKeyBindings.activateArtifact3;
            keybind = getString(keyMapping);
        }
        int keybindWidth = mc.font.width(keybind);
        int xPosition = x + 1 + slot * 20 + 18 - keybindWidth;
        int yPosition = y + 3;
        GuiComponent.drawString(poseStack, mc.font, keybind, xPosition, yPosition, 0xFFFFFF);
    }

    private static String getString(KeyMapping keyMapping) {
//        return keyMapping.getKeyModifier().getCombinedName(keyMapping.getKey(), () -> keyMapping.getKey().getDisplayName()).getString();
        return keyMapping.getKey().getDisplayName().getString();
    }

}
