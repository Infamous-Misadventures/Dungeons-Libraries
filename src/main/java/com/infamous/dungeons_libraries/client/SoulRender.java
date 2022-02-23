package com.infamous.dungeons_libraries.client;

import com.infamous.dungeons_libraries.capabilities.soulcaster.ISoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SOUL_CAP;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class SoulRender {
    private static final ResourceLocation SOUL_BAR_RESOURCE = new ResourceLocation(MODID, "textures/misc/soul_bar.png");
    public static final int SOUL_LEVEL_COLOR = 0x10B0E4;
    private static float RENDER_SOULS = 0;

    @SubscribeEvent
    public static void displaySoul(RenderGameOverlayEvent.Post event) {
        MatrixStack matrixStack = event.getMatrixStack();
        MainWindow sr = event.getWindow();
        int scaledWidth = sr.getGuiScaledWidth();
        int scaledHeight = sr.getGuiScaledHeight();
        final Minecraft mc = Minecraft.getInstance();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR) && mc.getCameraEntity() instanceof PlayerEntity) {
            //draw souls
            mc.getTextureManager().bind(SOUL_BAR_RESOURCE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

            PlayerEntity renderPlayer = (PlayerEntity) mc.getCameraEntity();
            if(renderPlayer == null) return;
            ISoulCaster soulCasterCapability = SoulCasterHelper.getSoulCasterCapability(renderPlayer);
            if(soulCasterCapability == null) return;

            float souls = soulCasterCapability.getSouls();
            double maxSouls = renderPlayer.getAttributeValue(SOUL_CAP.get());

            GlStateManager._enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderHelper.turnBackOn();

            mc.getProfiler().push("soulBar");

            adjustRenderSouls(souls);

            if (RENDER_SOULS > 0) {
                int backgroundBarWidth = 122;
                int foregroundBarWidth = (int) (RENDER_SOULS / maxSouls * 122.0F);
                int xPos = scaledWidth - 123;
                int yPos = scaledHeight - 7;
                AbstractGui.blit(matrixStack, xPos, yPos, 0, 0, backgroundBarWidth, 5, 121, 10);
                AbstractGui.blit(matrixStack, xPos, yPos, 0, 5, foregroundBarWidth, 5, 121, 10);
            }
            mc.getProfiler().pop();

            if (RENDER_SOULS > 0) {
                mc.getProfiler().push("soulLevel");
                String soulLevel = "" + RENDER_SOULS;
                int baseXPos = scaledWidth - mc.font.width(soulLevel) - (123 / 2) + 11;
                int baseYPos = scaledHeight - 7 - 6;
                mc.font.draw(matrixStack, soulLevel, (float)(baseXPos + 1), (float)baseYPos, 0);
                mc.font.draw(matrixStack, soulLevel, (float)(baseXPos - 1), (float)baseYPos, 0);
                mc.font.draw(matrixStack, soulLevel, (float)baseXPos, (float)(baseYPos + 1), 0);
                mc.font.draw(matrixStack, soulLevel, (float)baseXPos, (float)(baseYPos - 1), 0);
                mc.font.draw(matrixStack, soulLevel, (float)baseXPos, (float)baseYPos, SOUL_LEVEL_COLOR);
                mc.getProfiler().pop();
            }

            mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            RenderHelper.turnOff();
            RenderSystem.disableBlend();
        }


    }

    private static void adjustRenderSouls(float souls) {
        boolean close = true;
        if (RENDER_SOULS < souls) {
            RENDER_SOULS++;
            close = false;
        }
        if (RENDER_SOULS > souls) {
            RENDER_SOULS--;
            close = !close;
        }
        if (close) RENDER_SOULS = souls;
    }

}
