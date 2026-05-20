package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.block.entity.BlueprintTableBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<BlueprintTableBlockEntity> BLUEPRINT_TABLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(InfinityForge.MOD_ID, "blueprint_table_be"),
                    FabricBlockEntityTypeBuilder.create(BlueprintTableBlockEntity::new, ModBlocks.BLUEPRINT_TABLE).build());

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering Block Entities for: {}", InfinityForge.MOD_ID);
    }
}
