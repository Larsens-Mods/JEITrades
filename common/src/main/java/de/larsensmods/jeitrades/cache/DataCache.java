package de.larsensmods.jeitrades.cache;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.BarteringData;
import de.larsensmods.jeitrades.data.ClientDataStore;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DataCache {

    public static File villagerTradeDataFile = new File("./config/jeitrades-villager-cache.dat");
    public static File barteringDataFile = new File("./config/jeitrades-bartering-cache.dat");

    public static void writeVillagerTradeData(VillagerTradeData data){
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        data.writeTo(byteBuf);
        try {
            Files.write(villagerTradeDataFile.toPath(), byteBuf.array());
        } catch (IOException e) {
            JEITradesMod.LOG.error("Unable to write cache file.", e);
        }
    }

    public static void writeBarteringData(BarteringData data){
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        data.writeTo(byteBuf);
        try {
            Files.write(barteringDataFile.toPath(), byteBuf.array());
        } catch (IOException e) {
            JEITradesMod.LOG.error("Unable to write cache file.", e);
        }
    }

    public static void loadIfAvailable(){
        if(villagerTradeDataFile.exists()){
            try {
                byte[] bytes = Files.readAllBytes(villagerTradeDataFile.toPath());
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.copiedBuffer(bytes));
                ClientDataStore.VILLAGER_TRADE_DATA = VillagerTradeData.readFrom(buf);
                JEITradesMod.LOG.info("Read trades data from cache file.");
            }catch (IOException e){
                JEITradesMod.LOG.error("Unable to read from existing cache file.", e);
            }
        }
        if(barteringDataFile.exists()){
            try {
                byte[] bytes = Files.readAllBytes(barteringDataFile.toPath());
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.copiedBuffer(bytes));
                ClientDataStore.BARTERING_DATA = BarteringData.readFrom(buf);
                JEITradesMod.LOG.info("Read bartering data from cache file.");
            }catch (IOException e){
                JEITradesMod.LOG.error("Unable to read from existing cache file.", e);
            }
        }
    }

}
