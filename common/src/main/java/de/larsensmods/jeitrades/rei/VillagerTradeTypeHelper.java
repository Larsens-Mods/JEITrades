package de.larsensmods.jeitrades.rei;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import de.larsensmods.jeitrades.data.transformed.TransformedVillagerTrade;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VillagerTradeTypeHelper implements Display {

    public static List<VillagerTradeTypeHelper> buildRecipes(VillagerTradeData data) {
        JEITradesMod.LOG.info("Building VillagerTrades recipes");

        if(data == null) {
            JEITradesMod.LOG.warn("VillagerTrades data is null");
            return Collections.emptyList();
        }

        List<VillagerTradeTypeHelper> recipes = new ArrayList<>();

        //Wandering Trader
        ItemStackTemplate traderPoiBlock = ItemStackTemplate.fromNonEmptyStack(Items.LEAD.getDefaultInstance());
        Component traderProfession = Component.translatable("entity.minecraft.wandering_trader");
        //Buys
        for(TransformedVillagerTrade trade : data.wanderingBuy().trades()){
            recipes.add(new VillagerTradeTypeHelper(traderPoiBlock, trade.wants(), trade.additionalWants(), trade.gives(), Component.literal("B"), traderProfession, true));
        }
        //Sells Common
        for(TransformedVillagerTrade trade : data.wanderingSellCommon().trades()){
            recipes.add(new VillagerTradeTypeHelper(traderPoiBlock, trade.wants(), trade.additionalWants(), trade.gives(), Component.literal("C"), traderProfession, true));
        }
        //Sells Uncommon
        for(TransformedVillagerTrade trade : data.wanderingSellUncommon().trades()){
            recipes.add(new VillagerTradeTypeHelper(traderPoiBlock, trade.wants(), trade.additionalWants(), trade.gives(), Component.literal("U"), traderProfession, true));
        }

        //Professions
        for(VillagerTradeData.ProfessionData profession : data.villagerProfessions()){
            Component professionName = profession.name();
            ItemStackTemplate professionPoiBlock = profession.poiBlock();
            for(int level : profession.tradeSetsByLevel().keySet()){
                Identifier trade = profession.tradeSetsByLevel().get(level);
                if(data.villagerTradeSets().containsKey(trade)){
                    for(TransformedVillagerTrade tradeData : data.villagerTradeSets().get(trade).trades()){
                        recipes.add(new VillagerTradeTypeHelper(professionPoiBlock, tradeData.wants(), tradeData.additionalWants(), tradeData.gives(), Component.translatable("jeitrades.villager.level_" + level), professionName, false));
                    }
                }
            }
        }

        return recipes;
    }

    //Implementation

    public final ItemStackTemplate poiBlock, wants, gives;
    public final Optional<ItemStackTemplate> additionalWants;
    public final Component levelDisplay, professionName;
    public final boolean isTrader;

    public VillagerTradeTypeHelper(ItemStackTemplate poiBlock, ItemStackTemplate wants, Optional<ItemStackTemplate> additionalWants, ItemStackTemplate gives, Component levelDisplay, Component professionName, boolean isTrader) {
        this.poiBlock = poiBlock;
        this.wants = wants;
        this.additionalWants = additionalWants;
        this.gives = gives;
        this.levelDisplay = levelDisplay;
        this.professionName = professionName;
        this.isTrader = isTrader;
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.of(poiBlock.create()));
        inputs.add(EntryIngredients.of(wants.create()));
        additionalWants.ifPresent(itemStackTemplate -> inputs.add(EntryIngredients.of(itemStackTemplate.create())));

        if(isTrader){
            inputs.add(EntryIngredients.of(Items.WANDERING_TRADER_SPAWN_EGG));
        }else{
            inputs.add(EntryIngredients.of(Items.VILLAGER_SPAWN_EGG));
        }
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(EntryIngredients.of(gives.create()));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return VillagerTradesCategory.VILLAGER_TRADES;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return Optional.empty();
    }

    @Override
    public @Nullable DisplaySerializer<? extends Display> getSerializer() {
        return null;
    }
}
