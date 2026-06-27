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

public class BarteringCategory implements DisplayCategory<BarteringTypeHelper> {

    public static final CategoryIdentifier<BarteringTypeHelper> BARTERING = CategoryIdentifier.of(JEITradesMod.MOD_ID, "bartering");

    @Override
    public CategoryIdentifier<? extends BarteringTypeHelper> getCategoryIdentifier() {
        return BARTERING;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("jeitrades.bartering.title");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Items.GOLD_INGOT);
    }

    @Override
    public int getDisplayHeight() {
        return 36;
    }

    @Override
    public List<Widget> setupDisplay(BarteringTypeHelper display, Rectangle bounds) {
        Point startingPoint = new Point(bounds.x, bounds.y);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(startingPoint.x + 10, startingPoint.y + 10)).entry(EntryStacks.of(Items.GOLD_INGOT)).markInput());

        widgets.add(Widgets.createResultSlotBackground(new Point(startingPoint.x + 44, startingPoint.y + 10)));
        widgets.add(Widgets.createSlot(new Point(startingPoint.x + 44, startingPoint.y + 10)).disableBackground().entries(display.getOutputs().stream().map(EntryStacks::of).peek(entry -> {
            if(display.entry.minCount() != display.entry.maxCount()){
                entry.tooltip(Component.translatable("jeitrades.bartering.count_range", display.entry.minCount(), display.entry.maxCount()));
            }else if(display.entry.minCount() != 1){
                entry.tooltip(Component.translatable("jeitrades.bartering.count", display.entry.minCount()));
            }
        }).toList()).markOutput());

        widgets.add(Widgets.createLabel(new Point(startingPoint.x + 85, startingPoint.y + 13), display.buildChanceText()));

        return widgets;
    }

}
