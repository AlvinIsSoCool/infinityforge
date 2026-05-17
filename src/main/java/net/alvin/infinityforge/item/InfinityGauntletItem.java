package net.alvin.infinityforge.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.GauntletAbilityRegistry;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.infinity.abilities.base.*;
import net.alvin.infinityforge.screen.GauntletScreenHandler;
import net.alvin.infinityforge.server.state.GauntletChargeState;
import net.alvin.infinityforge.server.state.GauntletCooldownState;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InfinityGauntletItem extends Item {
    private static final String GAUNTLET_ID_KEY = "gauntlet_id";
    private static final String STONES_KEY = "gauntlet_equipped_stones";
    private static final String CHARGES_KEY = "gauntlet_charges";
    private static final String COOLDOWNS_KEY = "gauntlet_cooldowns";
    private static final WeakHashMap<ItemStack, List<InfinityStoneType>> STONE_CACHE
            = new WeakHashMap<>();

    public InfinityGauntletItem() {
        super(new FabricItemSettings().maxDamage(0).fireproof());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new GauntletScreenHandler(syncId, playerInventory, user.getStackInHand(hand));
                }

                @Override
                public Text getDisplayName() {
                    return Text.empty();
                }

                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeEnumConstant(hand);
                }
            });
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                    Item.ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 9.0, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                    Item.ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -2.8, EntityAttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);

        if (!activeStones.isEmpty()) {
            tooltip.add(Text.empty());
            tooltip.add(Text.literal("Equipped Stones: ").formatted(Formatting.GRAY));
            for (int i = 0; i < activeStones.size(); i++) {
                InfinityStoneType stoneType = activeStones.get(i);
                String stoneName = InfinityStoneTypeRegistry.getStoneNameFromType(stoneType, " Stone", true);
                Text stoneTooltip = Text.literal("  - ").append(stoneName)
                        .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(stoneType.glintColor())));

                tooltip.add(stoneTooltip);
            }
        }
    }

    public static List<InfinityStoneType> getAddedStones(ItemStack stack) {
        List<InfinityStoneType> cached = STONE_CACHE.get(stack);
        if (cached != null) return cached;

        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(STONES_KEY)) return List.of();

        NbtList list = nbt.getList(STONES_KEY, NbtElement.STRING_TYPE);
        List<InfinityStoneType> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            InfinityStoneType type = InfinityStoneTypeRegistry.REGISTRY
                    .get(new Identifier(list.getString(i)));
            if (type != null) result.add(type);
        }

        List<InfinityStoneType> immutable = Collections.unmodifiableList(result);
        STONE_CACHE.put(stack, immutable);
        return immutable;
    }

    public static void addStones(ItemStack stack, List<InfinityStoneType> stones) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtList list = new NbtList();
        for (InfinityStoneType stone : stones) {
            Identifier typeId = InfinityStoneTypeRegistry.REGISTRY.getId(stone);
            if (typeId != null) list.add(NbtString.of(typeId.toString()));
        }
        nbt.put(STONES_KEY, list);
        STONE_CACHE.remove(stack);
    }

    public static List<ActiveAbility> getActiveAbilities(List<InfinityStoneType> activeStones) {
        List<ActiveAbility> result = new ArrayList<>();
        for (InfinityStoneType stone : activeStones) {
            for (GauntletAbility ability : stone.gauntletAbilities()) {
                if (ability instanceof ActiveAbility a && a.meetsCondition(activeStones))
                    result.add(a);
            }
        }
        return result;
    }

    public static List<PassiveAbility> getPassiveAbilities(List<InfinityStoneType> activeStones) {
        List<PassiveAbility> result = new ArrayList<>();
        for (InfinityStoneType stone : activeStones) {
            for (GauntletAbility ability : stone.gauntletAbilities()) {
                if (ability instanceof PassiveAbility p && p.meetsCondition(activeStones))
                    result.add(p);
            }
        }
        return result;
    }

    public static List<ToggleAbility> getToggleAbilities(List<InfinityStoneType> activeStones) {
        List<ToggleAbility> result = new ArrayList<>();
        for (InfinityStoneType stone : activeStones) {
            for (GauntletAbility ability : stone.gauntletAbilities()) {
                if (ability instanceof ToggleAbility t && t.meetsCondition(activeStones))
                    result.add(t);
            }
        }
        return result;
    }

    public static List<HeldAbility> getHeldAbilities(List<InfinityStoneType> activeStones) {
        List<HeldAbility> result = new ArrayList<>();
        for (InfinityStoneType stone : activeStones) {
            for (GauntletAbility ability : stone.gauntletAbilities()) {
                if (ability instanceof HeldAbility h && h.meetsCondition(activeStones))
                    result.add(h);
            }
        }
        return result;
    }

    public static List<GauntletAbility> getVisibleAbilities(List<InfinityStoneType> activeStones) {
        List<GauntletAbility> result = new ArrayList<>();
        for (InfinityStoneType stone : activeStones) {
            for (GauntletAbility ability : stone.gauntletAbilities()) {
                if ((ability instanceof ActiveAbility
                        || ability instanceof ToggleAbility
                        || ability instanceof HeldAbility)
                        && ability.meetsCondition(activeStones))
                    result.add(ability);
            }
        }
        return result;
    }

    public static UUID getOrCreateGauntletId(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(GAUNTLET_ID_KEY)) {
            nbt.putUuid(GAUNTLET_ID_KEY, UUID.randomUUID());
            STONE_CACHE.remove(stack);
        }
        return nbt.getUuid(GAUNTLET_ID_KEY);
    }

    public static void saveToStack(ItemStack stack, UUID gauntletId, long currentTick) {
        NbtCompound nbt = stack.getOrCreateNbt();

        NbtCompound charges = new NbtCompound();
        Map<Identifier, Integer> chargeMap = GauntletChargeState.getAll(gauntletId);
        if (chargeMap != null)
            chargeMap.forEach((id, charge) -> {
                GauntletAbility ability = GauntletAbilityRegistry.get(id);
                int max = ability instanceof ToggleAbility t ? t.getMaxChargeTicks()
                        : ability instanceof HeldAbility h ? h.getMaxChargeTicks()
                          : Integer.MAX_VALUE;
                if (charge < max)
                    charges.putInt(id.toString(), charge);
            });
        nbt.put(CHARGES_KEY, charges);

        NbtCompound cooldowns = new NbtCompound();
        Map<Identifier, Long> cooldownMap = GauntletCooldownState.getAll(gauntletId);
        if (cooldownMap != null)
            cooldownMap.forEach((id, expiry) -> {
                if (expiry > currentTick)
                    cooldowns.putLong(id.toString(), expiry);
            });
        nbt.put(COOLDOWNS_KEY, cooldowns);
    }

    public static void loadFromStack(ItemStack stack, UUID gauntletId) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) return;

        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
        Set<Identifier> validIds = new HashSet<>();
        for (int i = 0; i < activeStones.size(); i++) {
            for (GauntletAbility ability : activeStones.get(i).gauntletAbilities()) {
                validIds.add(ability.getId());
            }
        }

        if (nbt.contains(CHARGES_KEY)) {
            NbtCompound charges = nbt.getCompound(CHARGES_KEY);
            for (String key : charges.getKeys()) {
                Identifier id = new Identifier(key);
                if (validIds.contains(id))
                    GauntletChargeState.setCharge(gauntletId, id, charges.getInt(key));
            }
        }

        if (nbt.contains(COOLDOWNS_KEY)) {
            NbtCompound cooldowns = nbt.getCompound(COOLDOWNS_KEY);
            for (String key : cooldowns.getKeys()) {
                Identifier id = new Identifier(key);
                if (validIds.contains(id))
                    GauntletCooldownState.setRawExpiry(gauntletId, id, cooldowns.getLong(key));
            }
        }
    }

    @Nullable
    public static ItemStack findGauntlet(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof InfinityGauntletItem) return stack;
        }
        return null;
    }

    @Nullable
    public static <T extends GauntletAbility> T findAbility(List<T> abilities, Identifier id) {
        for (T ability : abilities) {
            if (ability.getId().equals(id)) return ability;
        }
        return null;
    }
}
