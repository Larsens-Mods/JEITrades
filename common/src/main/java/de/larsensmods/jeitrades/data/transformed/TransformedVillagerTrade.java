package de.larsensmods.jeitrades.data.transformed;

import de.larsensmods.jeitrades.JEITradesMod;
import de.larsensmods.jeitrades.mixin.VillagerTradeAccessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Optional;

public record TransformedVillagerTrade(ItemStackTemplate wants, Optional<ItemStackTemplate> additionalWants, ItemStackTemplate gives) {

    public void writeTo(FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(ItemStackTemplate.CODEC, wants);
        buf.writeOptional(additionalWants, (b, template) -> b.writeJsonWithCodec(ItemStackTemplate.CODEC, template));
        buf.writeJsonWithCodec(ItemStackTemplate.CODEC, gives);
    }

    public static TransformedVillagerTrade readFrom(FriendlyByteBuf buf) {
        ItemStackTemplate wants = buf.readLenientJsonWithCodec(ItemStackTemplate.CODEC);
        Optional<ItemStackTemplate> additionalWants = buf.readOptional(b -> b.readLenientJsonWithCodec(ItemStackTemplate.CODEC));
        ItemStackTemplate gives = buf.readLenientJsonWithCodec(ItemStackTemplate.CODEC);

        return new TransformedVillagerTrade(wants, additionalWants, gives);
    }

    public static TransformedVillagerTrade fromVillagerTrade(VillagerTrade trade) {
        VillagerTradeAccessor accessor = (VillagerTradeAccessor) trade;

        ItemStackTemplate wants;
        Optional<ItemStackTemplate> additionalWants;
        ItemStackTemplate gives;

        wants = templateFromTradeCost(accessor.jeitrades$wants());
        if(accessor.jeitrades$additionalWants().isPresent()){
            additionalWants = Optional.of(templateFromTradeCost(accessor.jeitrades$additionalWants().get()));
        }else{
            additionalWants = Optional.empty();
        }
        gives = accessor.jeitrades$gives();

        return new TransformedVillagerTrade(wants, additionalWants, gives);
    }

    private static ItemStackTemplate templateFromTradeCost(TradeCost cost){
        int count = 0;
        if(cost.count() instanceof ConstantValue(float value)){
            count = (int) value;
        }else{
            JEITradesMod.LOG.warn("Trade wants count is not a constant value, defaulting to 0");
        }
        JEITradesMod.LOG.debug("Parsed data: item={}, count={}", cost.item().getRegisteredName(), count);
        if(count == 0){
            JEITradesMod.LOG.warn("Trade Item {} wants count is zero, changing to 1", cost.item().getRegisteredName());
            count = 1;
        }
        return new ItemStackTemplate(cost.item(), count, DataComponentPatch.EMPTY);
    }

}
