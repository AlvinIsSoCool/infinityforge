package net.alvin.infinityforge.server.event;

import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.abilities.impl.space.ForcefieldAbility;
import net.alvin.infinityforge.infinity.abilities.impl.space.PhasingAbility;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.network.s2c.ClearAllClientStatesS2CPacket;
import net.alvin.infinityforge.registry.ModDamageSources;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import java.util.HashSet;
import java.util.List;

public class InfinityStoneEventHandler {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(ForcefieldAbility::onDamageEntity);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(PhasingAbility::onDamageEntity);

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(InfinityStoneEventHandler::onAllowDamage);
        ServerLivingEntityEvents.ALLOW_DEATH.register(InfinityStoneEventHandler::onAllowDeath);
        ServerLivingEntityEvents.AFTER_DEATH.register(InfinityStoneEventHandler::onAfterDeath);
    }

    public static void applyDamageInfinity(LivingEntity entity, DamageSource source, boolean breakArmor) {
        if (!entity.isAlive()) return;

        if (breakArmor) {
            if (entity.isBlocking()) {
                ItemStack shield = entity.getActiveItem();
                if (!shield.isEmpty()) {
                    shield.setDamage(shield.getMaxDamage());
                    EquipmentSlot slot = entity.getActiveHand() == Hand.MAIN_HAND
                            ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND;
                    entity.sendEquipmentBreakStatus(slot);
                    entity.equipStack(slot, ItemStack.EMPTY);
                }
            } else {
                for (EquipmentSlot slot : new EquipmentSlot[]{
                        EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                        EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                    ItemStack armor = entity.getEquippedStack(slot);
                    if (!armor.isEmpty() && armor.isDamageable()) {
                        armor.setDamage(armor.getMaxDamage());
                        entity.sendEquipmentBreakStatus(slot);
                        entity.equipStack(slot, ItemStack.EMPTY);
                    }
                }
            }
        }

        float previousHealth = entity.getHealth();
        boolean deathAllowed = ServerLivingEntityEvents.ALLOW_DEATH.invoker()
                .allowDeath(entity, source, previousHealth);
        if (!deathAllowed) return;

        entity.getDamageTracker().onDamage(source, previousHealth);
        entity.setHealth(0.0f);
        entity.playSound(entity.getDeathSound(), entity.getSoundVolume(), entity.getSoundPitch());
        entity.onDeath(source);
        ServerLivingEntityEvents.AFTER_DEATH.invoker().afterDeath(entity, source);
    }
    
    private static boolean onAllowDamage(LivingEntity entity, DamageSource source, float amount) {
        boolean isHoldingStone = entity.getMainHandStack().isOf(ModItems.POWER_STONE)
                || entity.getOffHandStack().isOf(ModItems.POWER_STONE);
        if (isHoldingStone) return false;

        Entity attacker = source.getAttacker();
        if (attacker instanceof ServerPlayerEntity player) {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack != null && InfinityGauntletItem.getAddedStones(stack).contains(ModStones.POWER)) {
                InfinityStoneEventHandler.applyDamageInfinity(entity,
                        ModDamageSources.powerStone(entity.getWorld()), true);
                return false;
            }
        }

        return true;
    }

    private static boolean onAllowDeath(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return true;
        if (!(entity instanceof ServerPlayerEntity player)) return true;

        ItemStack stack = InfinityGauntletItem.findGauntlet(player);
        if (stack == null) return true;

        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
        if (InfinityForgeConfig.get().godMode
                && new HashSet<>(activeStones).containsAll(ModStones.ALL_STONES)) {
            player.setHealth(player.getMaxHealth());
            world.playSound(null, player.getBlockPos(),
                    SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1f, 0.75f);
            return false;
        }

        return true;
    }

    private static void onAfterDeath(LivingEntity entity, DamageSource damageSource) {
        if (entity instanceof ServerPlayerEntity player) {
            GauntletConnectionEvents.cleanupPlayerAll(player);
            ServerPlayNetworking.send(player, new ClearAllClientStatesS2CPacket());
            GauntletConnectionEvents.populateAbilityDynamicIcons(player);
        }
    }
}
