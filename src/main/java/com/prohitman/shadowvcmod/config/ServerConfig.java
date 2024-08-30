package com.prohitman.shadowvcmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.BooleanValue groupInteraction;
    public static final ForgeConfigSpec.BooleanValue whisperInteraction;
    public static final ForgeConfigSpec.BooleanValue sneakInteraction;
    //public static final ForgeConfigSpec.IntValue voiceSculkFrequency;
    //public static final ForgeConfigSpec.IntValue minSuspicionActivationThreshold;
    public static final ForgeConfigSpec.IntValue minTargetActivationThreshold;

    public static final ForgeConfigSpec.IntValue minDetectionRange;



    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.push("VC Integration Configs");

        groupInteraction = COMMON_BUILDER.comment("If talking in groups should trigger the VC mob")
                .define("group_interaction", true);

        whisperInteraction = COMMON_BUILDER.comment("If whispering should trigger the VC mob")
                .define("whisper_interaction", true);

        sneakInteraction = COMMON_BUILDER.comment("If talking while sneaking should trigger VC mob")
                .define("sneak_interaction", true);

        //voiceSculkFrequency = COMMON_BUILDER.comment("The frequency of the voice vibration")
        //        .defineInRange("voice_sculk_frequency", 7, 1, 15);

       // minSuspicionActivationThreshold = COMMON_BUILDER.comment("The audio level threshold where the vc mob detects the voice player's location in dB")
       //         .defineInRange("minimum_suspicion_threshold", -60, -127, 0);

        minTargetActivationThreshold = COMMON_BUILDER.comment("The audio level threshold where the vc mob targets the voice player in dB, should be higher than the suspicion threshold.")
                        .defineInRange("minimum_target_threshold", -60, -127, 0);

        minDetectionRange = COMMON_BUILDER.comment("The minimum range where the vc entity would detect the player regardless of voice.")
                        .defineInRange("minDetectionRange", 8, 0, 64);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
