package net.alvin.infinityforge.server.packet;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.abilities.base.HeldAbility;
import net.alvin.infinityforge.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.network.c2s.PickupStoneC2SPacket;
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
import net.alvin.infinityforge.server.state.PendingStonePickups;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class GauntletPacketHandlers {
    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletAbilityC2SPacket.TYPE, GauntletPacketHandlers::onAbilityPacket);
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletToggleC2SPacket.TYPE, GauntletPacketHandlers::onTogglePacket);
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletHeldC2SPacket.TYPE, GauntletPacketHandlers::onHeldPacket);
        ServerPlayNetworking.registerGlobalReceiver(
                PickupStoneC2SPacket.TYPE, GauntletPacketHandlers::onPickupStone);
    }

    private static void onAbilityPacket(GauntletAbilityC2SPacket packet,
                                        ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            System.out.println("Server: Ability Processing Called!");
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            InfinityGauntletItem gauntletItem = (InfinityGauntletItem) stack.getItem();
            List<InfinityStoneType> activeStones = gauntletItem.getAddedStones(stack);
            ServerWorld world = (ServerWorld) player.getWorld();

            ActiveAbility ability = gauntletItem.getActiveAbilities(stack).stream()
                    .filter(a -> a.getId().equals(packet.abilityId()))
                    .findFirst()
                    .orElse(null);

            if (ability == null) return;
            if (GauntletCooldownState.isOnCooldown(player, ability.getId())) return;

            System.out.println("Server: Has Ability! Not on cooldown.");
            ability.onActivate(world, player, activeStones);

            if (ability.getCooldownTicks() > 0) {
                GauntletCooldownState.setCooldown(player, ability.getId(), ability.getCooldownTicks());

                // Sync to client for HUD rendering
                ServerPlayNetworking.send(player, new SyncCooldownS2CPacket(
                        ability.getId(),
                        ability.getCooldownTicks(),
                        world.getTime()
                ));
            }
        });
    }

    private static void onTogglePacket(GauntletToggleC2SPacket packet,
                                       ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            InfinityGauntletItem gauntlet = (InfinityGauntletItem) stack.getItem();
            List<InfinityStoneType> activeStones = gauntlet.getAddedStones(stack);
            ServerWorld world = (ServerWorld) player.getWorld();

            ToggleAbility ability = gauntlet.getToggleAbilities(stack).stream()
                    .filter(a -> a.getId().equals(packet.abilityId()))
                    .findFirst().orElse(null);
            if (ability == null) return;

            boolean nowActive = GauntletToggleState.flip(player, ability.getId());
            if (nowActive) ability.onEnable(world, player, activeStones);
            else ability.onDisable(world, player, activeStones);

            // Tell client so HUD can reflect toggle state
            ServerPlayNetworking.send(player, new SyncToggleStateS2CPacket(ability.getId(), nowActive));
        });
    }

    private static void onHeldPacket(GauntletHeldC2SPacket packet,
                                     ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            InfinityGauntletItem gauntlet = (InfinityGauntletItem) stack.getItem();
            List<InfinityStoneType> activeStones = gauntlet.getAddedStones(stack);
            ServerWorld world = (ServerWorld) player.getWorld();

            HeldAbility ability = gauntlet.getHeldAbilities(stack).stream()
                    .filter(a -> a.getId().equals(packet.abilityId()))
                    .findFirst().orElse(null);
            if (ability == null) return;

            GauntletHeldState.setHeld(player, ability.getId(), packet.pressing());

            if (packet.pressing()) ability.onStart(world, player, activeStones);
            else ability.onStop(world, player, activeStones);
        });
    }

    private static void onPickupStone(PickupStoneC2SPacket packet,
                                      ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            PendingStonePickups.markPending(player);
            Entity entity = player.getServerWorld().getEntityById(packet.entityId());
            if (!(entity instanceof ItemEntity itemEntity)) return;
            if (!(itemEntity.getStack().getItem() instanceof InfinityStoneItem)) return;

            if (itemEntity.squaredDistanceTo(player) > 16.0) return;

            ItemStack stone = itemEntity.getStack().copy();

            if (player.getInventory().insertStack(stone)) {
                itemEntity.discard();
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                        0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f
                );
            }
        });
    }
}
