package net.alvin.infinityforge;

import net.alvin.infinityforge.client.render.InfinityStoneRenderer;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class InfinityForgeClient implements ClientModInitializer {
    public static final InfinityStoneRenderer STONE_RENDERER = new InfinityStoneRenderer();

    @Override
    public void onInitializeClient() {
        for (InfinityStoneType stoneType : InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY) {
            Identifier id = InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stoneType);
            Item item = Registries.ITEM.get(id);
            System.out.println("Registering renderer for: " + id + " item: " + item);
            BuiltinItemRendererRegistry.INSTANCE.register(item,
                    (stack, mode, matrices, vertexConsumers, light, overlay) -> STONE_RENDERER.render(stack, mode, matrices, vertexConsumers, light, overlay, stoneType));
        }
    }
}
