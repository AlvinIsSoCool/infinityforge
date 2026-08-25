package net.alvin.infinityforge.client.render.item;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
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

    public static final String ITEM_RENDERER_REG_MSG = "Registering renderer for Item: {} ({})";

    @SuppressWarnings("LoggingSimilarMessage")
    public static void register() {
        for (InfinityStoneType stoneType : InfinityStoneTypeRegistry.REGISTRY) {
            String stoneName = InfinityStoneTypeRegistry.getStoneNameFromType(stoneType, " Stone", true);
            Identifier stoneId = InfinityStoneTypeRegistry.getStoneIdFromType(stoneType, "_stone");
            Item stoneItem = Registries.ITEM.get(stoneId);

            InfinityForge.LOGGER.info(ITEM_RENDERER_REG_MSG, stoneName, stoneId.toString());
            BuiltinItemRendererRegistry.INSTANCE.register(stoneItem,
                    (stack, mode,
                     matrices, vertexConsumers,
                     light, overlay) -> STONE_RENDERER.render(stack, mode,
                            matrices, vertexConsumers,
                            light, overlay, stoneType));

            String tesseractName = InfinityStoneTypeRegistry.getStoneNameFromType(stoneType, " Tesseract", true);
            Identifier tesseractId = InfinityStoneTypeRegistry.getStoneIdFromType(stoneType, "_tesseract");
            Item tesseractItem = Registries.ITEM.get(tesseractId);

            InfinityForge.LOGGER.info(ITEM_RENDERER_REG_MSG, tesseractName, tesseractId.toString());
            BuiltinItemRendererRegistry.INSTANCE.register(tesseractItem,
                    (stack, mode,
                     matrices, vertexConsumers,
                     light, overlay) -> TESSERACT_RENDERER.render(stack, mode,
                            matrices, vertexConsumers,
                            light, overlay, stoneType));
        }

        ModelLoadingPlugin.register(pluginContext ->
                pluginContext.addModels(InfinityGauntletRenderer.GAUNTLET_MODEL_2D));
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.INFINITY_GAUNTLET, GAUNTLET_RENDERER::render);
        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.FAKE_ITEM, FAKE_ITEM_RENDERER::render);
    }
}
