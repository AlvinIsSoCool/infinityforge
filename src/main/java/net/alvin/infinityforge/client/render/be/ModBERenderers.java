package net.alvin.infinityforge.client.render.be;

import net.alvin.infinityforge.block.entity.ModBlockEntities;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class ModBERenderers {
    public static void register() {
        BlockEntityRendererFactories.register(ModBlockEntities.FAKE_BLOCK_ENTITY,
                ctx -> new FakeBERenderer());
        BlockEntityRendererFactories.register(ModBlockEntities.BLUEPRINT_TABLE_BLOCK_ENTITY,
                ctx -> new BlueprintTableBERenderer());
    }
}
