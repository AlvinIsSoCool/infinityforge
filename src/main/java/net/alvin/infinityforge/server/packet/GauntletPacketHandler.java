package net.alvin.infinityforge.server.packet;

import net.alvin.infinityforge.entity.PortalEntity;
import net.alvin.infinityforge.infinity.abilities.base.*;
import net.alvin.infinityforge.infinity.abilities.impl.reality.SpawnItemAbility;
import net.alvin.infinityforge.item.InfinityStoneItem;
import net.alvin.infinityforge.item.InfinityTesseractItem;
import net.alvin.infinityforge.network.c2s.*;
import net.alvin.infinityforge.network.s2c.SyncAbilityDynamicIconS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncHeldForceStopS2CPacket;
import net.alvin.infinityforge.registry.GauntletAbilityRegistry;
import net.alvin.infinityforge.registry.ModSounds;
import net.alvin.infinityforge.screen.ItemSelectionScreenHandler;
import net.alvin.infinityforge.server.state.*;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.network.s2c.SyncCooldownS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncToggleStateS2CPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.UUID;

public class GauntletPacketHandler {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletAbilityC2SPacket.TYPE, GauntletPacketHandler::onAbilityPacket);
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletToggleC2SPacket.TYPE, GauntletPacketHandler::onTogglePacket);
        ServerPlayNetworking.registerGlobalReceiver(
                GauntletHeldC2SPacket.TYPE, GauntletPacketHandler::onHeldPacket);
        ServerPlayNetworking.registerGlobalReceiver(
                PickupInfinityItemC2SPacket.TYPE, GauntletPacketHandler::onPickupInfinityItem);
        ServerPlayNetworking.registerGlobalReceiver(
                ItemSelectionC2SPacket.TYPE, GauntletPacketHandler::onItemSelection);
        ServerPlayNetworking.registerGlobalReceiver(
                OpenPortalC2SPacket.TYPE, GauntletPacketHandler::onOpenPortal);
    }

    private static void onAbilityPacket(GauntletAbilityC2SPacket packet,
                                        ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            if (player.isSpectator()) return;
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            UUID gauntletId = InfinityGauntletItem.getOrCreateGauntletId(stack);
            ServerWorld world = (ServerWorld) player.getWorld();
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);

            ActiveAbility ability = GauntletAbility.findAbility(InfinityGauntletItem.getActiveAbilities(activeStones), packet.abilityId());
            if (ability == null) return;
            if (GauntletCooldownState.isOnCooldown(gauntletId, ability.getId(), world.getTime())) return;

            boolean success = ability.onActivate(world, player, activeStones);
            if (!success) return;

            if (ability.getCooldownTicks() > 0) {
                GauntletCooldownState.setCooldown(gauntletId, ability.getId(), ability.getCooldownTicks(), world.getTime());
                ServerPlayNetworking.send(player, new SyncCooldownS2CPacket(
                        ability.getId(), ability.getCooldownTicks(), world.getTime()));
            }
        });
    }

    private static void onTogglePacket(GauntletToggleC2SPacket packet,
                                       ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            if (player.isSpectator()) return;
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            ServerWorld world = (ServerWorld) player.getWorld();
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            ToggleAbility ability = GauntletAbility.findAbility(
                    InfinityGauntletItem.getToggleAbilities(activeStones), packet.abilityId());
            if (ability == null) return;

            boolean nowActive = GauntletToggleState.flip(player, ability.getId());
            if (nowActive) {
                boolean success = ability.onEnable(world, player, activeStones);
                if (!success) {
                    GauntletToggleState.setActive(player, ability.getId(), false);
                    return;
                }
            } else {
                ability.onDisable(world, player, activeStones);
            }

            ServerPlayNetworking.send(player, new SyncToggleStateS2CPacket(ability.getId(), nowActive));
        });
    }

    private static void onHeldPacket(GauntletHeldC2SPacket packet,
                                     ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            if (player.isSpectator()) return;
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);
            if (stack == null) return;

            ServerWorld world = (ServerWorld) player.getWorld();
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            HeldAbility ability = GauntletAbility.findAbility(
                    InfinityGauntletItem.getHeldAbilities(activeStones), packet.abilityId());
            if (ability == null) return;

            if (packet.pressing()) {
                boolean success = ability.onStart(world, player, activeStones);
                if (!success) {
                    ServerPlayNetworking.send(player, new SyncHeldForceStopS2CPacket(ability.getId()));
                    return;
                }
                GauntletHeldState.setHeld(player, ability.getId(), true);
            } else {
                GauntletHeldState.setHeld(player, ability.getId(), false);
                ability.onStop(world, player, activeStones);
            }
        });
    }

    private static void onPickupInfinityItem(PickupInfinityItemC2SPacket packet,
                                             ServerPlayerEntity player, PacketSender responseSender) {
        player.server.execute(() -> {
            if (player.isSpectator()) return;
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

    private static void onItemSelection(ItemSelectionC2SPacket packet,
                                        ServerPlayerEntity player, PacketSender responseSender) {
        if (player.currentScreenHandler instanceof ItemSelectionScreenHandler handler) {
            Item pickedItem = Registries.ITEM.get(packet.id());
            ItemStack pickedStack = new ItemStack(pickedItem, packet.shiftClicked() ? pickedItem.getMaxCount() : 1);

            Identifier abilityId = handler.getAbilityId();
            GauntletAbilityStates.set(player, abilityId, pickedStack);

            GauntletAbility ability = GauntletAbilityRegistry.get(abilityId);
            if (ability instanceof SpawnItemAbility spa) {
                ServerPlayNetworking.send(player,
                        new SyncAbilityDynamicIconS2CPacket(abilityId,
                                spa.getDynamicIcon(pickedStack)));
            }

            player.closeHandledScreen();
        }
    }

    private static void onOpenPortal(OpenPortalC2SPacket packet,
                                        ServerPlayerEntity player, PacketSender responseSender) {
        HitResult hit = player.raycast(3.0, 1.0f, false);
        Vec3d spawnPos, facing;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            Vec3d faceNormal = Vec3d.of(blockHit.getSide().getVector());
            spawnPos = hit.getPos().add(faceNormal.multiply(0.05));
            facing = faceNormal;
        } else {
            Vec3d lookVec = player.getRotationVec(1.0f);
            spawnPos = player.getEyePos().add(lookVec.multiply(3.0));
            facing = lookVec.negate();
        }

        ServerWorld world = player.getServerWorld();
        ServerWorld destination = world.getServer().getWorld(RegistryKey.of(
                RegistryKeys.WORLD, packet.dimId()));
        float portalYaw = (float)Math.toDegrees(Math.atan2(facing.z, facing.x)) - 90.0f;
        double horiz = Math.sqrt(facing.x * facing.x + facing.z * facing.z);
        float portalPitch = MathHelper.clamp((float)-Math.toDegrees(Math.atan2(facing.y, horiz)),
                -70f, 70f);
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.USE_GAUNTLET, SoundCategory.PLAYERS, 1f, 1f);
        PortalEntity.spawnLinkedPair(world, spawnPos.x, spawnPos.y, spawnPos.z,
                portalYaw, portalPitch,
                destination, packet.x(), packet.y(), packet.z(),
                portalYaw + 180f, portalPitch);
        Text portalText = Text.literal("Opening Portal to: ")
                .append(Text.literal(String.valueOf(packet.x())).formatted(Formatting.AQUA))
                .append(Text.literal(", "))
                .append(Text.literal(String.valueOf(packet.y())).formatted(Formatting.AQUA))
                .append(Text.literal(", "))
                .append(Text.literal(String.valueOf(packet.z())).formatted(Formatting.AQUA))
                .append(Text.literal(" in "))
                .append(Text.literal(packet.dimId().getPath().toUpperCase()).formatted(Formatting.GOLD))
                .append(Text.literal(String.format(" (%s)", packet.dimId())).formatted(Formatting.GRAY));
        player.sendMessage(portalText, true);
    }
}