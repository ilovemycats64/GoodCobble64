package com.horriblenerd.cobblegenrandomizer.util;

import com.horriblenerd.cobblegenrandomizer.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.horriblenerd.cobblegenrandomizer.CobbleGenRandomizer.GENERATORS;
import static com.horriblenerd.cobblegenrandomizer.Config.SEPARATOR;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class Util {

    private static final Logger LOGGER = LogManager.getLogger();

    public static Generator createGenerator(List<Object> listIn) {
        if (!isCustomGeneratorValid(listIn)) {
            return null;
        }

        String typeString = (String) listIn.get(0);
        Generator.Type type;
        if (typeString.equals("cobblestone")) {
            type = Generator.Type.COBBLESTONE;
        }
        else if (typeString.equals("stone")) {
            type = Generator.Type.STONE;
        }
        else {
            return null;
        }

        String req = (String) listIn.get(1);
        Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(req));

        List<?> list = (List<?>) listIn.get(2);
        ArrayList<WeightedBlock> blockList = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof String) {
                if (isValidBlock((String) o)) {
                    blockList.add(new WeightedBlock((String) o));
                }
                else if (isValidTag((String) o)) {
                    blockList.addAll(getBlocksFromTag((String) o));
                }
            }
        }
        return new Generator(type, block, blockList);
    }

    public static List<WeightedBlock> getBlocksFromTag(String s) {
        ArrayList<WeightedBlock> weightedBlocks = new ArrayList<>();
        ResourceLocation resourceLocation = ResourceLocation.tryParse(s.split(SEPARATOR)[0]);
        if (resourceLocation != null) {
            Tag<Block> tag = BlockTags.getAllTags().getTag(resourceLocation);
            if (tag != null && !tag.getValues().isEmpty()) {
                int weight = s.split(SEPARATOR).length > 1 ? Integer.parseInt(s.split(SEPARATOR)[1]) : 1;
                for (Block b : tag.getValues()) {
                    weightedBlocks.add(new WeightedBlock(b, weight));
                }
            }
        }
        return weightedBlocks;
    }

    public static List<WeightedBlock> getWeightedList(List<? extends String> list) {
        ArrayList<WeightedBlock> weightedBlocks = new ArrayList<>();
        for (String s : list) {
            // Add block
            if (WeightedBlock.isValid(s)) {
                WeightedBlock weightedBlock = new WeightedBlock(s);
                weightedBlocks.add(weightedBlock);
            }
            // Add all blocks with certain tag
            else {
                weightedBlocks.addAll(getBlocksFromTag(s));
            }
        }
        return weightedBlocks;
    }

    public static Block getLoot(ServerLevel world, BlockPos pos, Generator.Type type) {
        Block block = Blocks.AIR;
        if (Config.USE_CONFIG.get()) {

            List<WeightedBlock> list = null;

            // Check for a custom generator that satisfies the conditions
            if (!GENERATORS.CUSTOM_GENERATOR_LIST.isEmpty()) {
                for (Generator g : GENERATORS.CUSTOM_GENERATOR_LIST) {
                    if (g.getType() == type && g.getBlock() == world.getBlockState(pos.below()).getBlock()) {
                        list = g.getBlockList();
                        break;
                    }
                }
            }

            // Else load the default generator lists
            if (list == null || list.isEmpty()) {
                if (type == Generator.Type.COBBLESTONE) {
                    list = GENERATORS.COBBLE_LIST;
                }
                else if (type == Generator.Type.STONE) {
                    list = GENERATORS.STONE_LIST;
                }
                else if (type == Generator.Type.BASALT) {
                    list = GENERATORS.BASALT_LIST;
                }
            }

            if (list != null && !list.isEmpty()) {
                Optional<WeightedBlock> randomItem = WeightedRandom.getRandomItem(world.getRandom(), list);
                if (randomItem.isPresent()) {
                    block = randomItem.get().getBlock();
                }
            }
        }
        else {
            ResourceLocation resourceLocation = new ResourceLocation("");
            if (type == Generator.Type.COBBLESTONE) {
                resourceLocation = new ResourceLocation("cobblegenrandomizer", "cobble_gen");
            }
            else if (type == Generator.Type.STONE) {
                resourceLocation = new ResourceLocation("cobblegenrandomizer", "stone_gen");
            }
            else if (type == Generator.Type.BASALT) {
                resourceLocation = new ResourceLocation("cobblegenrandomizer", "basalt_gen");
            }

            // Load the correct loottable and get a random block
            LootTable loottable = world.getServer().getLootTables().get(resourceLocation);
            LootContext.Builder lootcontext$builder = (new LootContext.Builder(world));
            List<ItemStack> list = loottable.getRandomItems(lootcontext$builder.create(LootContextParamSet.builder().build()));
            if (!list.isEmpty()) {
                ItemStack loot = list.get(world.random.nextInt(list.size()));
                Item item = loot.getItem();
                if (item instanceof BlockItem) {
                    block = ((BlockItem) item).getBlock();
                }
            }
        }
        return block;
    }

    public static boolean isCustomGeneratorValid(List<Object> l) {
        if (l == null || l.size() != 3) {
            return false;
        }
        if (!(l.get(0) instanceof String type)) {
            return false;
        }
        if (!(l.get(1) instanceof String req)) {
            return false;
        }
        if (!(l.get(2) instanceof List<?> blocks)) {
            return false;
        }

        if (!type.equals("cobblestone") && !type.equals("stone") && !type.equals("basalt")) {
            return false;
        }

        if (!isValidBlock(req)) {
            return false;
        }

        for (Object o : blocks) {
            if (!(o instanceof String)) {
                return false;
            }
            if (!isValidBlock((String) o) && !isValidTag((String) o)) {
                LOGGER.debug("Invalid block or tag: " + o);
                return false;
            }
        }

        return true;
    }

    public static boolean isValidTag(String s) {
        String[] strings = s.split(SEPARATOR);
        boolean resourceNameValid = isResourceNameValid(strings[0]);
        if (!resourceNameValid) {
            return false;
        }

        ResourceLocation resourceLocation = ResourceLocation.tryParse(strings[0]);
        return resourceLocation != null;
    }

    public static boolean isValidBlock(String s) {
        String[] strings = s.split(SEPARATOR);
        boolean resourceNameValid = isResourceNameValid(strings[0]);
        if (!resourceNameValid) {
            return false;
        }

        ResourceLocation resourceLocation = ResourceLocation.tryParse(strings[0]);
        if (resourceLocation == null) {
            return false;
        }

        Block value = ForgeRegistries.BLOCKS.getValue(resourceLocation);
        if (value == null || value == Blocks.AIR) {
            return false;
        }

        boolean numeric = true;
        if (strings.length == 2) {
            numeric = StringUtils.isNumeric(strings[1]);
        }
        return numeric;
    }

    public static boolean isResourceNameValid(String resourceName) {
        String[] split = resourceName.split(":");
        if (split.length == 1) {
            return isValidPath(split[0]);
        }
        if (split.length == 2) {
            return isValidPath(split[0]) && isValidPath(split[1]);
        }
        return false;

    }

    private static boolean isValidPath(String path) {
        for (int i = 0; i < path.length(); i++) {
            if (!isValidChar(path.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidChar(char c) {
        return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.' || c == '/';
    }

}
