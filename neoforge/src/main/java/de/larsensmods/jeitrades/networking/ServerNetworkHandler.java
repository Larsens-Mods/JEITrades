package de.larsensmods.jeitrades.networking;

import de.larsensmods.jeitrades.data.BarteringData;
import de.larsensmods.jeitrades.data.VillagerTradeData;

public class ServerNetworkHandler implements INetworkHandler {

    public VillagerTradeData villagerTradeData;
    public BarteringData barteringData;

    @Override
    public void setVillagerTradeData(VillagerTradeData villagerTradeData) {
        this.villagerTradeData = villagerTradeData;
    }

    @Override
    public void setBarteringData(BarteringData barteringData) {
        this.barteringData = barteringData;
    }
}
