package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("cobblegenrandomizer")
public class CobbleGenRandomizer {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    ResourceLocation COBBLE = new ResourceLocation("cobblegenrandomizer", "cobble_gen");
    ResourceLocation STONE = new ResourceLocation("cobblegenrandomizer", "stone_gen");
    ResourceLocation BASALT = new ResourceLocation("cobblegenrandomizer", "basalt_gen");

    public CobbleGenRandomizer() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private List<WeightedBlock> getWeightedList(List<? extends String> list) {
        return list.stream().map(WeightedBlock::new).collect(Collectors.toCollection(ArrayList::new));
    }

    private net.minecraft.block.Block getLoot(ServerWorld world, ResourceLocation table) {
        net.minecraft.block.Block block = Blocks.AIR;
        if (Config.USE_CONFIG.get()) {
            List<? extends String> list = null;
            if (table == COBBLE)
                list = Config.BLOCK_LIST_COBBLE.get();
            else if (table == STONE)
                list = Config.BLOCK_LIST_STONE.get();
            else if (table == BASALT)
                list = Config.BLOCK_LIST_BASALT.get();

            if (list != null && !list.isEmpty()) {
                block = WeightedRandom.getRandomItem(world.getRandom(), getWeightedList(list)).getBlock();
            }

        } else {
            LootTable loottable = world.getServer().getLootTableManager().getLootTableFromLocation(table);
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
            if (event.getNewState().getBlock() == Blocks.COBBLESTONE)
                block = getLoot(world, COBBLE);
            else if (event.getNewState().getBlock() == Blocks.STONE)
                block = getLoot(world, STONE);
            else if (event.getNewState().getBlock() == Blocks.field_235337_cO_)
                block = getLoot(world, BASALT);

            if (block != Blocks.AIR)
                event.setNewState(block.getDefaultState());
        }
    }

    private static class WeightedBlock extends WeightedRandom.Item {
        private final net.minecraft.block.Block block;

        public WeightedBlock(String string) {
            // I hate Java
            super(string.split(Config.SEPARATOR).length > 1 ? Integer.parseInt(string.split(Config.SEPARATOR)[1]) : 1);
            this.block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(string.split(Config.SEPARATOR)[0]));
        }

        public int getWeight() {
            return super.itemWeight;
        }

        public net.minecraft.block.Block getBlock() {
            return block;
        }
    }

}

