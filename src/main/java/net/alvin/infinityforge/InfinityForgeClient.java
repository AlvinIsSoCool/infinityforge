package net.alvin.infinityforge;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class InfinityForgeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        for (InfinityStoneType stoneType : InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY) {
            Identifier id = InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stoneType);
            Item item = Registries.ITEM.get(id);
            System.out.println("Registering color provider for: " + id + " item: " + item);
            ColorProviderRegistry.ITEM.register(
                    (stack, tintIndex) -> tintIndex == 0 ? stoneType.getBaseColor() : stoneType.getGlintColor(),
                    item
            );
        }
    }
}
