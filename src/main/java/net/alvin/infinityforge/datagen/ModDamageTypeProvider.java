package net.alvin.infinityforge.datagen;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeProvider extends FabricDynamicRegistryProvider {
    public ModDamageTypeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        registries.getWrapperOrThrow(RegistryKeys.DAMAGE_TYPE).streamEntries()
                .filter(entry -> entry.getKey().isPresent()
                        && entry.getKey().get().getValue().getNamespace().equals(InfinityForge.MOD_ID))
                .forEach(entry -> entries.add(entry.getKey().get(), entry.value()));
    }

    @Override
    public String getName() {
        return "Damage Types";
    }
}
