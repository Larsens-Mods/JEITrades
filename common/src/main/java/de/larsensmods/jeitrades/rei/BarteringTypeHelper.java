package de.larsensmods.jeitrades.rei;

import de.larsensmods.jeitrades.data.BarteringData;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BarteringTypeHelper implements Display {

    public static int totalWeight = 0;

    public static List<BarteringTypeHelper> buildRecipes(BarteringData data) {
        if(data == null){
            return List.of();
        }
        List<BarteringTypeHelper> recipes = new ArrayList<>();
        int newTotalWeight = 0;
        for(BarteringData.Entry entry : data.entries()){
            newTotalWeight += entry.weight();
            recipes.add(new BarteringTypeHelper(entry));
        }
        totalWeight = newTotalWeight;
        return recipes;
    }

    //Implementation

    public final BarteringData.Entry entry;

    public  BarteringTypeHelper(BarteringData.Entry entry) {
        this.entry = entry;
    }

    public List<ItemStack> getOutputs() {
        List<ItemStack> outputs = new ArrayList<>();

        ItemStack stack = entry.item().create();
        if(entry.potionType().isPresent() && stack.getItem() instanceof PotionItem){
            stack.set(DataComponents.POTION_CONTENTS, new PotionContents(entry.potionType().get()));
        }
        if(entry.enchantmentOptions().isPresent()){
            if(Minecraft.getInstance().level != null) {
                for (ResourceKey<Enchantment> enchantmentKey : entry.enchantmentOptions().get()) {
                    Optional<Holder.Reference<Enchantment>> optionalEnchantmentReference = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(enchantmentKey);
                    if (optionalEnchantmentReference.isPresent()) {
                        Holder.Reference<Enchantment> holder = optionalEnchantmentReference.get();
                        for (int level = holder.value().getMinLevel(); level <= holder.value().getMaxLevel(); level++) {
                            ItemStack cloneStack = stack.copy();
                            cloneStack.enchant(holder, level);
                            outputs.add(cloneStack);
                        }
                    }
                }
            }
        }

        if(outputs.isEmpty()){
            outputs.add(stack);
        }

        return outputs;
    }

    public Component buildChanceText() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Component text = Component.literal(decimalFormat.format(((double) entry.weight() / totalWeight) * 100) + "%");
        return text;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(EntryIngredients.of(Items.GOLD_INGOT));
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return getOutputs().stream().map(EntryIngredients::of).toList();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BarteringCategory.BARTERING;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.empty();
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
}
