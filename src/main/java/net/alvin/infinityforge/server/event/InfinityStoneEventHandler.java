package net.alvin.infinityforge.server.event;

import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.ModStones;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;

public class InfinityStoneEventHandler {
    public static void register() {
        AttackEntityCallback.EVENT.register(InfinityStoneEventHandler::onAttack);
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(InfinityStoneEventHandler::onAllowDamage);
        ServerLivingEntityEvents.ALLOW_DEATH.register(InfinityStoneEventHandler::onAllowDeath);
    }

    private static ActionResult onAttack(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;
        if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
        if (entity == player) return ActionResult.PASS;

        ItemStack stack = InfinityGauntletItem.findGauntlet(player);
        if (stack == null) return ActionResult.PASS;

        InfinityGauntletItem gauntletItem = (InfinityGauntletItem) stack.getItem();
        List<InfinityStoneType> activeStones = gauntletItem.getAddedStones(stack);

        if (new HashSet<>(activeStones).containsAll(ModStones.ALL_STONES)) {
            if (entity instanceof LivingEntity target) {
                if (target.isBlocking()) {
                    ItemStack shield = target.getActiveItem();
                    if (!shield.isEmpty()) {
                        shield.setDamage(shield.getMaxDamage());
                        if (target.getActiveHand() == Hand.MAIN_HAND) {
                            target.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                            target.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        } else {
                            target.sendEquipmentBreakStatus(EquipmentSlot.OFFHAND);
                            target.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                        }
                    }
                }

                else {
                    for (EquipmentSlot slot : new EquipmentSlot[]{
                            EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                            EquipmentSlot.LEGS, EquipmentSlot.FEET}
                    ) {
                        ItemStack armor = target.getEquippedStack(slot);
                        if (!armor.isEmpty() && armor.isDamageable()) {
                            armor.setDamage(armor.getMaxDamage());
                            target.sendEquipmentBreakStatus(slot);
                            target.equipStack(slot, ItemStack.EMPTY);
                        }
                    }
                }

                target.damage(world.getDamageSources().playerAttack(player), Float.MAX_VALUE);
                return ActionResult.SUCCESS;
            }
            if (entity instanceof EnderDragonPart part) {
                part.owner.setHealth(0f);
                part.owner.getPhaseManager().setPhase(PhaseType.DYING);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    private static boolean onAllowDamage(LivingEntity entity, DamageSource source, float amount) {
        return true;
    }

    private static boolean onAllowDeath(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity.getWorld() instanceof ServerWorld world)) return true;
        if (!(entity instanceof ServerPlayerEntity player)) return true;

        ItemStack stack = InfinityGauntletItem.findGauntlet(player);
        if (stack == null) return true;

        InfinityGauntletItem gauntletItem = (InfinityGauntletItem) stack.getItem();
        List<InfinityStoneType> activeStones = gauntletItem.getAddedStones(stack);

        if (new HashSet<>(activeStones).containsAll(ModStones.ALL_STONES)) {
            player.setHealth(player.getMaxHealth());
            world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 1.0f, 0.8f);
            return false;
        }

        return true;
    }
}
