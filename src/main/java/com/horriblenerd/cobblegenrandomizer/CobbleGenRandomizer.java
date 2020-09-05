package com.horriblenerd.cobblegenrandomizer;

import com.horriblenerd.cobblegenrandomizer.util.GeneratorLists;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CobbleGenRandomizer.MODID)
public class CobbleGenRandomizer {
    public static final String MODID = "cobblegenrandomizer";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final GeneratorLists GENERATORS = new GeneratorLists();


    public CobbleGenRandomizer() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
    }

}

