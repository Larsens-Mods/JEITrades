package de.larsensmods.jeitrades;

import de.larsensmods.jeitrades.networking.ServerNetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.NonNull;

public class JEITradesModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ServerNetworkHandler networkHandler = new ServerNetworkHandler();

        ServerLoginConnectionEvents.QUERY_START.register(networkHandler::sendData);

        JEITradesMod.init(networkHandler);

        ServerLevelEvents.LOAD.register(new ServerLevelEvents.Load() {
            boolean loaded = false;

            @Override
            public void onLevelLoad(@NonNull MinecraftServer server, @NonNull ServerLevel level) {
                if(!loaded) {
                    JEITradesMod.onWorldLoaded(server);
                    loaded = true;
                }
            }
        });
    }
}
