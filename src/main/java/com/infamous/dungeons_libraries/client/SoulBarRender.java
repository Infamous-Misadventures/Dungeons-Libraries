package com.infamous.dungeons_libraries.client;

import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SOUL_CAP;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = MODID)
public class SoulBarRender {
    private static final ResourceLocation SOUL_BAR_RESOURCE = new ResourceLocation(MODID, "textures/misc/soul_bar.png");
    public static final int SOUL_LEVEL_COLOR = 0x10B0E4;

    @SubscribeEvent
    public static void displaySoulBar(RenderGameOverlayEvent.Post event) {
        PoseStack matrixStack = event.getMatrixStack();
        Window sr = event.getWindow();
        int scaledWidth = sr.getGuiScaledWidth();
        int scaledHeight = sr.getGuiScaledHeight();
        final Minecraft mc = Minecraft.getInstance();

        if (event.getType().equals(RenderGameOverlayEvent.ElementType.LAYER) && mc.getCameraEntity() instanceof Player) {
            //draw souls
            RenderSystem.setShaderTexture(0, SOUL_BAR_RESOURCE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            Player renderPlayer = (Player) mc.getCameraEntity();
            if(renderPlayer == null) return;
            SoulCaster soulCasterCapability = SoulCasterHelper.getSoulCasterCapability(renderPlayer);
            if(soulCasterCapability == null) return;

            float souls = soulCasterCapability.getSouls();
            double maxSouls = renderPlayer.getAttributeValue(SOUL_CAP.get());

//            GlStateManager._enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
//            RenderHelper.turnBackOn();

            mc.getProfiler().push("soulBar");

            if (souls > 0) {
                int backgroundBarWidth = 122;
                int foregroundBarWidth = (int) (souls / maxSouls * 122.0F);
                int xPos = scaledWidth - 123;
                int yPos = scaledHeight - 7;
                GuiComponent.blit(matrixStack, xPos, yPos, 0, 0, backgroundBarWidth, 5, 121, 10);
                GuiComponent.blit(matrixStack, xPos, yPos, 0, 5, foregroundBarWidth, 5, 121, 10);
            }
            mc.getProfiler().pop();

            if (souls > 0) {
                mc.getProfiler().push("soulLevel");
                String soulLevel = "" + souls;
                int baseXPos = scaledWidth - mc.font.width(soulLevel) - (123 / 2) + 11;
                int baseYPos = scaledHeight - 7 - 6;
                mc.font.draw(matrixStack, soulLevel, (float)(baseXPos + 1), (float)baseYPos, 0);
                mc.font.draw(matrixStack, soulLevel, (float)(baseXPos - 1), (float)baseYPos, 0);
                mc.font.draw(matrixStack, soulLevel, (float)baseXPos, (float)(baseYPos + 1), 0);
                mc.font.draw(matrixStack, soulLevel, (float)baseXPos, (float)(baseYPos - 1), 0);
                mc.font.draw(matrixStack, soulLevel, (float)baseXPos, (float)baseYPos, SOUL_LEVEL_COLOR);
                mc.getProfiler().pop();
            }

            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
//            RenderHelper.turnOff();
            RenderSystem.disableBlend();
        }


    }

}
