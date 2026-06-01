package de.larsensmods.jeitrades;

import de.larsensmods.jeitrades.data.ClientDataStore;
import de.larsensmods.jeitrades.networking.Channels;
import de.larsensmods.jeitrades.networking.ServerNetworkHandler;
import de.larsensmods.jeitrades.networking.VillagerTradesPayload;
import de.larsensmods.jeitrades.networking.VillagerTradesTask;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(JEITradesMod.MOD_ID)
public class JEITradesModNeoForge {

    public static ServerNetworkHandler networkHandler;

    public JEITradesModNeoForge(IEventBus eventBus) {
        networkHandler = new ServerNetworkHandler();

        JEITradesMod.init(networkHandler);
    }

    @EventBusSubscriber
    public static class ServerStartHandlerGameBus {

        static boolean loaded = false;

        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent event) {
            if(!loaded) {
                JEITradesMod.onWorldLoaded(event.getServer());
                loaded = true;
            }
        }

    }

    @EventBusSubscriber
    public static class ServerStartHandlerModBus {

        @SubscribeEvent
        public static void register(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar(Channels.PROTOCOL_VERSION).optional();
            registrar.configurationToClient(
                    VillagerTradesPayload.TYPE,
                    VillagerTradesPayload.STREAM_CODEC,
                    (payload, context) -> {
                        JEITradesMod.LOG.info("Received data sync packet");
                        ClientDataStore.VILLAGER_TRADE_DATA = payload.data();
                    }
            );
        }

        @SubscribeEvent
        public static void onConfigurationTaskRegister(RegisterConfigurationTasksEvent event){
            if(event.getListener().hasChannel(VillagerTradesPayload.TYPE)) {
                event.register(new VillagerTradesTask(networkHandler, event.getListener()));
            }
        }
    }

}