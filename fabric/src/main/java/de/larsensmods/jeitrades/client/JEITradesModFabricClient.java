package de.larsensmods.jeitrades.client;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.ClientDataStore;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import de.larsensmods.jeitrades.networking.Channels;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;

import java.util.concurrent.CompletableFuture;

public final class JEITradesModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientLoginNetworking.registerGlobalReceiver(Channels.VILLAGER_TRADE_SYNC, (_, _, buf, _) -> {
            JEITradesMod.LOG.info("Received villager trade sync packet");
            ClientDataStore.VILLAGER_TRADE_DATA = VillagerTradeData.readFrom(buf);
            return CompletableFuture.completedFuture(FriendlyByteBufs.empty());
        });
    }
}
