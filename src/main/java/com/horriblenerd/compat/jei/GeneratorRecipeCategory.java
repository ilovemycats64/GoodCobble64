package com.horriblenerd.compat.jei;

import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by HorribleNerd on 12/11/2020
 */
public class GeneratorRecipeCategory extends GeneratorRecipeCategoryBase<GeneratorRecipeWrapper> {

    public final ResourceLocation UID;

    public GeneratorRecipeCategory(IGuiHelper guiHelper, Item icon, String name, ResourceLocation id, int size) {
        super(guiHelper, new ItemStack(icon), name, size);
        this.UID = id;
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends GeneratorRecipeWrapper> getRecipeClass() {
        return GeneratorRecipeWrapper.class;
    }

}
