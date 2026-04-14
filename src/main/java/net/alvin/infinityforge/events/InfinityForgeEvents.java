package net.alvin.infinityforge.events;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.render.InfinityStoneRenderer;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class InfinityForgeEvents {
    public static final InfinityStoneRenderer STONE_RENDERER = new InfinityStoneRenderer();

    public static void registerEventsCommon() {
        System.out.println("Registering common events for: " + InfinityForge.MOD_ID);
        InfinityStoneEvents.registerAllEvents();
    }

    public static void registerEventsClient() {
        System.out.println("Registering client events for: " + InfinityForge.MOD_ID);
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
