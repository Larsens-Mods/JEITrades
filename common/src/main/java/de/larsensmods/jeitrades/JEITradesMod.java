package de.larsensmods.jeitrades;

import de.larsensmods.jeitrades.data.BarteringData;
import de.larsensmods.jeitrades.data.VillagerTradeData;
import de.larsensmods.jeitrades.data.transformed.TransformedTradeSet;
import de.larsensmods.jeitrades.mixin.*;
import de.larsensmods.jeitrades.networking.INetworkHandler;
import de.larsensmods.jeitrades.util.NumberProviderUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.TradeSets;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

public class JEITradesMod {

    public static final String MOD_ID = "jeitrades";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    private static INetworkHandler networkHandler;

    public static void init(INetworkHandler netHandler) {
        LOG.info("Initializing JEITradesMod");
        networkHandler = netHandler;
    }

    public static void onWorldLoaded(MinecraftServer server) {
        //Wandering Trader & Villager Trades
        final Set<Holder.Reference<PoiType>> poiTypes = new HashSet<>();
        server.registryAccess().lookup(Registries.POINT_OF_INTEREST_TYPE).ifPresent(registry -> buildPoiTypes(registry, poiTypes));

        final Set<VillagerTradeData.ProfessionData> professions = new HashSet<>();
        server.registryAccess().lookup(Registries.VILLAGER_PROFESSION).ifPresent(registry ->  buildVillagerProfessions(registry, poiTypes, professions));

        final Int2ObjectMap<TransformedTradeSet> wanderingTrades = new Int2ObjectArrayMap<>();
        final Map<Identifier, TransformedTradeSet> villagerTradeSets = new HashMap<>();
        server.reloadableRegistries().lookup().lookup(Registries.TRADE_SET).ifPresent(registry -> buildTradeSetData(registry, wanderingTrades, villagerTradeSets));

        VillagerTradeData data = new VillagerTradeData(wanderingTrades.get(1), wanderingTrades.get(2), wanderingTrades.get(3), villagerTradeSets, professions);

        networkHandler.setVillagerTradeData(data);

        //Piglin Bartering
        LootTable barteringTable = server.reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);

        BarteringData barteringData = buildBarteringData(barteringTable);

