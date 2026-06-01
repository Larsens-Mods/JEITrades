package de.larsensmods.jeitrades.networking;

import de.larsensmods.jeitrades.data.VillagerTradeData;

public class ServerNetworkHandler implements INetworkHandler {

    public VillagerTradeData villagerTradeData;

    @Override
    public void setVillagerTradeData(VillagerTradeData villagerTradeData) {
        this.villagerTradeData = villagerTradeData;
    }
}
