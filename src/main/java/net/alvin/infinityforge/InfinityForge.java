package net.alvin.infinityforge;

import net.alvin.infinityforge.abilities.ActiveAbility;
import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.abilities.HeldAbility;
import net.alvin.infinityforge.abilities.ToggleAbility;
import net.alvin.infinityforge.abilities.state.GauntletHeldState;
import net.alvin.infinityforge.abilities.state.GauntletToggleState;
import net.alvin.infinityforge.events.InfinityForgeEvents;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.InfinityStones;
import net.alvin.infinityforge.item.ModItemGroups;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.packets.GauntletAbilityC2SPacket;
import net.alvin.infinityforge.packets.GauntletHeldC2SPacket;
import net.alvin.infinityforge.packets.GauntletToggleC2SPacket;
import net.alvin.infinityforge.packets.SyncToggleStateS2CPacket;
import net.alvin.infinityforge.registries.GauntletAbilityRegistry;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InfinityForge implements ModInitializer {
	public static final String MOD_ID = "infinityforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		InfinityStoneTypeRegistry.initialize();
		InfinityStones.initialize();

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();

		InfinityForgeEvents.registerEventsCommon();

		System.out.println("Server: Registering Ability Packet Receiver for: " + InfinityForge.MOD_ID);
		ServerPlayNetworking.registerGlobalReceiver(
				GauntletAbilityC2SPacket.TYPE,
				(packet, player, responseSender) -> {
					player.server.execute(() -> {
						System.out.println("Server: Ability Processing Called!");
						ItemStack stack = InfinityGauntletItem.findGauntlet(player);
						if (stack == null) return;

						System.out.println("Server: Gauntlet Found!");

						InfinityGauntletItem gauntletItem = (InfinityGauntletItem) stack.getItem();
						List<InfinityStoneType> activeStones = gauntletItem.getAddedStones(stack);
						ServerWorld world = (ServerWorld) player.getWorld();

						ActiveAbility ability = gauntletItem.getActiveAbilities(stack).stream()
								.filter(a -> a.getId().equals(packet.abilityId()))
								.findFirst()
								.orElse(null);

						if (ability == null) return;
						System.out.println("Server: Has Ability!");

						ability.onActivate(player.getWorld(), player, activeStones);
					});
				}
		);

		ServerPlayNetworking.registerGlobalReceiver(GauntletToggleC2SPacket.TYPE,
				(packet, player, responseSender) -> {
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
		);

		ServerPlayNetworking.registerGlobalReceiver(GauntletHeldC2SPacket.TYPE,
				(packet, player, responseSender) -> {
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
		);

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				ItemStack stack = InfinityGauntletItem.findGauntlet(player);
				if (stack == null) continue;

				InfinityGauntletItem gauntletItem = (InfinityGauntletItem) stack.getItem();
				List<InfinityStoneType> activeStones = gauntletItem.getAddedStones(stack);
				ServerWorld world = (ServerWorld) player.getWorld();

				gauntletItem.getPassiveAbilities(stack)
						.forEach(p -> p.onTick(world, player, activeStones));

				gauntletItem.getToggleAbilities(stack).stream()
						.filter(t -> GauntletToggleState.isActive(player, t.getId()))
						.forEach(t -> t.onTick(world, player, activeStones));

				gauntletItem.getHeldAbilities(stack).stream()
						.filter(h -> GauntletHeldState.isHeld(player, h.getId()))
						.forEach(h -> h.onTick(world, player, activeStones));}
		});
	}
}