        networkHandler.setBarteringData(barteringData);
    }

    private static void buildPoiTypes(Registry<PoiType> registry, Set<Holder.Reference<PoiType>> outputSet) {
        LOG.debug("Building PoiTypes:");
        registry.listElements().forEach(poiType -> {
            LOG.debug("- Found PoiType: {}, {}", poiType.key().identifier(), poiType.value().matchingStates().toArray());
            outputSet.add(poiType);
        });
    }

    private static void buildVillagerProfessions(Registry<VillagerProfession> registry, Set<Holder.Reference<PoiType>> poiTypes, Set<VillagerTradeData.ProfessionData> outputSet) {
        LOG.debug("Villager Professions:");
        registry.listElements().forEach(professionRef -> {
            Component name = professionRef.value().name();
            Int2ObjectMap<ResourceKey<TradeSet>> tradeSets = professionRef.value().tradeSetsByLevel();
            Predicate<Holder<PoiType>> poiPredicate = professionRef.value().heldJobSite();
            Holder.Reference<PoiType> poiType = null;
            for(Holder.Reference<PoiType> typeRef : poiTypes) {
                if(poiPredicate.test(typeRef)){
                    poiType = typeRef;
                }
            }
            LOG.debug("- Found profession {}, {}, {}:", professionRef.key().identifier(), name, poiType != null ? poiType.value().matchingStates().toArray() : null);
            for(int i : tradeSets.keySet()){
                ResourceKey<TradeSet> tradeSet = tradeSets.get(i);
                LOG.debug("  - Level {}: {}", i, tradeSet != null ? tradeSet.identifier() : null);
            }

            if(tradeSets.isEmpty()){
                LOG.debug("  - No trade sets, continuing");
                return;
            }else if(poiType == null || poiType.value().matchingStates().isEmpty()){
                LOG.warn("{} has no poi type, continuing", professionRef.key().identifier());
                return;
            }

            Int2ObjectMap<Identifier> transformedTradeSets = new Int2ObjectArrayMap<>();
            for(int i : tradeSets.keySet()){
                transformedTradeSets.put(i, tradeSets.get(i).identifier());
            }

            ItemStackTemplate stackTemplate = ItemStackTemplate.fromNonEmptyStack(poiType.value().matchingStates().iterator().next().getBlock().asItem().getDefaultInstance());
            VillagerTradeData.ProfessionData professionData = new VillagerTradeData.ProfessionData(professionRef.key().identifier(), stackTemplate, name, transformedTradeSets);
            outputSet.add(professionData);
        });
    }

    private static void buildTradeSetData(HolderLookup.RegistryLookup<TradeSet> registry, Int2ObjectMap<TransformedTradeSet> wanderingTrades, Map<Identifier, TransformedTradeSet> villagerTradeSets) {
        LOG.debug("Trade set data:");
        registry.listElements().forEach(entryRef -> {
            LOG.debug("- Found trade set {}:", entryRef.key().identifier());
            entryRef.value().getTrades().forEach(trade -> {
                LOG.debug("  - {}: {}", trade.getRegisteredName(), trade.value());
            });
            if(entryRef.is(TradeSets.WANDERING_TRADER_BUYING)){
                wanderingTrades.put(1, TransformedTradeSet.fromTradeSet(entryRef.value()));
            }else if(entryRef.is(TradeSets.WANDERING_TRADER_COMMON)){
                wanderingTrades.put(2, TransformedTradeSet.fromTradeSet(entryRef.value()));
            }else if(entryRef.is(TradeSets.WANDERING_TRADER_UNCOMMON)){
                wanderingTrades.put(3, TransformedTradeSet.fromTradeSet(entryRef.value()));
            }else{
                villagerTradeSets.put(entryRef.key().identifier(), TransformedTradeSet.fromTradeSet(entryRef.value()));
            }
        });
    }

    private static BarteringData buildBarteringData(LootTable barteringLootTable){
        List<LootPoolEntryContainer> entries = ((LootPoolAccessor) ((LootTableAccessor) barteringLootTable).jeitrades$pools().getFirst()).jeitrades$entries();

        Set<BarteringData.Entry> barteringEntries = new HashSet<>();

        for(LootPoolEntryContainer entry : entries){
            if(entry instanceof LootItem lootItem){
                Holder<Item> itemHolder = ((LootItemAccessor) lootItem).jeitrades$item();
                int weight = ((LootPoolSingletonContainerAccessor) lootItem).jeitrades$weight();
                int minCount = 1, maxCount = 1;

                Optional<Holder<Potion>> potionType = Optional.empty();
                Optional<Set<ResourceKey<Enchantment>>> enchantmentOptions = Optional.empty();

                List<LootItemFunction> lootFunctions = ((LootPoolSingletonContainerAccessor) lootItem).jeitrades$functions();
                for(LootItemFunction function : lootFunctions){
                    if(function instanceof SetItemCountFunction itemCountFunction){
                        NumberProvider numberProvider = ((SetItemCountFunctionAccessor) itemCountFunction).jeitrades$count();
                        minCount = NumberProviderUtils.minFromNumberProvider(numberProvider);
                        maxCount = NumberProviderUtils.maxFromNumberProvider(numberProvider);
                    }else if(function instanceof SetPotionFunction potionFunction){
                        potionType = Optional.of(((SetPotionFunctionAccessor) potionFunction).jeitrades$potion());
                    }else if(function instanceof EnchantRandomlyFunction enchantRandomlyFunction){
                        Optional<HolderSet<Enchantment>> enchantmentHolderSet = ((EnchantRandomlyFunctionAcessor) enchantRandomlyFunction).jeitrades$options();
                        if(enchantmentHolderSet.isEmpty()){
                            continue;
                        }
                        Set<ResourceKey<Enchantment>> enchantmentSet = new HashSet<>();

                        for(Holder<Enchantment> enchantmentHolder : enchantmentHolderSet.get()){
                            if(enchantmentHolder.unwrapKey().isPresent()){
                                enchantmentSet.add(enchantmentHolder.unwrapKey().get());
                            }else{
                                JEITradesMod.LOG.warn("Could not unwrap key for enchantment {}", enchantmentHolder.getRegisteredName());
                            }
                        }

                        enchantmentOptions = Optional.of(enchantmentSet);
                    }else{
                        JEITradesMod.LOG.warn("Unhandled loot function {}", function.getClass().getSimpleName());
                    }
                }
                barteringEntries.add(new BarteringData.Entry(weight, ItemStackTemplate.fromNonEmptyStack(itemHolder.value().getDefaultInstance()), minCount, maxCount, potionType, enchantmentOptions));
            }else{
                JEITradesMod.LOG.warn("Encountered unknown loot table entry container type {}", entry.getClass().getSimpleName());
            }
        }

        return new BarteringData(barteringEntries);
    }
}