package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CobbleGenRandomizer.MODID)
public class CobbleGenRandomizer {
    public static final String MODID = "cobblegenrandomizer";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public List<WeightedBlock> COBBLE_LIST;
    public List<WeightedBlock> STONE_LIST;
    public List<WeightedBlock> BASALT_LIST;

    public List<Generator> CUSTOM_GENERATOR_LIST;

    public CobbleGenRandomizer() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void reloadLists() {
        LOGGER.debug("Reloading generator lists...");
        COBBLE_LIST = getWeightedList(Config.BLOCK_LIST_COBBLE.get());
        STONE_LIST = getWeightedList(Config.BLOCK_LIST_STONE.get());
        BASALT_LIST = getWeightedList(Config.BLOCK_LIST_BASALT.get());

        LOGGER.debug("Reloading custom generators...");
        CUSTOM_GENERATOR_LIST = new ArrayList<>();
        for (List<Object> l : Config.CUSTOM_GENERATORS.get()) {
            Generator gen = createGenerator(l);
            if (gen != null) {
                CUSTOM_GENERATOR_LIST.add(gen);
            }
            else {
                LOGGER.error("Invalid custom generator: " + l);
            }
        }
    }

    private Generator createGenerator(List<Object> listIn) {
        if (!Config.isCustomGeneratorValid(listIn)) {
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
        Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(req));

        List<?> list = (List<?>) listIn.get(2);
        ArrayList<WeightedBlock> blockList = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof String && Config.isValidBlock((String) o)) {
                blockList.add(new WeightedBlock((String) o));
            }
        }

        return new Generator(type, block, blockList);
    }

    private List<WeightedBlock> getWeightedList(List<? extends String> list) {
        ArrayList<WeightedBlock> weightedBlocks = new ArrayList<>();
        for (String s : list) {
            // Add block
            if (WeightedBlock.isValid(s)) {
                WeightedBlock weightedBlock = new WeightedBlock(s);
                weightedBlocks.add(weightedBlock);
            }
            // Add all blocks with certain tag
            else {
                ResourceLocation resourceLocation = ResourceLocation.tryCreate(s.split(Config.SEPARATOR)[0]);
                if (resourceLocation != null) {
                    ITag<Block> tag = BlockTags.getCollection().get(resourceLocation);
                    if (tag != null && !tag.func_230236_b_().isEmpty()) {
                        int weight = s.split(Config.SEPARATOR).length > 1 ? Integer.parseInt(s.split(Config.SEPARATOR)[1]) : 1;
                        for (Block b : tag.func_230236_b_()) {
                            weightedBlocks.add(new WeightedBlock(b, weight));
                        }
                    }
                }
            }
        }
        return weightedBlocks;
    }

    private net.minecraft.block.Block getLoot(ServerWorld world, BlockPos pos, Generator.Type type) {
        net.minecraft.block.Block block = Blocks.AIR;
        if (Config.USE_CONFIG.get()) {

            List<WeightedBlock> list = null;

            // Check for a custom generator that satisfies the conditions
            if (!CUSTOM_GENERATOR_LIST.isEmpty()) {
                for (Generator g : CUSTOM_GENERATOR_LIST) {
                    if (g.getType() == type && g.getBlock() == world.getBlockState(pos.down()).getBlock()) {
                        list = g.getBlockList();
                        break;
                    }
                }
            }

            // Else load the default generator lists
            if (list == null || list.isEmpty()) {
                if (type == Generator.Type.COBBLESTONE) {
                    list = COBBLE_LIST;
                }
                else if (type == Generator.Type.STONE) {
                    list = STONE_LIST;
                }
                else if (type == Generator.Type.BASALT) {
                    list = BASALT_LIST;
                }
            }

            if (list != null && !list.isEmpty()) {
                block = WeightedRandom.getRandomItem(world.getRandom(), list).getBlock();
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
            LootTable loottable = world.getServer().getLootTableManager().getLootTableFromLocation(resourceLocation);
            LootContext.Builder lootcontext$builder = (new LootContext.Builder(world));
            List<ItemStack> list = loottable.generate(lootcontext$builder.build(LootParameterSets.EMPTY));
            if (!list.isEmpty()) {
                ItemStack loot = list.get(world.rand.nextInt(list.size()));
                Item item = loot.getItem();
                if (item instanceof BlockItem) {
                    block = ((BlockItem) item).getBlock();
                }
            }
        }
        return block;
    }

    @SubscribeEvent
    public void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {
        IWorld worldIn = event.getWorld();
        if (worldIn instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldIn;
            net.minecraft.block.Block block = Blocks.AIR;

            if (event.getNewState().getBlock() == Blocks.COBBLESTONE) {
                block = getLoot(world, event.getPos(), Generator.Type.COBBLESTONE);
            }
            else if (event.getNewState().getBlock() == Blocks.STONE) {
                block = getLoot(world, event.getPos(), Generator.Type.STONE);
            }
            else if (event.getNewState().getBlock() == Blocks.field_235337_cO_) {
                block = getLoot(world, event.getPos(), Generator.Type.BASALT);
            }

            if (block != Blocks.AIR) {
                event.setNewState(block.getDefaultState());
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        reloadLists();
    }

}

