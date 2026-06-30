package net.alvin.infinityforge.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> INFINITY_STONES = TagKey.of(
                RegistryKeys.ITEM,
                new Identifier("infinityforge", "infinity_stones")
        );
        public static final TagKey<Item> INFINITY_TESSERACTS = TagKey.of(
                RegistryKeys.ITEM,
                new Identifier("infinityforge", "infinity_tesseracts")
        );
        public static final TagKey<Item> INFINITY_ITEMS = TagKey.of(
                RegistryKeys.ITEM,
                new Identifier("infinityforge", "infinity_items")
        );
    }
}
