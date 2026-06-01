package de.larsensmods.jeitrades.jei;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.ClientDataStore;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

@JeiPlugin
public class JEITradesPlugin implements IModPlugin {
    /**
     * The unique ID for this mod plugin.
     * The namespace should be your mod's modId.
     */
    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(JEITradesMod.MOD_ID, "trades_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new VillagerTradesCategory(registration.getJeiHelpers()));
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        //ClientEvents.playerJoinedWorld(Minecraft.getInstance().player);
        JEITradesMod.LOG.info("Registering trades recipes");
        VillagerTradeData data = ClientDataStore.VILLAGER_TRADE_DATA;
        if(data != null) {
            JEITradesMod.LOG.info("Trades data has been registered: {}", data.villagerProfessions().size());
        }else{
            JEITradesMod.LOG.warn("Trades data has not been registered");
        }

        registration.addRecipes(VillagerTradeTypeHelper.RECIPE_TYPE, VillagerTradeTypeHelper.buildRecipes(data));
    }

    @Override
    public void registerGuiHandlers(@NonNull IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }
}
