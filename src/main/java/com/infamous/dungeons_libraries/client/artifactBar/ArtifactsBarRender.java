package com.infamous.dungeons_libraries.client.artifactBar;

import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
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

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class ArtifactsBarRender {
    private static final ResourceLocation ARTIFACT_BAR_RESOURCE = new ResourceLocation(MODID, "textures/misc/soul_bar.png");
    public static final int SOUL_LEVEL_COLOR = 0x10B0E4;

    @SubscribeEvent
    public static void displaySoulBar(RenderGameOverlayEvent.Pre event) {
        MatrixStack matrixStack = event.getMatrixStack();
        MainWindow sr = event.getWindow();
        int scaledWidth = sr.getGuiScaledWidth();
        int scaledHeight = sr.getGuiScaledHeight();
        final Minecraft mc = Minecraft.getInstance();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR) && mc.getCameraEntity() instanceof PlayerEntity) {
            //draw souls
//            mc.getTextureManager().bind(ARTIFACT_BAR_RESOURCE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            PlayerEntity renderPlayer = (PlayerEntity) mc.getCameraEntity();
            if(renderPlayer == null) return;

            GlStateManager._enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderHelper.turnBackOn();

            mc.getProfiler().push("artifactBarBorder");
            mc.getProfiler().pop();

            CuriosApi.getCuriosHelper().getCuriosHandler(renderPlayer).ifPresent(iCuriosItemHandler -> {
                Optional<ICurioStacksHandler> artifactStackHandler = iCuriosItemHandler.getStacksHandler("artifact");
                if (artifactStackHandler.isPresent()) {
                    int slots = artifactStackHandler.get().getStacks().getSlots();
                    for(int slot = 0; slot < slots; slot++) {
                        mc.getProfiler().push("artifact Slot " + slot);
                        ItemStack artifact = artifactStackHandler.get().getStacks().getStackInSlot(slot);
                        if (!artifact.isEmpty() && artifact.getItem() instanceof ArtifactItem) {
                            int xPos = scaledWidth - 123 + slot * 16 + 2;
                            int yPos = scaledHeight - 16;
                            renderSlot(xPos, yPos, renderPlayer, artifact);
                        }
                        mc.getProfiler().pop();
                    }
                }
            });

            mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            RenderHelper.turnOff();
            RenderSystem.disableBlend();
        }


    }

    private static void renderSlot(int xPos, int yPos, PlayerEntity renderPlayer, ItemStack artifactStack) {
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

}
