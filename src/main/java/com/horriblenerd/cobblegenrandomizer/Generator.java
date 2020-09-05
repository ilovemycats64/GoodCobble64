package com.horriblenerd.cobblegenrandomizer;

import net.minecraft.block.Block;

import java.util.List;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class Generator {

    private final Type type;
    private final Block block;
    private final List<WeightedBlock> blockList;

    public Generator(Type type, Block block, List<WeightedBlock> blockList) {
        this.type = type;
        this.block = block;
        this.blockList = blockList;
    }

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
