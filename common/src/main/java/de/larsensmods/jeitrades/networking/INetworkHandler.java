package de.larsensmods.jeitrades.networking;

import de.larsensmods.jeitrades.data.BarteringData;
import de.larsensmods.jeitrades.data.VillagerTradeData;

public interface INetworkHandler {

    void setVillagerTradeData(VillagerTradeData villagerTradeData);

    void setBarteringData(BarteringData barteringData);

}
