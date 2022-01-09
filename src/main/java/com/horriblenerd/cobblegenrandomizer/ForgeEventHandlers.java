package com.horriblenerd.cobblegenrandomizer;

import com.horriblenerd.cobblegenrandomizer.util.Generator;
import com.horriblenerd.cobblegenrandomizer.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.horriblenerd.cobblegenrandomizer.CobbleGenRandomizer.GENERATORS;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class ForgeEventHandlers {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onFluidPlaceBlockEvent(BlockEvent.FluidPlaceBlockEvent event) {
        LevelAccessor worldIn = event.getWorld();
        if (worldIn instanceof ServerLevel serverLevel) {
            Block block = Blocks.AIR;

            if (event.getNewState().getBlock() == Blocks.COBBLESTONE) {
                block = Util.getLoot(serverLevel, event.getPos(), Generator.Type.COBBLESTONE);
            }
            else if (event.getNewState().getBlock() == Blocks.STONE) {
                block = Util.getLoot(serverLevel, event.getPos(), Generator.Type.STONE);
            }
            else if (event.getNewState().getBlock() == Blocks.BASALT) {
                block = Util.getLoot(serverLevel, event.getPos(), Generator.Type.BASALT);
            }

            if (block != Blocks.AIR) {
                event.setNewState(block.defaultBlockState());
            }
        }
    }

    // Ideally should happen after config reload, but that seems broken?
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (GENERATORS.reload()) {
            LOGGER.info("Successfully loaded generators");
        }
        else {
            LOGGER.error("An error occurred during generator loading");
        }
    }

    // Configs don't reload so this is currently useless
//    @SubscribeEvent
//    public void registerCommands(RegisterCommandsEvent event) {
//        ModCommands.register(event.getDispatcher());
//    }

}
