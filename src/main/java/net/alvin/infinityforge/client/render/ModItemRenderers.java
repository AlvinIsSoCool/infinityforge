package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ModItemRenderers {
    public static final InfinityStoneRenderer STONE_RENDERER = new InfinityStoneRenderer();
    public static final InfinityTesseractRenderer TESSERACT_RENDERER = new InfinityTesseractRenderer();
    public static final InfinityGauntletRenderer GAUNTLET_RENDERER = new InfinityGauntletRenderer();
    public static final FakeItemRenderer FAKE_ITEM_RENDERER = new FakeItemRenderer();

    public static void register() {
        for (InfinityStoneType stoneType : InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY) {
            Identifier typeId = InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stoneType);

            Identifier stoneId = typeId.withPath(typeId.getPath() + "_stone");
            Item stone = Registries.ITEM.get(stoneId);
            InfinityForge.LOGGER.info("Registering renderer for: " + stoneId + " item: " + stone);
            BuiltinItemRendererRegistry.INSTANCE.register(stone,
                    (stack, mode,
                     matrices, vertexConsumers,
                     light, overlay) -> STONE_RENDERER.render(stack, mode,
                            matrices, vertexConsumers,
                            light, overlay, stoneType));

            Identifier tesseractId = typeId.withPath(typeId.getPath() + "_tesseract");
            Item tesseract = Registries.ITEM.get(tesseractId);
            InfinityForge.LOGGER.info("Registering renderer for: " + tesseractId + " item: " + tesseract);
            BuiltinItemRendererRegistry.INSTANCE.register(tesseract,
                    (stack, mode,
                     matrices, vertexConsumers,
                     light, overlay) -> TESSERACT_RENDERER.render(stack, mode,
                            matrices, vertexConsumers,
                            light, overlay, stoneType));
        }

        ModelLoadingPlugin.register(pluginContext -> pluginContext.addModels(InfinityGauntletRenderer.GAUNTLET_MODEL_2D));
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.INFINITY_GAUNTLET, GAUNTLET_RENDERER::render);

        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.FAKE_ITEM, FAKE_ITEM_RENDERER::render);
    }
}
