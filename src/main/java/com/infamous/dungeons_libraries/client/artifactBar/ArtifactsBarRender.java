package com.infamous.dungeons_libraries.client.artifactBar;

import com.infamous.dungeons_libraries.client.gui.elementconfig.GuiElementConfig;
import com.infamous.dungeons_libraries.client.gui.elementconfig.GuiElementConfigRegistry;
import com.infamous.dungeons_libraries.integration.curios.client.CuriosKeyBindings;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static net.minecraft.client.gui.widget.Widget.WIDGETS_LOCATION;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class ArtifactsBarRender {
    private static final ResourceLocation ARTIFACT_BAR_RESOURCE = new ResourceLocation(MODID, "textures/misc/soul_bar.png");
    public static final ResourceLocation CURIOS_ICON_TEXTURE = new ResourceLocation(MODID, "textures/icon/empty_artifact_slot.png");
    public static final int SOUL_LEVEL_COLOR = 0x10B0E4;

    @SubscribeEvent
    public static void displaySoulBar(RenderGameOverlayEvent.Pre event) {
        MatrixStack matrixStack = event.getMatrixStack();
        MainWindow sr = event.getWindow();
        int scaledWidth = sr.getGuiScaledWidth();
        int scaledHeight = sr.getGuiScaledHeight();
        final Minecraft mc = Minecraft.getInstance();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR) && mc.getCameraEntity() instanceof PlayerEntity) {
            PlayerEntity renderPlayer = (PlayerEntity) mc.getCameraEntity();
            if(renderPlayer == null) return;

            GlStateManager._enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderHelper.turnBackOn();

            mc.getProfiler().push("artifactBarBorder");
            mc.getProfiler().pop();

            GuiElementConfig guiElementConfig = GuiElementConfigRegistry.getConfig(new ResourceLocation(MODID, "artifact_bar"));

//            int x = scaledWidth - 123;
//            int y = scaledHeight - 16;

            int x = guiElementConfig.getXPosition(scaledWidth);
            int y = guiElementConfig.getYPosition(scaledHeight);

            CuriosApi.getCuriosHelper().getCuriosHandler(renderPlayer).ifPresent(iCuriosItemHandler -> {
                Optional<ICurioStacksHandler> artifactStackHandler = iCuriosItemHandler.getStacksHandler("artifact");
                if (artifactStackHandler.isPresent()) {
                    int slots = artifactStackHandler.get().getStacks().getSlots();
                    renderSlotBg(matrixStack, mc, x, y, slots);
                    for(int slot = 0; slot < slots; slot++) {
                        mc.getProfiler().push("artifact Slot " + slot);
                        ItemStack artifact = artifactStackHandler.get().getStacks().getStackInSlot(slot);
                        if (!artifact.isEmpty() && artifact.getItem() instanceof ArtifactItem) {
                            int xPos = x + slot * 22 +3;
                            int yPos = y +3;
                            renderSlot(matrixStack, mc, xPos, yPos, renderPlayer, artifact);
                        }else{
                            int xPos = x + slot * 22 + 3;
                            int yPos = y + 3;
                            renderEmptySlot(matrixStack, mc, xPos, yPos);
                        }
                        renderSlotKeybind(matrixStack, mc, x, y, slot);
                        mc.getProfiler().pop();
                    }
                }
            });

            mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            RenderHelper.turnOff();
            RenderSystem.disableBlend();
        }


    }

    private static void renderSlot(MatrixStack matrixStack, Minecraft mc,int xPos, int yPos, PlayerEntity renderPlayer, ItemStack artifactStack) {
        if (!artifactStack.isEmpty()) {
            float f = (float)artifactStack.getPopTime() - 0;
            if (f > 0.0F) {
                RenderSystem.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                RenderSystem.translatef((float)(xPos + 8), (float)(yPos + 12), 0.0F);
                RenderSystem.scalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                RenderSystem.translatef((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0F);
            }

            Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(renderPlayer, artifactStack, xPos, yPos);
            if (f > 0.0F) {
                RenderSystem.popMatrix();
            }

            Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, artifactStack, xPos, yPos);
        }
    }

    private static void renderEmptySlot(MatrixStack matrixStack, Minecraft mc, int xPos, int yPos) {
        mc.getTextureManager().bind(CURIOS_ICON_TEXTURE);
        AbstractGui.blit(matrixStack, xPos, yPos, 0, 0, 16, 16, 16, 16);
    }

    private static void renderSlotBg(MatrixStack matrixStack, Minecraft mc, int xPos, int yPos, int slots) {
        mc.getTextureManager().bind(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        for(int i = 0; i < slots; i++) {
            AbstractGui.blit(matrixStack, xPos + i * 22, yPos, 24, 23, 22, 22, 256, 256);
        }
    }

    private static void renderSlotKeybind(MatrixStack matrixStack, Minecraft mc, int x, int y, int slot) {
        String keybind = "";
        if(slot == 0) {
            KeyBinding keybinding = CuriosKeyBindings.activateArtifact1;
            keybind = getString(keybinding);
        }else if(slot == 1) {
            KeyBinding keybinding = CuriosKeyBindings.activateArtifact2;
            keybind = getString(keybinding);
        }else if(slot == 2) {
            KeyBinding keybinding = CuriosKeyBindings.activateArtifact3;
            keybind = getString(keybinding);
        }
        int keybindWidth = mc.font.width(keybind);
        int xPosition = x + slot * 22 + 19 - keybindWidth;
        int yPosition = y + 3;
        mc.font.draw(matrixStack, keybind, xPosition, yPosition, 0xFFFFFF);
    }

    private static String getString(KeyBinding keybinding) {
//        return keybinding.getKeyModifier().getCombinedName(keybinding.getKey(), () -> keybinding.getKey().getDisplayName()).getString();
        return keybinding.getKey().getDisplayName().getString();
    }

}
