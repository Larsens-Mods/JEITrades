package de.larsensmods.jeitrades.mixin;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Optional;

@Mixin(VillagerTrade.class)
public interface VillagerTradeAccessor {

    @Accessor("wants")
    TradeCost jeitrades$wants();

    @Accessor("additionalWants")
    Optional<TradeCost> jeitrades$additionalWants();

    @Accessor("gives")
    ItemStackTemplate jeitrades$gives();

    @Accessor("merchantPredicate")
    Optional<LootItemCondition> jeitrades$merchantPredicate();

    @Accessor("givenItemModifiers")
    List<LootItemFunction> jeitrades$givenItemModifiers();

    @Accessor("maxUses")
    NumberProvider jeitrades$maxUses();

    @Accessor("reputationDiscount")
    NumberProvider jeitrades$reputationDiscount();

    @Accessor("xp")
    NumberProvider jeitrades$xp();

    @Accessor("doubleTradePriceEnchantments")
    Optional<HolderSet<Enchantment>> jeitrades$doubleTradePriceEnchantments();

}
