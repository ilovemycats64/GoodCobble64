package com.horriblenerd.compat.jei;

import com.horriblenerd.cobblegenrandomizer.util.Generator;
import com.horriblenerd.cobblegenrandomizer.util.WeightedBlock;
import net.minecraft.block.Block;

import javax.annotation.Nonnull;

/**
 * Created by HorribleNerd on 12/11/2020
 */
public class GeneratorRecipeWrapper implements Comparable<GeneratorRecipeWrapper> {

    public final WeightedBlock weightedBlock;
    public final Generator.Type type;
    public final Block catalyst;

    public GeneratorRecipeWrapper(WeightedBlock block, Generator.Type type, Block catalyst) {
        this.weightedBlock = block;
        this.type = type;
        this.catalyst = catalyst;
    }

    @Override
    public int compareTo(@Nonnull GeneratorRecipeWrapper o) {
        return Integer.compare(o.weightedBlock.getWeight(), weightedBlock.getWeight());
    }
}
