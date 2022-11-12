package com.infamous.dungeons_libraries.client.gui.elementconfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class GuiElementConfig {
    public static final GuiElementConfig DEFAULT = new GuiElementConfig(0, 0, 0, 0, Alignment.BOTTOM_RIGHT);

    public static final Codec<GuiElementConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("sizeX").forGetter(GuiElementConfig::getSizeX),
            Codec.INT.fieldOf("sizeY").forGetter(GuiElementConfig::getSizeY),
            Codec.INT.fieldOf("offsetX").forGetter(GuiElementConfig::getOffsetX),
            Codec.INT.fieldOf("offsetY").forGetter(GuiElementConfig::getOffsetY),
            Alignment.CODEC.fieldOf("alignment").forGetter(GuiElementConfig::getAlignment)
    ).apply(instance, GuiElementConfig::new));

    private final int sizeX;
    private final int sizeY;
    private final int offsetX;
    private final int offsetY;
    private final Alignment alignment;

    public GuiElementConfig(int sizeX, int sizeY, int offsetX, int offsetY, Alignment alignment) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.alignment = alignment;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Alignment getAlignment() {
        return alignment;
    }
    
    public int getXPosition(int scaledWidth){
        switch(alignment){
            case TOP_LEFT:
            case BOTTOM_LEFT:
                return offsetX;
            case TOP_RIGHT:
            case BOTTOM_RIGHT:
                return scaledWidth - sizeX + offsetX;
            case TOP_CENTER:
            case BOTTOM_CENTER:
                return (scaledWidth - sizeX) / 2 + offsetX;
        }
        return 0;
    }

    public int getYPosition(int scaledHeight){
        switch(alignment){
            case TOP_LEFT:
            case TOP_RIGHT:
            case TOP_CENTER:
                return offsetY;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
            case BOTTOM_CENTER:
                return scaledHeight - sizeY + offsetY;
        }
        return 0;
    }

    public enum Alignment {
        TOP_CENTER("top_center"),
        TOP_LEFT("top_left"),
        TOP_RIGHT("top_right"),
        BOTTOM_CENTER("bottom_center"),
        BOTTOM_RIGHT("bottom_right"),
        BOTTOM_LEFT("bottom_left");

        public static final Codec<Alignment> CODEC = Codec.STRING.flatComapMap((s) -> {
            return byName(s, (Alignment)null);
        }, (d) -> {
            return DataResult.success(d.getName());
        });

        private final String name;

        Alignment(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Alignment byName(String name, Alignment defaultValue) {
            Alignment[] var2 = values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Alignment value = var2[var4];
                if (value.name.equals(name)) {
                    return value;
                }
            }

            return defaultValue;
        }
    }

}