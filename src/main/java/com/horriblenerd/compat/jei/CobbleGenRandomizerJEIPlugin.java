package com.horriblenerd.compat.jei;

import com.horriblenerd.cobblegenrandomizer.CobbleGenRandomizer;
import com.horriblenerd.cobblegenrandomizer.util.Generator;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

import java.util.stream.Collectors;

/**
 * Created by HorribleNerd on 12/11/2020
 */
@JeiPlugin
public class CobbleGenRandomizerJEIPlugin implements IModPlugin {

    private static final ResourceLocation ID = new ResourceLocation(CobbleGenRandomizer.MODID, "jei_plugin");

    GeneratorRecipeCategory cobblestone_generator;
    GeneratorRecipeCategory stone_generator;
    GeneratorRecipeCategory basalt_generator;
    GeneratorRecipeCategory custom_generator;


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        cobblestone_generator = new GeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper(), Items.COBBLESTONE, I18n.get("cobblegenrandomizer.jei.cobbleCategory"), new ResourceLocation(CobbleGenRandomizer.MODID, "cobble_gen"), 0);
        stone_generator = new GeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper(), Items.STONE, I18n.get("cobblegenrandomizer.jei.stoneCategory"), new ResourceLocation(CobbleGenRandomizer.MODID, "stone_gen"), 1);
        basalt_generator = new GeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper(), Items.BASALT, I18n.get("cobblegenrandomizer.jei.basaltCategory"), new ResourceLocation(CobbleGenRandomizer.MODID, "basalt_gen"), 1);
        ItemStack custom_icon = new ItemStack(Items.COBBLESTONE);
        custom_icon.enchant(Enchantments.BLOCK_FORTUNE, 0);
        custom_generator = new GeneratorRecipeCategory(registration.getJeiHelpers().getGuiHelper(), custom_icon, I18n.get("cobblegenrandomizer.jei.customCategory"), new ResourceLocation(CobbleGenRandomizer.MODID, "custom_gen"), 2);

        registration.addRecipeCategories(cobblestone_generator);
        registration.addRecipeCategories(stone_generator);
        registration.addRecipeCategories(basalt_generator);
        registration.addRecipeCategories(custom_generator);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        registration.addRecipes(CobbleGenRandomizer.GENERATORS.COBBLE_LIST.stream()
                        .map((b) -> new GeneratorRecipeWrapper(b, Generator.Type.COBBLESTONE, Blocks.AIR))
                        .sorted()
                        .collect(Collectors.toList()),
                cobblestone_generator.getUid());
        registration.addRecipes(CobbleGenRandomizer.GENERATORS.STONE_LIST.stream()
                        .map((b) -> new GeneratorRecipeWrapper(b, Generator.Type.STONE, Blocks.AIR))
                        .sorted()
                        .collect(Collectors.toList()),
                stone_generator.getUid());
        registration.addRecipes(CobbleGenRandomizer.GENERATORS.BASALT_LIST.stream()
                        .map((b) -> new GeneratorRecipeWrapper(b, Generator.Type.BASALT, Blocks.AIR))
                        .sorted()
                        .collect(Collectors.toList()),
                basalt_generator.getUid());

        for (Generator gen : CobbleGenRandomizer.GENERATORS.CUSTOM_GENERATOR_LIST) {
            registration.addRecipes(gen.getBlockList().stream()
                    .map((b) -> new GeneratorRecipeWrapper(b, gen.getType(), gen.getBlock()))
                    .sorted()
                    .collect(Collectors.toList()), custom_generator.getUid());
        }

    }

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }
}
