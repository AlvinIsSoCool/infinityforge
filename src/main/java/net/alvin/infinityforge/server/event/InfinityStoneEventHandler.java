package net.alvin.infinityforge.server.event;

import net.alvin.infinityforge.abilities.ModAbilities;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.ModStones;
import net.alvin.infinityforge.server.state.GauntletToggleState;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(InfinityStoneEventHandler::onAllowDamage);
        ServerLivingEntityEvents.ALLOW_DEATH.register(InfinityStoneEventHandler::onAllowDeath);
    }

    @SuppressWarnings("SameReturnValue")
    private static boolean onAllowDamage(LivingEntity entity, DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof ServerPlayerEntity player) {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return true;

            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            if (new HashSet<>(activeStones).containsAll(ModStones.ALL_STONES)) {
                if (entity.isBlocking()) {
                    ItemStack shield = entity.getActiveItem();
                    if (!shield.isEmpty()) {
                        shield.setDamage(shield.getMaxDamage());
                        if (entity.getActiveHand() == Hand.MAIN_HAND) {
                            entity.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                            entity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        } else {
                            entity.sendEquipmentBreakStatus(EquipmentSlot.OFFHAND);
                            entity.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                        }
                    }
                } else {
                    for (EquipmentSlot slot : new EquipmentSlot[]{
                            EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                            EquipmentSlot.LEGS, EquipmentSlot.FEET}
                    ) {
                        ItemStack armor = entity.getEquippedStack(slot);
                        if (!armor.isEmpty() && armor.isDamageable()) {
                            armor.setDamage(armor.getMaxDamage());
                            entity.sendEquipmentBreakStatus(slot);
                            entity.equipStack(slot, ItemStack.EMPTY);
                        }
                    }
                }

                entity.getDamageTracker().onDamage(source, entity.getHealth());
                entity.setHealth(0.0f);
                entity.onDeath(source);
            }
        }

        if (entity instanceof ServerPlayerEntity player)
            return !GauntletToggleState.isActive(player, ModAbilities.FORCEFIELD.getId());

        return true;
    }

    private static boolean onAllowDeath(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return true;
        if (!(entity instanceof ServerPlayerEntity player)) return true;

        ItemStack stack = InfinityGauntletItem.findGauntlet(player);
        if (stack == null) return true;

        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
        if (new HashSet<>(activeStones).containsAll(ModStones.ALL_STONES)) {
            player.setHealth(player.getMaxHealth());
            world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0f, 0.8f);
            return false;
        }

        return true;
    }
}
