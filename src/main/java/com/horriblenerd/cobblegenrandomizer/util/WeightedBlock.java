package com.horriblenerd.cobblegenrandomizer.util;

import com.horriblenerd.cobblegenrandomizer.Config;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class WeightedBlock extends WeightedRandom.Item {
    private final net.minecraft.block.Block block;

    public WeightedBlock(String string) {
        // I hate Java
        super(string.split(Config.SEPARATOR).length > 1 ? Integer.parseInt(string.split(Config.SEPARATOR)[1]) : 1);
        this.block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryCreate(string.split(Config.SEPARATOR)[0]));
    }

    public WeightedBlock(Block block, int weight) {
        super(weight);
        this.block = block;
    }

    public static boolean isValid(String string) {
        boolean valid;
        ResourceLocation resourceLocation = ResourceLocation.tryCreate(string.split(Config.SEPARATOR)[0]);
        valid = ForgeRegistries.BLOCKS.containsKey(resourceLocation);
        return valid;
    }

    public int getWeight() {
        return super.itemWeight;
    }

    public net.minecraft.block.Block getBlock() {
        return block;
    }
}