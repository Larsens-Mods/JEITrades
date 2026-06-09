package de.larsensmods.jeitrades.networking;

import de.larsensmods.jeitrades.data.BarteringData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record BarteringPayload(BarteringData data) implements CustomPacketPayload {

    public static final Type<BarteringPayload> TYPE = new Type<>(Channels.BARTERING_SYNC);

    public static final StreamCodec<ByteBuf, BarteringPayload> STREAM_CODEC = StreamCodec.of(
            (byteBuf, payload) -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
                payload.data.writeTo(buf);
            },
            byteBuf -> {
                FriendlyByteBuf buf = new FriendlyByteBuf(byteBuf);
                return new BarteringPayload(BarteringData.readFrom(buf));
            }
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
