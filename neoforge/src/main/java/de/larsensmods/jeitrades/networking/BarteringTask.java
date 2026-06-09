package de.larsensmods.jeitrades.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record BarteringTask(ServerNetworkHandler networkHandler, ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {

    public static final Type TYPE = new Type(Channels.BARTERING_SYNC);

    @Override
    public void run(@NonNull Consumer<CustomPacketPayload> consumer) {
        consumer.accept(new BarteringPayload(networkHandler.barteringData));
        listener.finishCurrentTask(type());
    }

    @Override
    public @NonNull Type type() {
        return TYPE;
    }
}
