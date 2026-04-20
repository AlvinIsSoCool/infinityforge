package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.InfinityStoneTypeRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItemRenderers {
    public static final InfinityStoneRenderer STONE_RENDERER = new InfinityStoneRenderer();

    public static void initialize() {
        for (InfinityStoneType stoneType : InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY) {
            Identifier typeId = InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stoneType);
            Identifier stoneId = typeId.withPath(typeId.getPath() + "_stone");
            Item item = Registries.ITEM.get(stoneId);
            System.out.println("Registering renderer for: " + stoneId + " item: " + item);
            BuiltinItemRendererRegistry.INSTANCE.register(item,
                    (stack, mode,
                     matrices, vertexConsumers,
                     light, overlay) -> STONE_RENDERER.render(stack, mode,
                            matrices, vertexConsumers,
                            light, overlay, stoneType));
        }
    }
}
