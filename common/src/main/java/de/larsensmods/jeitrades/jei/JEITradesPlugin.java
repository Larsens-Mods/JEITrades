package de.larsensmods.jeitrades.jei;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.BarteringData;
import de.larsensmods.jeitrades.data.ClientDataStore;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@JeiPlugin
public class JEITradesPlugin implements IModPlugin {
    /**
     * The unique ID for this mod plugin.
     * The namespace should be your mod's modId.
     */
    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(JEITradesMod.MOD_ID, "trades_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new VillagerTradesCategory(registration.getJeiHelpers()));
        registration.addRecipeCategories(new BarteringCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        JEITradesMod.LOG.info("Registering trades recipes");

        VillagerTradeData villagerTradeData = ClientDataStore.VILLAGER_TRADE_DATA;
        if(villagerTradeData != null) {
            JEITradesMod.LOG.info("Trades data has been registered: {}", villagerTradeData.villagerProfessions().size());
        }else{
            JEITradesMod.LOG.warn("Trades data has not been registered");
        }
        registration.addRecipes(VillagerTradeTypeHelper.RECIPE_TYPE, VillagerTradeTypeHelper.buildRecipes(villagerTradeData));

        BarteringData barteringData = ClientDataStore.BARTERING_DATA;
        if(barteringData != null) {
            JEITradesMod.LOG.info("Bartering data has been registered: {}", barteringData.entries().size());
        }else{
            JEITradesMod.LOG.warn("Bartering data has not been registered");
        }
        registration.addRecipes(BarteringTypeHelper.RECIPE_TYPE, BarteringTypeHelper.buildRecipes(barteringData));
    }

    @Override
    public void registerGuiHandlers(@NonNull IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }
}
