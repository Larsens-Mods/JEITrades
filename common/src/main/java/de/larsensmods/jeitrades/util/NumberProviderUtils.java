package de.larsensmods.jeitrades.util;

import de.larsensmods.jeitrades.JEITradesMod;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class NumberProviderUtils {

    public static int minFromNumberProvider(NumberProvider provider){
        switch (provider) {
            case ConstantValue constant -> {
                return (int) constant.value();
            }
            case BinomialDistributionGenerator ignored -> {
                return 0;
            }
            case UniformGenerator uniform -> {
                return minFromNumberProvider(uniform.min());
            }
            default -> {
                JEITradesMod.LOG.warn("Found unsupported loot item function number provider type (min): {}", provider.getClass().getName());
                return Integer.MIN_VALUE;
            }
        }
    }

    public static int maxFromNumberProvider(NumberProvider provider){
        switch (provider) {
            case ConstantValue constant -> {
                return (int) constant.value();
            }
            case BinomialDistributionGenerator binomial -> {
                return maxFromNumberProvider(binomial.n());
            }
            case UniformGenerator uniform -> {
                return minFromNumberProvider(uniform.max());
            }
            default -> {
                JEITradesMod.LOG.warn("Found unsupported loot item function number provider type (max): {}", provider.getClass().getName());
                return Integer.MAX_VALUE;
            }
        }
    }

}
