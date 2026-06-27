package de.larsensmods.jeitrades.rei;

import de.larsensmods.jeitrades.JEITradesMod;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VillagerTradesCategory implements DisplayCategory<VillagerTradeTypeHelper> {

    public static final CategoryIdentifier<VillagerTradeTypeHelper> VILLAGER_TRADES = CategoryIdentifier.of(JEITradesMod.MOD_ID, "villager_trades");

    @Override
    public CategoryIdentifier<? extends VillagerTradeTypeHelper> getCategoryIdentifier() {
        return VILLAGER_TRADES;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("jeitrades.villager.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.EMERALD);
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public List<Widget> setupDisplay(VillagerTradeTypeHelper display, Rectangle bounds) {
        Point startingPoint = new Point(bounds.x, bounds.y);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(startingPoint.x + 10, startingPoint.y + 10)).entry(EntryStacks.of(display.poiBlock.create()).tooltip(display.professionName)).markInput());

        widgets.add(Widgets.createSlot(new Point(startingPoint.x + 44, startingPoint.y + 10)).entry(EntryStacks.of(display.wants.create())).markInput());
        display.additionalWants.ifPresent(itemStackTemplate -> widgets.add(Widgets.createSlot(new Point(startingPoint.x + 68, startingPoint.y + 10)).entry(EntryStacks.of(itemStackTemplate.create())).markInput()));

        widgets.add(Widgets.createResultSlotBackground(new Point(startingPoint.x + 100, startingPoint.y + 10)));
        widgets.add(Widgets.createSlot(new Point(startingPoint.x + 100, startingPoint.y + 10)).disableBackground().entry(EntryStacks.of(display.gives.create())).markOutput());

        widgets.add(Widgets.createArrow(new Point(startingPoint.x + 70, startingPoint.y + 9)));
        widgets.add(Widgets.createLabel(new Point(startingPoint.x + 34, startingPoint.y + 13), display.levelDisplay));

        /*if(recipe.isTrader){
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(Items.WANDERING_TRADER_SPAWN_EGG);
        }else{
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).add(Items.VILLAGER_SPAWN_EGG);
        }*/

        return widgets;
    }
}
