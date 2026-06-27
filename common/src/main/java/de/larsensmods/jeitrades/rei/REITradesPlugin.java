package de.larsensmods.jeitrades.rei;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.BarteringData;
import de.larsensmods.jeitrades.data.ClientDataStore;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

public class REITradesPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new VillagerTradesCategory());
        registry.add(new BarteringCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        JEITradesMod.LOG.info("Registering trades recipes");

        VillagerTradeData villagerTradeData = ClientDataStore.VILLAGER_TRADE_DATA;
        if(villagerTradeData != null) {
            JEITradesMod.LOG.info("Trades data has been registered: {}", villagerTradeData.villagerProfessions().size());
        }else{
            JEITradesMod.LOG.warn("Trades data has not been registered");
        }
        VillagerTradeTypeHelper.buildRecipes(villagerTradeData).forEach(registry::add);

        BarteringData barteringData = ClientDataStore.BARTERING_DATA;
        if(barteringData != null) {
            JEITradesMod.LOG.info("Bartering data has been registered: {}", barteringData.entries().size());
        }else{
            JEITradesMod.LOG.warn("Bartering data has not been registered");
        }
        BarteringTypeHelper.buildRecipes(barteringData).forEach(registry::add);
    }
}
