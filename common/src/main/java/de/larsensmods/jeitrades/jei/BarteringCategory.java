package de.larsensmods.jeitrades.jei;

import de.larsensmods.jeitrades.JEITradesMod;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class BarteringCategory implements IRecipeCategory<BarteringTypeHelper> {

    private final IJeiHelpers helpers;
    private final IDrawable background;

    public BarteringCategory(IJeiHelpers helpers) {
        this.helpers = helpers;
        this.background = new BGDrawable(Identifier.fromNamespaceAndPath(JEITradesMod.MOD_ID, "textures/gui/category_bartering_bg.png"), 138, 26);
    }

    @Override
    public @NonNull IRecipeType<BarteringTypeHelper> getRecipeType() {
        return BarteringTypeHelper.RECIPE_TYPE;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("jeitrades.bartering.title");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.helpers.getGuiHelper().createDrawableItemLike(Items.GOLD_INGOT);
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BarteringTypeHelper recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(Items.GOLD_INGOT);

        builder.addOutputSlot(10, 10).addItemStacks(recipe.getOutputs()).addRichTooltipCallback((_, tooltip) -> {
            if(recipe.entry.minCount() != recipe.entry.maxCount()){
                tooltip.add(Component.translatable("jeitrades.bartering.count_range", recipe.entry.minCount(), recipe.entry.maxCount()));
            } else if(recipe.entry.minCount() != 1) {
                tooltip.add(Component.translatable("jeitrades.bartering.count", recipe.entry.minCount()));
            }
        });
    }

    @Override
    public void draw(BarteringTypeHelper recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        //Draw Background
        this.background.draw(guiGraphics, 0, 0);

        //Draw Info
        recipe.drawInfo(recipe, getWidth(), getHeight(), guiGraphics, mouseX, mouseY);
    }

}
