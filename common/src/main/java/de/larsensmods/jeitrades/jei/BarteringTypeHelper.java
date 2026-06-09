package de.larsensmods.jeitrades.jei;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.BarteringData;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;

import java.text.DecimalFormat;
import java.util.*;

public class BarteringTypeHelper implements IRecipeCategoryExtension<BarteringTypeHelper> {

    public static final IRecipeType<BarteringTypeHelper> RECIPE_TYPE = IRecipeType.create(Identifier.fromNamespaceAndPath(JEITradesMod.MOD_ID, "bartering"), BarteringTypeHelper.class);

    public static int totalWeight = 0;

    public static List<BarteringTypeHelper> buildRecipes(BarteringData data) {
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

    @Override
    public void drawInfo(BarteringTypeHelper recipe, int recipeWidth, int recipeHeight, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        int textX = 35, textY = 13;

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(textX, textY);
        guiGraphics.pose().scale(1f, 1f);
        guiGraphics.text(Minecraft.getInstance().font, buildChanceText(), 0, 0, ARGB.opaque(8), false);
        guiGraphics.pose().popMatrix();
    }

    private Component buildChanceText() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        Component text = Component.literal(decimalFormat.format(((double) entry.weight() / totalWeight) * 100) + "%");
        return text;
    }

}
