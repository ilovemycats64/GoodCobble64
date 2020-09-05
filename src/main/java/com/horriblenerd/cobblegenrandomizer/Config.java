package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        BLOCK_LIST_COBBLE = COMMON_BUILDER.comment("Cobble gen").defineList("block_list_cobble", cobble, (b) -> b instanceof String && isValidBlock((String) b));
        BLOCK_LIST_STONE = COMMON_BUILDER.comment("Stone gen").defineList("block_list_stone", stone, (b) -> b instanceof String && isValidBlock((String) b));
        BLOCK_LIST_BASALT = COMMON_BUILDER.comment("Basalt gen").defineList("block_list_basalt", basalt, (b) -> b instanceof String && isValidBlock((String) b));

        COMMON_BUILDER.comment("Custom settings").push(CATEGORY_CUSTOM);
        CUSTOM_GENERATORS = COMMON_BUILDER.comment("Custom generators",
                "Syntax: [gen]",
                "Gen: [type, block, list]",
                "Type: cobblestone, stone",
                "Block: resource location of the block below the generated block",
                "List: see List settings",
                "Example:",
                "custom_generators = [[\"cobblestone\", \"minecraft:diamond_block\", [\"minecraft:diamond_ore\"]]]").defineList("custom_generators", custom_gens, (p) -> p instanceof List && isCustomGeneratorValid((List<Object>) p));

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static boolean isCustomGeneratorValid(List<Object> l) {
        if (l == null || l.size() != 3) {
            return false;
        }
        if (!(l.get(0) instanceof String)) {
            return false;
        }
        if (!(l.get(1) instanceof String)) {
            return false;
        }
        if (!(l.get(2) instanceof List<?>)) {
            return false;
        }

        String type = (String) l.get(0);
        String req = (String) l.get(1);
        List<?> blocks = (List<?>) l.get(2);

        if (!type.equals("cobblestone") && !type.equals("stone") && !type.equals("basalt")) {
            return false;
        }

        boolean resourceNameValid = (ResourceLocation.isResouceNameValid(req) && ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(req)) != null);
        if (!resourceNameValid) {
            return false;
        }

        for (Object o : blocks) {
            if (!(o instanceof String)) {
                return false;
            }
            if (!isValidBlock((String) o)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidBlock(String s) {
        String[] strings = s.split(SEPARATOR);
        boolean resourceNameValid = (ResourceLocation.isResouceNameValid(strings[0]) && ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(strings[0])) != null);
        boolean numeric = true;
        if (strings.length == 2) {
            numeric = StringUtils.isNumeric(strings[1]);
        }
        return resourceNameValid && numeric;
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

        addBlock(basalt, Blocks.field_235337_cO_, 60); // Basalt
        addBlock(basalt, Blocks.field_235406_np_, 60); // Blackstone
        addBlock(basalt, Blocks.NETHERRACK, 60);
        addBlock(basalt, Blocks.NETHER_QUARTZ_ORE, 30);
        addBlock(basalt, Blocks.field_235334_I_, 30); // Nether Gold Ore
        addBlock(basalt, Blocks.field_235398_nh_, 1); // Ancient Debris

        ArrayList<String> diamondList = new ArrayList<>();
        diamondList.add("minecraft:diamond_ore");
        ArrayList<Object> customList = new ArrayList<>();
        customList.add("cobblestone");
        customList.add("minecraft:diamond_block");
        customList.add(diamondList);
        custom_gens.add(customList);
    }

    private static void addBlock(List<String> list, Block block, int weight) {
        list.add(String.format("%s%s%d", Objects.requireNonNull(block.getRegistryName()).toString(), SEPARATOR.replaceAll("\\\\", ""), weight));
    }

    private static void addBlock(List<String> list, Block block) {
        addBlock(list, block, 1);
    }

}
