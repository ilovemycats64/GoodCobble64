package com.horriblenerd.compat.jei;

import com.horriblenerd.cobblegenrandomizer.util.Generator;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

/**
 * Created by HorribleNerd on 12/11/2020
 */
public abstract class GeneratorRecipeCategoryBase<T extends GeneratorRecipeWrapper> implements IRecipeCategory<T> {

    private final IDrawableStatic background;
    private final String localizedName;
    private final IDrawable icon;
    private final ItemStack iconStack;
    private final int size;

    public GeneratorRecipeCategoryBase(IGuiHelper guiHelper, ItemStack iconStack, String localizedName, int size) {
        this.size = size;
        if (this.size == 0) {
            background = guiHelper.createBlankDrawable(52, 27);
        }
        else if (this.size == 1) {
            background = guiHelper.createBlankDrawable(52, 45);
        }
        else {
            background = guiHelper.createBlankDrawable(52, 63);
        }
        this.localizedName = localizedName;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, iconStack);
        this.iconStack = iconStack;
    }

    @Nonnull
    @Override
    public abstract Class<? extends T> getRecipeClass();

    @Nonnull
    @Override
    public Component getTitle() {
        return new TextComponent(localizedName);
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(T recipe, IIngredients ingredients) {
        ItemStack stack = new ItemStack(recipe.weightedBlock.getBlock());
        ingredients.setOutput(VanillaTypes.ITEM, stack);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull T recipeWrapper, @Nonnull IIngredients ingredients) {
        final IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        final IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

        int offset = 18;
        int startingX = 0;
        int startingY = 0;

        if (recipeWrapper.type == Generator.Type.COBBLESTONE) {
            if (this.size != 0) {
                startingY += 18;
            }
            // Left - Water
            fluidStacks.init(0, false, startingX, startingY);
            fluidStacks.set(0, new FluidStack(Fluids.WATER, 1000));
            // Right - Lava
            fluidStacks.init(1, false, startingX + 2 * offset, startingY);
            fluidStacks.set(1, new FluidStack(Fluids.LAVA, 1000));
            // Middle - Result
            itemStacks.init(2, false, startingX + offset - 1, startingY - 1);
            itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
            // Bottom - Catalyst
            if (recipeWrapper.catalyst != Blocks.AIR) {
                itemStacks.init(3, false, startingX + offset - 1, startingY + offset - 1);
                itemStacks.set(3, new ItemStack(recipeWrapper.catalyst));
            }
        }
        else if (recipeWrapper.type == Generator.Type.STONE) {
            // Top - Lava
            fluidStacks.init(0, false, startingX + offset, startingY);
            fluidStacks.set(0, new FluidStack(Fluids.LAVA, 1000));
            // Left - Water
            fluidStacks.init(1, false, startingX, startingY + offset);
            fluidStacks.set(1, new FluidStack(Fluids.WATER, 1000));
            // Middle - Result
            itemStacks.init(2, false, startingX + offset - 1, startingY + offset - 1);
            itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
            // Bottom - Catalyst
            if (recipeWrapper.catalyst != Blocks.AIR) {
                itemStacks.init(3, false, startingX + offset - 1, startingY + 2 * offset - 1);
                itemStacks.set(3, new ItemStack(recipeWrapper.catalyst));
            }
        }
        else if (recipeWrapper.type == Generator.Type.BASALT) {
            // Left - Lava
            fluidStacks.init(0, false, startingX, startingY);
            fluidStacks.set(0, new FluidStack(Fluids.LAVA, 1000));
            // Right - Blue Ice
            itemStacks.init(1, false, startingX + 2 * offset, startingY - 1);
            itemStacks.set(1, new ItemStack(Items.BLUE_ICE));
            // Middle - Result
            itemStacks.init(2, false, startingX + offset - 1, startingY - 1);
            itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
            // Below - Soul Soil
            itemStacks.init(3, false, startingX + offset - 1, startingY + offset - 1);
            itemStacks.set(3, new ItemStack(Items.SOUL_SOIL));
        }

    }

    public void draw(T recipe, PoseStack stack, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        TextComponent weightString = new TextComponent(String.format("%s: %d", I18n.get("cobblegenrandomizer.jei.weight"), recipe.weightedBlock.getWeight().asInt()));
        int y = (1 + this.size) * 18;
        minecraft.font.draw(stack, weightString, 2, y, 0xFF808080);
    }

}
