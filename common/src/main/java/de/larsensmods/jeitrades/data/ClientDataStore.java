package de.larsensmods.jeitrades.data;

import de.larsensmods.jeitrades.cache.DataCache;
import de.larsensmods.jeitrades.config.ConfigManager;

public final class ClientDataStore {

    public static VillagerTradeData VILLAGER_TRADE_DATA = null;
    public static BarteringData BARTERING_DATA = null;

    public static void storeVillagerTradeData(VillagerTradeData data){
        VILLAGER_TRADE_DATA = data;
        if(ConfigManager.getConfig().isCacheLastDataset()){
            DataCache.writeVillagerTradeData(data);
        }
    }

    public static void storeBarteringData(BarteringData data){
        BARTERING_DATA = data;
        if(ConfigManager.getConfig().isCacheLastDataset()){
            DataCache.writeBarteringData(data);
        }
    }

}
