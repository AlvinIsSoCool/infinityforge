package net.alvin.infinityforge.server.packet;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.abilities.base.HeldAbility;
import net.alvin.infinityforge.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityTesseractItem;
import net.alvin.infinityforge.network.c2s.PickupInfinityItemC2SPacket;
import net.alvin.infinityforge.server.state.GauntletCooldownState;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.alvin.infinityforge.server.state.GauntletToggleState;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.network.c2s.GauntletAbilityC2SPacket;
import net.alvin.infinityforge.network.c2s.GauntletHeldC2SPacket;
import net.alvin.infinityforge.network.c2s.GauntletToggleC2SPacket;
import net.alvin.infinityforge.network.s2c.SyncCooldownS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncToggleStateS2CPacket;
import net.alvin.infinityforge.server.state.PendingInfinityItemPickups;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class GauntletPacketHandlers {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletAbilityC2SPacket.TYPE, GauntletPacketHandlers::onAbilityPacket);
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletToggleC2SPacket.TYPE, GauntletPacketHandlers::onTogglePacket);
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletHeldC2SPacket.TYPE, GauntletPacketHandlers::onHeldPacket);
        ServerPlayNetworking.registerGlobalReceiver(
                PickupInfinityItemC2SPacket.TYPE, GauntletPacketHandlers::onPickupInfinityItem);
    }

    private static void onAbilityPacket(GauntletAbilityC2SPacket packet,
                                        ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            ActiveAbility ability = InfinityGauntletItem.findAbility(InfinityGauntletItem.getActiveAbilities(activeStones), packet.abilityId());
            if (ability == null) return;
            if (GauntletCooldownState.isOnCooldown(player, ability.getId())) return;

            ServerWorld world = (ServerWorld) player.getWorld();
            boolean success = ability.onActivate(world, player, activeStones);

            if (ability.getCooldownTicks() > 0 && success) {
                GauntletCooldownState.setCooldown(player, ability.getId(), ability.getCooldownTicks());
                ServerPlayNetworking.send(player, new SyncCooldownS2CPacket(
                        ability.getId(), ability.getCooldownTicks(), world.getTime()));
            }
        });
    }

    private static void onTogglePacket(GauntletToggleC2SPacket packet,
                                       ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            ToggleAbility ability = InfinityGauntletItem.findAbility(InfinityGauntletItem.getToggleAbilities(activeStones), packet.abilityId());
            if (ability == null) return;

            ServerWorld world = (ServerWorld) player.getWorld();
            boolean nowActive = GauntletToggleState.flip(player, ability.getId());
            if (nowActive) ability.onEnable(world, player, activeStones);
            else ability.onDisable(world, player, activeStones);

            ServerPlayNetworking.send(player, new SyncToggleStateS2CPacket(ability.getId(), nowActive));
        });
    }

    private static void onHeldPacket(GauntletHeldC2SPacket packet,
                                     ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            HeldAbility ability = InfinityGauntletItem.findAbility(InfinityGauntletItem.getHeldAbilities(activeStones), packet.abilityId());
            if (ability == null) return;

            ServerWorld world = (ServerWorld) player.getWorld();
            GauntletHeldState.setHeld(player, ability.getId(), packet.pressing());

            if (packet.pressing()) ability.onStart(world, player, activeStones);
            else ability.onStop(world, player, activeStones);
        });
    }

    private static void onPickupInfinityItem(PickupInfinityItemC2SPacket packet,
                                             ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            if (PendingInfinityItemPickups.isPending(player)) return;

            Entity entity = player.getServerWorld().getEntityById(packet.entityId());
            if (!(entity instanceof ItemEntity itemEntity)) return;
            if (itemEntity.isRemoved()) return;

            Item item = itemEntity.getStack().getItem();
            if (!(item instanceof InfinityStoneItem
                    || item instanceof InfinityGauntletItem
                    || item instanceof InfinityTesseractItem)) return;
            if (itemEntity.squaredDistanceTo(player) > 16.0) return;

            ItemStack picked = itemEntity.getStack().copy();
            if (player.getInventory().insertStack(picked)) {
                itemEntity.discard();
                PendingInfinityItemPickups.markPending(player);
                Random random = player.getRandom();
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                        0.2f, ((random.nextFloat() - random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            }
        });
    }
}