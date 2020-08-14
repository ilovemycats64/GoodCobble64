package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("cobblegenrandomizer")
public class CobbleGenRandomizer {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    ResourceLocation COBBLE = new ResourceLocation("cobblegenrandomizer", "cobble_gen");
    ResourceLocation STONE = new ResourceLocation("cobblegenrandomizer", "stone_gen");
    ResourceLocation BASALT = new ResourceLocation("cobblegenrandomizer", "basalt_gen");

    public CobbleGenRandomizer() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private Block getLoot(ServerWorld world, ResourceLocation table) {
        LootTable loottable = world.getServer().getLootTableManager().getLootTableFromLocation(table);
        LootContext.Builder lootcontext$builder = (new LootContext.Builder(world));
        List<ItemStack> list = loottable.generate(lootcontext$builder.build(LootParameterSets.EMPTY));

        Block block = Blocks.AIR;
        if (!list.isEmpty()) {
            ItemStack loot = list.get(world.rand.nextInt(list.size()));
            Item item = loot.getItem();
            if (item instanceof BlockItem) {
                block = ((BlockItem) item).getBlock();
            }
        }
        return block;
    }

    @SubscribeEvent
    public void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {
        IWorld worldIn = event.getWorld();
        if (worldIn instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldIn;
            Block block = Blocks.AIR;
            if (event.getNewState().getBlock() == Blocks.COBBLESTONE)
                block = getLoot(world, COBBLE);
            else if (event.getNewState().getBlock() == Blocks.STONE)
                block = getLoot(world, STONE);
            else if (event.getNewState().getBlock() == Blocks.field_235337_cO_)
                block = getLoot(world, BASALT);

            if (block != Blocks.AIR) event.setNewState(block.getDefaultState());
        }
    }
}

