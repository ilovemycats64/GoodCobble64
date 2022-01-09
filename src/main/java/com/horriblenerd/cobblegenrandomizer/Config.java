package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.horriblenerd.cobblegenrandomizer.util.Util.*;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String SEPARATOR = "\\|";


    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_LISTS = "lists";
    public static final String CATEGORY_CUSTOM = "custom";

    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.BooleanValue USE_CONFIG;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_LIST_COBBLE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_LIST_STONE;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLOCK_LIST_BASALT;

    public static final ForgeConfigSpec.ConfigValue<List<? extends List<Object>>> CUSTOM_GENERATORS;


    private static final List<String> cobble = new ArrayList<>();
    private static final List<String> stone = new ArrayList<>();
    private static final List<String> basalt = new ArrayList<>();

    private static final List<List<Object>> custom_gens = new ArrayList<>();

    static {
        initLists();

        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        USE_CONFIG = COMMON_BUILDER.comment("Use config instead of datapack").define("use_config", true);

        COMMON_BUILDER.comment("List settings",
                "Syntax: [\"modid:block|weight\"]",
                "Example: [\"minecraft:stone|2\",\"minecraft:dirt|1\"]",
                "Forge tags are supported").push(CATEGORY_LISTS);
        BLOCK_LIST_COBBLE = COMMON_BUILDER.comment("Cobble gen").defineList("block_list_cobble", cobble, (b) -> b instanceof String && (isValidBlock((String) b) || isValidTag((String) b)));
        BLOCK_LIST_STONE = COMMON_BUILDER.comment("Stone gen").defineList("block_list_stone", stone, (b) -> b instanceof String && (isValidBlock((String) b) || isValidTag((String) b)));
        BLOCK_LIST_BASALT = COMMON_BUILDER.comment("Basalt gen").defineList("block_list_basalt", basalt, (b) -> b instanceof String && (isValidBlock((String) b) || isValidTag((String) b)));

        COMMON_BUILDER.comment("Custom settings").push(CATEGORY_CUSTOM);
        CUSTOM_GENERATORS = COMMON_BUILDER.comment("Custom generators",
                "Syntax: [gen]",
                "Gen: [type, block, list]",
                "Type: cobblestone, stone",
                "Block: resource location of the block below the generated block",
                "List: see List settings",
                "Examples:",
                "custom_generators = [",
                "   [\"cobblestone\", \"minecraft:diamond_block\", [\"minecraft:diamond_block\"]],",
                "   [\"cobblestone\", \"minecraft:dirt\", [\"forge:dirt\"]],",
                "   [\"cobblestone\", \"minecraft:white_wool\", [\"minecraft:wool\"]]]").defineList("custom_generators", custom_gens, (p) -> p instanceof List && isCustomGeneratorValid((List<Object>) p));

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    private static void initLists() {
        addBlock(cobble, Blocks.COBBLESTONE, 60);
        addBlock(cobble, Blocks.COAL_ORE, 25);
        addBlock(cobble, Blocks.IRON_ORE, 20);
        addBlock(cobble, Blocks.REDSTONE_ORE, 20);
        addBlock(cobble, Blocks.LAPIS_ORE, 20);
        addBlock(cobble, Blocks.EMERALD_ORE, 5);
        addBlock(cobble, Blocks.GOLD_ORE, 10);
        addBlock(cobble, Blocks.DIAMOND_ORE, 5);

        addBlock(stone, Blocks.STONE, 30);
        addBlock(stone, Blocks.GRANITE, 10);
        addBlock(stone, Blocks.DIORITE, 10);
        addBlock(stone, Blocks.ANDESITE, 10);

        addBlock(basalt, Blocks.BASALT, 60); // Basalt
        addBlock(basalt, Blocks.BLACKSTONE, 60); // Blackstone
        addBlock(basalt, Blocks.NETHERRACK, 60);
        addBlock(basalt, Blocks.NETHER_QUARTZ_ORE, 30);
        addBlock(basalt, Blocks.NETHER_GOLD_ORE, 30); // Nether Gold Ore
        addBlock(basalt, Blocks.ANCIENT_DEBRIS, 1); // Ancient Debris
    }

    private static void addBlock(List<String> list, Block block, int weight) {
        list.add(String.format("%s%s%d", Objects.requireNonNull(block.getRegistryName()), SEPARATOR.replaceAll("\\\\", ""), weight));
    }

    private static void addBlock(List<String> list, Block block) {
        addBlock(list, block, 1);
    }

}
