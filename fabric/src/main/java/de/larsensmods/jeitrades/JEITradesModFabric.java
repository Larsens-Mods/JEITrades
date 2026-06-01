package de.larsensmods.jeitrades;

import net.fabricmc.api.ModInitializer;

public class JEITradesModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        JEITradesMod.init();
    }
}
