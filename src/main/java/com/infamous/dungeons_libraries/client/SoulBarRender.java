package com.infamous.dungeons_libraries.client;

import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.infamous.dungeons_libraries.client.gui.elementconfig.GuiElementConfig;
import com.infamous.dungeons_libraries.client.gui.elementconfig.GuiElementConfigRegistry;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SOUL_CAP;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class SoulBarRender {
    private static final ResourceLocation SOUL_BAR_RESOURCE = new ResourceLocation(MODID, "textures/misc/soul_bar.png");
    public static final int SOUL_LEVEL_COLOR = 0x10B0E4;

    @SubscribeEvent
    public static void displaySoulBar(RenderGuiOverlayEvent.Post event) {
        PoseStack matrixStack = event.getPoseStack();
        Window sr = event.getWindow();
        int scaledWidth = sr.getGuiScaledWidth();
        int scaledHeight = sr.getGuiScaledHeight();
        final Minecraft mc = Minecraft.getInstance();

        if (event.getOverlay().equals(VanillaGuiOverlay.HOTBAR.type()) && mc.getCameraEntity() instanceof Player) {
            GuiElementConfig guiElementConfig = GuiElementConfigRegistry.getConfig(new ResourceLocation(MODID, "soul_bar"));
            if (guiElementConfig.isHidden()) return;
            //draw souls
            RenderSystem.setShaderTexture(0, SOUL_BAR_RESOURCE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            Player renderPlayer = (Player) mc.getCameraEntity();
            if (renderPlayer == null) return;
            SoulCaster soulCasterCapability = SoulCasterHelper.getSoulCasterCapability(renderPlayer);

            float souls = soulCasterCapability.getSouls();
            double maxSouls = renderPlayer.getAttributeValue(SOUL_CAP.get());

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            mc.getProfiler().push("soulBar");

            int xPos = guiElementConfig.getXPosition(scaledWidth);
            int yPos = guiElementConfig.getYPosition(scaledHeight);

            if (souls > 0) {
                int backgroundBarWidth = guiElementConfig.getSizeX();
                int foregroundBarWidth = (int) (souls / maxSouls * guiElementConfig.getSizeX());
                GuiComponent.blit(matrixStack, xPos, yPos, 0, 0, backgroundBarWidth, 5, 121, 10);
                GuiComponent.blit(matrixStack, xPos, yPos, 0, 5, foregroundBarWidth, 5, 121, 10);
            }
            mc.getProfiler().pop();

            if (souls > 0) {
                mc.getProfiler().push("soulLevel");
                String soulLevel = "" + souls;
                int baseXPos = xPos + (guiElementConfig.getSizeX() / 2) - (mc.font.width(soulLevel) / 2);
                int baseYPos = scaledHeight - guiElementConfig.getSizeY() - mc.font.lineHeight;
                GuiComponent.drawString(matrixStack, mc.font, soulLevel, baseXPos, baseYPos, SOUL_LEVEL_COLOR);
                mc.getProfiler().pop();
            }

            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
            RenderSystem.disableBlend();
        }


    }

}
