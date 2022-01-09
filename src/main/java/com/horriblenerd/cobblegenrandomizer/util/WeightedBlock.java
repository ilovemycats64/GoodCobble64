package com.horriblenerd.cobblegenrandomizer.util;

import com.horriblenerd.cobblegenrandomizer.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class WeightedBlock extends WeightedEntry.Wrapper<Block> {
    private final Block block;

    public WeightedBlock(String string) {
        // I hate Java
        super(getBlockFromString(string),
                string.split(Config.SEPARATOR).length > 1 ? Weight.of(Integer.parseInt(string.split(Config.SEPARATOR)[1])) : Weight.of(1));
        this.block = getBlockFromString(string);
    }

    private static Block getBlockFromString(String string) {
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(string.split(Config.SEPARATOR)[0]));
    }

    public WeightedBlock(Block block, Weight weight) {
        super(block, weight);
        this.block = block;
    }

    public WeightedBlock(Block block, int weight) {
        super(block, Weight.of(weight));
        this.block = block;
    }

    public static boolean isValid(String string) {
        boolean valid;
        ResourceLocation resourceLocation = ResourceLocation.tryParse(string.split(Config.SEPARATOR)[0]);
        valid = ForgeRegistries.BLOCKS.containsKey(resourceLocation);
        return valid;
    }

    public Block getBlock() {
        return block;
    }
}