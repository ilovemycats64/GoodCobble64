package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final String CATEGORY_GENERAL = "general";

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue USE_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_LIST_COBBLE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_LIST_STONE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_LIST_BASALT;


    private static final List<String> cobble = new ArrayList<>();
    private static final List<String> stone = new ArrayList<>();
    private static final List<String> basalt = new ArrayList<>();

    static {
        initLists();


        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        USE_CONFIG = COMMON_BUILDER.comment("Use config instead of datapack").define("use_config", true);
        BLOCK_LIST_COBBLE = COMMON_BUILDER.comment("Cobble gen").defineList("block_list_cobble", cobble, (b) -> b instanceof String && ResourceLocation.isResouceNameValid((String) b));
        BLOCK_LIST_STONE = COMMON_BUILDER.comment("Stone gen").defineList("block_list_stone", stone, (b) -> b instanceof String && ResourceLocation.isResouceNameValid((String) b));
        BLOCK_LIST_BASALT = COMMON_BUILDER.comment("Basalt gen").defineList("block_list_basalt", basalt, (b) -> b instanceof String && ResourceLocation.isResouceNameValid((String) b));

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void initLists() {
        cobble.add(Blocks.COBBLESTONE.getRegistryName().toString());
        cobble.add(Blocks.COAL_ORE.getRegistryName().toString());
        cobble.add(Blocks.IRON_ORE.getRegistryName().toString());
        cobble.add(Blocks.REDSTONE_ORE.getRegistryName().toString());
        cobble.add(Blocks.LAPIS_ORE.getRegistryName().toString());
        cobble.add(Blocks.EMERALD_ORE.getRegistryName().toString());
        cobble.add(Blocks.GOLD_ORE.getRegistryName().toString());
        cobble.add(Blocks.DIAMOND_ORE.getRegistryName().toString());


        stone.add(Blocks.STONE.getRegistryName().toString());
        stone.add(Blocks.GRANITE.getRegistryName().toString());
        stone.add(Blocks.DIORITE.getRegistryName().toString());
        stone.add(Blocks.ANDESITE.getRegistryName().toString());


        basalt.add(Blocks.field_235337_cO_.getRegistryName().toString());
        basalt.add(Blocks.field_235406_np_.getRegistryName().toString());
        basalt.add(Blocks.NETHERRACK.getRegistryName().toString());
        basalt.add(Blocks.NETHER_QUARTZ_ORE.getRegistryName().toString());
        basalt.add(Blocks.field_235334_I_.getRegistryName().toString());
        basalt.add(Blocks.field_235398_nh_.getRegistryName().toString());
    }
}
