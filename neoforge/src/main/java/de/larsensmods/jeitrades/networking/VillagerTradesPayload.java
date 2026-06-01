package de.larsensmods.jeitrades.networking;

import de.larsensmods.jeitrades.data.VillagerTradeData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record VillagerTradesPayload(VillagerTradeData data) implements CustomPacketPayload {

    public static final Type<VillagerTradesPayload> TYPE = new Type<>(Channels.VILLAGER_TRADE_SYNC);

    public static final StreamCodec<ByteBuf, VillagerTradesPayload> STREAM_CODEC = StreamCodec.of(
            (byteBuf, payload) -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
                payload.data.writeTo(buf);
            },
            byteBuf -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
                return new VillagerTradesPayload(VillagerTradeData.readFrom(buf));
            }
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
