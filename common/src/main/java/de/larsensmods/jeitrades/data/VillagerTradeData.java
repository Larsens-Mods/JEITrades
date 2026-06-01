package de.larsensmods.jeitrades.data;

import de.larsensmods.jeitrades.data.transformed.TransformedTradeSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record VillagerTradeData(TransformedTradeSet wanderingBuy, TransformedTradeSet wanderingSellCommon, TransformedTradeSet wanderingSellUncommon,
                                Map<Identifier, TransformedTradeSet> villagerTradeSets,
                                Set<ProfessionData> villagerProfessions) {

    public static VillagerTradeData readFrom(FriendlyByteBuf buf) {
        TransformedTradeSet wanderingBuy = TransformedTradeSet.readFrom(buf);
        TransformedTradeSet wanderingSellCommon = TransformedTradeSet.readFrom(buf);
        TransformedTradeSet wanderingSellUncommon = TransformedTradeSet.readFrom(buf);

        Map<Identifier, TransformedTradeSet> villagerTradeSets = new HashMap<>();
        int size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            Identifier key = buf.readIdentifier();
            TransformedTradeSet value = TransformedTradeSet.readFrom(buf);
            villagerTradeSets.put(key, value);
        }

        Set<ProfessionData> villagerProfessions = new HashSet<>();
        size = buf.readInt();
        for (int i = 0; i < size; ++i) {
            villagerProfessions.add(ProfessionData.readFrom(buf));
        }

        return new VillagerTradeData(wanderingBuy, wanderingSellCommon, wanderingSellUncommon, villagerTradeSets, villagerProfessions);
    }

    public void writeTo(FriendlyByteBuf buf) {
        wanderingBuy.writeTo(buf);
        wanderingSellCommon.writeTo(buf);
        wanderingSellUncommon.writeTo(buf);

        buf.writeInt(villagerTradeSets.size());
        for (Map.Entry<Identifier, TransformedTradeSet> entry : villagerTradeSets.entrySet()) {
            buf.writeIdentifier(entry.getKey());
            entry.getValue().writeTo(buf);
        }

        buf.writeInt(villagerProfessions.size());
        for (ProfessionData professionData : villagerProfessions) {
            professionData.writeTo(buf);
        }
    }

    public record ProfessionData(Identifier id, ItemStackTemplate poiBlock, Component name,
                                 Int2ObjectMap<Identifier> tradeSetsByLevel) {

        static ProfessionData readFrom(FriendlyByteBuf byteBuf) {
            Identifier id = byteBuf.readIdentifier();
            ItemStackTemplate poiBlock = byteBuf.readLenientJsonWithCodec(ItemStackTemplate.CODEC);
            Component name = byteBuf.readLenientJsonWithCodec(ComponentSerialization.CODEC);

            Int2ObjectMap<Identifier> tradeSetsByLevel = new Int2ObjectOpenHashMap<>();
            int size = byteBuf.readInt();
            for (int i = 0; i < size; ++i) {
                int level = byteBuf.readInt();
                Identifier key = byteBuf.readIdentifier();
                tradeSetsByLevel.put(level, key);
            }

            return new ProfessionData(id, poiBlock, name, tradeSetsByLevel);
        }

        void writeTo(FriendlyByteBuf byteBuf) {
            byteBuf.writeIdentifier(this.id);
            byteBuf.writeJsonWithCodec(ItemStackTemplate.CODEC, this.poiBlock);
            byteBuf.writeJsonWithCodec(ComponentSerialization.CODEC, this.name);

            //Serialize Trade Sets
            byteBuf.writeInt(this.tradeSetsByLevel.size());
            for (int level : this.tradeSetsByLevel.keySet()) {
                byteBuf.writeInt(level);
                byteBuf.writeIdentifier(this.tradeSetsByLevel.get(level));
            }
        }

    }

}
