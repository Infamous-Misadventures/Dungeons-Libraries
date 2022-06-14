package com.infamous.dungeons_libraries.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DungeonsLibrariesConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_AREA_OF_EFFECT_ON_OTHER_PLAYERS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENEMY_BLACKLIST;

    public static class Common {

        public Common(ForgeConfigSpec.Builder builder){

            builder.comment("Combat Configuration").push("combat_configuration");
            ENABLE_AREA_OF_EFFECT_ON_OTHER_PLAYERS = builder
                    .comment("Enable area of effects also being applied to players. \n" +
                            "If you do not want area of effects being applied to other players, disable this feature. [true / false]")
                    .define("enableAreaOfEffectOnOtherPlayers", false);
            ENEMY_BLACKLIST = builder
                    .comment("Add entities that will never be targeted by aggressive Dungeons effects. \n"
                            + "To do so, enter their registry names.")
                    .defineList("effectTargetBlacklist", Lists.newArrayList(
                            "guardvillagers:guard",
                            "minecraft:bat",
                            "minecraft:bee",
                            "minecraft:chicken",
                            "minecraft:cod",
                            "minecraft:cow",
                            "minecraft:dolphin",
                            "minecraft:donkey",
                            "minecraft:fox",
                            "minecraft:horse",
                            "minecraft:iron_golem",
                            "minecraft:mooshroom",
                            "minecraft:ocelot",
                            "minecraft:panda",
                            "minecraft:parrot",
                            "minecraft:pig",
                            "minecraft:polar_bear",
                            "minecraft:pufferfish",
                            "minecraft:rabbit",
                            "minecraft:salmon",
                            "minecraft:sheep",
                            "minecraft:squid",
                            "minecraft:strider",
                            "minecraft:trader_llama",
                            "minecraft:tropical_fish",
                            "minecraft:turtle",
                            "minecraft:villager",
                            "minecraft:wandering_trader",
                            "minecraft:wolf"
                            ),
                            (itemRaw) -> itemRaw instanceof String);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}