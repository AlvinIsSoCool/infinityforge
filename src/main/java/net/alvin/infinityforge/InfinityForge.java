package net.alvin.infinityforge;

import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.events.InfinityForgeEvents;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.InfinityStones;
import net.alvin.infinityforge.item.ModItemGroups;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.packets.GauntletAbilityC2SPacket;
import net.alvin.infinityforge.registries.GauntletAbilityRegistry;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
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
						System.out.println("Stone in gauntlet: " + activeStones.get(0));

						boolean hasAbility = activeStones.stream()
								.flatMap(s -> s.gauntletAbilities().stream())
								.anyMatch(a -> a.getId().equals(packet.abilityId()));

						if (!hasAbility) return;

						System.out.println("Server: Has Ability!");
						GauntletAbility ability = GauntletAbilityRegistry.get(packet.abilityId());
						if (ability != null)
							ability.onActivate(player.getWorld(), player, activeStones);
					});
				}
		);
	}
}