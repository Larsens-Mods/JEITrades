package de.larsensmods.jeitrades;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(JEITradesMod.MOD_ID)
public class JEITradesModNeoForge {

    public JEITradesModNeoForge(IEventBus eventBus) {
        JEITradesMod.init();
    }
}