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

public class VillagerTradesCategory implements IRecipeCategory<VillagerTradeTypeHelper> {

    private final IJeiHelpers helpers;
    private final IDrawable background;

    public VillagerTradesCategory(IJeiHelpers helpers) {
        this.helpers = helpers;
        this.background = new BGDrawable(Identifier.fromNamespaceAndPath(JEITradesMod.MOD_ID, "textures/gui/category_villager_bg.png"), 138, 26);
    }

    @Override
    public @NonNull IRecipeType<VillagerTradeTypeHelper> getRecipeType() {
        return VillagerTradeTypeHelper.RECIPE_TYPE;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("jeitrades.villager.title");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.helpers.getGuiHelper().createDrawableItemLike(Items.EMERALD);
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
    public void setRecipe(IRecipeLayoutBuilder builder, VillagerTradeTypeHelper recipe, IFocusGroup focuses) {
        builder.addInputSlot(6, 9).addItemStacks(List.of(recipe.poiBlock.create())).addRichTooltipCallback((_, tooltip) -> {
            tooltip.add(recipe.professionName);
        });

        builder.addInputSlot(40, 9).addItemStacks(List.of(recipe.wants.create()));
        recipe.additionalWants.ifPresent(itemStackTemplate -> builder.addInputSlot(64, 9).addItemStacks(List.of(itemStackTemplate.create())));

        builder.addOutputSlot(122, 10).addItemStacks(List.of(recipe.gives.create()));

        if(recipe.isTrader){
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(Items.WANDERING_TRADER_SPAWN_EGG);
        }else{
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(Items.VILLAGER_SPAWN_EGG);
        }
    }

    @Override
    public void draw(VillagerTradeTypeHelper recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        //Draw Background
        this.background.draw(guiGraphics, 0, 0);

        //Draw Info
        recipe.drawInfo(recipe, getWidth(), getHeight(), guiGraphics, mouseX, mouseY);
    }
}
