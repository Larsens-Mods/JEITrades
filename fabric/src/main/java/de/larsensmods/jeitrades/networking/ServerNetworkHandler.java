package de.larsensmods.jeitrades.networking;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import net.fabricmc.fabric.api.networking.v1.FriendlyByteBufs;
import net.fabricmc.fabric.api.networking.v1.LoginPacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;
import net.minecraft.server.MinecraftServer;

public class ServerNetworkHandler implements INetworkHandler {

    private VillagerTradeData villagerTradeData;

    public ServerNetworkHandler(){
        ServerLoginNetworking.registerGlobalReceiver(Channels.VILLAGER_TRADE_SYNC, ((_, _, understood, _, _, _) -> {
            if(understood){
                JEITradesMod.LOG.info("Received villager trade sync response");
            }else{
                JEITradesMod.LOG.info("Client didn't understand villager trade sync");
            }
        }));
    }

    @Override
    public void setVillagerTradeData(VillagerTradeData villagerTradeData) {
        this.villagerTradeData = villagerTradeData;
    }

    public void sendData(ServerLoginPacketListener handler, MinecraftServer server, LoginPacketSender sender, ServerLoginNetworking.LoginSynchronizer synchronizer) {
        if(villagerTradeData != null){
            FriendlyByteBuf byteBuf = FriendlyByteBufs.create();
            villagerTradeData.writeTo(byteBuf);
            sender.sendPacket(Channels.VILLAGER_TRADE_SYNC, byteBuf);
            JEITradesMod.LOG.info("Sent villager trade sync packet");
        }else{
            JEITradesMod.LOG.warn("No villager trade data present on client connect");
        }
    }

}
