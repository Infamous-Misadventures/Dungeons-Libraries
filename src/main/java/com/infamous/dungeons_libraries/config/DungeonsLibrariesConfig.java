package com.infamous.dungeons_libraries.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DungeonsLibrariesConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_AREA_OF_EFFECT_ON_OTHER_PLAYERS;
    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_KEEP_SOULS_ON_DEATH;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ENEMY_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ARMORED_MOBS;
    public static ForgeConfigSpec.ConfigValue<Double> ARMORED_MOBS_BASE_CHANCE;

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

            builder.comment("Souls Configuration").push("souls_configuration");
            ENABLE_KEEP_SOULS_ON_DEATH = builder
                    .comment("Enables keeping of souls upon death, disabled by default. [true / false]")
                    .define("enableKeepSoulsOnDeath", false);
            builder.pop();

            builder.comment("Armored Mob Configuration").push("souls_configuration");
            ENABLE_ARMORED_MOBS = builder
                    .comment("Enables armored mobs, enabled by default. [true / false]")
                    .define("enableArmoredMobs", true);
            ARMORED_MOBS_BASE_CHANCE = builder
                    .comment("Base chance of an armored mob spawning. [0.0 - 1.0] \n" +
                            "Calculation: chance * difficulty.getSpecialMultiplier() \n" +
                            "Base chance for vanilla armor spawning is 0.15. Default is 0.05")
                    .defineInRange("armoredMobsBaseChance", 0.05, 0.0, 1.0);
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