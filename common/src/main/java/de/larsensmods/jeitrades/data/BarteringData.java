package de.larsensmods.jeitrades.data;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.*;

public record BarteringData(Set<Entry> entries) {

    public static BarteringData readFrom(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Set<Entry> entries = new HashSet<>(size);
        for (int i = 0; i < size; i++) {
            entries.add(Entry.readFrom(buf));
        }
        return new BarteringData(entries);
    }

    public void writeTo(FriendlyByteBuf buf) {
        buf.writeInt(entries.size());

        for (BarteringData.Entry entry : entries) {
            entry.writeTo(buf);
        }
    }

    public record Entry(int weight, ItemStackTemplate item, int minCount, int maxCount, Optional<Holder<Potion>> potionType, Optional<Set<ResourceKey<Enchantment>>> enchantmentOptions) {

        public static Entry readFrom(FriendlyByteBuf buf) {
            int weight = buf.readInt();
            ItemStackTemplate itemStackTemplate = buf.readLenientJsonWithCodec(ItemStackTemplate.CODEC);
            int minCount = buf.readInt();
            int maxCount = buf.readInt();
            Optional<Holder<Potion>> potionType = buf.readOptional(buf1 -> buf1.readLenientJsonWithCodec(Potion.CODEC));
            Optional<Set<ResourceKey<Enchantment>>> enchantmentOptions = buf.readOptional(buf1 -> {
                        int size = buf1.readInt();
                        Set<ResourceKey<Enchantment>> enchantments = new HashSet<>(size);
                        for (int i = 0; i < size; i++) {
                            enchantments.add(buf1.readResourceKey(Registries.ENCHANTMENT));
                        }
                        return enchantments;
                    });

            return new Entry(weight, itemStackTemplate, minCount, maxCount, potionType, enchantmentOptions);
        }

        public void writeTo(FriendlyByteBuf buf) {
            buf.writeInt(weight);
            buf.writeJsonWithCodec(ItemStackTemplate.CODEC, item);
            buf.writeInt(minCount);
            buf.writeInt(maxCount);
            buf.writeOptional(potionType, (buf1, potionHolder) -> buf1.writeJsonWithCodec(Potion.CODEC, potionHolder));
            buf.writeOptional(enchantmentOptions, (buf1, holderSet) -> {
                buf1.writeInt(holderSet.size());
                for (ResourceKey<Enchantment> enchantmentKey : holderSet) {
                    buf.writeResourceKey(enchantmentKey);
                }
            });
        }
    }

}
