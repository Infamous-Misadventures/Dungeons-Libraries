package com.infamous.dungeons_libraries.datagen;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String locale) {
        super(gen, DungeonsLibraries.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addConfigOptions();
        addTips();
    }

    private void addTips() {
    }

    private void addConfigOptions() {
    }

    private String getNameFromId(String idString) {
        StringBuilder sb = new StringBuilder();
        for (String word : idString.toLowerCase().split("_")) {
            sb.append(word.substring(0, 1).toUpperCase());
            sb.append(word.substring(1));
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
