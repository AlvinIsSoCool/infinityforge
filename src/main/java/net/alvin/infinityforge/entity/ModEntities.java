package net.alvin.infinityforge.entity;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<PortalEntity> PORTAL_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(InfinityForge.MOD_ID, "portal_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, PortalEntity::new)
                    .dimensions(EntityDimensions.fixed(2f, 3f))
                    .build()
    );
    public static final EntityType<BlackHoleEntity> BLACKHOLE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(InfinityForge.MOD_ID, "blackhole_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlackHoleEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .build()
    );

    public static void initialise() {
        InfinityForge.LOGGER.info("Registering Entities for: {}", InfinityForge.MOD_ID);
    }
}
