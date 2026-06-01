package de.larsensmods.jeitrades.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.server.network.ConfigurationTask;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public record VillagerTradesTask(ServerNetworkHandler networkHandler, ServerConfigurationPacketListener listener) implements ICustomConfigurationTask {

    public static final ConfigurationTask.Type TYPE = new Type(Channels.VILLAGER_TRADE_SYNC);

    @Override
    public void run(@NonNull Consumer<CustomPacketPayload> consumer) {
        consumer.accept(new VillagerTradesPayload(networkHandler.villagerTradeData));
        listener.finishCurrentTask(type());
    }

    @Override
    public @NonNull Type type() {
        return TYPE;
    }
}
