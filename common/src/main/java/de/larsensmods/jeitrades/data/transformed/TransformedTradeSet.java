package de.larsensmods.jeitrades.data.transformed;

import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.HashSet;
import java.util.Set;

public record TransformedTradeSet(Set<TransformedVillagerTrade> trades) {

    public void writeTo(FriendlyByteBuf buf) {
        buf.writeInt(trades.size());
        for (TransformedVillagerTrade trade : trades) {
            trade.writeTo(buf);
        }
    }

    public static TransformedTradeSet readFrom(FriendlyByteBuf buf) {
        Set<TransformedVillagerTrade> trades = new HashSet<>();
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            trades.add(TransformedVillagerTrade.readFrom(buf));
        }
        return new TransformedTradeSet(trades);
    }

    public static TransformedTradeSet fromTradeSet(TradeSet tradeSet) {
        Set<TransformedVillagerTrade> trades = new HashSet<>();
        for(Holder<VillagerTrade> trade : tradeSet.getTrades()) {
            trades.add(TransformedVillagerTrade.fromVillagerTrade(trade.value()));
        }
        return new TransformedTradeSet(trades);
    }

}
