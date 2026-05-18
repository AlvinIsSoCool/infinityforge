package net.alvin.infinityforge.datagen;

import net.alvin.infinityforge.registry.ModBlocks;
import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public static final Model BUILTIN_ENTITY = new Model(
            Optional.of(new Identifier("builtin/entity")), Optional.empty()
    );

    public ModModelProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.TITANIUM_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_TITANIUM_ORE);
        blockStateModelGenerator.registerSimpleState(ModBlocks.BLUEPRINT_TABLE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.POWER_STONE, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.SPACE_STONE, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.REALITY_STONE, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.SOUL_STONE, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.MIND_STONE, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.TIME_STONE, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.POWER_TESSERACT, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.SPACE_TESSERACT, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.REALITY_TESSERACT, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.SOUL_TESSERACT, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.MIND_TESSERACT, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.TIME_TESSERACT, BUILTIN_ENTITY);
        itemModelGenerator.register(ModItems.FAKE_ITEM, BUILTIN_ENTITY);

        itemModelGenerator.register(ModItems.RAW_TITANIUM, Models.GENERATED);
        itemModelGenerator.register(ModItems.TITANIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLD_TITANIUM_ALLOY_INGOT, Models.GENERATED);

        Models.GENERATED.upload(
                new Identifier("infinityforge", "item/blueprint"),
                TextureMap.layer0(new Identifier("minecraft", "item/paper")),
                itemModelGenerator.writer
        );

        Models.GENERATED.upload(
                new Identifier("infinityforge", "item/infinity_gauntlet_2d"),
                TextureMap.layer0(new Identifier("infinityforge", "item/infinity_gauntlet_2d")),
                itemModelGenerator.writer
        );
    }
}
