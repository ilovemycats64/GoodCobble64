package com.horriblenerd.cobblegenrandomizer.util;

import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public record Generator(com.horriblenerd.cobblegenrandomizer.util.Generator.Type type,
                        Block block,
                        List<WeightedBlock> blockList) {

    public Type getType() {
        return type;
    }

    public Block getBlock() {
        return block;
    }

    public List<WeightedBlock> getBlockList() {
        return blockList;
    }


    public enum Type {
        COBBLESTONE,
        STONE,
        BASALT
    }

}
