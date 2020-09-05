package com.horriblenerd.cobblegenrandomizer.util;

import com.horriblenerd.cobblegenrandomizer.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class GeneratorLists {

    private static final Logger LOGGER = LogManager.getLogger();

    public List<WeightedBlock> COBBLE_LIST;
    public List<WeightedBlock> STONE_LIST;
    public List<WeightedBlock> BASALT_LIST;

    public List<Generator> CUSTOM_GENERATOR_LIST;

    public boolean reload() {
        LOGGER.debug("Reloading generator lists...");
        COBBLE_LIST = Util.getWeightedList(Config.BLOCK_LIST_COBBLE.get());
        STONE_LIST = Util.getWeightedList(Config.BLOCK_LIST_STONE.get());
        BASALT_LIST = Util.getWeightedList(Config.BLOCK_LIST_BASALT.get());

        LOGGER.debug("Reloading custom generators...");
        CUSTOM_GENERATOR_LIST = new ArrayList<>();
        boolean ret = true;
        for (List<Object> l : Config.CUSTOM_GENERATORS.get()) {
            Generator gen = Util.createGenerator(l);
            if (gen != null) {
                CUSTOM_GENERATOR_LIST.add(gen);
            }
            else {
                LOGGER.error("Invalid custom generator: " + l);
                ret = false;
            }
        }
        return ret;
    }


}
